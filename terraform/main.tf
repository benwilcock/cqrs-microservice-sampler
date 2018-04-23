################
# Common stuff #
################

# Sandpit VPC
module "vpc" {
  source                  = "terraform-aws-modules/vpc/aws"
  version                 = "1.30.0"

  name                    = "${var.candidate}"
  cidr                    = "192.168.0.0/16"

  # No multi-az at this point
  azs                     = ["eu-west-1a"]#, "eu-west-1b"]
  # TODO: remove the secondary subnets part before giving to candidate to make the provisioning fail
  #       alternatively: uncomment the secondary subnets
  public_subnets          = ["192.168.0.0/20"]#, "192.168.16.0/20"]
  private_subnets         = ["192.168.32.0/19"]#, "192.168.64.0/19"] # odd
  database_subnets        = ["192.168.96.0/20"]#, "192.168.112.0/20"]
  

  enable_nat_gateway      = true
  enable_vpn_gateway      = true
  map_public_ip_on_launch = false
  create_database_subnet_group = true

  tags = {
    Terraform   = "true"
    Environment = "dev"
  }

  vpc_tags = {
    Candidate   = "${var.candidate}"
  }
}

# Security groups
resource "aws_security_group" "app_to_db" {
  name        = "app-to-db-sg"
  description = "Allow comms between app and DB but nothing else"
  vpc_id      = "${module.vpc.vpc_id}"

  ingress {
    from_port   = 0
    to_port     = 0
    protocol    = "-1"
    cidr_blocks = ["0.0.0.0/0"]
  }

  tags = {
    Name = "${var.candidate}-app-to-db"
  }
}

# DB

resource "aws_db_instance" "app" {
  name                    = "appdb"
  username                = "appuser"
  password                = "${var.app_db_password}"
  multi_az                = false
  skip_final_snapshot     = true

  engine                  = "mysql"
  engine_version          = "5.7"

  instance_class          = "db.t2.micro"
  allocated_storage       = 10
  storage_type            = "gp2"

  
  parameter_group_name    = "default.mysql5.7"
  db_subnet_group_name    = "${module.vpc.database_subnet_group}" # Horrible
  vpc_security_group_ids  = ["${aws_security_group.app_to_db.id}"]
}

# Now the cluster
module "ecs" {
  source = "./ecs"

  prefix = "${var.candidate}"
  vpc_id = "${module.vpc.vpc_id}"
  private_subnets = "${module.vpc.private_subnets}"
}
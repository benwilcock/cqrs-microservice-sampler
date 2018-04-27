##########################
# ECS Specific resources #
##########################

# Cluster with manual scaling
# TODO: add automatic scaling
resource "aws_ecs_cluster" "system" {
  name = "${var.prefix}-system"
}

resource "aws_autoscaling_group" "system" {
  name                 = "${var.prefix}-system-asg"
  availability_zones   = ["eu-west-1a"]
  launch_configuration = "${aws_launch_configuration.system.name}"
  vpc_zone_identifier  = ["${var.private_subnets}"]
  min_size             = 1
  max_size             = 3
  desired_capacity     = 1
}

resource "aws_launch_configuration" "system" {
  name_prefix          = "${var.prefix}-system-ecs-"
  image_id             = "ami-2d386654"
  instance_type        = "m5.2xlarge"
  iam_instance_profile = "${aws_iam_instance_profile.ecs_system.name}"
  security_groups      = ["${aws_security_group.ecs_system.id}"]
  user_data            = "#!/bin/bash\necho ECS_CLUSTER=${aws_ecs_cluster.system.name} >> /etc/ecs/ecs.config"

  lifecycle {
    create_before_destroy = true
  }
}

resource "aws_iam_instance_profile" "ecs_system" {
  name = "${var.prefix}-system-ecs-instance-profile"
  role = "${aws_iam_role.ecs_instance_role.name}"
}

data "aws_iam_policy_document" "instance_assume_role_policy_system" {
  statement {
    actions = ["sts:AssumeRole"]
    effect  = "Allow"

    principals {
      type        = "Service"
      identifiers = ["ec2.amazonaws.com"]
    }
  }
}

resource "aws_iam_role" "ecs_instance_role" {
  name               = "${var.prefix}-system-ecs-instance-role"
  assume_role_policy = "${data.aws_iam_policy_document.instance_assume_role_policy_system.json}"
}

resource "aws_iam_role_policy_attachment" "ecs_instance_role" {
  role       = "${aws_iam_role.ecs_instance_role.name}"
  policy_arn = "arn:aws:iam::aws:policy/service-role/AmazonEC2ContainerServiceforEC2Role"
}

# Application logs in CW
resource "aws_cloudwatch_log_group" "ecs_log_group" {
  name = "${var.prefix}-ecs-logs"

  tags = {
    Name        = "${var.prefix}-logs"
    ManagedBy   = "terraform"
  }
}

# SG for instance comms
resource "aws_security_group" "ecs_system" {
  name        = "system-ecs-cluster-sg"
  description = "Allow all to group"
  vpc_id      = "${var.vpc_id}"

  ingress {
    from_port = 0
    to_port   = 65535
    protocol  = "TCP"
    cidr_blocks = [ "0.0.0.0/0" ]
  }

  egress {
    from_port   = 0
    to_port     = 0
    protocol    = "-1"
    cidr_blocks = ["0.0.0.0/0"]
  }

  tags = {
    Name = "${var.prefix}-system-ecs-cluster-sg"
  }
}
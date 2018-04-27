##########################
# k8s Specific resources #
##########################

resource "aws_s3_bucket" "kops" {
  bucket = "${replace(var.candidate, "_", "-")}-k8s-infinityworks-com"
  acl    = "private"

  tags {
    Name        = "${var.candidate}.k8s.infinityworks.com"
    Terraform   = "true"
    Environment = "dev"
  }
}


# kops require a dedicated user
resource "aws_iam_user" "kops" {
  name          = "${var.candidate}-kops"
  path          = "/k8s/"
  force_destroy = true
}

resource "aws_iam_user_policy_attachment" "kops" {
  count       = "${length(var.aws_permissions)}"
  user        = "${aws_iam_user.kops.name}"
  policy_arn  = "${element(var.aws_permissions, count.index)}"
}


resource "aws_iam_access_key" "kops" {
  user    = "${aws_iam_user.kops.name}"
  pgp_key = "${var.pgp_key}"
}

# We need the credentials
output "AWS_ACCESS_KEY_ID" {
  value = "${aws_iam_access_key.kops.id}"
}

output "AWS_SECRET_ACCESS_KEY" {
  value = "${aws_iam_access_key.kops.encrypted_secret}"
}
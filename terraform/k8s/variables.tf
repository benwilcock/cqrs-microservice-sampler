#
variable "candidate" {
  description = "the name of the candidate"
}

variable "pgp_key" {
  description = "the pgp key for the user's secret"
}

variable "aws_permissions" {
  description = "The policy arns required for kops"
  default = [
    "arn:aws:iam::aws:policy/AmazonEC2FullAccess",
    "arn:aws:iam::aws:policy/AmazonRoute53FullAccess",
    "arn:aws:iam::aws:policy/AmazonS3FullAccess",
    "arn:aws:iam::aws:policy/IAMFullAccess",
    "arn:aws:iam::aws:policy/AmazonVPCFullAccess"
  ]
}
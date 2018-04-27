variable "prefix" {
  description = "the prefix to apply to the resources"
}

variable "private_subnets" {
  description = "The list of subnets where the ECS cluster should live"
  type = "list"
}

variable "vpc_id" {
  description = "The vpc_id where the cluster is being created"
}
provider "aws" {
  region  = var.aws_region
  version = "~> 2.59.0"
  access_key = var.access_key
  secret_key = var.secret_key
}

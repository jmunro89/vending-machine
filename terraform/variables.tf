variable "aws_region" {
  default = "eu-west-1"
}
variable "artifact_name" {
  default = "vending-machine-0.0.1.jar"
}
variable "bucket_name" {} //Name of bucket that the app uses to store state. Bucket names are global and so much be different per deployment.
variable "aws_account_id" {}
variable "access_key" {}
variable "secret_key" {}
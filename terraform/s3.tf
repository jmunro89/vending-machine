resource "aws_s3_bucket" "vending-machine-bucket" {
  bucket = var.bucket_name
  acl    = "private"
}
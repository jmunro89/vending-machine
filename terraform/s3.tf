resource "aws_s3_bucket" "vending-machine-bucket" {
  bucket = "10x-vending-machine"
  acl    = "private"
}
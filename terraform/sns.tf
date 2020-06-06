variable "delivery_policy" {
  default = <<EOF
{
  "http": {
    "defaultHealthyRetryPolicy": {
      "minDelayTarget": 1,
      "maxDelayTarget": 1,
      "numRetries": 3,
      "numMaxDelayRetries": 0,
      "numNoDelayRetries": 3,
      "numMinDelayRetries": 0,
      "backoffFunction": "linear"
    },
    "disableSubscriptionOverrides": true
  }
}
EOF
}

resource "aws_sns_topic" "banking_requests" {
  name            = "banking-requests"
  delivery_policy = var.delivery_policy
}

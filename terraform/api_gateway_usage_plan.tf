resource "aws_api_gateway_api_key" "vending_machine_api_key" {
  name = "vending-machine-api-key"
}

output "vending_machine_api_key" {
  value = aws_api_gateway_api_key.vending_machine_api_key.value
}

resource "aws_api_gateway_usage_plan" "vending_machine_usage_plan" {
  name         = "vending-maching-usage-plan"

  api_stages {
    api_id = aws_api_gateway_rest_api.vending_machine.id
    stage  = aws_api_gateway_stage.vending_machine.stage_name
  }

  quota_settings {
    limit  = 120
    period = "DAY"
  }

  throttle_settings {
    burst_limit = 5
    rate_limit  = 10
  }
}

resource "aws_api_gateway_usage_plan_key" "vending_machine_usage_plan_key" {
  key_id        = aws_api_gateway_api_key.vending_machine_api_key.id
  key_type      = "API_KEY"
  usage_plan_id = aws_api_gateway_usage_plan.vending_machine_usage_plan.id
}

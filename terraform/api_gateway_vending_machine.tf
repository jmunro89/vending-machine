resource "aws_api_gateway_resource" "vending_machine" {
  rest_api_id = aws_api_gateway_rest_api.vending_machine.id
  parent_id   = aws_api_gateway_rest_api.vending_machine.root_resource_id
  path_part   = "vend"
}

resource "aws_api_gateway_method" "vending_machine" {
  rest_api_id   = aws_api_gateway_rest_api.vending_machine.id
  resource_id   = aws_api_gateway_resource.vending_machine.id
  http_method   = "ANY"
  authorization = "NONE"
  api_key_required = true
}

resource "aws_api_gateway_method_response" "vending_machine" {
  lifecycle {
    create_before_destroy = true
  }

  rest_api_id = aws_api_gateway_rest_api.vending_machine.id
  resource_id = aws_api_gateway_resource.vending_machine.id
  http_method = aws_api_gateway_method.vending_machine.http_method
  status_code = "200"

  response_models = {
    "application/json" = aws_api_gateway_model.vending_machine.name
  }

  response_parameters = {
    "method.response.header.x-amzn-lambda-RequestId" = "true"
  }
}


// AWS_API_GATEWAY_MODEL
resource "aws_api_gateway_model" "vending_machine" {
  rest_api_id  = aws_api_gateway_rest_api.vending_machine.id
  name         = "Default"
  description  = "a default JSON schema"
  content_type = "application/json"

  schema = <<EOF
{
  "$schema": "http://json-schema.org/draft-04/schema#",
  "title" : "Default",
  "type" : "object"
}
EOF
}

resource "aws_api_gateway_integration" "vending_machine" {
  rest_api_id             = aws_api_gateway_rest_api.vending_machine.id
  resource_id             = aws_api_gateway_resource.vending_machine.id
  http_method             = aws_api_gateway_method.vending_machine.http_method
  type                    = "AWS"
  integration_http_method = "POST"
  uri                     = aws_lambda_function.vending-machine.invoke_arn
  credentials             = aws_iam_role.vending_machine_api.arn
}

resource "aws_api_gateway_integration_response" "vending_machine" {
  depends_on = [
    aws_api_gateway_integration.vending_machine,
  ]

  lifecycle {
    create_before_destroy = true
  }

  rest_api_id = aws_api_gateway_rest_api.vending_machine.id
  resource_id = aws_api_gateway_resource.vending_machine.id
  http_method = aws_api_gateway_method.vending_machine.http_method
  status_code = aws_api_gateway_method_response.vending_machine.status_code

  response_parameters = {
    "method.response.header.x-amzn-lambda-RequestId" = "integration.response.header.x-amzn-RequestId"
  }
}
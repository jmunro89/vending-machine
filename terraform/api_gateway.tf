resource "aws_api_gateway_rest_api" "vending_machine" {
  depends_on = [
    aws_lambda_function.vending-machine,
  ]

  lifecycle {
    create_before_destroy = true
  }

  name        = "vending-machine"
  description = "vending-machine API"
  policy      = data.aws_iam_policy_document.vending_machine_policy.json
}

resource "aws_api_gateway_deployment" "vending_machine" {
  lifecycle {
    create_before_destroy = true
  }

  depends_on = [
    aws_api_gateway_integration.vending_machine
  ]

  rest_api_id = aws_api_gateway_rest_api.vending_machine.id

  // The blank string below is to avoid creating a stage, so we don't get conflicts when aws_api_gateway_stage is applied
  stage_name        = ""
  stage_description = timestamp()
  description       = "Deployed Locally"
}

resource "aws_api_gateway_stage" "vending_machine" {
  rest_api_id           = aws_api_gateway_rest_api.vending_machine.id
  stage_name            = "dev"
  deployment_id         = aws_api_gateway_deployment.vending_machine.id
}

data "aws_iam_policy_document" "vending_machine_policy" {
  statement {
    effect = "Allow"

    principals {
      type = "*"

      identifiers = [
        "*",
      ]
    }

    actions = [
      "execute-api:invoke",
    ]

    resources = [
      "arn:aws:execute-api:${var.aws_region}:${var.aws_account_id}:*",
    ]
  }
}

output "api_endpoint" {
  value = aws_api_gateway_deployment.vending_machine.invoke_url
}

output "aws_api_gateway_stage-vending_machine-invoke_url" {
  value = aws_api_gateway_stage.vending_machine.invoke_url
}
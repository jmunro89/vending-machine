resource "aws_lambda_function" "vending-machine" {
  lifecycle {
    create_before_destroy = true
  }

  filename         = "../build/libs/${var.artifact_name}"
  runtime          = "java8"
  function_name    = "vending-machine"
  handler          = "com.tenx.banking.core.api.handler.VendingMachineHandler::handleRequest"
  role             = aws_iam_role.vending_machine_lambda.arn
  memory_size      = 1536
  timeout          = "60"
  publish          = true
  source_code_hash = filebase64sha256("../build/libs/${var.artifact_name}")

  environment {
    variables = {
      TOPIC_NAME = "arn:aws:sns:${var.aws_region}:${var.aws_account_id}:banking-requests"
      BUCKET_NAME = var.bucket_name
    }
  }

}

resource "aws_lambda_function" "bank" {
  lifecycle {
    create_before_destroy = true
  }

  filename         = "../build/libs/${var.artifact_name}"
  runtime          = "java8"
  function_name    = "bank"
  handler          = "com.tenx.banking.core.api.handler.BankHandler::handleEvent"
  role             = aws_iam_role.bank_lambda.arn
  memory_size      = 1536
  timeout          = "60"
  publish          = true
  source_code_hash = filebase64sha256("../build/libs/${var.artifact_name}")
  environment {
    variables = {
      BUCKET_NAME = var.bucket_name
    }
  }
}

resource "aws_sns_topic_subscription" "banking-requests" {
  depends_on = [
    aws_lambda_function.bank,
  ]

  topic_arn = aws_sns_topic.banking_requests.arn
  protocol  = "lambda"
  endpoint  = aws_lambda_function.bank.arn
}

resource "aws_lambda_permission" "bank_permission" {
  statement_id  = "AllowExecutionFromSNS"
  action        = "lambda:InvokeFunction"
  function_name = aws_lambda_function.bank.arn
  principal     = "sns.amazonaws.com"
  source_arn    = aws_sns_topic.banking_requests.arn
}

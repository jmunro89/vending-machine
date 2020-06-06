// Vending Machine API

resource "aws_iam_role" "vending_machine_api" {
  lifecycle {
    create_before_destroy = true
  }

  name = "vending-machine-api-role"
  assume_role_policy = data.aws_iam_policy_document.vending_machine_api_role_trust_entities.json
}

data "aws_iam_policy_document" "vending_machine_api_role_trust_entities" {
  statement {
    effect = "Allow"

    principals {
      type = "Service"

      identifiers = [
        "apigateway.amazonaws.com",
      ]
    }

    actions = [
      "sts:AssumeRole",
    ]
  }
}

resource "aws_iam_role_policy" "vending_machine_api_role_policy" {
  lifecycle {
    create_before_destroy = true
  }

  name = "vending-machine-api-role-policy"
  role = aws_iam_role.vending_machine_api.id
  policy = data.aws_iam_policy_document.vending_machine_api_role_policy_actions.json
}

data "aws_iam_policy_document" "vending_machine_api_role_policy_actions" {
  statement {
    effect = "Allow"

    actions = [
      "logs:CreateLogGroup",
      "logs:CreateLogStream",
      "logs:PutLogEvents",
    ]

    resources = [
      "*",
    ]
  }

  statement {
    effect = "Allow"

    actions = [
      "lambda:InvokeFunction",
    ]

    resources = [
      aws_lambda_function.vending-machine.arn,
    ]
  }
}

// Vending Machine Lambda

resource "aws_iam_role" "vending_machine_lambda" {
  lifecycle {
    create_before_destroy = true
  }

  name = "vending-machine-lambda-role"
  assume_role_policy = data.aws_iam_policy_document.vending_machine_lambda_role_trust_entities.json
}

data "aws_iam_policy_document" "vending_machine_lambda_role_trust_entities" {
  statement {
    effect = "Allow"

    principals {
      type = "Service"

      identifiers = [
        "lambda.amazonaws.com",
      ]
    }

    actions = [
      "sts:AssumeRole",
    ]
  }
}

resource "aws_iam_role_policy" "vending_machine_lambda_role_policy" {
  lifecycle {
    create_before_destroy = true
  }

  name = "vending-machine-lambda-role-policy"
  role = aws_iam_role.vending_machine_lambda.id
  policy = data.aws_iam_policy_document.vending_machine_lambda_role_policy_actions.json
}

data "aws_iam_policy_document" "vending_machine_lambda_role_policy_actions" {
  statement {
    effect = "Allow"

    actions = [
      "logs:CreateLogGroup",
      "logs:CreateLogStream",
      "logs:PutLogEvents",
    ]

    resources = [
      "*",
    ]
  }

  statement {
    effect = "Allow"

    actions = [
      "sns:Publish",
    ]

    resources = [
      aws_sns_topic.banking_requests.arn
    ]
  }

  statement {
    effect = "Allow"

    actions = [
      "s3:PutObject",
      "s3:GetObject",
      "s3:ListBucket",
      "s3:DeleteObject",
    ]

    resources = [
      "${aws_s3_bucket.vending-machine-bucket.arn}/*",
      aws_s3_bucket.vending-machine-bucket.arn,
    ]
  }
}

// Bank Lambda

resource "aws_iam_role" "bank_lambda" {
  lifecycle {
    create_before_destroy = true
  }

  name = "bank-lambda-role"
  assume_role_policy = data.aws_iam_policy_document.bank_lambda_role_trust_entities.json
}

data "aws_iam_policy_document" "bank_lambda_role_trust_entities" {
  statement {
    effect = "Allow"

    principals {
      type = "Service"

      identifiers = [
        "lambda.amazonaws.com",
      ]
    }

    actions = [
      "sts:AssumeRole",
    ]
  }
}

resource "aws_iam_role_policy" "bank_lambda_role_policy" {
  lifecycle {
    create_before_destroy = true
  }

  name = "bank-lambda-role-policy"
  role = aws_iam_role.bank_lambda.id
  policy = data.aws_iam_policy_document.bank_lambda_role_policy_actions.json
}

data "aws_iam_policy_document" "bank_lambda_role_policy_actions" {
  statement {
    effect = "Allow"

    actions = [
      "logs:CreateLogGroup",
      "logs:CreateLogStream",
      "logs:PutLogEvents",
    ]

    resources = [
      "*",
    ]
  }

  statement {
    effect = "Allow"

    actions = [
      "s3:PutObject",
      "s3:GetObject",
      "s3:ListBucket",
      "s3:DeleteObject",
    ]

    resources = [
      "${aws_s3_bucket.vending-machine-bucket.arn}/*",
      aws_s3_bucket.vending-machine-bucket.arn,
    ]
  }
}
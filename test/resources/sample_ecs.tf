terraform {
  backend "s3" {
      bucket = "mybucket"
      key = "state/foo.json"
      region = "us-west-2"
  }
}

module "environment_info" {
    source = "foo/bar"
    team        = "myteam"
    application = "myapp"
    environment = "staging"
}

provider "aws" {
  version = "1.8.0"
    region = "${module.environment_info.region}"
    assume_role {
        role_arn = "${module.environment_info.assume_role}"
    }
}

module "ecs-service" {
  source = "foo/bar"

  # metadata #
  team          = "myteam"
  application   = "myapp"
  environment   = "stage"

  # Compute resource configuration #
  cpu           = "2048"
  memory        = "5120"
  min_capacity  = "2"
  max_capacity  = "10"

  # Application configuration #
  drain_time = "30"
  health_check_timeout = "10"
  health_check_interval = "40"
  health_check_healthy_threshold = "2"
  health_check_unhealthy_threshold = "10"
  health_check_grace_period_seconds = "0"
  container_port = "8080"
  health_check_path = "/healthcheck"

  # Autoscaling configuration #
  scaledown_adjustment = "-1"
  scaleup_percent_adjustment = "100"
  cpu_evaluation_periods = "3"
  cpu_period = "60"
  high_cpu_threshold = "50"
  low_cpu_threshold = "15"
  scaledown_cooldown = "60"
  scaleup_cooldown = "60"

  # IAM policy statements #
  iam_statements = [
    {
      actions   = [
        "s3:*"
      ]
      resources = [
        "arn:aws:s3:::myteam-dev",
        "arn:aws:s3:::myteam-dev/*"
      ]
    }
  ]

  # Advanced configuration #
  lb_internal = "false"
  use_dns = 1
  use_elb_ssl = 1
  use_http = 0
  use_application_ssl = "false"
  certificate_override = ""
  cluster_override = ""
  application_path = "/*"
  ingress_cidr = ["0.0.0.0/0"]
}

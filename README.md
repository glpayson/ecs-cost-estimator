# ecs-cost-estimator
**TODO:**
- with-open
- Non-happy path unit tests
- Handle non-ECS .tf files (e.g. lambdas)
- Don't blow up when *.tf values are vars and not integers
- Better packaging for team members without Clojure and Leiningen installed

## Usage

`lein run -m ecs-cost-estimator.core /path/to/a/terraform.tf`

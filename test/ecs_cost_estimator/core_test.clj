(ns ecs-cost-estimator.core-test
  (:require [ecs-cost-estimator.core :as target]
            [midje.sweet :refer [fact facts]]))

(facts "Terraform ECS file is correctly parsed with costs calculated"
  (with-redefs [clojure.pprint/pprint identity]
    (let [actual (target/-main "test/resources/sample_ecs.tf")]
      (fact "Application name is correctly parsed"
        (:application actual) => "myapp")
      (fact "Minimum capacity is correctly parsed"
        (:min-capacity actual) => 2)
      (fact "Maximum capacity is correctly parsed"
        (:max-capacity actual) => 10)
      (fact "Memory is correctly parsed to GBs"
        (:memory-gb actual) => 5.0)
      (fact "CPU shares is correctly parsed to VCPUs"
        (:vcpus actual) => 2.0)
      (fact "Minimum cost per month is correctly calculated"
        (:min-cost-month actual) => 584.00)
      (fact "Maximum cost per month is correctly calculated"
        (:max-cost-month actual) => 1168.00))))

(facts "Incorrect number of args prints usage"
  (let [expected (str @#'target/usage "\n")]
    (fact "No args prints usage"
      (with-out-str (target/-main)) => expected)
    (fact "Too many args prints usage"
      (with-out-str (target/-main 0 1)) => expected)))

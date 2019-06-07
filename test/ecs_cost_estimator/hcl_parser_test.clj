(ns ecs-cost-estimator.hcl-parser-test
  (:require [ecs-cost-estimator.hcl-parser :as target]
            [midje.sweet :refer [fact facts]]))

(facts "Relevant fields are parsed from terraform file."
  (let [actual (target/parse "test/resources/sample_ecs.tf")]
    (fact "CPU shares are pared to number of VCPUs."
      (:vcpus actual) => 2.0)
    (fact "Memory is parsed to gb"
      (:memory-gb actual) => 5.0)
    (fact "Minimum capacity is parsed"
      (:min-capacity actual) => 2)
    (fact "Maximum capacity is parsed"
      (:max-capacity actual) => 10)
    (fact "Application name is parsed"
      (:application actual) => "myapp")))

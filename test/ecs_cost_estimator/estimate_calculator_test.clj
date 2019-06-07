(ns ecs-cost-estimator.estimate-calculator-test
  (:require [ecs-cost-estimator.estimate-calculator :as target]
            [ecs-cost-estimator.ecs-specs :as ecs]
            [midje.sweet :refer [fact facts]]))

(def cpu-bound {:application  "myapp"
                :max-capacity 10
                :memory-gb    5.0
                :min-capacity 2
                :vcpus        2.0})


(def memory-bound {:application  "myapp"
                   :min-capacity 2
                   :max-capacity 10
                   :memory-gb    33.0
                   :vcpus        2.0})

(facts "Costs are correctly calculated"
  (with-redefs [ecs/standard-cost-month 584.00
                ecs/standard-vcpus 16
                ecs/standard-mem 64]
    (facts "When bound by VCPUs"
      (let [actual (target/calculate-ecs-estimate cpu-bound)]
        (fact "Minimum cost is correctly calculated."
          (:min-cost-month actual) => 584.00)
        (fact "Maximum cost is correctly calculated."
          (:max-cost-month actual) 1752.00)))
    (fact "When bound by memory"
      (let [actual (target/calculate-ecs-estimate memory-bound)]
        (fact "Minimum cost is correctly calculated."
          (:min-cost-month actual) => 1168.00)
        (fact "Maximum cost is correctly calculated."
          (:max-cost-month actual) 11680.00)))))



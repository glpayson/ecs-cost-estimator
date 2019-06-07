(ns ecs-cost-estimator.ecs-specs-test
  (:require [ecs-cost-estimator.ecs-specs :as target]
            [midje.sweet :refer [fact]]))

(fact "Standard vcpus is for m4.4xlarge"
  target/standard-vcpus => (get-in @#'target/specs [:m4.4xlarge :vcpus]))

(fact "Standard memory is for m4.4xlarge"
  target/standard-mem => (get-in @#'target/specs [:m4.4xlarge :memory]))

(fact "Standard cost per month is for m4.4xlarge unreserved"
  (let [m4-4xl-cost-year (get-in @#'target/specs [:m4.4xlarge :cost :unreserved])]
    target/standard-cost-month => (/ (* m4-4xl-cost-year 24 365) 12)))
(ns ecs-cost-estimator.ecs-specs
  (:require [clojure.java.io :as io]
            [clojure.edn :as edn]))

(def ^:private specs
  (-> (io/resource "ecs-specs.edn")
      slurp
      edn/read-string))

(def standard-vcpus
  (get-in specs [:m4.4xlarge :vcpus]))

(def standard-mem
  (get-in specs [:m4.4xlarge :memory]))

(defn- cost-per-month [cost-per-hour]
  (/ (* cost-per-hour 24 365) 12))

(def standard-cost-month
  (-> (get-in specs [:m4.4xlarge :cost :unreserved])
      cost-per-month))

(ns ecs-cost-estimator.core
  (:require [clojure.pprint :refer [pprint]]
            [ecs-cost-estimator.hcl-parser :as parser]
            [ecs-cost-estimator.estimate-calculator :as calc]))

(def ^:private usage "USAGE: lein run -m ecs-cost-estimator.core /path/to/a/terraform/file/main.tf")

(defn- estimate [filename]
  (let [instance-specs (parser/parse filename)]
    (-> (calc/calculate-ecs-estimate instance-specs)
        (merge instance-specs))))

(defn -main
  "Parse an ECS terraform file and calculate estimates for minimum and maximum cost per month based
  upon minimum capacity, maximum capacity, CPU shares, and memory, along with EC2 instance cost,
  memory and vCPU's."
  [& args]
  (if-not (= 1 (count args))
    (println usage)
    (-> (first args)
        estimate
        pprint)))

(ns ecs-cost-estimator.core
  (:require [clojure.pprint :refer [pprint]]
            [ecs-cost-estimator.ecs-specs :as ecs]
            [ecs-cost-estimator.hcl-parser :as parser]))

(defn- tasks-per-instance
  [req-vcpus req-mem instance-vcpus instance-mem]
  (int
    (Math/floor
      (min (/ instance-vcpus req-vcpus)
           (/ instance-mem req-mem)))))

(defn- cost-month
  [num-tasks]
  (* ecs/standard-cost-month (Math/ceil num-tasks)))

(defn- add-costs
  [{:keys [vcpus, memory-gb, min-capacity, max-capacity] :as service}]
  (let [tasks-per-instance
        (tasks-per-instance vcpus memory-gb ecs/standard-vcpus ecs/standard-memory)]
    (-> service
        (assoc :min-cost-month (cost-month (/ min-capacity tasks-per-instance)))
        (assoc :max-cost-month (cost-month (/ max-capacity tasks-per-instance))))))

(defn- calculate-ecs-estimate
  "Parses an ECS terraform file and calculates its minimum and maximum cost per month"
  [filename]
  (-> (parser/parse filename)
      (add-costs)))

(defn -main [& args]
  (if (= 1 (count args))
    (pprint (calculate-ecs-estimate (first args)))
    (println "USAGE: lein run -m ecs-cost-estimator.core /path/to/a/terraform/file/main.tf")))

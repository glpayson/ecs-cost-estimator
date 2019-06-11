(ns ecs-cost-estimator.estimate-calculator
  (:require [ecs-cost-estimator.ecs-specs :as ecs]))

(defn round-to-cents [cost]
  (Double/parseDouble (format "%.2f" cost)))

(defn- cost-month
  "Minimum number of EC2 instances required to host the tasks-per-instance number of tasks times the
  cost per month of the EC2 instance type."
  [capacity tasks-per-instance instance-cost-month]
  (-> (/ capacity tasks-per-instance)
      float
      Math/ceil
      (* instance-cost-month)
      (round-to-cents)))

(defn- tasks-per-instance
  "Returns the minimum of the number of tasks that will fit on an instance due to cpu requirements
  and the number of tasks that will fit on an instance due to memory requirements."
  [task-vcpus task-mem instance-vcpus instance-mem]
  (let [instances-req-by-cpu (/ instance-vcpus task-vcpus)
        instances-req-by-mem (/ instance-mem task-mem)]
    (-> (min instances-req-by-cpu instances-req-by-mem)
        Math/floor)))

(defn calculate-ecs-estimate
  "Calculates an ECS service's minimum and maximum cost per month"
  [{:keys [vcpus, memory-gb, min-capacity, max-capacity]}]
  (let [instance-cost-month ecs/standard-cost-month
        tasks-per-instance (tasks-per-instance vcpus memory-gb ecs/standard-vcpus ecs/standard-mem)]
    {:min-cost-month (cost-month min-capacity tasks-per-instance instance-cost-month)
     :max-cost-month (cost-month max-capacity tasks-per-instance instance-cost-month)}))


(ns ecs-cost-estimator.hcl-parser
  (:require [clojure.walk :refer [keywordize-keys]]
            [clojure.set :refer [rename-keys]]
            [camel-snake-kebab.core :as csk]
            [camel-snake-kebab.extras :as cske])
  (:import [com.bertramlabs.plugins.hcl4j HCLParser]))

(defn- parse-ecs-specs
  [filename]
  (as-> (java.io.File. filename) %
        (.parse (HCLParser.) %)
        (.get % "module")
        (.get % "ecs-service")
        (assoc (into {} %) "key" "value")
        (keywordize-keys %)
        (cske/transform-keys csk/->kebab-case-keyword %)))

(defn- string-to-gbs
  [str]
  (-> str (Integer/parseInt) (/ 1024.0)))

(defn parse
  "Parse relevant fields from an ECS Terraform file"
  [filename]
  (as-> (parse-ecs-specs filename) %
        (select-keys % [:application :memory :cpu :min-capacity :max-capacity])
        (update % :memory #(string-to-gbs %))
        (update % :cpu #(string-to-gbs %))
        (update % :min-capacity #(Integer/parseInt %))
        (update % :max-capacity #(Integer/parseInt %))
        (rename-keys % {:memory :memory-gb :cpu :vcpus})))

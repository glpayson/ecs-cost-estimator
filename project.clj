(defproject ecs-cost-estimator "0.1.0-SNAPSHOT"
  :description "Parse ECS terraform file and calculate estimated min and max cost per month."
  :license {:name "EPL-2.0 OR GPL-2.0-or-later WITH Classpath-exception-2.0"
            :url  "https://www.eclipse.org/legal/epl-2.0/"}
  :plugins [[lein-midje "3.2.1"]]
  :dependencies [[org.clojure/clojure "1.10.0"]
                 [camel-snake-kebab "0.4.0"]
                 [com.bertramlabs.plugins/hcl4j "0.2.0"]]
  :profiles {:dev {:dependencies [[midje "1.9.8"]]}}
  :repl-options {:init-ns ecs-cost-estimator.core})

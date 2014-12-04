(defproject asciiparse "0.1.0-SNAPSHOT"
  :description "Tools for parsing ascii art numbers in a specific format"
  :url "http://github.com/joshhead/asciiparse"
  :license {:name "MIT License"
            :url "http://opensource.org/licenses/MIT"}
  :dependencies [[org.clojure/clojure "1.6.0"]]
  :main ^:skip-aot asciiparse.core
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all}})

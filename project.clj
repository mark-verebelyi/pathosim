(defproject pathosim "0.1.0-SNAPSHOT"
  :description "A simulation with pathogens"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.6.0"]]
  :main ^:skip-aot pathosim.core
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all}})

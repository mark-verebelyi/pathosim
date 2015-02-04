(ns pathosim.randoms-test
  (:require [pathosim.randoms :refer :all]
            [clojure.test.check.generators :as gen]
            [clojure.test.check.properties :as prop]
            [clojure.test.check.clojure-test :as ct]))

(ct/defspec test-random-int-between 10000
            (prop/for-all [x gen/int y gen/int]
                          (let [lo (min x y)
                                hi (max x y)
                                r (random-int-between lo hi)]
                            (and (<= lo r) (<= r hi)))))

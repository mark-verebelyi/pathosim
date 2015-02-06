(ns pathosim.stat-test
  (:require [clojure.test :refer :all]
            [pathosim.stat :refer :all]
            [clojure.test.check.generators :as gen]
            [clojure.test.check.properties :as prop]
            [clojure.test.check.clojure-test :refer [defspec]]))

(deftest test-create-stat-blueprint
  (testing "valid instance"
    (let [blueprint (create-stat-blueprint 50 0 100 0.1 -10 10)]
      (is (= 50 (blueprint :initial-amount)))
      (is (= 0 (blueprint :min-amount)))
      (is (= 100 (blueprint :max-amount)))
      (is (= 0.1 (blueprint :mutation-probability)))
      (is (= -10 (blueprint :min-mutation)))
      (is (= 10 (blueprint :max-mutation)))))
  (testing "initial-amount must be non-nil numeric"
    (is (thrown? AssertionError (create-stat-blueprint "a" 0 100 0.1 -10 10)))
    (is (thrown? AssertionError (create-stat-blueprint nil 0 100 0.1 -10 10))))
  (testing "min-amount must be non-nil numeric"
    (is (thrown? AssertionError (create-stat-blueprint 50 "a" 100 0.1 -10 10)))
    (is (thrown? AssertionError (create-stat-blueprint 50 nil 100 0.1 -10 10))))
  (testing "max-amount must be non-nil numeric"
    (is (thrown? AssertionError (create-stat-blueprint 50 0 "a" 0.1 -10 10)))
    (is (thrown? AssertionError (create-stat-blueprint 50 0 nil 0.1 -10 10))))
  (testing "mutation-probability must be non-nil numeric"
    (is (thrown? AssertionError (create-stat-blueprint 50 0 100 "a" -10 10)))
    (is (thrown? AssertionError (create-stat-blueprint 50 0 100 nil -10 10))))
  (testing "min-mutation must be non-nil numeric"
    (is (thrown? AssertionError (create-stat-blueprint 50 0 100 0.1 "a" 10)))
    (is (thrown? AssertionError (create-stat-blueprint 50 0 100 0.1 nil 10))))
  (testing "max-mutation must be non-nil numeric"
    (is (thrown? AssertionError (create-stat-blueprint 50 0 100 0.1 -10 "a")))
    (is (thrown? AssertionError (create-stat-blueprint 50 0 100 0.1 -10 nil))))
  (testing "mutation-probability must be 0 <= p <= 1"
    (is (thrown? AssertionError (create-stat-blueprint 50 0 100 1.5 -10 10)))
    (is (thrown? AssertionError (create-stat-blueprint 50 0 100 -0.1 -10 10))))
  (testing "initial-amount must be between min-amount and max-amount"
    (is (thrown? AssertionError (create-stat-blueprint 50 60 100 0.1 -10 10)))
    (is (thrown? AssertionError (create-stat-blueprint 110 0 100 0.1 -10 10))))
  (testing "max-amount must be greater than or equal to min-amount"
    (is (thrown? AssertionError (create-stat-blueprint 50 100 0 0.1 -10 10)))))

(deftest test-create-stat
    (let [blueprint (create-stat-blueprint 50 0 100 0.1 -10 10)]
      (testing "valid instance"
        (let [stat (create-stat :some-stat 30 blueprint)]
          (is (= :some-stat (stat :name)))
          (is (= 30 (stat :amount)))
          (is (identical? blueprint (stat :blueprint)))))
      (testing "name must be non-nil"
        (is (thrown? AssertionError (create-stat nil 50 blueprint))))
      (testing "amount must be non-nil numeric"
        (is (thrown? AssertionError (create-stat :some-stat "a" blueprint)))
        (is (thrown? AssertionError (create-stat :some-stat nil blueprint))))
      (testing "blueprint must be non-nil"
        (is (thrown? AssertionError (create-stat :some-stat 50 nil))))
      (testing "amount must be between blueprint's min-amount and max-amount"
        (is (thrown? AssertionError (create-stat :some-stat -10 blueprint)))
        (is (thrown? AssertionError (create-stat :some-stat 110 blueprint))))))

(defspec test-mutate-stat 1000
         (prop/for-all [min-amount (gen/elements (range 0 51))
                        max-amount (gen/elements (range 50 101))
                        min-mutation (gen/elements (range -100 21))
                        max-mutation (gen/elements (range 20 201))]
                       (let [blueprint (create-stat-blueprint min-amount min-amount max-amount 0.1 min-mutation max-mutation)
                             original-amount (if (= min-amount max-amount)
                                      min-amount
                                      (rand-nth (range min-amount max-amount)))
                             stat (create-stat :whatever original-amount blueprint)
                             mutated-stat (mutate-stat stat)
                             new-amount (mutated-stat :amount)]
                         (<= (min
                               (+ original-amount min-mutation)
                               min-amount)
                             new-amount
                             (max
                               (+ original-amount max-mutation)
                               max-amount)))))
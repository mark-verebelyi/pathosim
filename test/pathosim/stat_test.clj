(ns pathosim.stat-test
  (:require [clojure.test :refer :all]
            [pathosim.stat :refer :all]))

(deftest test-create-blueprint
  (testing "valid"
    (let [blueprint (create-stat-blueprint 50 0 100 0.1 -10 10)]
      (is (= 50 (blueprint :initial-amount)))
      (is (= 0 (blueprint :min-amount)))
      (is (= 100 (blueprint :max-amount)))
      (is (= 0.1 (blueprint :mutation-probability)))
      (is (= -10 (blueprint :min-mutation)))
      (is (= 10 (blueprint :max-mutation)))))
  (testing "max-amount must be greater than or equal to min-amount"
    (is (thrown? AssertionError (create-stat-blueprint 50 100 0 0.1 -10 10))))
  (testing "initial-amount must be numeric"
    (is (thrown? AssertionError (create-stat-blueprint "a" 0 100 0.1 -10 10))))
  (testing "min-amount must be numeric"
    (is (thrown? AssertionError (create-stat-blueprint 50 "a" 100 0.1 -10 10))))
  (testing "max-amount must be numeric"
    (is (thrown? AssertionError (create-stat-blueprint 50 0 "a" 0.1 -10 10))))
  (testing "mutation-probability must be numeric"
    (is (thrown? AssertionError (create-stat-blueprint 50 0 100 "a" -10 10))))
  (testing "min-mutation must be numeric"
    (is (thrown? AssertionError (create-stat-blueprint 50 0 100 0.1 "a" 10))))
  (testing "max-mutation must be numeric"
    (is (thrown? AssertionError (create-stat-blueprint 50 0 100 0.1 -10 "a")))))
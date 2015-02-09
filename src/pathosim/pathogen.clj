(ns pathosim.pathogen
  (:require [pathosim.stat :refer :all]))

(defn create-pathogen
  [name stats]
  {:pre [(not (nil? name))
         (every? #(= :pathosim.stat/Stat (type %)) stats)]}
  ^{:type ::Pathogen}
  {:name name
   :stats stats})

(defn- reproduce-pathogen
  [pathogen]
  (let [{stats :stats} pathogen
        basic-reproduction-number-stat (find-stat stats :basic-reproduction-number)
        basic-reproduction-number (:amount basic-reproduction-number-stat)]
    (if (< basic-reproduction-number 1)
      []
      (map mutate-pathogen (repeat basic-reproduction-number pathogen)))))

(defn- mutate-pathogen
  [pathogen]
  (let [luck (rand)
        {:keys [name stats]} pathogen
        mutated-stats (mutate-stats stats luck)]
    (create-pathogen name mutated-stats)))
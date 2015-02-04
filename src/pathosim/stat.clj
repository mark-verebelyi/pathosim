(ns pathosim.stat
  (:require [pathosim.preconditions :refer [numbers? between?]]))

(defn create-stat-blueprint
  [initial-amount min-amount max-amount mutation-probability min-mutation max-mutation]
  {:pre [(numbers? initial-amount min-amount max-amount mutation-probability min-mutation max-mutation)
         (<= min-amount max-amount)
         (between? mutation-probability 0 1)
         (between? initial-amount min-amount max-amount)]}
  {:initial-amount initial-amount
   :min-amount min-amount
   :max-amount max-amount
   :mutation-probability mutation-probability
   :min-mutation min-mutation
   :max-mutation max-mutation})

(defn create-stat
  [name amount blueprint]
  {:pre [(not (nil? name))
         (number? amount)
         (not (nil? blueprint))
         (between? amount (blueprint :min-amount) (blueprint :max-amount))]}
  {:name name
   :amount amount
   :blueprint blueprint})

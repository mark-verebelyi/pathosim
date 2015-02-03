(ns pathosim.stat)

(defn create-stat-blueprint
  [initial-amount min-amount max-amount mutation-probability min-mutation max-mutation]
  {:pre [(number? initial-amount)
         (number? min-amount)
         (number? max-amount)
         (number? mutation-probability)
         (number? min-mutation)
         (number? max-mutation)
         (<= min-amount max-amount)]}
  {:initial-amount initial-amount
   :min-amount min-amount
   :max-amount max-amount
   :mutation-probability mutation-probability
   :min-mutation min-mutation
   :max-mutation max-mutation})

(defn create-stat
  [name amount blueprint]
  {:name name
   :amount amount
   :blueprint blueprint})

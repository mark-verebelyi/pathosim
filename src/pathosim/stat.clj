(ns pathosim.stat
  (:require [pathosim.preconditions :refer [numbers? between?]]
            [pathosim.log :refer [log]]
            [pathosim.randoms :refer [random-int-between]]))

(defn create-stat-blueprint
  [initial-amount min-amount max-amount mutation-probability min-mutation max-mutation]
  {:pre [(numbers? initial-amount min-amount max-amount mutation-probability min-mutation max-mutation)
         (<= min-amount max-amount)
         (between? mutation-probability 0 1)
         (between? initial-amount min-amount max-amount)]}
  ^{:type ::StatBlueprint}
  {:initial-amount initial-amount
   :min-amount min-amount
   :max-amount max-amount
   :mutation-probability mutation-probability
   :min-mutation min-mutation
   :max-mutation max-mutation})

(defn create-stat
  ([name blueprint]
    (create-stat name (:initial-amount blueprint) blueprint))
  ([name amount blueprint]
    {:pre [(not (nil? name))
           (not (nil? blueprint))
           (number? amount)
           (between? amount (blueprint :min-amount) (blueprint :max-amount))]}
    ^{:type ::Stat}
    {:name name
    :amount amount
    :blueprint blueprint}))

(defn mutate-stat
  [stat]
  (let [{:keys [name amount blueprint]} stat
        {:keys [min-amount max-amount min-mutation max-mutation]} blueprint
        mutation-amount (random-int-between min-mutation max-mutation)
        new-proposed-amount (+ amount mutation-amount)
        new-amount (cond
                     (<= new-proposed-amount min-amount) min-amount
                     (>= new-proposed-amount max-amount) max-amount
                     :else new-proposed-amount)]
    (log :DEBUG "Mutating stat [%s]" name)
    (log :DEBUG "  Original amount is               : [%s]" amount)
    (log :DEBUG "  Mutation amount must be          : [%s - %s]" min-mutation max-mutation)
    (log :DEBUG "  Amount must be                   : [%s - %s]" min-amount max-amount)
    (log :DEBUG "  Random mutation amount is        : [%s]" mutation-amount)
    (log :DEBUG "  New proposed amount is           : [%s]" new-proposed-amount)
    (log :DEBUG "  New amount considering bounds is : [%s]" new-amount)
    (create-stat name new-amount blueprint)))

(defn should-mutate-stat?
  [stat luck]
  (let [{name :name {mutation-probability :mutation-probability} :blueprint} stat
        should-mutate (<= luck mutation-probability)]
    (log :DEBUG "Deciding whether stat should be mutated [%s]" name)
    (log :DEBUG "  Mutation probability : [%s]" mutation-probability)
    (log :DEBUG "  Luck                 : [%s]" luck)
    (log :DEBUG "  Should mutate        : [%s]" (if should-mutate "Yes" "No"))
    should-mutate))



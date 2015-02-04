(ns pathosim.preconditions)

(defn numbers? [& xs]
  (every? number? xs))

(defn between? [x low hi]
  (and (<= low x) (<= x hi)))

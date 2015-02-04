(ns pathosim.randoms)

(defn random-int-between [lo hi]
  (let [diff (+ (- hi lo) 1)
        rnd (rand-int diff)]
    (+ rnd lo)))

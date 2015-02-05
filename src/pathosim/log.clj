(ns pathosim.log)

(defn log [level message & arguments]
  (println (str *ns* " - " level ": " (apply format message arguments))))

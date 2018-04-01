(ns line-stat.core
  (:gen-class))

(defn filter-big-lines
  [line max-size]
  (if (> (.length line) max-size)
    line
    nil))

(defn -main
  [mode & args]
  (cond
    (= mode "stat")
    (let [dir (first args)]
      (->> (file-seq (java.io.File. dir))
           (filter (fn [f] (.endsWith (.getPath f) ".java")))
           (remove (fn [f] (.contains (.getPath f) "/test/")))
           (map clojure.java.io/reader)
           (map line-seq)
           flatten
           (remove nil?)
           (map (fn [line] (.length line)))
           frequencies
           prn))

    (= mode "lines")
    (let [dir      (first args)
          max-size (second args)]
      (->> (file-seq (java.io.File. dir))
           (filter (fn [f] (.endsWith (.getPath f) ".java")))
           (remove (fn [f] (.contains (.getPath f) "/test/")))
           (map clojure.java.io/reader)
           (map line-seq)
           flatten
           (remove nil?)
           (map #(filter-big-lines % (Integer. max-size)))
           (remove nil?)
           prn))))

(ns line-stat.core
  (:gen-class))

(defn -main
  [dir & args]
  (->> (file-seq (java.io.File. dir))
       (filter (fn [f] (.endsWith (.getPath f) ".java")))
       (map clojure.java.io/reader)
       (map line-seq)
       flatten
       (remove nil?)
       (map (fn [line] (.length line)))
       frequencies))

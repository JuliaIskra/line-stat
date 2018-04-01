(ns line-stat.core
  (:gen-class))

(defn filter-big-lines
  [line max-size]
  (if (> (.length line) max-size)
    line
    nil))

(defn print-lines
  [[filename big-lines]]
  (str (reduce str (repeat 80 "=")) "\n"
       filename ":\n"
       (reduce str (interpose "\n" big-lines))
       "\n\n"))

(defn -main
  [mode & args]
  (cond
    (= mode "stat")
    (let [dir (first args)]
      (println (str "lines\tcount\n"))
      (->> (file-seq (java.io.File. dir))
           (filter (fn [f] (.endsWith (.getPath f) ".java")))
           ;(remove (fn [f] (.contains (.getPath f) "/test/")))
           (map clojure.java.io/reader)
           (map line-seq)
           flatten
           (remove nil?) ; for empty files
           (map (fn [line] (.length line)))
           frequencies
           (sort-by first)
           (map (fn [[l c]] (str l "\t\t" c "\n")))
           (reduce str)
           (println)))

    (= mode "lines")
    (let [dir       (first args)
          max-size  (second args)
          files     (->> (file-seq (java.io.File. dir))
                         (filter (fn [f] (.endsWith (.getPath f) ".java"))))
          ;(remove (fn [f] (.contains (.getPath f) "/test/")))
          big-lines (for [file files
                          :let [lines (->> file
                                           clojure.java.io/reader
                                           line-seq
                                           (map #(filter-big-lines % (Integer. max-size)))
                                           (remove nil?))]
                          :when (not (empty? lines))]
                      [(.getPath file) lines])]
      (->> big-lines
           (map print-lines)
           (reduce str)
           println))))

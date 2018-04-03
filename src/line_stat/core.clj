(ns line-stat.core
  (:gen-class))

(defn print-line-stat
  [dir]
  (println (str "lines\tcount\n"))
  (->> (file-seq (java.io.File. dir))
       (filter (fn [f] (.endsWith (.getPath f) ".java")))
       (map clojure.java.io/reader)
       (map line-seq)
       flatten
       (remove nil?)  ; for empty files
       (map (fn [line] (.length line)))
       frequencies
       (sort-by first)
       (map (fn [[l c]] (str l "\t\t" c "\n")))
       (reduce str)
       (println)))

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

(defn print-big-lines
  [dir max-size]
  (let [files     (->> (file-seq (java.io.File. dir))
                       (filter (fn [f] (.endsWith (.getPath f) ".java"))))
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
         println)))

(defn -main
  [& args]
  (let [mode (first args)]
    (cond
      (= mode "stat")
      (let [dir (second args)]
        (print-line-stat dir))

      (= mode "lines")
      (let [dir      (second args)
            max-size (nth args 2)]
        (print-big-lines dir max-size))

      :else
      (do
        (println "Unexpected input")
        (println "This program accepts two types of input:")
        (println "stat /path/to/java/project # to show stat on lines")
        (println "lines /path/to/java/project $line-size # to show lines longer than $line-size")))))

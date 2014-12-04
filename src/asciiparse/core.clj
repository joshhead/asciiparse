(ns asciiparse.core
  (:require [clojure.java.io :as io])
  (:gen-class))

(def coll-to-n
  {[\space \_ \space \| \space \| \| \_ \|]                 0
   [\space \space \space \space \space \| \space \space \|] 1
   [\space \_ \space \space \_ \| \| \_ \space]             2
   [\space \_ \space \space \_ \| \space \_ \|]             3
   [\space \space \space \| \_ \| \space \space \|]         4
   [\space \_ \space \| \_ \space \space \_ \|]             5
   [\space \_ \space \| \_ \space \| \_ \|]                 6
   [\space \_ \space \space \space \| \space \space \|]     7
   [\space \_ \space \| \_ \| \| \_ \|]                     8
   [\space \_ \space \| \_ \| \space \_ \|]                 9})

(defn transpose-entry-chars
  "Given a sequence of 3 strings with 27 characters each, return a
  sequence of 9 sequences of 9 characters each.  First seq will be
  Line 1 characters 1, 2, 3, line 2 characters 1, 2, 3, line 3
  characters 1, 2, 3.  Second seq will be Line 1 characters 4, 5, 6,
  line 2 characters 4, 5, 6, line 3 characters 4, 5, 6. And so on."
  [input]
  (->> input
       (map #(partition 3 %))
       (apply #(map list %1 %2 %3))
       (map flatten)))

(defn three-of-four-seq
  "Given a collection, return a seq of three entries at a time,
  dropping every fourth entry."
  [lines]
  (->> lines
       (partition 4)
       (map #(take 3 %))))

(defn reduce-entry
  "Take one entry as three ascii-art lines and convert it to a seq of
  numbers, appending the result to the accumulated collection of
  entries."
  [entries three-lines]
  (conj entries (map coll-to-n (transpose-entry-chars three-lines))))

(defn transform-lines
  "Given a sequence of input ascii-art lines, return a sequence of
  converted numbers. Unrecognized numbers will be nil."
  [lines]
  (reduce reduce-entry [] (three-of-four-seq lines)))

(defn legible-entry?
  "True if all items in the sequence are numbers, false otherwise."
  [entry]
  (every? number? entry))

(defn valid-entry?
  "Validate an entry against checksum calculation. Sequences shorter
  or longer than 9 characters are always considered invalid.  Return
  true if entry passes checksum, false otherwise."
  [entry]
  (and (= (count entry) 9)
       (-> (map * [9 8 7 6 5 4 3 2 1] entry)
           (#(reduce + %))
           (mod 11)
           (= 0))))

(defn entry->str
  [entry]
  (apply str (replace {nil \?} entry)))

(defn entry->status
  [entry]
  (cond
   (not (legible-entry? entry)) ::illegible
   (not (valid-entry? entry)) ::invalid
   :else ::valid))

(defn status->str
  "Return a formatted string. Valid or unknown status are empty
  string, others have a leading space so all can be appended to entry
  number in formatted output."
  [status]
  (get {::illegible " ILL"
        ::invalid " ERR"
        ::valid ""} status ""))

(defn formatted-entries
  "Return sequence of strings formatted for printing.
  e.g. [[4 5 7 5 0 8 0 0 0]
        [6 6 4 3 7 1 4 9 5]
        [8 6 1 1 0 nil nil 3 6]]
  becomes (\"457508000\"
           \"664371495 ERR\"
           \"86110??36 ILL\")"
  [entries]
  (->> entries
       (map (juxt entry->str (comp status->str entry->status)))
       (map #(apply str %))))

(defn write-formatted
  "Write formatted entries to given output stream."
  [out entries]
  (dorun (map #(.write out (str % \newline)) (formatted-entries entries))))

(defn print-formatted
  "Print formatted entries to standard out."
  [entries]
  (write-formatted *out* entries))

(defn formatted-string
  "Return a string containing formatted output for all entries."
  [entries]
  (with-out-str (print-formatted entries)))

(defn -main
  ([]
   (println "Usage: asciiparse infile [outfile]"))
  ([one two & more]
   (-main))
  ([infile]
   (with-open [r (io/reader infile)]
     (let [entries (transform-lines (line-seq r))]
       (print-formatted entries))))
  ([infile outfile]
   (with-open [r (io/reader infile)
               w (io/writer outfile)]
     (let [entries (transform-lines (line-seq r))]
       (write-formatted w entries)))))
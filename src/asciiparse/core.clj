(ns asciiparse.core
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
  [entry]
  (every? number? entry))

(defn valid-entry?
  [entry]
  (and (= (count entry) 9)
       (-> (map * [9 8 7 6 5 4 3 2 1] entry)
           (#(reduce + %))
           (mod 11)
           (= 0))))

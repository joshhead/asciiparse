(ns asciiparse.core-test
  (:require [clojure.test :refer :all]
            [asciiparse.core :refer :all]
            [clojure.java.io :as io]))

(deftest parse-well-formed-file
  (testing "Read in well formed input and get expected output."
    (let [parsed (with-open [r (io/reader "testdata/wellformed.txt")]
                   (doall (transform-lines (line-seq r))))]
      (is (= parsed [[1 2 3 4 5 6 7 8 9]
                     [0 0 0 0 0 0 0 0 0]
                     [1 1 1 1 1 1 1 1 1]
                     [2 2 2 2 2 2 2 2 2]
                     [3 3 3 3 3 3 3 3 3]
                     [4 4 4 4 4 4 4 4 4]
                     [5 5 5 5 5 5 5 5 5]
                     [6 6 6 6 6 6 6 6 6]
                     [7 7 7 7 7 7 7 7 7]
                     [8 8 8 8 8 8 8 8 8]
                     [9 9 9 9 9 9 9 9 9]])))))

(deftest read-illegible-input
  (testing "Read well formed input with illegible numbers."
    (let [parsed (with-open [r (io/reader "testdata/illegible.txt")]
                   (doall (transform-lines (line-seq r))))]
      (is (= parsed [[1 2 3 4 nil 6 7 8 nil]])))))

(deftest trailing-space-in-file
  (testing "Read mostly good file with trailing whitespace and newline"
    (let [parsed (with-open [r (io/reader "testdata/extraspace.txt")]
                   (doall (transform-lines (line-seq r))))]
      (is (= parsed [[1 2 3 4 5 6 7 8 9]
                     [0 0 0 0 0 0 0 0 0]
                     [1 1 1 1 1 1 1 1 1]
                     [2 2 2 2 2 2 2 2 2]])))))

(deftest checksums
  (testing "Checksum function works on example inputs."
    (is (valid-entry? [7 1 1 1 1 1 1 1 1]))
    (is (valid-entry? [1 2 3 4 5 6 7 8 9]))
    (is (valid-entry? [4 9 0 8 6 7 7 1 5]))
    (is (not (valid-entry? [8 8 8 8 8 8 8 8 8])))
    (is (not (valid-entry? [4 9 0 0 6 7 7 1 5])))
    (is (not (valid-entry? [0 1 2 3 4 5 6 7 8])))))


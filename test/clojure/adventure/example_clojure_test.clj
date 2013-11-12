(ns clojure.adventure.example-clojure-test
  (:use clojure.test))

(deftest clojure-test-example
  (is (= (+ 21 21) 42))
  (is (= (* 21 2) 42)))

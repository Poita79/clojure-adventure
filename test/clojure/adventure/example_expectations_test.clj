(ns clojure.adventure.example-expectations-test
  (:use [expectations]))

(expect 42 (+ 21 21))
(expect 42 (* 21 2))

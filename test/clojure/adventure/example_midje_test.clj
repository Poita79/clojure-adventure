(ns clojure.adventure.example-midje-test
  (:use [midje.sweet]))

(facts "Can create grouped facts"
       (fact "Testing (+ 21 21) = 42"
             (+ 21 21) => 42)

       (fact "Testing (* 21 2) = 42"
             (* 21 2) => 42))



(defproject clojure-adventure "0.0.1"
  :description "Clojure Adventure"
  :license "Eclipse Public License 1.0"
  :url "http://example.com"
  :min-lein-version "2.0.0"

  :aot  'clojure.adventure.main
  :main clojure.adventure.main

  :dependencies [[org.clojure/clojure "1.5.1"]
                 [clojure-lanterna "0.9.4"]
                 [org.clojure/tools.namespace "0.2.4"]

;                 [expectations "1.4.52" :scope "test"]
;                 [midje "1.5.1" :scope "test"]
                 ]

  :libdir-path "lib"

  :plugins [[lein-libdir "0.1.1"]
;            [lein-expectations "0.0.7"]
;            [lein-autoexpect "1.0"]
;            [lein-midje "3.0.0"]
            ])
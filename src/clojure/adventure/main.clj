(ns clojure.adventure.main
  (:require [lanterna.screen :as screen]
            [clojure.tools.namespace :as ns]
            [clojure.java.io :as io]
            [clojure.adventure.game :as game])
  (:gen-class))

(defn reload [gameState]
  (doseq [ns (remove #{*ns*} (ns/find-namespaces-in-dir (io/file "src/clojure/adventure")))]
    (require ns :reload))
  (dissoc gameState :last-error))

(defn -main [& m]
  (let [scr (screen/get-screen (or (keyword (first m)) :swing))
        gameState (atom (game/init nil))]
    (screen/in-screen
      scr
      (screen/clear scr)
      (game/update scr @gameState)
      (screen/redraw scr)

      (loop []
        (let [key (screen/get-key-blocking scr)]
          (screen/clear scr)

          (reset! gameState
                  (try
                    (game/update scr (cond
                                       (= :right key) (game/move-right @gameState)
                                       (= :left key) (game/move-left @gameState)
                                       (= :up key) (game/move-up @gameState)
                                       (= :down key) (game/move-down @gameState)
                                       (= :backspace key) (game/init @gameState)
                                       (= :enter key) (reload @gameState)
                                       :else @gameState))
                    (catch Throwable e (assoc @gameState :last-error e))))

          (if-let [error (get-in @gameState [:last-error])]
            (screen/put-sheet scr 0 0 (cons (.toString error) (map str (seq (.getStackTrace error))))))

          (screen/redraw scr)

          (if-not (= key :escape) (recur))

          )
        )
      )
    )
  )

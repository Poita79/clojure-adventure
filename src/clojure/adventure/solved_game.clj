(ns clojure.adventure.solved-game
  (:require [lanterna.screen :refer :all]))

(defn load-fields [lines]
  (into {} (for [[y line] (map vector (range) lines)
                 x (range (count line))
                 :when (not= \space (nth line x))]
             [[x y] :wall])))

(defn random-position [[width height] fields]
  (first
    (drop-while #(contains? fields %)
                (repeatedly #(vector (rand-int width) (rand-int height))))))

(defn load-maze [file items]
  (let [lines (clojure.string/split (slurp file) #"\n")
        [width height :as size] [(count (first lines)) (count lines)]
        newMap (load-fields lines)
        fields (merge
                 (into {} (map #(vector (random-position size newMap) %) (rest items)))
                 {[(dec width) (- height 2)] (first items)}
                 newMap)]

    {:fields fields
     :items  items
     :size   size}))


(defn init [gameState]
  {:map    (load-maze "src/clojure/adventure/data/maze0.txt" [\∆ \ø \π \∂])
   :player {
             :inventory []
             :position  [0 1]
             }})

(defn update [screen gameState]
  (let [[x y] (get-in gameState [:player :position])]
    (put-string screen x y "ƒ" {:fg :blue :bg :cyan}))

  (doseq [[[x y] value] (get-in gameState [:map :fields])]
    (cond
      (= :wall value) (put-string screen x y " " {:bg :red})
      :else (put-string screen x y (str value) {:fg :yellow :styles [:blinking :bold]})))

  (let [[width height] (get-in gameState [:map :size])]
    (if (= (set (get-in gameState [:player :inventory])) (set (get-in gameState [:map :items])))
      (put-string screen (/ (- width 11) 2) (/ height 2) " Game Over " {:bg :white :fg :blue :styles [:blinking]})
      (put-string screen 0 height (str "Collected: " (reduce str (get-in gameState [:player :inventory]))))))

  gameState)

(defn current-item [gameState]
  ((get-in gameState [:map :fields]) (get-in gameState [:player :position])))

(defn remove-current-item [gameState]
  (update-in gameState [:map :fields] #(dissoc % (get-in gameState [:player :position]))))

(defn add-item-to-inventoty [item gameState]
  (update-in gameState [:player :inventory] #(conj % item)))

(defn take-item [gameState]
  (when-let [item (current-item gameState)]
    (->> gameState
         (remove-current-item)
         (add-item-to-inventoty item))))

(defn outside? [[width height :as size] [x y :as position]]
  (or (< x 0) (< y 0) (>= x width) (>= y height)))

(defn wall? [item]
  (= :wall item))

(defn move-player [gameState [fnX fnY]]
  (let [newGameState (update-in gameState [:player :position] (fn [[x y]] [(fnX x) (fnY y)]))
        newPosition (get-in newGameState [:player :position])
        collision ((get-in newGameState [:map :fields]) newPosition)]
    (cond
      (outside? (get-in gameState [:map :size]) newPosition) gameState
      (wall? collision) gameState
      (nil? collision) newGameState
      :else (take-item newGameState)
      )))

(defn move-right [gameState]
  (move-player gameState [inc identity]))

(defn move-left [gameState]
  (move-player gameState [dec identity]))

(defn move-down [gameState]
  (move-player gameState [identity inc]))

(defn move-up [gameState]
  (move-player gameState [identity dec]))
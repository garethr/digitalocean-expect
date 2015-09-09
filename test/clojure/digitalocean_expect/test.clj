(ns clojure.digitalocean-expect.test
  (:require [digitalocean.v2.core :as do]
            [environ.core :refer [env]]
            [expectations :refer [expect from-each]]))

(defonce token (env :digitalocean-access-token))

(def nodes ((do/droplets token) :droplets))

(defn active? [node]
  (= (:status node) "active"))

(defn stopped? [node]
  (= (:status node) "stopped"))

(def active-nodes (filter active? nodes))

(def stopped-nodes (filter stopped? nodes))

(defn small? [node] (= (:size_slug node) "512mb" ))
(defn medium? [node] (= (:size_slug node) "gb" ))
(defn large? [node] (= (:size_slug node) "8gb" ))

(defn london? [node] (= (:slug (:region node)) "lon1"))
(defn sf? [node] (= (:slug (:region node)) "sfo1"))

(defn valid-name? [node]
  (re-matches #"test|staging|prod+-[a-z]+" (:name node)))

(defn using-ubuntu? [node]
  (= "Ubuntu" (:distribution (:image node))))

(defn feature-named? [node feature]
  (.contains (:features node) feature))

(defn backups-enabled? [node]
  (feature-named? node "backups"))

(defn private-networking-enabled? [node]
  (feature-named? node "private_networking"))

; not more than 100 nodes
(expect (< (count nodes) 100))

; check number of stopped nodes
(expect 0 (count stopped-nodes))

; check node with specific name
(expect true (.contains (map :name nodes) "Test"))

; check backups are enabled for all nodes
(expect (every? backups-enabled? nodes))

; check private networks are enabled for all nodes
(expect (every? private-networking-enabled? nodes))

; only use prescribed sizes
(expect 0
  (count
    (remove large?
      (remove medium?
        (remove small? nodes)))))

; only use prescribed regions
(expect 0
  (count
    (remove london?
      (remove sf? nodes))))

; no more than 2 large nodes
(expect (< (count (filter large? nodes)) 2))

; check names match prescribed pattern
(expect (every? valid-name? nodes))

; check all droplets are using the correct OS
(expect (every? using-ubuntu? nodes))

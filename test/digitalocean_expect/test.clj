(ns digitalocean-expect.test
  (:require [digitalocean.droplet :refer :all]
            [environ.core :refer [env]])
  (:use expectations))

(def nodes (droplets (env :digitalocean-client-id) (env :digitalocean-api-key)))
(def creds {:client (env :digitalocean-client-id) :key (env :digitalocean-api-key)})

(def not-nil? (complement nil?))

(defn nodes-by-status [status]
  (droplets-with-status creds status))

(def stopped-nodes (nodes-by-status "stopped"))
(def active-nodes (nodes-by-status "active"))

(defn small? [size] (= 66 size))
(defn medium? [size] (= 62 size))
(defn large? [size] (= 60 size))
(defn london? [region] (= 7 region))
(defn sf? [region] (= 3 region))

; not more than 100 nodes
(expect (< (count nodes) 100))

; check number of stopped nodes
(expect 0 (count stopped-nodes))

; check node with specific name
(expect (droplet-by-name creds "test-digitalocean"))

; check backups are disabled for all nodes
(expect false? (from-each
  [node (map :backups_active nodes)] node))

; check private networks are disabled for all nodes
(expect nil? (from-each
  [node (map :private_ip_address nodes)] node))

; only use prescribed sizes
(expect 0
  (count
    (filter (complement large?)
      (filter (complement medium?)
        (filter (complement small?) (map :size_id nodes))))))

; only use prescribed regions
(expect 0
  (count
    (filter (complement london?)
      (filter (complement sf?) (map :region_id nodes)))))

; no more than 2 large nodes
(expect (< (count (filter large? (map :size_id nodes))) 2))

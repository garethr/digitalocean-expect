(ns digitalocean-expect.test
  (:require [digitalocean.droplet :refer :all]
            [environ.core :refer [env]])
  (:use expectations))

(def nodes (droplets (env :digitalocean-client-id) (env :digitalocean-api-key)))
(def creds {:client (env :digitalocean-client-id) :key (env :digitalocean-api-key)})

(defn nodes-by-status [status]
  (droplets-with-status creds status))

(def stopped-nodes (nodes-by-status "stopped"))
(def active-nodes (nodes-by-status "active"))

; these magic numbers are unfortunate details
; of the DigitalOcean API

(def DIGITALOCEAN-SMALL-ID 66)
(def DIGITALOCEAN-MEDIUM-ID 62)
(def DIGITALOCEAN-LARGE-ID 60)

(def DIGITALOCEAN-LONDON-ID 7)
(def DIGITALOCEAN-SANFRANCISCO-ID 3)

(defn small? [size] (= DIGITALOCEAN-SMALL-ID size))
(defn medium? [size] (= DIGITALOCEAN-MEDIUM-ID size))
(defn large? [size] (= DIGITALOCEAN-LARGE-ID size))
(defn london? [region] (= DIGITALOCEAN-LONDON-ID region))
(defn sf? [region] (= DIGITALOCEAN-SANFRANCISCO-ID region))

; not more than 100 nodes
(expect (< (count nodes) 100))

; check number of stopped nodes
(expect 0 (count stopped-nodes))

; check node with specific name
(expect (droplet-by-name creds "test-digitalocean"))

; check backups are disabled for all nodes
(expect false? (from-each
  [node nodes] (:backups_active node)))

; check private networks are disabled for all nodes
(expect nil? (from-each
  [node nodes] (:private_ip_address node)))

; only use prescribed sizes
(expect 0
  (count
    (remove large?
      (remove medium?
        (remove small? (map :size_id nodes))))))

; only use prescribed regions
(expect 0
  (count
    (remove london?
      (remove sf? (map :region_id nodes)))))

; no more than 2 large nodes
(expect (< (count (filter large? (map :size_id nodes))) 2))

(ns digitalocean-expect.test
  (:require [digitalocean.droplet :refer :all]
            [environ.core :refer [env]]
            [expectations :refer [expect from-each]]))

(def nodes (droplets (env :digitalocean-client-id) (env :digitalocean-api-key)))
(def creds {:client (env :digitalocean-client-id) :key (env :digitalocean-api-key)})

(defn nodes-by-status [status]
  (droplets-with-status creds status))

(def stopped-nodes (nodes-by-status "stopped"))
(def active-nodes (nodes-by-status "active"))

(def DIGITALOCEAN-SIZES
  {:small  66
   :medium 62
   :large  60})

(def DIGITALOCEAN-LOCATIONS
  {:london       7
   :sanfrancisco 3})

(defn size-id [size id]
  (= (size DIGITALOCEAN-SIZES) id))

(defn small? [id] (size-id :small id))
(defn medium? [id] (size-id :medium id))
(defn large? [id] (size-id :large id))

(defn location-id [location region-id]
  (= (location DIGITALOCEAN-LOCATIONS) region-id))

(defn london? [region] (location-id :london region))
(defn sf? [region] (location-id :sanfrancisco region))

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

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

; check number of nodes
(expect 2 (count nodes))

; check number of stopped nodes
(expect 0 (count stopped-nodes))

; check node with specific name
(expect 0 (count (droplet-by-name creds "brian")))

(ns digitalocean-expect.test
  (:require [digitalocean.droplet :refer :all]
            [environ.core :refer [env]])
  (:use expectations))

(def nodes (droplets (env :digitalocean-client-id) (env :digitalocean-api-key)))

; check number of nodes
(expect 2 (count nodes))

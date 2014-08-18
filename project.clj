(defproject digitalocean-expect "0.1.0-SNAPSHOT"
  :description "Experiments writing tests against an IaaS provider"
  :url "https://github.com/garethr/digitalocean-expect"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :plugins [
            [lein-expectations "0.0.7"]
            [lein-kibit "0.0.8"]
            [lein-autoexpect "1.0"]]
  :dependencies [
                 [org.clojure/clojure "1.5.1"]
                 [expectations "2.0.9"]
                 [environ "0.5.0"]
                 [digitalocean "0.1.0"]])

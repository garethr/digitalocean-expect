(defproject digitalocean-expect "0.1.0-SNAPSHOT"
  :description "Experiments writing tests against an IaaS provider"
  :url "https://github.com/garethr/digitalocean-expect"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :junit ["test/java"]
  :java-source-paths ["test/java"]
  :plugins [
            [lein-expectations "0.0.7"]
            [lein-kibit "0.0.8"]
            [lein-ancient "0.6.7"]
            [jonase/eastwood "0.1.4"]
            [lein-junit "1.1.2"]
            [lein-autoexpect "1.0"]]
  :aliases {"test" ["expectations"]}
  :dependencies [
                 [org.clojure/clojure "1.5.1"]
                 [expectations "2.1.3"]
                 [environ "1.0.0"]
                 [junit/junit "4.12"]
                 [digitalocean "1.2"]])

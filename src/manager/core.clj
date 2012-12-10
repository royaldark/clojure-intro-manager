(ns manager.core
  (:gen-class)
  (:import [java.io File])
  (:require [manager.gui :as gui]
            [manager.file :as file]
            [manager.config :as config]))

(defn -main [& args]
  (let [config-dir-str (file/path (System/getProperty "user.home") ".ummclj")
        config-dir (File. config-dir-str)
        config-file (File. (str config-dir-str (File/separator) "config"))]
    (config/setup-config-dir config-dir)
    (config/read-config-file config-file)))

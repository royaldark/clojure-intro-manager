(ns manager.core
  (:import [java.io File])
  (:require [clojure.java.io :as io]
            [manager.gui :as gui]
            [manager.file :as file])
  (:use [seesaw.core]
        [clojure.string :only [join split]]))





(defn write-default-config [config-file]
  (let [default-workspace (file/path (System/getProperty "user.home") "clj-workspace")]
    (gui/ask 
      "You do not appear to have a workspace set up. Please choose a location (the default should be fine in most cases)."
      default-workspace
      (fn [success value] (if success
                            (do
                              (println (type value))
                              (file/mkdir value)
                              (file/write config-file (str "project-dir=" value)))
                            (do
                              (file/rm config-file)
                              (System/exit 1)))))))


(defn parse-config-file [config-file]
  "")


(defn setup-config-file [config-file]
  (when-not (. config-file exists)
    (write-default-config config-file))
  (parse-config-file config-file))


(defn setup-config-dir
  "Attempts to make the configuration directory.
  If it already exists, this will fail silently."
  [config-dir]
  (. config-dir mkdir))


(defn -main [& args]
  (let [config-dir-str (str (System/getProperty "user.home") (File/separator) ".ummclj")
        config-dir (File. config-dir-str)
        config-file (File. (str config-dir-str (File/separator) "config"))]
    (setup-config-dir config-dir)
    (setup-config-file config-file)
    (gui/show-main-window)))

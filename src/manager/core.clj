(ns manager.core
  (:gen-class)
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
                              (file/mkdir value)
                              (file/write config-file (str "project-dir=" value)))
                            (do
                              (file/rm config-file)
                              (System/exit 1)))))))

(defn get-opt [opt txt]
  (let [pattern (re-pattern (str "^" opt "=(.*)"))
        matches (re-find pattern txt)]
    (if (< (count matches) 2)
      (throw (Exception. (str "Problem getting option " opt " from config file:" matches)))
      (nth matches 1))))


(defn parse-config-file [config-file]
  (let [config-contents (slurp config-file)
        proj-dir (get-opt "project-dir" config-contents)]
    {:proj-dir proj-dir}))


(defn read-config-file [config-file]
  (when-not (. config-file exists)
    (write-default-config config-file))
  (parse-config-file config-file))


(defn setup-config-dir
  "Attempts to make the configuration directory.
  If it already exists, this will fail silently."
  [config-dir]
  (. config-dir mkdir))

(defn get-projects [proj-dir]
  (let [files (.listFiles (File. proj-dir))
        dirs (filter #(.isDirectory %) files)]
    dirs))


(defn -main [& args]
  (let [config-dir-str (file/path (System/getProperty "user.home") ".ummclj")
        config-dir (File. config-dir-str)
        config-file (File. (str config-dir-str (File/separator) "config"))]
    (setup-config-dir config-dir)
    (get-projects (:proj-dir (read-config-file config-file)))
    (gui/show-main-window)))

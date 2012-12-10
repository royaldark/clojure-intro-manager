(ns manager.config
  (:import [java.io File])
  (:require [manager.file :as file]
            [manager.gui :as gui]))

(defn get-opt [opt txt]
  (let [pattern (re-pattern (str "^" opt "=(.*)"))
        matches (re-find pattern txt)]
    (if (< (count matches) 2)
      (throw (Exception. (str "Problem getting option " opt " from config file: " matches)))
      (nth matches 1))))


(defn parse-config-file [config-file]
  (let [config-contents (slurp config-file)
        proj-dir (get-opt "project-dir" config-contents)]
    {:proj-dir proj-dir}))


(defn start-manager [config-file]
  (gui/show-main-window
    (file/get-projects
      (:proj-dir
        (parse-config-file config-file)))))


(defn write-default-config [config-file]
  (let [default-workspace (file/path (System/getProperty "user.home") "clj-workspace")]
    (gui/ask 
      "You do not appear to have a workspace set up. Please choose a location (the default should be fine in most cases)."
      default-workspace
      (fn [success value] (if success
                            (do
                              (file/mkdir value)
                              (file/write config-file (str "project-dir=" value))
                              (start-manager config-file))
                            (do
                              (file/rm config-file)
                              (System/exit 1)))))))


(defn read-config-file [config-file]
  (if (. config-file exists)
    (do
      (parse-config-file config-file)
      (start-manager config-file))
    (write-default-config config-file)))


(defn setup-config-dir
  "Attempts to make the configuration directory.
  If it already exists, this will fail silently."
  [config-dir]
  (. config-dir mkdir))

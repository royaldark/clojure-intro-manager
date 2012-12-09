(ns manager.core
  (:import [java.io File])
  (:require [clojure.java.io :as io]
            [manager.gui :as gui])
  (:use [seesaw.core]
        [clojure.string :only [join split]]))


(defn path [& dirs]
  (join (File/separator) dirs))


(defn write-default-config [config-file]
  (let [default-contents (str "project-dir=" (path (System/getProperty "user.home") "clj-workspace"))
        output (io/writer config-file)]
    (.write output default-contents)
    (.close output)))


(defn parse-config-file [config-file]
  "")


(defn setup-config-file [config-file]
  (when (. config-file createNewFile)
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

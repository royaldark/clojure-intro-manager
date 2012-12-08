(ns manager.core
  (:gen-class)
  (:import [java.io File])
  (:require [clojure.java.io :as io])
  (:use [seesaw.core]
        [clojure.string :only [join split]]))

(def version "0.1")

(def f (frame :title "Clojure Manager"
              :on-close :exit))

(defn display [content]
  (config! f :content content)
  (-> f pack! show!)
  content)

(defn make-button [desc]
  (apply button desc))

(def buttons (map make-button [[:text "New Project"]
                               [:text "Open Project"]
                               [:text "Run Project"]
                               [:text "Duplicate Project"]
                               [:text "Import Project"]
                               [:text "Export Project"]
                               [:text "Exit"
                                :listen [:action (fn [e] (dispose! f))]]]))

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
    (invoke-later
      (native!)
      (def panel (grid-panel :rows (+ (count buttons) 1) :columns 1
                             :hgap 5 :vgap 5 :border 5
                             :items (cons (str "UMM Clojure Manager v." version) buttons)))
      (display panel))))

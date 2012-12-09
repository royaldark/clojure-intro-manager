(ns manager.file
  (:import [java.io File])
  (:require [clojure.java.io :as io])
  (:use [clojure.string :only [join split]]))


(defn path [& dirs]
  (join (File/separator) dirs))

(defn mkdir [dir]
  (.mkdir (File. dir)))

(defn rm [f]
  (if (instance? File f)
    (.delete f)
    (.delete (File. f))))

(defn write [file contents]
  (let [output (io/writer file)]
    (.write output contents)
    (.close output)))

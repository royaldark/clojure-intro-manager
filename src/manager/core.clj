(ns manager.core
  (:use [seesaw.core]))

(def version "0.1")

(def f (frame :title "Clojure Manager"
              :on-close :exit))

(defn display [content]
  (config! f :content content)
  (-> f pack! show!)
  content)

(defn make-button [text]
  (button :text text))

(defn -main [& args]
  (invoke-later
    (native!)
    (def buttons (map make-button ["New Project", "Open Project", "Run Project", "Exit"]))
    (def panel (grid-panel :rows 5 :columns 1
                           :hgap 5 :vgap 5 :border 5
                           :items (cons (str "UMM Clojure Manager v." version) buttons)))
    (display panel)))

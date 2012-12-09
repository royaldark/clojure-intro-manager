(ns manager.gui
  (:use [seesaw.core]))

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

(defn show-main-window []
  (invoke-later
    (native!)
    (def panel (grid-panel :rows (+ (count buttons) 1) :columns 1
                           :hgap 5 :vgap 5 :border 5
                           :items (cons (str "UMM Clojure Manager v." version) buttons)))
    (display panel)))

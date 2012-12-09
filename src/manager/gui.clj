(ns manager.gui
  (:use [seesaw.core]))

; Always use the native look and feel.
(native!)

(def version "0.1")

(def f (frame :title "Clojure Manager"
              :on-close :exit))

(defn display
  ([content] 
   (display content f))
  ([content frame]
   (config! frame :content content)
   (-> frame pack! show!)
   content))

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

(defn ask [txt deftxt callback]
  (invoke-later
    (let [txtbox (text :text deftxt)
          dlg (dialog :type :question
                      :option-type :ok-cancel
                      :success-fn (fn [p] (callback true (.getText txtbox)))
                      :cancel-fn (fn [p] (callback false nil))
                      :content (grid-panel
                                 :rows 2 :columns 1
                                 :items [txt txtbox]))]
      (-> dlg pack! show!))))


(defn show-main-window []
  (invoke-later
    (def panel (grid-panel :rows (+ (count buttons) 1) :columns 1
                           :hgap 5 :vgap 5 :border 5
                           :items (cons (str "UMM Clojure Manager v." version) buttons)))
    (display panel)))

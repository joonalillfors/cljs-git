(ns cljs-test.core
    (:require-macros [cljs.core.async.macros :refer [go]])
    (:require
      [reagent.core :as r]
      [cljs-http.client :as http]
      [cljs.core.async :refer [<!]]))

;; -------------------------
;; Views

(defonce input-name (r/atom ""))

(defonce latest (r/atom ""))

(defn user []
    [:div 
        [:p "Username: " @latest] ])
        ;[:p "Data: " d]])

(defn input []
    [:div 
        [:p "Enter github handler:"]
        [:div
            [:input {:on-change #(reset! input-name (-> % .-target .-value)) ; % = event ; same as event.target.value
                     :value @input-name}]
            [:button {:on-click #(reset! latest @input-name)} "Jesus"]]])

(defn show-if []
    (when (-> @latest clojure.string/blank? false?)
        (go (let [res (<! (http/get (str "http://localhost:3001/api/languages/" @latest)))]
            (prn (res :body)))) ;))
        (user)))

(defn home-page []
  [:div 
    [:h2 "Welcome to Reagent"]
    [:p "This is my test app."]
    (input)
    (show-if)])

;; -------------------------
;; Initialize app

(defn mount-root []
  (r/render [home-page] (.getElementById js/document "app")))

(defn ^:export init! []
  (mount-root))

(ns cljails.handler.index
  (:require [ataraxy.response :as response]
            [clojure.java.io :as io]
            [integrant.core :as ig]))


(defmethod ig/init-key :cljails.handler/index [_ _]
  (fn [_]
    [::response/ok (io/resource "cljails/handler/index.html")]))

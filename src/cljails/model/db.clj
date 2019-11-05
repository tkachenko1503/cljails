(ns cljails.model.db
  (:require [datahike.api :as datahike]
            [integrant.core :as integrant]))


(defmethod integrant/init-key :cljails.model/db [_ {:keys [uri schema]}]
  (datahike/create-database uri :initial-tx schema)
  (datahike/connect uri))


(defmethod integrant/halt-key! :cljails.model/db [_ connection]
  (datahike/release connection)
  (let [uri (get-in @connection [:config :storage :uri])]
    (datahike/delete-database uri)))


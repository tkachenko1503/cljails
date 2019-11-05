(ns cljails.model.schema
  (:require [integrant.core :as integrant]))


(defn- tag->attr [tag]
  (cond
    (= ::identity tag) [:db/unique :db.unique/identity]
    (= ::value tag) [:db/unique :db.unique/value]
    (= ::component tag) [:db/isComponent true]
    (= ::index tag) [:db/index true]
    (= ::ref tag) [:db/valueType :db.type/ref]
    (= ::many tag) [:db/cardinality :db.cardinality/many]
    (string? tag) [:db/doc tag]
    (= ::bigdec tag) [:db/valueType :db.type/bigdec]
    (= ::bigint tag) [:db/valueType :db.type/bigint]
    (= ::boolean tag) [:db/valueType :db.type/boolean]
    (= ::double tag) [:db/valueType :db.type/double]
    (= ::float tag) [:db/valueType :db.type/float]
    (= ::instant tag) [:db/valueType :db.type/instant]
    (= ::keyword tag) [:db/valueType :db.type/keyword]
    (= ::long tag) [:db/valueType :db.type/long]
    (= ::string tag) [:db/valueType :db.type/string]
    (= ::symbol tag) [:db/valueType :db.type/symbol]
    (= ::uuid tag) [:db/valueType :db.type/uuid]
    :else (throw (ex-info (str "Unexpected tag: " tag) {:tag tag}))))


(defn- read-schema [schema]
  (into
    []
    (for [[ns attrs] schema
          [attr tags] attrs]
      (if (= tags ::enum)
        {:db/ident (keyword (name ns) (name attr))}

        (into
          {:db/ident       (keyword (name ns) (name attr))
           :db/cardinality :db.cardinality/one}
          (map tag->attr tags))))))


(defmethod integrant/init-key :cljails.model/schema [_ _]
  (read-schema
    {:user
     {:id         [::uuid ::identity "User ID"]
      :first-name [::string "First name"]
      :last-name  [::string "Last name"]
      :email      [::string ::identity "Email"]
      :password   [::string "Password hash"]
      :role       [::ref "User role"]}

     :user.role
     {:admin ::enum
      :user  ::enum}}))

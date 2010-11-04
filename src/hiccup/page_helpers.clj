(ns hiccup.page-helpers
  "Functions for generating various common elements."
  (:import java.net.URLEncoder)
  (:require [clojure.string :as string])
  (:use [hiccup.core :only (defelem html as-str)]))

(def doctype
  {:html4
   (str "<!DOCTYPE html PUBLIC \"-//W3C//DTD HTML 4.01//EN\" "
        "\"http://www.w3.org/TR/html4/strict.dtd\">\n")
   :xhtml-strict
   (str "<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Strict//EN\" "
        "\"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd\">\n")
   :xhtml-transitional
   (str "<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" "
        "\"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">\n")
   :html5
   "<!DOCTYPE html>\n"})

(defn xhtml-tag
  "Create an XHTML tag for the specified language."
  [lang & contents]
  [:html {:xmlns "http://www.w3.org/1999/xhtml"
          "xml:lang" lang
          :lang lang}
    contents])

(defn xml-declaration
  "Create a standard XML declaration for the following encoding."
  [encoding]
  (str "<?xml version=\"1.0\" encoding=\"" encoding "\"?>\n"))

(defmacro html4
  "Create a HTML 4 document with the supplied contents. The first argument
  may be an optional attribute map."
  [& contents]
  `(html {:mode :sgml}
     (doctype :html4)
     [:html ~@contents]))

(defmacro xhtml
  "Create a XHTML 1.0 strict document with the supplied contents. The first
  argument may be an optional attribute may. The following attributes are
  treated specially:
    :lang     - The language of the document
    :encoding - The character encoding of the document, defaults to UTF-8."
  [options & contents]
  (if-not (map? options)
    `(xhtml {} ~options ~@contents)
    `(let [options# ~options]
       (html {:mode :xml}
         (xml-declaration (options# :encoding "UTF-8"))
         (doctype :xhtml-strict)
         (xhtml-tag (options# :lang) ~@contents)))))

(defmacro html5
  "Create a HTML5 document with the supplied contents."
  [options & contents]
  (if-not (map? options)
    `(html5 {} ~options ~@contents)
    (if (options :xml?)
      `(let [options# ~options]
         (html {:mode :xml}
           (xml-declaration (options# :encoding "UTF-8"))
           (doctype :html5)
           (xhtml-tag (options# :lang) ~@contents)))
      `(let [options# ~options]
         (html {:mode :html}
           (doctype :html5)
           [:html {:lang (options# :lang)} ~@contents])))))

(defn include-js
  "Include a list of external javascript files."
  [& scripts]
  (for [script scripts]
    [:script {:type "text/javascript", :src script}]))

(defn include-css
  "Include a list of external stylesheet files."
  [& styles]
  (for [style styles]
    [:link {:type "text/css", :href style, :rel "stylesheet"}]))

(defn javascript-tag
  "Wrap the supplied javascript up in script tags and a CDATA section."
  [script]
  [:script {:type "text/javascript"}
    (str "//<![CDATA[\n" script "\n//]]>")])

(defelem link-to
  "Wraps some content in a HTML hyperlink with the supplied URL."
  [url & content]
  [:a {:href url} content])

(defelem unordered-list
  "Wrap a collection in an unordered list"
  [coll]
  [:ul (for [x coll] [:li x])])

(defelem ordered-list
  "Wrap a collection in an unordered list"
  [coll]
  [:ol (for [x coll] [:li x])])

(defn encode-params
  "Turn a map of parameters into a urlencoded string."
  [params]
  (letfn [(encode [s] (URLEncoder/encode (as-str s)))]
    (string/join "&"
      (for [[k v] params]
        (str (encode k) "=" (encode v))))))

(defn url
  "Creates a URL string from a variable list of arguments and an optional
  parameter map as the last argument. For example:
    (url \"/group/\" 4 \"/products\" {:page 9})
    => \"/group/4/products?page=9\""
  [& args]
  (let [params (last args)
        args   (butlast args)]
    (str (apply str args)
         (if (map? params)
           (str "?" (encode-params params))
           params))))

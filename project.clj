(defproject hiccup "0.3.0"
  :description "A fast library for rendering HTML in Clojure"
  :url "http://github.com/weavejester/hiccup"
  :dependencies [[org.clojure/clojure "1.3.0-master-SNAPSHOT"]
                 ;; [org.clojure.contrib/standalone "1.3.0-SNAPSHOT"]
		 
		 ;; [org.clojure/clojure "1.2.0"]
                 ;; [org.clojure/clojure-contrib "1.2.0"]
		 ]
  :dev-dependencies [[lein-clojars "0.6.0"]
                     [swank-clojure "1.2.1"]
                     [autodoc "0.7.1"]]
  :autodoc {:name "Hiccup"
            :description "A fast library for rendering HTML in Clojure."
            :copyright "Copyright 2009-2010 James Reeves"
            :root "."
            :source-path "src"
            :web-src-dir "http://github.com/weavejester/hiccup/blob/"
            :web-home "http://weavejester.github.com/hiccup"
            :output-path "autodoc"
            :namespaces-to-document ["hiccup"]
            :load-except-list [#"/test/" #"project.clj"]})

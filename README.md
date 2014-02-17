About
=====

Project demonstrates how to run [riemann server](http://riemann.io/) from Java/Scala program.

Seems like original version of server is not designed
[to be called from Java](http://stackoverflow.com/questions/2181774/calling-clojure-from-java).

The easiest way is to compile `riemann-0.1.5-standalone.jar` [from sources](https://github.com/aphyr/riemann/)
with [lein](http://leiningen.org)

```
lein uberjar
```

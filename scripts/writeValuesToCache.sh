. ./scripts/setenv.sh
java -classpath $migrate_classpath org.terracotta.demo.migration.WriteValuesToCache $ehcacheXML $cacheName $file1

. ./scripts/setenv.sh
java -classpath $migrate_classpath org.terracotta.demo.migration.ReadValuesFromCache $ehcacheXML $cacheName $file1

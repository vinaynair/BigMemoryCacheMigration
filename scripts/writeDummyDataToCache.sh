. ./scripts/setenv.sh
java -classpath $migrate_classpath org.terracotta.demo.migration.WriteDummyDataToCache $ehcacheXML $cacheName 0 2

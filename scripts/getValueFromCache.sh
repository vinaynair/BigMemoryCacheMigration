. ./scripts/setenv.sh
java -classpath $migrate_classpath org.terracotta.demo.migration.GetValueFromCache $ehcacheXML $cacheName $file1

. ./scripts/setenv.sh
java -classpath $migrate_classpath org.terracotta.demo.migration.WriteKeysToFile $ehcacheXML $cacheName $file1

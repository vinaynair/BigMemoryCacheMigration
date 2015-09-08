. ./scripts/setenv.sh
java -classpath $migrate_classpath org.terracotta.demo.migration.WriteValuesToFile $ehcacheXML $cacheName $file1 $file2

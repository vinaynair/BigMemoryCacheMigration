#ADD YOUR APP CLASSPATH
app_specific_classpath=

#PASSED in ARGUMENTS
terracottaVersion=$1
ehcacheXML=$2
cacheName=$3
file1=$4
file2=$5

for i in ./target/dependencies/$terracottaVersion/*.jar; 
do
	jar_files=$jar_files:$i
done

#FINAL CLASSPATH
migrate_classpath=$jar_files:target/simple-bm-migration-1.0-SNAPSHOT.jar:$app_specific_classpath
echo "\nUsing classpath["$migrate_classpath"]\n\n" 

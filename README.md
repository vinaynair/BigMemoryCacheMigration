# OBJECTIVE
Read data from a 3.7.5 cache and write to a 4.3.0 cache


# BUILDING

**1.** Using maven, build and copy the dependencies for Terracotta BigMemory 3.7.5 into the target folder
```
$>mvn clean compile package dependency:copy-dependencies -P3.7.5
```

**2.** Copy the dependencies for BigMemory 4.3.0 to the target folder as well
```
$>mvn package -P4.3.0
```

**3.** Copy terracotta-license.key to the root folder

#CONFIGURING
Add jars to the classpath for the scripts by updating the following variable within [./scripts/setenv.sh](./scripts/setenv.sh) script
 
```
#ADD YOUR APP CLASSPATH
app_specific_classpath=
```

# RUNNING 


**0(test).**  To _test_,  lets write some dummy data to cache, Cache1 that is deployed on Terracotta version 3.7.5.

[See Source](./src/main/java/org/terracotta/demo/migration/WriteDummyDataToCache.java)
```
$> sh scripts/writeDummyDataToCache.sh 3.7.5 config/ehcache-3.7.xml Cache1
```


**1.** If you already have data within a cache, the previous step is NOT required.
Now lets write the non-expired keys from our cache, Cache1, to a file called keys.txt.

[See Source](./src/main/java/org/terracotta/demo/migration/WriteKeysToFile.java)
```
$> sh scripts/writeKeysToFile.sh 3.7.5 config/ehcache-3.7.xml Cache1 keys.txt
```

**2.** Having written the keys, now lets write the values from this same cache, Cache1, to a file called values.txt.

[See Source](./src/main/java/org/terracotta/demo/migration/WriteValuesToFile.java)
```
$> sh scripts/writeValuesToFile.sh 3.7.5 config/ehcache-3.7.xml Cache1 keys.txt values.txt
```

**3.** Now lets write the values we exported from Terracotta-3.7.5  to Terracotta-4.3.0.

[See Source](./src/main/java/org/terracotta/demo/migration/WriteValuesToCache.java)
```
$> sh scripts/writeValuesToCache.sh 4.3.0 config/ehcache-4.3.xml Cache1 values.txt
```

**4(test)** To test, simply use the client that get()s the key from the cache and prints the expiry time.

[See Source](./src/main/java/org/terracotta/demo/migration/GetValueFromCache.java)
```
$> sh scripts/getValueFromCache.sh 4.3.0 config/ehcache-4.3.xml Cache1 key-0
```

# Troubleshooting common issues

* Running the above scripts against wrong Terracotta server version
If you get the below error essentially it implies that you tried running one of the above script against an incorrect
server version.
```
The configuration data in the base configuration from server at 'localhost:9510' does not obey the Terracotta schema:
```

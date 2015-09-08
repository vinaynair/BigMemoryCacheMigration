# BUILDING

```
$>mvn clean compile package dependency:copy-dependencies -P3.7.5

$>mvn dependency:copy-dependencies -P4.3.0
```
Copy terracotta-license.key to the root folder

# RUNNING 


To **test**,  lets write some dummy data to cache, Cache1 that is deployed on Terracotta version 3.7.5
```
$> sh scripts/writeDummyDataToCache.sh 3.7.5 config/ehcache-3.7.xml Cache1
```


Now lets write the non-expired keys from our cache, Cache1, to a file called keys.txt
```
$> sh scripts/writeKeysToFile.sh 3.7.5 config/ehcache-3.7.xml Cache1 keys.txt
```

Having written the keys, now lets write the values from this same cache, Cache1, to a file called values.txt
```
$> sh scripts/writeValuesToFile.sh 3.7.5 config/ehcache-3.7.xml Cache1 keys.txt values.txt
```

Now lets write the values we exported from Terracotta-3.7.5  to Terracotta-4.3.0
```
$> sh scripts/writeValuesToCache.sh 4.3.0 config/ehcache-4.3.xml Cache1 values.txt
```

To test, simply use a client that keeping listing the elements every 1s
```
$> sh scripts/readValuesFromCache.sh 4.3.0 config/ehcache-4.3.xml Cache1 keys.txt
```

# Troubleshooting common issues

* Running the above scripts against wrong Terracotta server version
If you get the below error essentially it implies that you tried running one of the above script against an incorrect
server version.
```
The configuration data in the base configuration from server at 'localhost:9510' does not obey the Terracotta schema:
```

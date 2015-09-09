package org.terracotta.demo.migration;

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;

import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.ObjectInputStream;

/**
 * Read the values from the values file & write them to the cache
 * Created by vinay on 8/28/15.
 */
public class WriteValuesToCache {
    public static void main(String[] args) throws Exception {
        if (args.length < 3) {
            System.err.println("Usage: java WriteDummyDataToCache ehcachexml cacheName valuesFile");
            System.exit(1);
        }
        int i = 0;
        String ehcacheXml = args[i++];
        String cacheName = args[i++];
        String valuesFileName = args[i++];

        CacheManager cacheManager = CacheManager.newInstance(ehcacheXml);
        Cache cache = cacheManager.getCache(cacheName);
        int cacheSize = cache.getSize();
        System.out.println("Currently the cache " + cacheName + " has " + cacheSize + " entries");

        ///read values file
        File valuesFile = new File(valuesFileName);
        FileInputStream fis = new FileInputStream(valuesFile);
        ObjectInputStream ois = new ObjectInputStream(fis);

        int elementsCount = 0;
        while (true) {
            ElementSerialized elementSerialized = null;
            try {
                elementSerialized = (ElementSerialized) ois.readObject();
            } catch (EOFException eof) {
                System.out.println("DONE reading values from file");
                break;
            }
            System.out.println("Write element[" + elementSerialized + "] to cache");
            elementsCount++;
            Element element = elementSerialized.createEhCacheElement(cache);
            cache.putQuiet(element);
        }

        System.out.println("Statistics: Put " + elementsCount + " into the cache");
        ois.close();
        fis.close();

        cacheManager.shutdown();
    }
}

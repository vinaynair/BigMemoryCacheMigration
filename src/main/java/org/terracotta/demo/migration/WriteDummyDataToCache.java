package org.terracotta.demo.migration;

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;

/**
 * Test program to write dummy data to cache
 * Created by vinay on 8/28/15.
 */
public class WriteDummyDataToCache {
    public static void main(String[] args) throws Exception {
        if (args.length < 4) {
            System.err.println("Usage: java WriteDummyDataToCache ehcachexml cacheName startIndex endIndex");
            System.exit(1);
        }
        int i = 0;
        String ehcacheXml = args[i++];
        String cacheName = args[i++];
        int startIndex = Integer.parseInt(args[i++]);
        int endIndex = Integer.parseInt(args[i++]);

        CacheManager cacheManager = CacheManager.newInstance(ehcacheXml);
        Cache cache = cacheManager.getCache(cacheName);

        int elementsCount=0;
        for (int j = startIndex; j < endIndex; j++) {
            Element element=new Element("key-" + j, "value-" + j);
            cache.put(element);
            elementsCount++;
        }


        System.out.println("Statistics: Put "+elementsCount+" elements into cache "+cacheName);
        cacheManager.shutdown();
    }
}

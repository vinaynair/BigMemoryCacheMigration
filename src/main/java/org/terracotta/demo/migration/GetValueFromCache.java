package org.terracotta.demo.migration;

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;

/**
 * Created by vinay on 8/28/15.
 */
public class GetValueFromCache {
    public static void main(String[] args) throws Exception {
        if (args.length < 3) {
            System.err.println("Usage: java ReadValuesFromCache ehcachexml cacheName key");
            System.exit(1);
        }
        int i = 0;
        String ehcacheXml = args[i++];
        String cacheName = args[i++];
        String key = args[i++];

        CacheManager cacheManager = CacheManager.newInstance(ehcacheXml);
        Cache cache = cacheManager.getCache(cacheName);
        Element e = cache.get(key);
        long timeToLive = 0;
        if (e != null)
            timeToLive = (e.getExpirationTime() - System.currentTimeMillis()) / 1000;
        System.out.println("Got element from cache " + e + " , and it will expire after " + timeToLive);

        cacheManager.shutdown();
    }
}

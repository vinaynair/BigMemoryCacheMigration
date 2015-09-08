package org.terracotta.demo.migration.test;

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.terracotta.demo.migration.ElementSerialized;

/**
 * Created by vinay on 9/2/15.
 */
public class SimpleTest {
    CacheManager cacheManager;

    @Before
    public void setup() {
        cacheManager = CacheManager.getInstance();
        cacheManager.addCache(new Cache("ttlCache", 100, false, false, 100, 0));
        cacheManager.addCache(new Cache("ttiCache", 100, false, false, 0, 100));
    }

    @After
    public void cleanup() {
        cacheManager.shutdown();

    }

    @Test
    public void ttlTest() {
        Cache cache = cacheManager.getCache("ttlCache");
        Assert.assertNotNull("ttlCache", cache.getName());
        Assert.assertEquals("TTI should be 0 but was "
                + cache.getCacheConfiguration().getTimeToIdleSeconds(), 0, cache.getCacheConfiguration().getTimeToIdleSeconds());
        cache.put(new Element(1, 1));
        Element e = cache.get(1);
        long timeRemaining = (e.getExpirationTime() - System.currentTimeMillis()) / 1000;
        Assert.assertTrue("A new element must have close to complete TTL",
                timeRemaining > 97);

        ElementSerialized elementSerialized = new ElementSerialized(2, 2, 1, 50000, 50000, 1);
        Element e1 = elementSerialized.createEhCacheElement(cache);
        System.out.println("Writing element "+e1+". current time="+System.currentTimeMillis());
        cache.putQuiet(e1);
        e1 = cache.getQuiet(2);
        System.out.println("Reading element "+e1+". current time="+System.currentTimeMillis());
        timeRemaining = (e1.getExpirationTime() - System.currentTimeMillis()) / 1000;
        Assert.assertTrue("An element that had already spent 50s in another cache must less TTL left",
                timeRemaining < 51);
        e1 = cache.get(2);
        timeRemaining = (e1.getExpirationTime() - System.currentTimeMillis()) / 1000;
        Assert.assertTrue(
                "Once I get the cache, since its a TTL based cache, it should have no effect "
                        + timeRemaining,
                timeRemaining <51);


    }

    @Test
    public void ttiTest() {
        Cache cache = cacheManager.getCache("ttiCache");
        Assert.assertNotNull(cache);
        Assert.assertEquals(cache.getCacheConfiguration().getTimeToLiveSeconds(), 0);
        cache.put(new Element(1, 1));
        Element e = cache.get(1);
        long timeRemaining = (e.getExpirationTime() - System.currentTimeMillis()) / 1000;
        Assert.assertTrue("A new element must have close to complete TTL",
                timeRemaining > 97);

        ElementSerialized elementSerialized = new ElementSerialized(2, 2, 1, 50000, 50000, 1);
        Element e1 = elementSerialized.createEhCacheElement(cache);
        System.out.println("Writing element "+e1+". current time="+System.currentTimeMillis());
        cache.putQuiet(e1);
        e1 = cache.getQuiet(2);
        System.out.println("Reading element "+e1+". current time="+System.currentTimeMillis());
        timeRemaining = (e1.getExpirationTime() - System.currentTimeMillis()) / 1000;
        Assert.assertTrue(
                "An element that had already spent 50s in another cache must less TTL left. Time remaining was"
                        + timeRemaining,
                timeRemaining < 51);
        e1 = cache.get(2);
        timeRemaining = (e1.getExpirationTime() - System.currentTimeMillis()) / 1000;
        Assert.assertTrue(
                "Once I get the cache, its expiration must be reset"
                        + timeRemaining,
                timeRemaining > 97 );


    }
}

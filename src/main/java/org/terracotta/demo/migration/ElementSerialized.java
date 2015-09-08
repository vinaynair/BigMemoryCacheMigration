package org.terracotta.demo.migration;

import net.sf.ehcache.Cache;
import net.sf.ehcache.Element;
import net.sf.ehcache.config.CacheConfiguration;

import java.io.Serializable;

/**
 * Created by vinay on 8/30/15.
 */
public class ElementSerialized implements Serializable {
    private static final long serialVersionUID = 1L;
    private Object key;
    private Object value;
    private long version;
    private long timeSpent;
    private long hitCount;

    public ElementSerialized(Object key, Object value, long version, long timeSpentOnTTL, long timeSpentOnTTI, long hitCount) {
        this.key = key;
        this.value = value;
        this.version = version;
        this.timeSpent = timeSpentOnTTI;
        this.hitCount = hitCount;
    }

    public ElementSerialized(Element element) {
        long currentTime = System.currentTimeMillis();
        key = element.getObjectKey();
        value = element.getObjectValue();
        version = element.getVersion();
        hitCount = element.getHitCount();
        timeSpent = currentTime - element.getLastAccessTime();
    }

    public Element createEhCacheElement(Cache cache) {
        long currentTime = System.currentTimeMillis();
        long adjustedTime = currentTime - timeSpent;
        CacheConfiguration config = cache.getCacheConfiguration();
        return new Element(key, value, version,
                adjustedTime, adjustedTime,
                hitCount, false,
                (int) config.getTimeToLiveSeconds(), (int) config.getTimeToIdleSeconds(),
                adjustedTime);
    }

    @Override
    public String toString() {
        return "key[" + key + "]value[" + value + "] time spent is [" + timeSpent + "]";
    }
}

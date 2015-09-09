package org.terracotta.demo.migration;

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;

import java.io.File;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.util.List;

/**
 * Write just the keys to the file<br/>
 * This is done so that we keep the data being streamed by the server as well as the data held in-memory on the client small
 * <p/>
 * Created by vinay on 8/28/15.
 */
public class WriteKeysToFile {
    public static void main(String[] args) throws Exception {
        if (args.length < 3) {
            System.err.println("Usage: java WriteKeysToFile ehcachexml cacheName keysFile");
            System.exit(1);
        }
        int i = 0;
        String ehcacheXml = args[i++];
        String cacheName = args[i++];
        String keysFile = args[i++];
        CacheManager cacheManager = CacheManager.newInstance(ehcacheXml);
        Cache cache = cacheManager.getCache(cacheName);
        int cacheSize = cache.getSize();
        //get keys that arent expired.
        List keys = cache.getKeysWithExpiryCheck();

        File file = new File(keysFile);
        FileOutputStream fos = new FileOutputStream(file);
        ObjectOutputStream oos = new ObjectOutputStream(fos);

        oos.writeObject(cacheSize);

        int elementCount = 0;
        for (Object key : keys) {
            oos.writeObject(key);
            elementCount++;
            if (elementCount % 1000 == 0) System.out.print(".");
        }
        oos.close();
        fos.close();
        System.out.println(
                "Statistics: Total cache size=" + cacheSize + ", and total keys written to file=" + elementCount);

        cacheManager.shutdown();
    }
}

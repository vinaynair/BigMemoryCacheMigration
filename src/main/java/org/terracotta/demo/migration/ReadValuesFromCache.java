package org.terracotta.demo.migration;

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;

import java.io.*;

/**
 * Created by vinay on 8/28/15.
 */
public class ReadValuesFromCache {
    public static void main(String[] args) throws Exception {
        if (args.length < 3) {
            System.err.println("Usage: java ReadValuesFromCache ehcachexml cacheName keysFile");
            System.exit(1);
        }
        int i = 0;
        String ehcacheXml = args[i++];
        String cacheName = args[i++];
        String keysFileName = args[i++];

        CacheManager cacheManager = CacheManager.newInstance(ehcacheXml);
        Cache cache = cacheManager.getCache(cacheName);
        int cacheSize = cache.getSize();

        ///read keys file


        while (true)
        {
            File keysFile = new File(keysFileName);
            FileInputStream fis = new FileInputStream(keysFile);
            ObjectInputStream ois = new ObjectInputStream(fis);
            int cacheSizeFromFile = Integer.parseInt("" + ois.readObject());
            int elementsFound = 0;
            while (true) {
                Object key = null;
                try {
                    key = ois.readObject();
                } catch (EOFException eof) {
                    System.out.println("DONE reading keys file");
                    break;
                }
                if (key == null) break; //DONE with reading all keys
                long currentTime = System.currentTimeMillis();
                Element element = cache.getQuiet(key);
                if (element == null) {
                    // NO value (might have expired)
                    continue;
                } else {
                    elementsFound++;
                }

                System.out.println("Element =" + element + ", and remaining time="
                        + (element.getExpirationTime() - currentTime) / 1000);
            }
            System.out.println(
                    "Statistics: Total cache size from file =" + cacheSizeFromFile + ".Total elements found in cache = "
                            + elementsFound);
            ois.close();
            fis.close();


            Thread.sleep(1000);
        }

//        cacheManager.shutdown();
    }
}

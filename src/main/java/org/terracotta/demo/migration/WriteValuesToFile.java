package org.terracotta.demo.migration;

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;

import java.io.*;

/**
 * For each key in the key file, read the value and write it to the file*
 * Created by vinay on 8/28/15.
 */
public class WriteValuesToFile {
    public static void main(String[] args) throws Exception {
        if (args.length < 4) {
            System.err.println("Usage: java WriteDummyDataToCache ehcachexml cacheName keysFile valuesFile");
            System.exit(1);
        }
        int i = 0;
        String ehcacheXml = args[i++];
        String cacheName = args[i++];
        String keysFileName = args[i++];
        String valuesFileName = args[i++];

        CacheManager cacheManager = CacheManager.newInstance(ehcacheXml);
        Cache cache = cacheManager.getCache(cacheName);
        int cacheSize = cache.getSize();


        ///read keys file
        File keysFile = new File(keysFileName);
        FileInputStream fis = new FileInputStream(keysFile);
        ObjectInputStream ois = new ObjectInputStream(fis);
        int cacheSizeFromFile = Integer.parseInt("" + ois.readObject());


        //write to values file
        File valuesFile = new File(valuesFileName);
        FileOutputStream fos = new FileOutputStream(valuesFile);
        ObjectOutputStream oos = new ObjectOutputStream(fos);

        int elementsAdded = 0;
        while (true) {
            Object key = null;
            try {
                key = ois.readObject();
            } catch (EOFException eof) {
                System.out.println("DONE reading keys file");
                break;
            }
            Element element = cache.getQuiet(key);
            if (element == null) continue; // key wasnt present we tried to fetch values
            ElementSerialized elementSerialized = new ElementSerialized(element);
            System.out.println("Writing element " + elementSerialized);
            oos.writeObject(elementSerialized);
            elementsAdded++;
        }

        if (cacheSizeFromFile != elementsAdded)
            System.out.println(
                    "FYI: Cache size now " + cacheSize + " is different from what it was when the keys were fetched "
                            + cacheSizeFromFile + ".\n You might want to re-write keys to file, if required");

        System.out.println(
                "Statistics: Total size of cache from keys file =" + cacheSizeFromFile
                        + ".\nTotal elements written to values file= "
                        + elementsAdded);


        ois.close();
        fis.close();
        oos.close();
        fos.close();

        cacheManager.shutdown();
    }
}

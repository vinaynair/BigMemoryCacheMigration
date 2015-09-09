package org.terracotta.demo.migration;

import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.ObjectInputStream;

/**
 * Debug program to print data from the values file
 * Created by vinay on 8/28/15.
 */
public class ReadValuesFromFile {
    public static void main(String[] args) throws Exception {
        if (args.length < 1) {
            System.err.println("Usage: java WriteDummyDataToCache valuesFile");
            System.exit(1);
        }
        int i = 0;
        String valuesFileName = args[i++];

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
            System.out.println("Element[" + elementSerialized + "] ");
            elementsCount++;

        }

        System.out.println("Statistics: " + elementsCount + " found within file");
        ois.close();
        fis.close();


    }
}

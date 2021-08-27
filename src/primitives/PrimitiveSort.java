/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package primitives;

/**
 *
 * @author alfieriv
 */

import com.sun.xml.internal.fastinfoset.util.StringArray;
import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

public class PrimitiveSort {
    
    public static void main(String[] argv) throws IOException {

        argv = new String[] { "C:\\Users\\alfieriv\\Desktop\\input.txt", "C:\\Users\\alfieriv\\Desktop\\output.txt" };
        if (argv.length != 2) {
            System.err.println("Usage: java -jar PrimitiveSort.jar InputFilePath OutputFilePath");
            System.exit(-1);
        }
        System.out.println("You selected " + argv[0] + "as Input File Path and " + argv[1] + "as Output File Path");
        try {        
            File inputfile = new File(argv[0]);
            File outputfile = new File(argv[1]);
            String lines = PrimitiveDatabase.readLineByLineJava8(inputfile.getAbsolutePath());
            StringArray cards = new StringArray();
            StringArray keys = new StringArray();
            Map<String, String> unsortedMap = new HashMap<>();
            while(lines.contains("[card]")) {
                String findStr = "[card]";
                int lastIndex = lines.indexOf(findStr);
                int endIndex = lines.indexOf("[/card]",lastIndex) + 8;
                
                String content = lines.substring(lastIndex, endIndex);
                String name = null;
                int a = lines.indexOf("name=",lastIndex);
                if (a > 0){
                    if(lines.substring(a, lines.indexOf("\n", a)).split("=").length > 1)
                        name = lines.substring(a, lines.indexOf("\n", a)).split("=")[1];
                }
                if (name != null){
                    unsortedMap.put(name, content);
                }
                else
                    System.err.println("\r\n" + "Error reading: " + content);
                        
                lines = lines.substring(endIndex);
                System.out.println("Added " + name + "card");
            }
            BufferedWriter fw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outputfile), "Cp1252"));
            Map<String, String> sortedMap = new TreeMap<>(unsortedMap);
            for(String key : sortedMap.keySet()){
                fw.append(sortedMap.get(key));
                fw.flush();
            }
            fw.close();
            System.out.println("\r\n" + "All primitives have been sorted, the output file is ready!");
        } catch (Exception e) {
            System.err.println("Error while reading primitive files: " + e.getMessage());
            System.exit(-1);
        }
    }
}

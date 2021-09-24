/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package images;

import com.sun.xml.internal.fastinfoset.util.StringArray;
import java.io.*;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class ImgMissing {

    public static void main(String[] argv) throws IOException {

        String basePath = "C:\\Program Files (x86)\\Emulatori\\Sony\\PSVita\\Games\\PSP\\Wagic\\WTH 0.23.1\\Res\\sets\\";
        File baseFolder = new File(basePath);
        File[] listOfSet = baseFolder.listFiles();
        Map<String, String> mappa = new HashMap<>();
        for (int y = 0; y < listOfSet.length; y++) {
            if (listOfSet[y].isDirectory() && !listOfSet[y].getName().equalsIgnoreCase("primitives")) {
                String Set = listOfSet[y].getName() + "\\";
                File folder = new File(basePath + Set);
                String filePath = folder.getAbsolutePath() + "\\_cards.dat";
                String lines = primitives.PrimitiveDatabase.readLineByLineJava8(filePath);
                while (lines.contains("[card]")) {
                    String findStr = "[card]";
                    int lastIndex = lines.indexOf(findStr);
                    String id = null;
                    String primitive = null;
                    String rarity = "";
                    int a = lines.indexOf("primitive=", lastIndex);
                    if (a > 0) {
                        if (lines.substring(a, lines.indexOf("\n", a)).replace("//", "-").split("=").length > 1)
                            primitive = lines.substring(a, lines.indexOf("\n", a)).replace("//", "-").split("=")[1];
                    }
                    int b = lines.indexOf("id=", lastIndex);
                    if (b > 0) {
                        if (lines.substring(b, lines.indexOf("\n", b)).replace("-", "").split("=").length > 1)
                            id = lines.substring(b, lines.indexOf("\n", b)).replace("-", "").split("=")[1];
                    }
                    int c = lines.indexOf("[/card]", lastIndex);
                    if (c > 0)
                        lines = lines.substring(c + 8);
                    if (id != null && (id.equals("209162") || id.equals("209163") || id.equals("401721") ||
                            id.equals("401722") || id.equals("22010012") || id.equals("2050321") ||
                            id.equals("2050322") || id.equals("3896522") || id.equals("3896521") ||
                            id.equals("3896523") || id.equals("4977512") || id.equals("4977511") ||
                            id.equals("4143881") || id.equals("3896122") || id.equals("207998") ||
                            id.equals("20787512") || id.equals("20787511") || id.equals("19784311") ||
                            id.equals("19784312") || id.equals("19784313") || id.equals("19784612") ||
                            id.equals("19784613") || id.equals("19784611") || id.equals("19784555") ||
                            id.equals("15208711") || id.equals("15208712") || id.equals("47316011") ||
                            id.equals("47316012") || id.equals("47316013") || id.equals("19462") ||
                            id.equals("19463") || id.equals("19464") || id.equals("19465") ||
                            id.equals("19476") || id.equals("19477") || id.equals("999901") ||
                            id.equals("47963911") || id.equals("4827131") || id.equals("48966310") ||
                            id.equals("48966311") || id.equals("53941712") || id.equals("53941713") || 
                            id.equals("53941711") || id.equals("29669413") || id.equals("29669412") || 
                            id.equals("29669411") || id.equals("53939512") || id.equals("53939511") || 
                            id.equals("53939513") || id.equals("54047311") || id.equals("54047313") || 
                            id.equals("54047312") || id.equals("999902")))
                        rarity = "t";
                    if (primitive != null && id != null && !id.equalsIgnoreCase("null") &&
                            !id.equals("53044910") && !id.equals("29643510") && !id.equals("29644410") && !id.equals("29644411"))
                        mappa.put(id + rarity, Set + primitive);
                    if (id.equals("114921")) {
                        mappa.put("11492111t", "Citizen");
                        mappa.put("11492112t", "Camarid");
                        mappa.put("11492113t", "Thrull");
                        mappa.put("11492114t", "Goblin");
                        mappa.put("11492115t", "Saproling");
                    }
                }
            }
        }

        basePath = "C:\\Program Files (x86)\\Emulatori\\Sony\\PSVita\\Games\\PSP\\Wagic\\WTH 0.23.1\\User\\sets\\";
        baseFolder = new File(basePath);
        listOfSet = baseFolder.listFiles();
        Map<String, String> mappaImg = new HashMap<>();
        for (int y = 0; y < listOfSet.length; y++) {
            if (listOfSet[y].isDirectory()) {
                String Set = listOfSet[y].getName() + "\\";
                File folder = new File(basePath + Set);
                File[] listOfFiles = folder.listFiles();
                if (listOfFiles.length > 1) {
                    for (int x = 0; x < listOfFiles.length; x++){
                        if(listOfFiles[x].getName().equalsIgnoreCase("thumbnails"))
                            continue;
                        mappaImg.put(listOfFiles[x].getName().replace(".jpg",""), listOfSet[y].getName());
                    }
                } else {
                    ZipFile zipFile = null;
                    try {
                        zipFile = new ZipFile(baseFolder + "\\" + Set + listOfFiles[0].getName());
                        Enumeration<? extends ZipEntry> e = zipFile.entries();
                        while (e.hasMoreElements()) {
                            ZipEntry entry = e.nextElement();
                            String entryName = entry.getName();
                            if(entryName.contains("thumbnails"))
                                continue;
                            mappaImg.put(entryName.replace(".jpg", ""), listOfSet[y].getName());
                        }
                    }
                    catch (IOException ioe) {
                        System.out.println("Error opening zip file" + ioe);
                    }
                    finally {
                        try {
                            if (zipFile!=null) {
                                zipFile.close();
                            }
                        }
                        catch (IOException ioe) {
                            System.out.println("Error while closing zip file" + ioe);
                        }
                    }
                }
            }
        }

        StringArray uns = new StringArray();
        /*try (BufferedReader br = new BufferedReader(new FileReader("C:\\Program Files (x86)\\Emulatori\\Sony\\PSVita\\Games\\PSP\\Wagic\\WTH 0.23.1\\Res\\sets\\primitives\\unsupported.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                uns.add(line);
            }
        }
        try (BufferedReader br = new BufferedReader(new FileReader("C:\\Program Files (x86)\\Emulatori\\Sony\\PSVita\\Games\\PSP\\Wagic\\WTH 0.23.1\\Res\\sets\\primitives\\crappy.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                uns.add(line);
            }
        }*/
        File missing = new File("C:\\Program Files (x86)\\Emulatori\\Sony\\PSVita\\Games\\PSP\\Wagic\\WTH 0.23.1\\missing.csv");
        FileWriter fw = new FileWriter(missing);
        fw.append("id;name;set" + "\n");
        Integer[] lista = new Integer[mappa.size()];
        for (int i = 0; i < (mappa.keySet()).toArray().length ; i++){
            String id = (mappa.keySet()).toArray()[i].toString();
            if(id != null && !id.isEmpty()) {
                if(mappaImg.get(id) == null || mappaImg.get(id).isEmpty()){
                    String Set = mappa.get(id).split("\\\\")[0];
                    String Name = mappa.get(id).split("\\\\")[1];
                    Boolean insert = true;
                    for (int h = 0; h < uns.getSize(); h ++){
                        if(uns.getArray()[h].equalsIgnoreCase(Name)){
                            insert = false;
                            break;
                        }
                    }
                    if(insert) {
                        fw.append(id + ";" + Name + ";" + Set + "\n");
                        fw.flush();
                    }
                }
            }
        }
        fw.close();
        System.out.println("check is done");
    }
}
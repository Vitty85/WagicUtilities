/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package images;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class ImgDownloader {

    public static void main(String[] argv) throws IOException {

        String set = "*.*";
        String targetres = "HI";
        String baseurl = "https://gatherer.wizards.com/Pages/Card/Details.aspx?multiverseid=";
        String imageurl = "https://scryfall.com/sets/";
        String basePath = "C:\\Program Files (x86)\\Emulatori\\Sony\\PSVita\\Games\\PSP\\Wagic\\WTH 0.23.1\\Res\\sets\\";
        String destinationPath = "C:\\Users\\alfieriv\\Desktop\\TODO\\";

        Integer ImgX = 0;
        Integer ImgY = 0;
        Integer ThumbX = 0;
        Integer ThumbY = 0;

        if (targetres.equals("HI")) {
            ImgX = 488;
            ImgY = 680;
            ThumbX = 90;
            ThumbY = 128;
        } else if (targetres.equals("LOW")) {
            ImgX = 244;
            ImgY = 340;
            ThumbX = 45;
            ThumbY = 64;
        }
        File baseFolder;
        File[] listOfSet = null;
        int finalsize = 1;
        if(set.equalsIgnoreCase("*.*")){
            baseFolder = new File(basePath);
            listOfSet = baseFolder.listFiles();
            finalsize = listOfSet.length;
        }
        for (int f = 0; f < finalsize; f++) {
            Map<String, String> mappa = new HashMap<>();

            if (listOfSet != null){
                if(listOfSet[f].isDirectory() && !listOfSet[f].getName().equalsIgnoreCase("primitives"))
                    set = listOfSet[f].getName();
                else
                    continue;
            }
            System.out.println("Downloading images for: " + set);
            File folder = new File(basePath + set + "\\");
            String filePath = folder.getAbsolutePath() + "\\_cards.dat";
            String lines = primitives.PrimitiveDatabase.readLineByLineJava8(filePath);
            int totalcards = 0;
            String findStr = "primitive=";
            int lastIndex = 0;
            while (lastIndex != -1) {
                lastIndex = lines.indexOf(findStr, lastIndex);
                if (lastIndex != -1) {
                    totalcards++;
                    lastIndex += findStr.length();
                }
            }
            for (int i = 0; i < totalcards; i++) {
                int a = lines.indexOf("primitive=") + "primitive=".length();
                int b = lines.indexOf("id=") + "id=".length();
                int c = lines.indexOf("rarity=");
                String primitive = lines.substring(a, b - "id=".length() - 1).replace("//", "-");
                String id = lines.substring(b, c - 1).replace("-", "");
                lines = lines.substring(c + "rarity=".length() + 2, lines.length());
                if (primitive != null && id != null && !id.equalsIgnoreCase("null"))
                    mappa.put(id, primitive);
            }

            File imgPath = new File(destinationPath + set + "\\");
            if (!imgPath.exists()) {
                System.out.println("creating directory: " + imgPath.getName());
                boolean result = false;
                try {
                    imgPath.mkdir();
                    result = true;
                } catch (SecurityException se) {
                    System.err.println(imgPath + " not created");
                    System.exit(1);
                }
                if (result) {
                    System.out.println(imgPath + " created");
                }
            }

            File thumbPath = new File(destinationPath + set + "\\thumbnails\\");
            if (!thumbPath.exists()) {
                System.out.println("creating directory: " + thumbPath.getName());
                boolean result = false;
                try {
                    thumbPath.mkdir();
                    result = true;
                } catch (SecurityException se) {
                    System.err.println(thumbPath + " not created");
                    System.exit(1);
                }
                if (result) {
                    System.out.println(thumbPath + " created");
                }
            }

            for (int y = 0; y < mappa.size(); y++) {
                String id = mappa.keySet().toArray()[y].toString();
                Document doc = Jsoup.connect(baseurl + id).get();
                Elements divs = doc.select("body div");
                String scryset = set;
                if(scryset.equalsIgnoreCase("MRQ"))
                    scryset = "MMQ";
                else if(scryset.equalsIgnoreCase("AVN"))
                    scryset = "DDH";
                else if(scryset.equalsIgnoreCase("BVC"))
                    scryset = "DDQ";
                else if(scryset.equalsIgnoreCase("CFX"))
                    scryset = "CON";
                else if(scryset.equalsIgnoreCase("DM"))
                    scryset = "DKM";
                else if(scryset.equalsIgnoreCase("EVK"))
                    scryset = "DDO";
                else if(scryset.equalsIgnoreCase("EVT"))
                    scryset = "DDF";
                else if(scryset.equalsIgnoreCase("FVD"))
                    scryset = "DRB";
                else if(scryset.equalsIgnoreCase("FVE"))
                    scryset = "V09";
                else if(scryset.equalsIgnoreCase("FVL"))
                    scryset = "V11";
                else if(scryset.equalsIgnoreCase("FVR"))
                    scryset = "V10";
                else if(scryset.equalsIgnoreCase("HVM"))
                    scryset = "DDL";
                else if(scryset.equalsIgnoreCase("IVG"))
                    scryset = "DDJ";
                else if(scryset.equalsIgnoreCase("JVV"))
                    scryset = "DDM";
                else if(scryset.equalsIgnoreCase("KVD"))
                    scryset = "DDG";
                else if(scryset.equalsIgnoreCase("PDS"))
                    scryset = "H09";
                else if(scryset.equalsIgnoreCase("PVC"))
                    scryset = "DDE";
                else if(scryset.equalsIgnoreCase("RV"))
                    scryset = "3ED";
                else if(scryset.equalsIgnoreCase("SVT"))
                    scryset = "DDK";
                else if(scryset.equalsIgnoreCase("VVK"))
                    scryset = "DDI";
                else if(scryset.equalsIgnoreCase("ZVE"))
                    scryset = "DDP";
                try {
                    doc = Jsoup.connect(imageurl + scryset.toLowerCase()).get();
                } catch (Exception e) {
                    System.err.println("Problem downloading card: " + mappa.get(id) + "-" + id + " from " + scryset + " on ScryFall");
                    continue;
                }
                Elements imgs = doc.select("body img");
                int k;
                for (k = 0; k < divs.size(); k++)
                    if (divs.get(k).childNodes().size() > 0 && divs.get(k).childNode(0).toString().toLowerCase().contains("card name"))
                        break;
                if (k >= divs.size())
                    continue;
                String cardname = divs.get(k + 1).childNode(0).attributes().get("#text").replace("\r\n", "").trim();

                for (int i = 0; i < imgs.size(); i++) {
                    String title = imgs.get(i).attributes().get("title");
                    if (title.toLowerCase().contains(cardname.toLowerCase())) {
                        String CardImage = imgs.get(i).attributes().get("src");
                        if (CardImage.isEmpty())
                            CardImage = imgs.get(i).attributes().get("data-src");
                        URL url = new URL(CardImage);
                        InputStream in = new BufferedInputStream(url.openStream());
                        ByteArrayOutputStream out = new ByteArrayOutputStream();
                        byte[] buf = new byte[1024];
                        int n = 0;
                        while (-1 != (n = in.read(buf))) {
                            out.write(buf, 0, n);
                        }
                        out.close();
                        in.close();
                        byte[] response = out.toByteArray();
                        String cardimage = imgPath + "\\" + id + ".jpg";
                        String thumbcardimage = thumbPath + "\\" + id + ".jpg";
                        FileOutputStream fos = new FileOutputStream(cardimage);
                        fos.write(response);
                        fos.close();

                        Toolkit toolkit = Toolkit.getDefaultToolkit();
                        MediaTracker tracker = new MediaTracker(new Panel());
                        Image image = toolkit.getImage(cardimage);
                        tracker.addImage(image, 0);
                        try {
                            tracker.waitForAll();
                        } catch (Exception e) {

                        }
                        BufferedImage resizedImg = new BufferedImage(ImgX, ImgY, BufferedImage.TYPE_INT_RGB);
                        Graphics2D tGraphics2DReiszed = resizedImg.createGraphics(); //create a graphics object to paint to
                        tGraphics2DReiszed.setBackground(Color.WHITE);
                        tGraphics2DReiszed.setPaint(Color.WHITE);
                        tGraphics2DReiszed.fillRect(0, 0, ImgX, ImgY);
                        tGraphics2DReiszed.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
                        tGraphics2DReiszed.drawImage(image, 0, 0, ImgX, ImgY, null); //draw the image scaled
                        ImageIO.write(resizedImg, "JPG", new File(cardimage)); //write the image to a file

                        BufferedImage tThumbImage = new BufferedImage(ThumbX, ThumbY, BufferedImage.TYPE_INT_RGB);
                        Graphics2D tGraphics2D = tThumbImage.createGraphics(); //create a graphics object to paint to
                        tGraphics2D.setBackground(Color.WHITE);
                        tGraphics2D.setPaint(Color.WHITE);
                        tGraphics2D.fillRect(0, 0, ThumbX, ThumbY);
                        tGraphics2D.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
                        tGraphics2D.drawImage(image, 0, 0, ThumbX, ThumbY, null); //draw the image scaled
                        ImageIO.write(tThumbImage, "JPG", new File(thumbcardimage)); //write the image to a file
                        String text = "";
                        for (k = 0; k < divs.size(); k++)
                            if (divs.get(k).childNodes().size() > 0 && divs.get(k).childNode(0).toString().toLowerCase().contains("card text"))
                                break;
                        if (k < divs.size()) {
                            Element tex = divs.get(k + 1);
                            for (int z = 0; z < divs.get(k + 1).childNodes().size(); z++) {
                                for (int u = 0; u < divs.get(k + 1).childNode(z).childNodes().size(); u++) {
                                    if (divs.get(k + 1).childNode(z).childNode(u).childNodes().size() > 1) {
                                        for (int w = 0; w < divs.get(k + 1).childNode(z).childNode(u).childNodes().size(); w++) {
                                            if (divs.get(k + 1).childNode(z).childNode(u).childNode(w).hasAttr("alt")) {
                                                String newtext = divs.get(k + 1).childNode(z).childNode(u).childNode(w).attributes().get("alt").trim();
                                                newtext = newtext.replace("Green", "{G}");
                                                newtext = newtext.replace("White", "{W}");
                                                newtext = newtext.replace("Black", "{B}");
                                                newtext = newtext.replace("Blue", "{U}");
                                                newtext = newtext.replace("Red", "{R}");
                                                newtext = newtext.replace("Tap", "{T}");
                                                text = text + newtext;
                                            } else
                                                text = text + " " + divs.get(k + 1).childNode(z).childNode(u).childNode(w).toString().replace("\r\n", "").trim() + " ";
                                            text = text.replace("} .", "}.");
                                            text = text.replace("} :", "}:");
                                            text = text.replace("} ,", "},");
                                        }
                                    } else {
                                        if (divs.get(k + 1).childNode(z).childNode(u).hasAttr("alt")) {
                                            String newtext = divs.get(k + 1).childNode(z).childNode(u).attributes().get("alt").trim();
                                            newtext = newtext.replace("Green", "{G}");
                                            newtext = newtext.replace("White", "{W}");
                                            newtext = newtext.replace("Black", "{B}");
                                            newtext = newtext.replace("Blue", "{U}");
                                            newtext = newtext.replace("Red", "{R}");
                                            newtext = newtext.replace("Tap", "{T}");
                                            text = text + newtext;
                                        } else
                                            text = text + " " + divs.get(k + 1).childNode(z).childNode(u).toString().replace("\r\n", "").trim() + " ";
                                        text = text.replace("} .", "}.");
                                        text = text.replace("} :", "}:");
                                        text = text.replace("} ,", "},");
                                    }
                                    if (z > 0 && z < divs.get(k + 1).childNodes().size() - 1)
                                        text = text + " -- ";
                                    text = text.replace("<i>", "");
                                    text = text.replace("</i>", "");
                                    text = text.replace("<b>", "");
                                    text = text.replace("</b>", "");
                                    text = text.replace(" -- (", " (");
                                    text = text.replace("  ", " ");
                                }
                            }
                        }
                        if (text.trim().toLowerCase().contains("create") && text.trim().toLowerCase().contains("creature token")) {
                            boolean tokenfound = false;
                            String arrays[] = text.trim().split(" ");
                            String nametoken = "";
                            for (int l = 1; l < arrays.length - 1; l++) {
                                if (arrays[l].equalsIgnoreCase("creature") && arrays[l + 1].toLowerCase().contains("token")) {
                                    nametoken = arrays[l - 1];
                                    break;
                                }
                            }
                            if (nametoken.isEmpty()) {
                                tokenfound = false;
                                nametoken = mappa.get(id);
                                doc = Jsoup.connect(imageurl + scryset.toLowerCase()).get();
                            } else {
                                try {
                                    doc = Jsoup.connect(imageurl + "t" + scryset.toLowerCase()).get();
                                    tokenfound = true;
                                } catch(Exception e) {
                                    tokenfound = false;
                                }
                            }
                            Elements imgstoken = doc.select("body img");
                            for (int p = 0; p < imgstoken.size(); p++) {
                                String titletoken = imgstoken.get(p).attributes().get("title");
                                if (titletoken.toLowerCase().contains(nametoken.toLowerCase())) {
                                    String CardImageToken = imgstoken.get(p).attributes().get("src");
                                    if (CardImageToken.isEmpty())
                                        CardImageToken = imgstoken.get(p).attributes().get("data-src");
                                    URL urltoken = new URL(CardImageToken);
                                    InputStream intoken = new BufferedInputStream(urltoken.openStream());
                                    ByteArrayOutputStream outtoken = new ByteArrayOutputStream();
                                    byte[] buftoken = new byte[1024];
                                    int ntoken = 0;
                                    while (-1 != (ntoken = intoken.read(buftoken))) {
                                        outtoken.write(buftoken, 0, ntoken);
                                    }
                                    outtoken.close();
                                    intoken.close();
                                    byte[] responsetoken = outtoken.toByteArray();
                                    String tokenimage = "";
                                    String tokenthumbimage = "";
                                    if (tokenfound) {
                                        tokenimage = imgPath + "\\" + id + "t.jpg";
                                        tokenthumbimage = thumbPath + "\\" + id + "t.jpg";
                                    } else {
                                        tokenimage = imgPath + "\\" + id + "_tocheck_t.jpg";
                                        tokenthumbimage = thumbPath + "\\" + id + "_tocheck_t.jpg";
                                        System.err.println("Problem downloading token: " + nametoken + " (" + id + "t) from T" + scryset + " on ScryFall");
                                    }
                                    FileOutputStream fos2 = new FileOutputStream(tokenimage);
                                    fos2.write(responsetoken);
                                    fos2.close();

                                    Toolkit toolkitToken = Toolkit.getDefaultToolkit();
                                    MediaTracker trackerToken = new MediaTracker(new Panel());
                                    Image imageToken = toolkitToken.getImage(tokenimage);
                                    trackerToken.addImage(imageToken, 0);
                                    try {
                                        trackerToken.waitForAll();
                                    } catch (Exception e) {

                                    }
                                    BufferedImage resizedImgToken = new BufferedImage(ImgX, ImgY, BufferedImage.TYPE_INT_RGB);
                                    Graphics2D tGraphics2DReiszedToken = resizedImgToken.createGraphics(); //create a graphics object to paint to
                                    tGraphics2DReiszedToken.setBackground(Color.WHITE);
                                    tGraphics2DReiszedToken.setPaint(Color.WHITE);
                                    tGraphics2DReiszedToken.fillRect(0, 0, ImgX, ImgY);
                                    tGraphics2DReiszedToken.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
                                    tGraphics2DReiszedToken.drawImage(imageToken, 0, 0, ImgX, ImgY, null); //draw the image scaled
                                    ImageIO.write(resizedImgToken, "JPG", new File(tokenimage)); //write the image to a file

                                    BufferedImage tThumbImageToken = new BufferedImage(ThumbX, ThumbY, BufferedImage.TYPE_INT_RGB);
                                    Graphics2D tGraphics2DToken = tThumbImageToken.createGraphics(); //create a graphics object to paint to
                                    tGraphics2DToken.setBackground(Color.WHITE);
                                    tGraphics2DToken.setPaint(Color.WHITE);
                                    tGraphics2DToken.fillRect(0, 0, ThumbX, ThumbY);
                                    tGraphics2DToken.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
                                    tGraphics2DToken.drawImage(imageToken, 0, 0, ThumbX, ThumbY, null); //draw the image scaled
                                    ImageIO.write(tThumbImageToken, "JPG", new File(tokenthumbimage)); //write the image to a file

                                    break;
                                }
                            }
                        }

                        break;
                    }
                }
            }
        }
    }
}


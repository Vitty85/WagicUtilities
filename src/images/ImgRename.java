/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package images;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.MediaTracker;
import java.awt.Panel;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.HashMap;
import java.util.Map;
import javax.imageio.ImageIO;

public class ImgRename {

    public static void main(String[] argv) {
        if (argv.length != 3) {
            System.err.println("Usage: java -jar ImageRename.jar ImageFolder DatFilePath Resolution");
            System.exit(-1);
        }
        String ImageFolder = argv[0];
        String DatFilePath = argv[1];
        String targetres = argv[2];
        System.out.println("Renaming images for in " + ImageFolder + " from datfile " + DatFilePath + " using resolution " + targetres);
        
        Integer ImgX = 0;
        Integer ImgY = 0;
        Integer ThumbX = 0;
        Integer ThumbY = 0;

        if (targetres.equals("High")) {
            ImgX = 672;
            ImgY = 936;
            ThumbX = 124;
            ThumbY = 176;
        } else if (targetres.equals("Medium")) {
            ImgX = 488;
            ImgY = 680;
            ThumbX = 90;
            ThumbY = 128;
        } else if (targetres.equals("Low")) {
            ImgX = 244;
            ImgY = 340;
            ThumbX = 45;
            ThumbY = 64;
        } else if (targetres.equals("Tiny")) {
            ImgX = 180;
            ImgY = 255;
            ThumbX = 45;
            ThumbY = 64;
        }
        
        File folder = new File(ImageFolder);
        File[] listOfFiles = folder.listFiles();
        String lines = primitives.PrimitiveDatabase.readLineByLineJava8(DatFilePath);
        Map<String, String> mappa = new HashMap<>();

        while(lines.contains("[card]")) {
            String findStr = "[card]";
            int lastIndex = lines.indexOf(findStr);
            String id = null;
            String primitive = null;
            int a = lines.indexOf("primitive=",lastIndex);
            if (a > 0){
                if(lines.substring(a, lines.indexOf("\n", a)).replace("//", "-").split("=").length > 1)
                    primitive = lines.substring(a, lines.indexOf("\n", a)).replace("//", "-").split("=")[1] + ".jpg";
            }
            int b = lines.indexOf("id=", lastIndex);
            if (b > 0){
                if(lines.substring(b, lines.indexOf("\n", b)).replace("-", "").split("=").length > 1)
                    id = lines.substring(b, lines.indexOf("\n", b)).replace("-", "").split("=")[1] + ".jpg";
            }
            int c = lines.indexOf("[/card]",lastIndex);
            if(c > 0)
                lines = lines.substring(c + 8);
            if (primitive != null && id != null && !id.equalsIgnoreCase("null.jpg")){
                mappa.put(primitive.replace("//", "-").toLowerCase(), id);
            }
        }

        File thumbFolder = new File(ImageFolder + File.separator + "thumbnails" + File.separator);
        if (!thumbFolder.exists()) {
            System.out.println("Creating directory: " + thumbFolder.getName());
            boolean result = false;
            try {
                thumbFolder.mkdir();
                result = true;
            } catch (Exception se) {
                System.err.println("Error: " + thumbFolder + " not created");
                throw se;
            }
            if (result) {
                System.out.println(thumbFolder + " created");
            }
        }
        
        for (int i = 0; i < listOfFiles.length; i++) {
            if (listOfFiles[i].isFile()) {
                File f = new File(ImageFolder + File.separator + listOfFiles[i].getName());
                String nomefile = listOfFiles[i].getName().replace(".full","");
                String newname = mappa.get(nomefile.toLowerCase());
                if (newname != null && !newname.isEmpty()) {
                    System.out.println("Trying to rename and resize " + newname);
                    f.renameTo(new File(ImageFolder + File.separator + newname));
                    try {
                        String cardimage = ImageFolder + File.separator + newname;
                        String thumbcardimage = ImageFolder + File.separator + "thumbnails" + File.separator + newname;
                        Toolkit toolkit = Toolkit.getDefaultToolkit();
                        MediaTracker tracker = new MediaTracker(new Panel());
                        Image image = toolkit.getImage(cardimage);
                        tracker.addImage(image, 0);
                        try {
                            tracker.waitForAll();
                        } catch (Exception e) { }

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
                    }catch (Exception e) {
                        System.err.println("Error while resizing " + newname);
                    }
                }
                else { 
                    System.err.println("Cannot rename " + nomefile + " you have to do by yourself...");
                }
            }
        }
        System.out.println("All cards has been renamed");
    }
}
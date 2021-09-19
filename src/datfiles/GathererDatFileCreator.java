/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package datfiles;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URL;

public class GathererDatFileCreator {

    public static void main(String[] argv) throws IOException {
        String base = "https://gatherer.wizards.com/Pages/Card/";
        String baseurl = base + "Details.aspx?multiverseid=";
        String head = "";

        String set = "MIC";
        Integer ImgX=672;
        Integer ImgY=936;
        Integer ThumbX=124;
        Integer ThumbY=176;
        Integer baseid = 0;
        Integer fine1 = 0;
        Integer baseid2 = 0;
        Integer fine2 = 0;
        Integer baseid3 = 0;
        if (set.equalsIgnoreCase("PCA")) {
            baseid = 423426;
            fine1 = 156;
            baseid2 = 0;
            fine2 = 0;
            baseid3 = 0;
            head = "[meta]\n" +
                    "author=Wagic Team\n" +
                    "name=Planechase Anthology\n" +
                    "year=2016-11-25\n" +
                    "total=156\n" +
                    "[/meta]";
        }
        if (set.equalsIgnoreCase("UST")) {
            baseid = 439390;
            fine1 = 269;
            baseid2 = 0;
            fine2 = 0;
            baseid3 = 0;
            head = "[meta]\n" +
                    "author=Wagic Team\n" +
                    "name=Unstable\n" +
                    "year=2017-12-08\n" +
                    "total=268\n" +
                    "[/meta]";
        } else if (set.equalsIgnoreCase("MH1")) {
            baseid = 463950;
            fine1 = 254;
            baseid2 = 469835;
            fine2 = 0;
            baseid3 = 466744;
            head = "[meta]\n" +
                    "author=Wagic Team\n" +
                    "name=Modern Horizon\n" +
                    "year=2019-06-14\n" +
                    "total=255\n" +
                    "[/meta]";
        } else if (set.equalsIgnoreCase("M20")) {
            baseid = 466755;
            fine1 = 280;
            baseid2 = 469835;
            fine2 = 63;
            baseid3 = 470528;
            head = "[meta]\n" +
                    "author=Wagic Team\n" +
                    "name=Core Set 2020\n" +
                    "year=2019-07-12\n" +
                    "total=344\n" +
                    "[/meta]";
        } else if (set.equalsIgnoreCase("C17")) {
            baseid = 432989;
            fine1 = 309;
            baseid2 = 0;
            fine2 = 0;
            baseid3 = 0;
            head = "[meta]\n" +
                    "author=Wagic Team\n" +
                    "name=Commander 2017\n" +
                    "year=2017-07-12\n" +
                    "total=309\n" +
                    "[/meta]";
        } else if (set.equalsIgnoreCase("C18")) {
            baseid = 450602;
            fine1 = 59;
            baseid2 = 451015;
            fine2 = 248;
            baseid3 = 452081;
            head = "[meta]\n" +
                    "author=Wagic Team\n" +
                    "name=Commander 2018\n" +
                    "year=2018-07-12\n" +
                    "total=308\n" +
                    "[/meta]";
        } else if (set.equalsIgnoreCase("CMD")) {
            baseid = 247207;
            fine1 = 1;
            baseid2 = 241853;
            fine2 = 0;
            baseid3 = 0;
            head = "[meta]\n" +
                    "author=Wagic Team\n" +
                    "name=Commander 2011\n" +
                    "year=2011-07-12\n" +
                    "total=318\n" +
                    "[/meta]";
        } else if (set.equalsIgnoreCase("DGM")) {
            baseid = 369107;
            fine1 = 172;
            baseid2 = 0;
            fine2 = 0;
            baseid3 = 0;
            head = "[meta]\n" +
                    "author=Wagic Team\n" +
                    "name=Dragon Maze\n" +
                    "year=2011-07-12\n" +
                    "total=156\n" +
                    "[/meta]";
        } else if (set.equalsIgnoreCase("DOM")) {
            baseid = 442889;
            fine1 = 282;
            baseid2 = 445848;
            fine2 = 11;
            baseid3 = 0;
            head = "[meta]\n" +
                    "author=Wagic Team\n" +
                    "name=Dragon Maze\n" +
                    "year=2011-07-12\n" +
                    "total=156\n" +
                    "[/meta]";
        } else if (set.equalsIgnoreCase("EVE")) {
            baseid = 442889;
            fine1 = 282;
            baseid2 = 445848;
            fine2 = 11;
            baseid3 = 0;
            head = "[meta]\n" +
                    "author=Wagic Team\n" +
                    "name=Dragon Maze\n" +
                    "year=2011-07-12\n" +
                    "total=156\n" +
                    "[/meta]";
        } else if (set.equalsIgnoreCase("GK1")) {
            baseid = 456216;
            fine1 = 24;
            baseid2 = 456360;
            fine2 = 26;
            baseid3 = 0;
            head = "[meta]\n" +
                    "author=Wagic Team\n" +
                    "name=Dragon Maze\n" +
                    "year=2011-07-12\n" +
                    "total=156\n" +
                    "[/meta]";
        } else if (set.equalsIgnoreCase("GRN")) {
            baseid = 452751;
            fine1 = 259;
            baseid2 = 455600;
            fine2 = 14;
            baseid3 = 0;
            head = "[meta]\n" +
                    "author=Wagic Team\n" +
                    "name=Dragon Maze\n" +
                    "year=2011-07-12\n" +
                    "total=156\n" +
                    "[/meta]";
        } else if (set.equalsIgnoreCase("DDG")) { //KVD
            baseid = 243416;
            fine1 = 0;
            baseid2 = 0;
            fine2 = 0;
            baseid3 = 243498;
            head = "[meta]\n" +
                    "author=Wagic Team\n" +
                    "name=Dragon Maze\n" +
                    "year=2011-07-12\n" +
                    "total=156\n" +
                    "[/meta]";
        } else if (set.equalsIgnoreCase("HOU")) {
            baseid = 430690;
            fine1 = 199;
            baseid2 = 432879;
            fine2 = 10;
            baseid3 = 0;
            head = "[meta]\n" +
                    "author=Wagic Team\n" +
                    "name=Dragon Maze\n" +
                    "year=2011-07-12\n" +
                    "total=156\n" +
                    "[/meta]";
        } else if (set.equalsIgnoreCase("M11")) {
            baseid = 213612;
            fine1 = 20;
            baseid2 = 0;
            fine2 = 0;
            baseid3 = 0;
            head = "[meta]\n" +
                    "author=Wagic Team\n" +
                    "name=Dragon Maze\n" +
                    "year=2011-07-12\n" +
                    "total=156\n" +
                    "[/meta]";
        } else if (set.equalsIgnoreCase("M12")) {
            baseid = 245184;
            fine1 = 5;
            baseid2 = 0;
            fine2 = 0;
            baseid3 = 0;
            head = "[meta]\n" +
                    "author=Wagic Team\n" +
                    "name=Dragon Maze\n" +
                    "year=2011-07-12\n" +
                    "total=156\n" +
                    "[/meta]";
        } else if (set.equalsIgnoreCase("M19")) {
            baseid = 447137;
            fine1 = 281;
            baseid2 = 450228;
            fine2 = 34;
            baseid3 = 0;
            head = "[meta]\n" +
                    "author=Wagic Team\n" +
                    "name=Dragon Maze\n" +
                    "year=2011-07-12\n" +
                    "total=156\n" +
                    "[/meta]";
        } else if (set.equalsIgnoreCase("RIX")) {
            baseid = 439658;
            fine1 = 203;
            baseid2 = 441891;
            fine2 = 9;
            baseid3 = 0;
            head = "[meta]\n" +
                    "author=Wagic Team\n" +
                    "name=Dragon Maze\n" +
                    "year=2011-07-12\n" +
                    "total=156\n" +
                    "[/meta]";
        } else if (set.equalsIgnoreCase("RNA")) {
            baseid = 457145;
            fine1 = 259;
            baseid2 = 459994;
            fine2 = 14;
            baseid3 = 0;
            head = "[meta]\n" +
                    "author=Wagic Team\n" +
                    "name=Dragon Maze\n" +
                    "year=2011-07-12\n" +
                    "total=156\n" +
                    "[/meta]";
        } else if (set.equalsIgnoreCase("XLN")) {
            baseid = 435152;
            fine1 = 300;
            baseid2 = 0;
            fine2 = 0;
            baseid3 = 0;
            head = "[meta]\n" +
                    "author=Wagic Team\n" +
                    "name=Dragon Maze\n" +
                    "year=2011-07-12\n" +
                    "total=156\n" +
                    "[/meta]";
        } else if (set.equalsIgnoreCase("ZEN")) {
            baseid = 201959;
            fine1 = 20;
            baseid2 = 195157;
            fine2 = 40;
            baseid3 = 0;
            head = "[meta]\n" +
                    "author=Wagic Team\n" +
                    "name=Dragon Maze\n" +
                    "year=2011-07-12\n" +
                    "total=156\n" +
                    "[/meta]";
        } else if (set.equalsIgnoreCase("AKH")) {
            baseid = 426703;
            fine1 = 269;
            baseid2 = 429662;
            fine2 = 18;
            baseid3 = 0;
            head = "[meta]\n" +
                    "author=Wagic Team\n" +
                    "name=Dragon Maze\n" +
                    "year=2011-07-12\n" +
                    "total=156\n" +
                    "[/meta]";
        } else if (set.equalsIgnoreCase("WAR")) {
            baseid = 460928;
            fine1 = 264;
            baseid2 = 463832;
            fine2 = 11;
            baseid3 = 0;
            head = "[meta]\n" +
                    "author=Wagic Team\n" +
                    "name=Dragon Maze\n" +
                    "year=2011-07-12\n" +
                    "total=156\n" +
                    "[/meta]";
        } else if (set.equalsIgnoreCase("SS2")) {
            baseid = 470539;
            fine1 = 8;
            baseid2 = 0;
            fine2 = 0;
            baseid3 = 0;
            head = "[meta]\n" +
                    "author=Wagic Team\n" +
                    "name=Signature Spellbok: Gideon\n" +
                    "year=2019-06-28\n" +
                    "total=8\n" +
                    "[/meta]";
        } else if (set.equalsIgnoreCase("C19")) {
            baseid = 470547;
            fine1 = 302;
            baseid2 = 0;
            fine2 = 0;
            baseid3 = 0;
            head = "[meta]\n" +
                    "author=Wagic Team\n" +
                    "name=Commander 2019\n" +
                    "year=2019-08-23\n" +
                    "total=302\n" +
                    "[/meta]";
        } else if (set.equalsIgnoreCase("JMP")) {
            baseid = 489168;
            fine1 = 495;
            baseid2 = 0;
            fine2 = 0;
            baseid3 = 0;
            head = "[meta]\n" +
                    "author=Wagic Team\n" +
                    "name=Jumpstart\n" +
                    "year=2020-07-03\n" +
                    "total=495\n" +
                    "[/meta]";
        }  else if (set.equalsIgnoreCase("M21")) {
            baseid = 488626; //488461 (15) //488626 (20) //485324 (259)
            fine1 = 20;
            baseid2 = 0;
            fine2 = 0;
            baseid3 = 0;
            head = "[meta]\n" +
                    "author=Wagic Team\n" +
                    "name=Core Set 2021\n" +
                    "year=2020-07-03\n" +
                    "total=284\n" +
                    "[/meta]";
        }  else if (set.equalsIgnoreCase("2XM")) {
            baseid = 491334; //491572 (10) // 489674 (332) // 491344 (38) // 491334 (2)
            fine1 = 2;
            baseid2 = 0;
            fine2 = 0;
            baseid3 = 0;
            head = "[meta]\n" +
                    "author=Wagic Team\n" +
                    "name=Double Masters\n" +
                    "year=2020-08-07\n" +
                    "total=424\n" +
                    "[/meta]";
        } else if (set.equalsIgnoreCase("ZNR")) {
            baseid = 495098; //491622 (316) // 495098 (5) // 491837 (100)
            fine1 = 5;
            baseid2 = 0;
            fine2 = 0;
            baseid3 = 0;
            head = "[meta]\n" +
                    "author=Wagic Team\n" +
                    "name=Zendikar Rising\n" +
                    "year=2020-09-25\n" +
                    "total=316\n" +
                    "[/meta]";
        } else if (set.equalsIgnoreCase("ZNE")) {
            baseid = 497161;
            fine1 = 30;
            baseid2 = 0;
            fine2 = 0;
            baseid3 = 0;
            head = "[meta]\n" +
                    "author=Wagic Team\n" +
                    "name=Zendikar Rising Expeditions\n" +
                    "year=2020-09-25\n" +
                    "total=30\n" +
                    "[/meta]";
        } else if (set.equalsIgnoreCase("ZNC")) {
            baseid = 495893; // 495947 (134)
            fine1 = 6;
            baseid2 = 0;
            fine2 = 0;
            baseid3 = 0;
            head = "[meta]\n" +
                    "author=Wagic Team\n" +
                    "name=Zendikar Rising Commander\n" +
                    "year=2020-09-25\n" +
                    "total=153\n" +
                    "[/meta]";
        }
        else if (set.equalsIgnoreCase("KHM")) {
            baseid = 507585; // 503605 (301) // 506916 (20) // 507585 (5)
            fine1 = 5;
            baseid2 = 0;
            fine2 = 0;
            baseid3 = 0;
            head = "[meta]\n" +
                    "author=Wagic Team\n" +
                    "name=Kaldheim\n" +
                    "orderindex=EXP-ZZH.KHM\n" +
                    "year=2021-02-05\n" +
                    "total=326\n" +
                    "[/meta]";
        }
        else if (set.equalsIgnoreCase("KHC")) {
            baseid = 508307; // 508147 (16) // 508307 (103)
            fine1 = 103;
            baseid2 = 0;
            fine2 = 0;
            baseid3 = 0;
            head = "[meta]\n" +
                    "author=Wagic Team\n" +
                    "name=Kaldheim Commander\n" +
                    "orderindex=COM-P.KHC\n" +
                    "year=2021-02-05\n" +
                    "total=119\n" +
                    "[/meta]";
        }
        else if (set.equalsIgnoreCase("AFR")) {
            baseid = 530155; //527289 (259) // 530155 (20)
            fine1 = 20;
            baseid2 = 0;
            fine2 = 0;
            baseid3 = 0;
            head = "[meta]\n" +
                    "author=Wagic Team\n" +
                    "name=Adventures in the Forgotten Realms\n" +
                    "orderindex=EXP-ZZI.AFR\n" +
                    "year=2021-07-23\n" +
                    "total=313\n" +
                    "[/meta]";
        }
        else if (set.equalsIgnoreCase("AFC")) {
            baseid = 531870; // 531830 (4) // 531870 (4) // 531910 (54) // 532450 (211) // 534560 (2)
            fine1 = 5;
            baseid2 = 0;
            fine2 = 0;
            baseid3 = 0;
            head = "[meta]\n" +
                    "author=Wagic Team\n" +
                    "name=Forgotten Realms Commander\n" +
                    "orderindex=COM-R.AFC\n" +
                    "year=2021-07-23\n" +
                    "total=271\n" +
                    "[/meta]";
        }
        else if (set.equalsIgnoreCase("MID")) {
            baseid = 538227; // 534751 (316) // 538227 (10)
            fine1 = 10;
            baseid2 = 0;
            fine2 = 0;
            baseid3 = 0;
            head = "[meta]\n" +
                    "author=Wagic Team\n" +
                    "name=Innistrad: Midnight Hunt\n" +
                    "orderindex=EXP-ZZK.MID\n" +
                    "year=2021-09-24\n" +
                    "total=368\n" +
                    "[/meta]";
        }
        else if (set.equalsIgnoreCase("MIC")) {
            baseid = 540728; // 539338 (111) // 540448 (26) // 540708 (2) // 540728 (2) // 540748 (8)
            fine1 = 2;
            baseid2 = 0;
            fine2 = 0;
            baseid3 = 0;
            head = "[meta]\n" +
                    "author=Wagic Team\n" +
                    "name=Midnight Hunt Commander\n" +
                    "orderindex=COM-S.MIC\n" +
                    "year=2021-09-24\n" +
                    "total=186\n" +
                    "[/meta]";
        }

        File cards = new File("C:\\Users\\alfieriv\\Desktop\\TODO\\" + set + "\\" + "_cards.dat");
        FileWriter fw = new FileWriter(cards);
        fw.append(head + "\n");

        for (int y = 0; y < fine1; y++) {
            Integer id = baseid + y;
            Document doc = Jsoup.connect(baseurl + id.toString()).get();
            Elements divs = doc.select("body div");
            int k;
            for (k = 0; k < divs.size(); k++)
                if (divs.get(k).childNodes().size() > 0 && divs.get(k).childNode(0).toString().toLowerCase().contains("card name"))
                    break;
            if (k >= divs.size())
                continue;
            String name = divs.get(k + 1).childNode(0).attributes().get("#text").replace("\r\n", "").trim();

            for (k = 0; k < divs.size(); k++)
                if (divs.get(k).childNodes().size() > 0 && divs.get(k).childNode(0).toString().contains("Rarity"))
                    break;
            String rare = divs.get(k + 1).childNode(1).childNodes().get(0).toString().replace("\r\n", "").trim();

            if (rare.toLowerCase().contains("uncommon"))
                rare = "U";
            else if (rare.toLowerCase().contains("mythic"))
                rare = "M";
            else if (rare.toLowerCase().contains("rare"))
                rare = "R";
            else if (rare.toLowerCase().contains("land"))
                rare = "L";
            else if (rare.toLowerCase().contains("token"))
                rare = "T";
            else if (rare.toLowerCase().contains("common"))
                rare = "C";

            String card = "[card]\n" +
                    "primitive=" + name + "\n" +
                    "id=" + id + "\n" +
                    "rarity=" + rare + "\n" +
                    "[/card]";
            fw.append(card + "\n");
            fw.flush();

            for (k = 0; k < divs.size(); k++)
                if (divs.get(k).childNodes().size() > 0 && divs.get(k).childNode(0).toString().toLowerCase().contains("card text"))
                    break;
            if (k >= divs.size())
                continue;
            String text = divs.get(k + 1).parentNode().toString().replace("\r\n", "").trim();

            String nametoken = "";
            if ((text.trim().toLowerCase().contains("create") && text.trim().toLowerCase().contains("creature token")) ||
                    (text.trim().toLowerCase().contains("put") && text.trim().toLowerCase().contains("token"))) {
                String arrays[] = text.trim().split(" ");
                for (int l = 1; l < arrays.length - 1; l++) {
                    if (arrays[l].equalsIgnoreCase("creature") && arrays[l + 1].toLowerCase().contains("token")) {
                        nametoken = arrays[l - 1];
                        if (nametoken.equalsIgnoreCase("artifact")) {
                            if (l - 2 > 0)
                                nametoken = arrays[l - 2];
                            break;
                        } else if (arrays[l].equalsIgnoreCase("put") && arrays[l + 3].toLowerCase().contains("token")) {
                            nametoken = arrays[l + 2];
                            break;
                        }
                    }
                }
                if (nametoken.isEmpty()) {
                    System.err.println("Error reading token info for (-" + id + "), you have to manually fix it later into Dat file!");
                    nametoken = "Unknown";
                }

                card = "[card]\n" +
                        "primitive=" + nametoken + "\n" +
                        "id=-" + id + "\n" +
                        "rarity=T" +"\n" +
                        "[/card]";
                fw.append(card + "\n");
                fw.flush();
            }

            doc = Jsoup.connect("https://scryfall.com/sets/" + set.toLowerCase()).get();
            Elements imgs = doc.select("body img");

            for (int i = 100000; i < imgs.size(); i++) {
                String title = imgs.get(i).attributes().get("title");
                if (title.toLowerCase().contains(name.toLowerCase())) {
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
                    String imagepath = "C:\\Users\\alfieriv\\Desktop\\TODO\\" + set + "\\" + id + ".jpg";
                    String thumbnailspath = "C:\\Users\\alfieriv\\Desktop\\TODO\\" + set + "\\thumbnails\\" + id + ".jpg";
                    FileOutputStream fos = new FileOutputStream(imagepath);
                    fos.write(response);
                    fos.close();

                    Toolkit toolkit = Toolkit.getDefaultToolkit();
                    MediaTracker tracker = new MediaTracker(new Panel());
                    Image image = toolkit.getImage(imagepath);
                    tracker.addImage(image, 0);
                    try{
                        tracker.waitForAll();
                    } catch (Exception e){

                    }
                    BufferedImage resizedImg = new BufferedImage( ImgX, ImgY, BufferedImage.TYPE_INT_RGB );
                    Graphics2D tGraphics2DReiszed = resizedImg.createGraphics(); //create a graphics object to paint to
                    tGraphics2DReiszed.setBackground( Color.WHITE );
                    tGraphics2DReiszed.setPaint( Color.WHITE );
                    tGraphics2DReiszed.fillRect( 0, 0, ImgX, ImgY );
                    tGraphics2DReiszed.setRenderingHint( RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR );
                    tGraphics2DReiszed.drawImage( image, 0, 0, ImgX, ImgY, null ); //draw the image scaled
                    ImageIO.write(resizedImg, "JPG", new File(imagepath)); //write the image to a file

                    BufferedImage tThumbImage = new BufferedImage( ThumbX, ThumbY, BufferedImage.TYPE_INT_RGB );
                    Graphics2D tGraphics2D = tThumbImage.createGraphics(); //create a graphics object to paint to
                    tGraphics2D.setBackground( Color.WHITE );
                    tGraphics2D.setPaint( Color.WHITE );
                    tGraphics2D.fillRect( 0, 0, ThumbX, ThumbY );
                    tGraphics2D.setRenderingHint( RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR );
                    tGraphics2D.drawImage( image, 0, 0, ThumbX, ThumbY, null ); //draw the image scaled
                    ImageIO.write(tThumbImage, "JPG", new File(thumbnailspath)); //write the image to a file
                    text = "";
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
                    if (text.trim().toLowerCase().contains("emblem") || (text.trim().toLowerCase().contains("create") && text.trim().toLowerCase().contains("creature token"))) {
                        boolean tokenfound = false;
                        String arrays[] = text.trim().split(" ");
                        nametoken = "";
                        for(int l = 1; l < arrays.length-1; l++){
                            if(arrays[l].equalsIgnoreCase("creature") && arrays[l+1].toLowerCase().contains("token")) {
                                nametoken = arrays[l - 1];
                                break;
                            }
                        }
                        if (nametoken.isEmpty()) {
                            tokenfound = false;
                            nametoken = name;
                            doc = Jsoup.connect("https://scryfall.com/sets/" + set.toLowerCase()).get();
                        } else {
                            tokenfound = true;
                            doc = Jsoup.connect("https://scryfall.com/sets/t" + set.toLowerCase()).get();
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
                                String filename = "";
                                String filenameThumb = "";
                                if(tokenfound) {
                                    filename = "C:\\Users\\alfieriv\\Desktop\\TODO\\" + set + "\\" + id + "t.jpg";
                                    filenameThumb = "C:\\Users\\alfieriv\\Desktop\\TODO\\" + set + "\\thumbnails\\" + id + "t.jpg";
                                }
                                else {
                                    filename = "C:\\Users\\alfieriv\\Desktop\\TODO\\" + set + "\\" + id + "_tocheck_t.jpg";
                                    filenameThumb = "C:\\Users\\alfieriv\\Desktop\\TODO\\" + set + "\\thumbnails\\" + id + "_tocheck_t.jpg";
                                }
                                FileOutputStream fos2 = new FileOutputStream(filename);
                                fos2.write(responsetoken);
                                fos2.close();

                                Toolkit toolkitToken = Toolkit.getDefaultToolkit();
                                MediaTracker trackerToken = new MediaTracker(new Panel());
                                Image imageToken = toolkitToken.getImage(filename);
                                trackerToken.addImage(imageToken, 0);
                                try{
                                    trackerToken.waitForAll();
                                } catch (Exception e){

                                }
                                BufferedImage resizedImgToken = new BufferedImage( ImgX, ImgY, BufferedImage.TYPE_INT_RGB );
                                Graphics2D tGraphics2DReiszedToken = resizedImgToken.createGraphics(); //create a graphics object to paint to
                                tGraphics2DReiszedToken.setBackground( Color.WHITE );
                                tGraphics2DReiszedToken.setPaint( Color.WHITE );
                                tGraphics2DReiszedToken.fillRect( 0, 0, ImgX, ImgY );
                                tGraphics2DReiszedToken.setRenderingHint( RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR );
                                tGraphics2DReiszedToken.drawImage( imageToken, 0, 0, ImgX, ImgY, null ); //draw the image scaled
                                ImageIO.write(resizedImgToken, "JPG", new File(filename)); //write the image to a file

                                BufferedImage tThumbImageToken = new BufferedImage( ThumbX, ThumbY, BufferedImage.TYPE_INT_RGB );
                                Graphics2D tGraphics2DToken = tThumbImageToken.createGraphics(); //create a graphics object to paint to
                                tGraphics2DToken.setBackground( Color.WHITE );
                                tGraphics2DToken.setPaint( Color.WHITE );
                                tGraphics2DToken.fillRect( 0, 0, ThumbX, ThumbY );
                                tGraphics2DToken.setRenderingHint( RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR );
                                tGraphics2DToken.drawImage( imageToken, 0, 0, ThumbX, ThumbY, null ); //draw the image scaled
                                ImageIO.write(tThumbImageToken, "JPG", new File(filenameThumb)); //write the image to a file

                                break;
                            }
                        }
                    }

                    break;
                }
            }
        }
        for (int y = 0; y < fine2; y++) {
            Integer id = baseid2 + y;
            Document doc = Jsoup.connect(baseurl + id.toString()).get();
            Elements divs = doc.select("body div");
            int k;
            for (k = 0; k < divs.size(); k++)
                if (divs.get(k).childNodes().size() > 0 && divs.get(k).childNode(0).toString().toLowerCase().contains("card name"))
                    break;
            if (k >= divs.size())
                continue;
            String name = divs.get(k + 1).childNode(0).attributes().get("#text").replace("\r\n", "").trim();

            for (k = 0; k < divs.size(); k++)
                if (divs.get(k).childNodes().size() > 0 && divs.get(k).childNode(0).toString().contains("Rarity"))
                    break;
            String rare = divs.get(k + 1).childNode(1).childNodes().get(0).toString().replace("\r\n", "").trim();

            if (rare.toLowerCase().contains("uncommon"))
                rare = "U";
            else if (rare.toLowerCase().contains("mythic"))
                rare = "M";
            else if (rare.toLowerCase().contains("rare"))
                rare = "R";
            else if (rare.toLowerCase().contains("land"))
                rare = "L";
            else if (rare.toLowerCase().contains("token"))
                rare = "T";
            else if (rare.toLowerCase().contains("common"))
                rare = "C";

            String card = "[card]\n" +
                    "primitive=" + name + "\n" +
                    "id=" + id + "\n" +
                    "rarity=" + rare + "\n" +
                    "[/card]";
            fw.append(card + "\n");
            fw.flush();

            doc = Jsoup.connect("https://scryfall.com/sets/" + set.toLowerCase()).get();
            Elements imgs = doc.select("body img");
            for (int i = 0; i < imgs.size(); i++) {
                String title = imgs.get(i).attributes().get("title");
                if (title.toLowerCase().contains(name.toLowerCase())) {
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
                    String imagepath = "C:\\Users\\alfieriv\\Desktop\\TODO\\" + set + "\\" + id + ".jpg";
                    String thumbnailspath = "C:\\Users\\alfieriv\\Desktop\\TODO\\" + set + "\\thumbnails\\" + id + ".jpg";
                    FileOutputStream fos = new FileOutputStream(imagepath);
                    fos.write(response);
                    fos.close();

                    Toolkit toolkit = Toolkit.getDefaultToolkit();
                    MediaTracker tracker = new MediaTracker(new Panel());
                    Image image = toolkit.getImage(imagepath);
                    tracker.addImage(image, 0);
                    try{
                        tracker.waitForAll();
                    } catch (Exception e){

                    }
                    BufferedImage resizedImg = new BufferedImage( ImgX, ImgY, BufferedImage.TYPE_INT_RGB );
                    Graphics2D tGraphics2DReiszed = resizedImg.createGraphics(); //create a graphics object to paint to
                    tGraphics2DReiszed.setBackground( Color.WHITE );
                    tGraphics2DReiszed.setPaint( Color.WHITE );
                    tGraphics2DReiszed.fillRect( 0, 0, ImgX, ImgY );
                    tGraphics2DReiszed.setRenderingHint( RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR );
                    tGraphics2DReiszed.drawImage( image, 0, 0, ImgX, ImgY, null ); //draw the image scaled
                    ImageIO.write(resizedImg, "JPG", new File(imagepath)); //write the image to a file

                    BufferedImage tThumbImage = new BufferedImage( ThumbX, ThumbY, BufferedImage.TYPE_INT_RGB );
                    Graphics2D tGraphics2D = tThumbImage.createGraphics(); //create a graphics object to paint to
                    tGraphics2D.setBackground( Color.WHITE );
                    tGraphics2D.setPaint( Color.WHITE );
                    tGraphics2D.fillRect( 0, 0, ThumbX, ThumbY );
                    tGraphics2D.setRenderingHint( RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR );
                    tGraphics2D.drawImage( image, 0, 0, ThumbX, ThumbY, null ); //draw the image scaled
                    ImageIO.write(tThumbImage, "JPG", new File(thumbnailspath)); //write the image to a file
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
                    if (text.trim().toLowerCase().contains("emblem") || (text.trim().toLowerCase().contains("create") && text.trim().toLowerCase().contains("creature token"))) {
                        boolean tokenfound = false;
                        String arrays[] = text.trim().split(" ");
                        String nametoken = "";
                        for(int l = 1; l < arrays.length-1; l++){
                            if(arrays[l].equalsIgnoreCase("creature") && arrays[l+1].toLowerCase().contains("token")) {
                                nametoken = arrays[l - 1];
                                break;
                            }
                        }
                        if (nametoken.isEmpty()) {
                            tokenfound = false;
                            nametoken = name;
                            doc = Jsoup.connect("https://scryfall.com/sets/" + set.toLowerCase()).get();
                        } else {
                            tokenfound = true;
                            doc = Jsoup.connect("https://scryfall.com/sets/t" + set.toLowerCase()).get();
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
                                String filename = "";
                                String filenameThumb = "";
                                if(tokenfound) {
                                    filename = "C:\\Users\\alfieriv\\Desktop\\TODO\\" + set + "\\" + id + "t.jpg";
                                    filenameThumb = "C:\\Users\\alfieriv\\Desktop\\TODO\\" + set + "\\thumbnails\\" + id + "t.jpg";
                                }
                                else {
                                    filename = "C:\\Users\\alfieriv\\Desktop\\TODO\\" + set + "\\" + id + "_tocheck_t.jpg";
                                    filenameThumb = "C:\\Users\\alfieriv\\Desktop\\TODO\\" + set + "\\thumbnails\\" + id + "_tocheck_t.jpg";
                                }
                                FileOutputStream fos2 = new FileOutputStream(filename);
                                fos2.write(responsetoken);
                                fos2.close();

                                Toolkit toolkitToken = Toolkit.getDefaultToolkit();
                                MediaTracker trackerToken = new MediaTracker(new Panel());
                                Image imageToken = toolkitToken.getImage(filename);
                                trackerToken.addImage(imageToken, 0);
                                try{
                                    trackerToken.waitForAll();
                                } catch (Exception e){

                                }
                                BufferedImage resizedImgToken = new BufferedImage( ImgX, ImgY, BufferedImage.TYPE_INT_RGB );
                                Graphics2D tGraphics2DReiszedToken = resizedImgToken.createGraphics(); //create a graphics object to paint to
                                tGraphics2DReiszedToken.setBackground( Color.WHITE );
                                tGraphics2DReiszedToken.setPaint( Color.WHITE );
                                tGraphics2DReiszedToken.fillRect( 0, 0, ImgX, ImgY );
                                tGraphics2DReiszedToken.setRenderingHint( RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR );
                                tGraphics2DReiszedToken.drawImage( imageToken, 0, 0, ImgX, ImgY, null ); //draw the image scaled
                                ImageIO.write(resizedImgToken, "JPG", new File(filename)); //write the image to a file

                                BufferedImage tThumbImageToken = new BufferedImage( ThumbX, ThumbY, BufferedImage.TYPE_INT_RGB );
                                Graphics2D tGraphics2DToken = tThumbImageToken.createGraphics(); //create a graphics object to paint to
                                tGraphics2DToken.setBackground( Color.WHITE );
                                tGraphics2DToken.setPaint( Color.WHITE );
                                tGraphics2DToken.fillRect( 0, 0, ThumbX, ThumbY );
                                tGraphics2DToken.setRenderingHint( RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR );
                                tGraphics2DToken.drawImage( imageToken, 0, 0, ThumbX, ThumbY, null ); //draw the image scaled
                                ImageIO.write(tThumbImageToken, "JPG", new File(filenameThumb)); //write the image to a file

                                break;
                            }
                        }
                    }

                    break;
                }
            }
        }
        if (baseid3 > 0) {
            Integer id = baseid3;
            Document doc = Jsoup.connect(baseurl + id.toString()).get();
            Elements divs = doc.select("body div");
            int k;
            for (k = 0; k < divs.size(); k++)
                if (divs.get(k).childNodes().size() > 0 && divs.get(k).childNode(0).toString().toLowerCase().contains("card name"))
                    break;
            if (k >= divs.size())
                return;
            String name = divs.get(k + 1).childNode(0).attributes().get("#text").replace("\r\n", "").trim();

            for (k = 0; k < divs.size(); k++)
                if (divs.get(k).childNodes().size() > 0 && divs.get(k).childNode(0).toString().contains("Rarity"))
                    break;
            String rare = divs.get(k + 1).childNode(1).childNodes().get(0).toString().replace("\r\n", "").trim();

            if (rare.toLowerCase().contains("uncommon"))
                rare = "U";
            else if (rare.toLowerCase().contains("mythic"))
                rare = "M";
            else if (rare.toLowerCase().contains("rare"))
                rare = "R";
            else if (rare.toLowerCase().contains("land"))
                rare = "L";
            else if (rare.toLowerCase().contains("token"))
                rare = "T";
            else if (rare.toLowerCase().contains("common"))
                rare = "C";

            String card = "[card]\n" +
                    "primitive=" + name + "\n" +
                    "id=" + id + "\n" +
                    "rarity=" + rare + "\n" +
                    "[/card]";
            fw.append(card);
            fw.flush();

            doc = Jsoup.connect("https://scryfall.com/sets/" + set.toLowerCase()).get();
            Elements imgs = doc.select("body img");
            for (int i = 0; i < imgs.size(); i++) {
                String title = imgs.get(i).attributes().get("title");
                if (title.toLowerCase().contains(name.toLowerCase())) {
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
                    String imagepath = "C:\\Users\\alfieriv\\Desktop\\TODO\\" + set + "\\" + id + ".jpg";
                    String thumbnailspath = "C:\\Users\\alfieriv\\Desktop\\TODO\\" + set + "\\thumbnails\\" + id + ".jpg";
                    FileOutputStream fos = new FileOutputStream(imagepath);
                    fos.write(response);
                    fos.close();

                    Toolkit toolkit = Toolkit.getDefaultToolkit();
                    MediaTracker tracker = new MediaTracker(new Panel());
                    Image image = toolkit.getImage(imagepath);
                    tracker.addImage(image, 0);
                    try{
                        tracker.waitForAll();
                    } catch (Exception e){

                    }
                    BufferedImage resizedImg = new BufferedImage( ImgX, ImgY, BufferedImage.TYPE_INT_RGB );
                    Graphics2D tGraphics2DReiszed = resizedImg.createGraphics(); //create a graphics object to paint to
                    tGraphics2DReiszed.setBackground( Color.WHITE );
                    tGraphics2DReiszed.setPaint( Color.WHITE );
                    tGraphics2DReiszed.fillRect( 0, 0, ImgX, ImgY );
                    tGraphics2DReiszed.setRenderingHint( RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR );
                    tGraphics2DReiszed.drawImage( image, 0, 0, ImgX, ImgY, null ); //draw the image scaled
                    ImageIO.write(resizedImg, "JPG", new File(imagepath)); //write the image to a file

                    BufferedImage tThumbImage = new BufferedImage( ThumbX, ThumbY, BufferedImage.TYPE_INT_RGB );
                    Graphics2D tGraphics2D = tThumbImage.createGraphics(); //create a graphics object to paint to
                    tGraphics2D.setBackground( Color.WHITE );
                    tGraphics2D.setPaint( Color.WHITE );
                    tGraphics2D.fillRect( 0, 0, ThumbX, ThumbY );
                    tGraphics2D.setRenderingHint( RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR );
                    tGraphics2D.drawImage( image, 0, 0, ThumbX, ThumbY, null ); //draw the image scaled
                    ImageIO.write(tThumbImage, "JPG", new File(thumbnailspath)); //write the image to a file
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
                    if (text.trim().toLowerCase().contains("emblem") || (text.trim().toLowerCase().contains("create") && text.trim().toLowerCase().contains("creature token"))) {
                        boolean tokenfound = false;
                        String arrays[] = text.trim().split(" ");
                        String nametoken = "";
                        for(int l = 1; l < arrays.length-1; l++){
                            if(arrays[l].equalsIgnoreCase("creature") && arrays[l+1].toLowerCase().contains("token")) {
                                nametoken = arrays[l - 1];
                                break;
                            }
                        }
                        if (nametoken.isEmpty()) {
                            tokenfound = false;
                            nametoken = name;
                            doc = Jsoup.connect("https://scryfall.com/sets/" + set.toLowerCase()).get();
                        } else {
                            tokenfound = true;
                            doc = Jsoup.connect("https://scryfall.com/sets/t" + set.toLowerCase()).get();
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
                                String filename = "";
                                String filenameThumb = "";
                                if(tokenfound) {
                                    filename = "C:\\Users\\alfieriv\\Desktop\\TODO\\" + set + "\\" + id + "t.jpg";
                                    filenameThumb = "C:\\Users\\alfieriv\\Desktop\\TODO\\" + set + "\\thumbnails\\" + id + "t.jpg";
                                }
                                else {
                                    filename = "C:\\Users\\alfieriv\\Desktop\\TODO\\" + set + "\\" + id + "_tocheck_t.jpg";
                                    filenameThumb = "C:\\Users\\alfieriv\\Desktop\\TODO\\" + set + "\\thumbnails\\" + id + "_tocheck_t.jpg";
                                }
                                FileOutputStream fos2 = new FileOutputStream(filename);
                                fos2.write(responsetoken);
                                fos2.close();

                                Toolkit toolkitToken = Toolkit.getDefaultToolkit();
                                MediaTracker trackerToken = new MediaTracker(new Panel());
                                Image imageToken = toolkitToken.getImage(filename);
                                trackerToken.addImage(imageToken, 0);
                                try{
                                    trackerToken.waitForAll();
                                } catch (Exception e){

                                }
                                BufferedImage resizedImgToken = new BufferedImage( ImgX, ImgY, BufferedImage.TYPE_INT_RGB );
                                Graphics2D tGraphics2DReiszedToken = resizedImgToken.createGraphics(); //create a graphics object to paint to
                                tGraphics2DReiszedToken.setBackground( Color.WHITE );
                                tGraphics2DReiszedToken.setPaint( Color.WHITE );
                                tGraphics2DReiszedToken.fillRect( 0, 0, ImgX, ImgY );
                                tGraphics2DReiszedToken.setRenderingHint( RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR );
                                tGraphics2DReiszedToken.drawImage( imageToken, 0, 0, ImgX, ImgY, null ); //draw the image scaled
                                ImageIO.write(resizedImgToken, "JPG", new File(filename)); //write the image to a file

                                BufferedImage tThumbImageToken = new BufferedImage( ThumbX, ThumbY, BufferedImage.TYPE_INT_RGB );
                                Graphics2D tGraphics2DToken = tThumbImageToken.createGraphics(); //create a graphics object to paint to
                                tGraphics2DToken.setBackground( Color.WHITE );
                                tGraphics2DToken.setPaint( Color.WHITE );
                                tGraphics2DToken.fillRect( 0, 0, ThumbX, ThumbY );
                                tGraphics2DToken.setRenderingHint( RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR );
                                tGraphics2DToken.drawImage( imageToken, 0, 0, ThumbX, ThumbY, null ); //draw the image scaled
                                ImageIO.write(tThumbImageToken, "JPG", new File(filenameThumb)); //write the image to a file

                                break;
                            }
                        }
                    }

                    break;
                }
            }
        }
        fw.close();
    }
}

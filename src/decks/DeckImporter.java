/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package decks;

import java.io.FileOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.Scanner;

public class DeckImporter
{
    public static void main(String[] argv) throws IOException {
        argv = new String[] { "C:\\Program Files (x86)\\Emulatori\\Wagic\\", "C:\\Program Files (x86)\\Emulatori\\Sony\\PSVita\\Games\\PSP\\Wagic\\Keisuke Sato Draft.txt" , "Black-Red Magic World Championship XXVII"};
        if (argv.length < 2 || argv.length > 3) {
            System.err.println("Usage: java -jar DeckImporter.jar WagicPath inputDeckFile [deckDescription]");
            System.exit(-1);
        }
        String activePath = argv[0];
        String mypath = argv[1];
        String desc = (argv.length < 3)?"":argv[2];
        File f = new File(mypath);
        String message = "";
        String deck = "";
        String deckname;
        String prefix = "";
        int cardcount = 0;
        if(f.exists() && !f.isDirectory())
        { 
            deckname = f.getName();
            int pos = deckname.lastIndexOf(".");
            if (pos > 0) 
            {
                deckname = deckname.substring(0, pos);
            }
            deck += "#NAME:"+deckname+"\n"; 
            if(!desc.isEmpty()){
                int index = desc.indexOf(" ", 40);
                if(desc.length() < 40 || index < 0)
                    deck += "#DESC:"+desc+"\n";
                else {
                    deck += "#DESC:"+desc.substring(0, index).trim()+"\n";
                    deck += "#DESC:"+desc.substring(index, desc.length()).trim()+"\n";
                }
            }
            try
            {
                Scanner scanner = new Scanner(new File(mypath));                
                if (scanner.hasNext()) 
                {
                    while (scanner.hasNext()) 
                    {
                        String line = scanner.nextLine();
                        line = line.trim();
                        if (line.equals("")) {
                            line = scanner.nextLine();
                            prefix = "#SB:"; // Sideboard started from next card.
                            if (line.equals("")) {
                                line = scanner.nextLine(); // Sometimes there are 2 blank lines from main deck and sideboard.
                            }
                        }
                        if (!line.equals("")) // don't write out blank lines
                        {
                            String[] slines = line.split("\\s+");
                            String arranged = "";
                            for (int idx = 1; idx < slines.length; idx++)
                            {
                                arranged += slines[idx] + " ";
                            }
                            if ((isNumeric(slines[0])))
                            {
                                if (slines[1] != null && slines[1].startsWith("["))
                                {
                                    arranged = arranged.substring(5);
                                    slines[1] = slines[1].replaceAll("\\[", "").replaceAll("\\]", "");
                                    deck += prefix + arranged + " (" + renameSet(slines[1]) + ") * " + slines[0] + "\n";
                                } else
                                {
                                    deck += prefix + arranged + "(*) * " + slines[0] + "\n";
                                }
                                cardcount += Integer.parseInt(slines[0]);
                            }
                        }
                    }
                    File profile = new File(activePath + "/User/settings/options.txt");
                    String profileName = "Default";
                    if (profile.exists() && !profile.isDirectory())
                        profileName = getActiveProfile(profile);
                    File rootDecks;
                    if (!profileName.equalsIgnoreCase("Default"))
                        rootDecks = new File(activePath + "/User/profiles/" + profileName);
                    else
                        rootDecks = new File(activePath + "/User/player/");
                    if (rootDecks.exists() && rootDecks.isDirectory())
                    {
                        //save deck
                        int countdeck = 1;
                        File[] files = rootDecks.listFiles();
                        for (int i = 0; i < files.length; i++)
                        {//check if there is available deck...
                            if (files[i].getName().startsWith("deck"))
                                countdeck++;
                        }
                        File toSave = new File(rootDecks + "/deck" + countdeck + ".txt");
                        try
                        {
                            FileOutputStream fop = new FileOutputStream(toSave);

                            // if file doesn't exists, then create it
                            if (!toSave.exists())
                            {
                                toSave.createNewFile();
                            }
                            // get the content in bytes
                            byte[] contentInBytes = deck.getBytes();
                            fop.write(contentInBytes);
                            fop.flush();
                            fop.close();
                            message = "The deck has been successfully imported as: " + toSave.getName() + "\n" + cardcount + " total cards in this deck\n\n" + deck;
                        } catch (IOException e)
                        {
                            message = e.getMessage();
                        }
                    } else
                    {
                        message = "Error: Problem opening decks folder: " + rootDecks.getAbsolutePath();
                    }
                } else
                {
                    message = "Warning: No errors but file is EMPTY";
                }
            } catch (IOException e)
            {
                message = "Error: " + e.getMessage();
            }
        } else 
        {
            message = "Error: Input deck file is missing or its path is wrong...";
        }
        System.out.println(message);
    }
      
    private static boolean isNumeric(String input)
    {
        try
        {
            Integer.parseInt(input);
        }
        catch(NumberFormatException ex)
        {
            return false;
        }
        return true;
    }

    private static String getActiveProfile(File mypath)
    {
        String name;
        try
            {
                Scanner scanner = new Scanner(new File(mypath.toString()));                
                if (scanner.hasNext()) 
                {
                    String line = scanner.nextLine();
                    name = line.substring(8);    
                }
                else
                {
                    return "Default";
                }
            }
            catch(IOException e)
            {
                return "Default";
            }
        return name;
    }

    private static String renameSet(String set)
    {
        if (set.isEmpty())
            return "*";
        if (set.equals("AL"))
            return "ALL";
        if (set.equals("AQ"))
            return "ATQ";
        if (set.equals("AP"))
            return "APC";
        if (set.equals("AN"))
            return "ARN";
        if (set.equals("AE"))
            return "ARC";
        if (set.equals("BR"))
            return "BRB";
        if (set.equals("BD"))
            return "BTD";
        if (set.equals("CH"))
            return "CHR";
        if (set.equals("6E"))
            return "6ED";
        if (set.equals("CS"))
            return "CSP";
        if (set.equals("DS"))
            return "DST";
        if (set.equals("D2"))
            return "DD2";
        if (set.equals("8E"))
            return "8ED";
        if (set.equals("EX"))
            return "EXO";
        if (set.equals("FE"))
            return "FEM";
        if (set.equals("FD"))
            return "5DN";
        if (set.equals("5E"))
            return "5ED";
        if (set.equals("4E"))
            return "4ED";
        if (set.equals("GP"))
            return "GPT";
        if (set.equals("HL"))
            return "HML";
        if (set.equals("IA"))
            return "ICE";
        if (set.equals("IN"))
            return "INV";
        if (set.equals("JU"))
            return "JUD";
        if (set.equals("LG"))
            return "LEG";
        if (set.equals("LE"))
            return "LGN";
        if (set.equals("A"))
            return "LEA";
        if (set.equals("B"))
            return "LEB";
        if (set.equals("MM"))
            return "MMQ";
        if (set.equals("MI"))
            return "MIR";
        if (set.equals("MR"))
            return "MRD";
        if (set.equals("NE"))
            return "NEM";
        if (set.equals("9E"))
            return "9ED";
        if (set.equals("OD"))
            return "ODY";
        if (set.equals("ON"))
            return "ONS";
        if (set.equals("PS"))
            return "PLS";
        if (set.equals("PT"))
            return "POR";
        if (set.equals("P2"))
            return "P02";
        if (set.equals("P3"))
            return "PTK";
        if (set.equals("PR"))
            return "PPR";
        if (set.equals("PY"))
            return "PCY";
        if (set.equals("R"))
            return "RV";
        if (set.equals("SC"))
            return "SCG";
        if (set.equals("7E"))
            return "7ED";
        if (set.equals("ST"))
            return "S99";
        if (set.equals("ST2K"))
            return "S00";
        if (set.equals("SH"))
            return "STH";
        if (set.equals("TE"))
            return "TMP";
        if (set.equals("DK"))
            return "DRK";
        if (set.equals("TO"))
            return "TOR";
        if (set.equals("UG"))
            return "UGL";
        if (set.equals("U"))
            return "2ED";
        if (set.equals("UD"))
            return "UDS";
        if (set.equals("UL"))
            return "ULG";
        if (set.equals("US"))
            return "USG";
        if (set.equals("VI"))
            return "VIS";
        if (set.equals("WL"))
            return "WTH";
        else
            return set;
    }
}

package utilities;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class Keywords {

    public static void main(String[] args) {

        String folderName = "C:\\Users\\Eduardo_\\Documents\\WagicWindows-0231\\Res\\sets\\primitives"; // replace with the name of your folder
        File folder = new File(folderName);
        for (File file : folder.listFiles()) {
            String fileName = file.getAbsolutePath();

            System.out.println("Processing file: " + file.getName());

            String line;
            String[] keywords = {"toxic", "combattoughness", "affinityforests", "weak", "evadebigger", "discardtoplaybyopponent", "legendruleremove", "cantbeblockedby(eldrazi scion)", "oppgcreatureexiler", "legendarylandwalk", "auraward", "infect", "swamphome", "skulk", "islandhome", "desertwalk", "spellmastery", "nonbasiclandwalk", "dethrone", "cantattack", "cantpwattack", "librarydeath", "phasing", "cantmilllose", "wilting", "foresthome", "offering", "phantom", "vigor", "poisontwotoxic", "poisontoxic", "cantchangelife", "threeblockers", "cantlifelose", "shackler", "shufflelibrarydeath", "twodngtrg", "wither", "affinityartifacts", "leyline", "hydra", "totemarmor", "nosolo", "showopponenthand", "showcontrollerhand", "canplayfromexile", "canplayfromgraveyard", "noentertrg", "foretell", "exiledeath", "lifefaker", "partner", "hasnokicker", "hasstrive", "tokenizer", "hasaftermath", "mutate", "Flying", "Trample", "doublefacedeath", "boast", "isconspiracy", "hasdisturb", "daybound", "nightbound", "mentor", "hellbent", "overload", "adventure", "undamageable", "Haste", "training", "overload", "inplaydeath", "canbecommander", "storm", "soulbond", "split second", "fear", "mygraveexiler", "undying", "shadow", "flanking", "horsemanship", "plainswalk", "snowforestlandwalk", "snowplainslandwalk", "snowmountainlandwalk", "snowislandlandwalk", "snowswamplandwalk", "hiddenface", "hasotherkicker", "changeling", "shroud", "cycling", "exalted", "persist", "controllershroud", "devoid", "prowess", "madness", "flash", "defender", "flying", "intimidate", "first strike", "double strike", "deathtouch", "opponentshroud", "menace", "indestructible", "vigilance", "reach", "trample", "lifelink", "haste", "islandwalk", "swampwalk", "mountainwalk", "forestwalk", "showfromtoplibrary", "nomaxhand", "nofizzle", "unblockable", "oneblocker", "cantblock", "mustattack", "strong", "cloud", "lure", "sunburst", "mustblock", "nolifegain", "nolifegainopponent", "doesnotuntap", "protection from", "chooseabackground", "modular"};
            String[] startingKeywords = {"grade", "[card]", "[/card]", "text", "name", "mana", "type", "subtype", "power", "toughness", "auto", "target", "abilities", "aicode", "other", "otherrestriction", "flashback", "color", "suspend", "kicker", "buyback", "modular", "doublefaced", "bestow", "retrace", "partner", "#AUTO_DEFINE", "dredge", "backside", "restriction", "anyzone", "alias", "facedown", "crewbonus", "phasedoutbonus", "#"};
            String[] validTargets = {"creature", "artifact", "<upto:2>", "player", "land", "enchantment", "opponent", "*|stack", "equipment", "wall", "*|battlefield", "*|mybattlefield", "*|mygraveyard"};
            String[] validTypes = {"artifact", "creature", "enchantment", "instant", "land", "legendary", "planeswalker", "sorcery", "tribal","conspiracy","emblem","dungeon"};

            int lineNumber = 0;

            try {
                FileReader fileReader = new FileReader(fileName);

                try ( BufferedReader bufferedReader = new BufferedReader(fileReader)) {
                    while ((line = bufferedReader.readLine()) != null) {
                        line = line.toLowerCase();
                        lineNumber++;
                        if (line.isEmpty()) {
                            continue;
                        }
                        if (line.startsWith("abilities=")) {
                            boolean containsKeyword = false;
                            for (String keyword : keywords) {
                                if (line.contains(keyword)) {
                                    containsKeyword = true;
                                    break;
                                }
                            }
                            if (!containsKeyword) {
                                System.out.println("Line " + lineNumber + " does not contain any of the keywords: " + line);
                            }
                        }
                        if (line.startsWith("type=")) {
                            boolean containsType = false;
                            for (String type : validTypes) {
                                if (line.contains(type)) {
                                    containsType = true;
                                    break;
                                }
                            }
                            if (!containsType) {
                                System.out.println("Line " + lineNumber + " does not contain any of the types: " + line);
                            }
                        }
                        if (line.startsWith("target=")) {
                            boolean containsTarget = false;
                            for (String validTarget : validTargets) {
                                if (line.contains(validTarget)) {
                                    containsTarget = true;
                                    break;
                                }
                            }
                            if (!containsTarget) {
                                //System.out.println("Line " + lineNumber + " does not contain any of the targets: " + line);
                            }
                        }
                        boolean startsWithKeyword = false;
                        for (String startingkeyword : startingKeywords) {
                            if (line.startsWith(startingkeyword)) {
                                startsWithKeyword = true;
                                break;
                            }
                        }
                        if (!startsWithKeyword) {
                            System.out.println("Line " + lineNumber + ": " + line);
                        }
                    }
                }
            } catch (IOException ex) {
                System.out.println("Error reading file '" + fileName + "'");
            }
        }
    }
}

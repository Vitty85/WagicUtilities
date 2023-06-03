package json;

import org.json.simple.JSONArray;

// @author vitty85
public class Abilities {

    protected static String processKeywordsAbilities(JSONArray keywords, String oracleText) {
        String abilities = "";
        for (Object keyword : keywords) {
            String keywordString = (String) keyword;

            switch (keywordString.toLowerCase()) {
                case "flash":
                    abilities += "flash,";
                    break;
                case "defender":
                    abilities += "defender,";
                    break;
                case "flying":
                    abilities += "flying,";
                    break;
                case "intimidate":
                    abilities += "intimidate,";
                    break;
                case "first strike":
                    abilities += "first strike,";
                    break;
                case "double strike":
                    abilities += "double strike,";
                    break;
                case "deathtouch":
                    abilities += "deathtouch,";
                    break;
                case "hexproof":
                    abilities += "opponentshroud,";
                    break;
                case "menace":
                    abilities += "menace,";
                    break;
                case "indestructible":
                    abilities += "indestructible,";
                    break;
                case "vigilance":
                    abilities += "vigilance,";
                    break;
                case "reach":
                    abilities += "reach,";
                    break;
                case "trample":
                    abilities += "trample,";
                    break;
                case "lifelink":
                    abilities += "lifelink,";
                    break;
                case "haste":
                    abilities += "haste,";
                    break;
                case "islandwalk":
                    abilities += "islandwalk,";
                    break;
                case "swampwalk":
                    abilities += "swampwalk,";
                    break;
                case "mountainwalk":
                    abilities += "mountainwalk,";
                    break;
                case "forestwalk":
                    abilities += "forestwalk,";
                    break;
            }
        }

        abilities = processCustomAbilities(oracleText, abilities);
        abilities = abilities.replaceAll(",$", "");
        return abilities;
    }

    protected static String processCustomAbilities(String oracleText, String abilities) {

        oracleText = oracleText.toLowerCase();
        
        if (oracleText.contains("you may look at the top card of your library any time")) {
            abilities += "showfromtoplibrary,";
        }
        if (oracleText.contains("you have no maximum hand size")) {
            abilities += "nomaxhand,";
        }
        if (oracleText.contains("can't be countered")) {
            abilities += "nofizzle,";
        }
        if (oracleText.contains("can't be blocked.")) {
            abilities += "unblockable,";
        }
        if (oracleText.contains("can't be blocked by more than one creature")) {
            abilities += "oneblocker,";
        }
        if (oracleText.contains("toxic 1")) {
            abilities += "poisontoxic,";
        }
        if (oracleText.contains("toxic 2")) {
            abilities += "poisontwotoxic,";
        }
        if (oracleText.contains("toxic 3")) {
            abilities += "poisonthreetoxic,";
        }
//        if (oracleText.contains("can't block.")) {
//            abilities += "cantblock,";
//        }
        if (oracleText.contains("attacks each turn if able.") || oracleText.contains("attacks each combat if able.")) {
            abilities += "mustattack,";
        }
        if (oracleText.contains("power can't block it.")) {
            abilities += "strong,";
        }
        if (oracleText.contains("can block only creatures with flying.")) {
            abilities += "cloud,";
        }
        if (oracleText.contains("all creatures able to block ")) {
            abilities += "lure,";
        }
        if (oracleText.contains("counter on it for each color of mana spent to cast it")) {
            abilities += "sunburst,";
        }
        if (oracleText.contains("blocks each turn if able") || oracleText.contains("blocks each combat if able")) {
            abilities += "mustblock,";
        }
        if (oracleText.contains("players can't gain life")) {
            abilities += "nolifegain,";
        }
        if (oracleText.contains("opponents can't gain life")) {
            abilities += "nolifegainopponent,";
        }
        if (oracleText.contains("doesn't untap during your untap step")) {
            abilities += "doesnotuntap,";
        }
        if (oracleText.contains("protection from")) {
            abilities += "protection from,";
        }
        if (oracleText.contains("affinity for artifacts")) {
            abilities += "affinityartifacts,";
        }
        if (oracleText.contains("choose a background")) {
            abilities += "chooseabackground,";
        }
        if (oracleText.contains("modular")) {
            abilities += "modular\nmodular=";
        }
        abilities = abilities.replaceAll(",$", "");

        return abilities;
    }
}

package json;

// @author vitty85
public class AutoLineVintage {

    protected static String processOracleTextAfflict(String oracleText, String name) {
        String afflict = "";
        String afflictDamage = "";
        try {
            String incidence = "Afflict";
            if (oracleText.contains(incidence)) {
                afflictDamage = oracleText.substring(oracleText.indexOf(incidence) + incidence.length() + 1, oracleText.indexOf(incidence) + incidence.length() + 2);
                afflict = "auto=@combat(blocked,turnlimited) source(this):life:-" + afflictDamage + " opponent";
            }
        } catch (Exception ex) {

        }
        return afflict;
    }

    //name(Bolster) notatarget(creature[toughness=toughness:lowest:creature:mybattlefield]) counter(1/1,1)
    protected static String processOracleTextBolster(String oracleText) {
        String bolster = "";
        String bolsterCounters;
        try {
            oracleText = oracleText.toLowerCase();
            if (oracleText.contains("bolster")) {
                bolsterCounters = oracleText.substring(oracleText.indexOf("bolster") + 8, oracleText.indexOf("bolster") + 9);
                bolster = "auto=name(Bolster) notatarget(creature[toughness=toughness:lowest:creature:mybattlefield]|mybattlefield) counter(1/1," + bolsterCounters + ")";
            }
        } catch (Exception ex) {

        }
        return bolster;
    }

    protected static String processOracleTextCycling(String oracleText) {
        String cycling = "";
        String cyclingCost;
        try {
            if (oracleText.contains("Cycling")) {
                cyclingCost = oracleText.substring(oracleText.indexOf("Cycling") + 8, oracleText.indexOf(" ({"));
                cycling = "autohand=__CYCLING__(" + cyclingCost + ")";
            }
        } catch (Exception ex) {

        }
        return cycling;
    }

    //    other={3}{B}{R} name(Dash)
    protected static String processOracleTextDash(String oracleText) {
        String dash = "";
        String dashCost;
        try {
            if (oracleText.contains("Dash")) {
                dashCost = oracleText.substring(oracleText.indexOf("Dash") + 5);
                dashCost = dashCost.substring(0, dashCost.indexOf("(") - 1);
                dash = "other=" + dashCost + " name(Dash)\n"
                        + "auto=if paid(alternative) then transforms((,newability[haste],newability[phaseaction[endofturn sourceinplay] "
                        + "moveto(ownerhand) all(this)])) forever";
            }

        } catch (Exception ex) {

        }
        return dash;
    }

    //Embalm {5}{W} ({5}{W}, Exile this card from your graveyard: Create a token that's a copy of it,
    //except it's a white Zombie Angel with no mana cost. Embalm only as a sorcery.)
    static String processOracleTextEmbalm(String oracleText) {
        String embalm = "";
        String embalmCost;
        try {
            String incidence = "Embalm ";

            if (oracleText.contains(incidence)) {
                embalmCost = oracleText.substring(oracleText.indexOf(incidence) + incidence.length());
                embalmCost = embalmCost.substring(0, embalmCost.indexOf(" ("));
                embalm = "autograveyard=" + embalmCost + "{E}:name(Embalm) clone this with(white) addtype(Zombie) asSorcery";

            }
        } catch (Exception ex) {

        }
        return embalm;
    }

    // auto=@damaged(this):
    static String processOracleEnraged(String oracleText) {
        String enrage = "";
        try {
            String incidence = "Enrage";

            if (oracleText.contains(incidence)) {

                enrage = "auto=@damaged(this):";

            }
        } catch (Exception e) {
        }
        return enrage;
    }

    static String processOracleTextEternalize(String oracleText) {
        String eternalize = "";
        String eternalizeCost;
        try {
            String incidence = "Eternalize";

            if (oracleText.contains(incidence)) {
                eternalizeCost = oracleText.substring(oracleText.indexOf(incidence) + incidence.length());
                eternalizeCost = eternalizeCost.substring(1, eternalizeCost.indexOf("(") - 1);
                eternalizeCost = eternalizeCost.replace(", Discard a card.", "{D}");
                eternalize = "autograveyard=" + eternalizeCost + "{E}:name(Eternalize) clone this with(black) addtype(Zombie) setpower=4 settoughness=4 asSorcery";
            }
        } catch (Exception ex) {

        }
        return eternalize;
    }

    protected static String processOracleTextExert(String oracleText) {
        String exert = "";
        try {
            if (oracleText.contains("ou may exert ")) {
                exert = "auto=_ATTACKING_may exert";
            }
        } catch (Exception ex) {

        }
        return exert;
    }

    // auto=may choice notatarget(creature|mybattlefield) sacrifice && NEW ABILITY
    protected static String processOracleTextExploit(String oracleText) {
        String exploit = "";
        try {
            if (oracleText.contains("Exploit")) {
                exploit = "auto=may notatarget(creature|mybattlefield) sacrifice name(Exploit) && transforms((,newability[choice";
            }

        } catch (Exception ex) {

        }
        return exploit;
    }

    protected static String processOracleTextFaceUp(String oracleText) {
        String faceUp = "";
        try {
            if (oracleText.contains("is turned face up,")) {
                faceUp = "autofaceup=";
            }
        } catch (Exception ex) {

        }
        return faceUp;
    }

    //Megamorph
    //facedown={3}
    //autofacedown={3}{W}{W}:morph
    //autofaceup=counter(1/1,5)
    protected static String processOracleTextMegaMorph(String oracleText) {
        String morph = "";
        String megamorphCost;
        try {
            if (oracleText.contains("Megamorph")) {
                megamorphCost = oracleText.substring(oracleText.indexOf("Megamorph") + 10);
                megamorphCost = megamorphCost.substring(0, megamorphCost.indexOf("(") - 1);
                morph = "facedown={3}\n"
                        + "autofacedown=" + megamorphCost + ":morph\n"
                        + "autofaceup=counter(1/1)";
            }
        } catch (Exception ex) {

        }
        return morph;
    }

    //auto={G}:regenerate
    protected static String processOracleTextRegenerate(String oracleText) {
        String regenerate = "";
        String regenerateCost;
        try {
            if (oracleText.contains("Regenerate")) {
                regenerateCost = oracleText.substring(oracleText.indexOf("{"), oracleText.indexOf("Regenerate") - 2);
                regenerate = "auto=" + regenerateCost + ":regenerate";
            }

        } catch (Exception ex) {

        }
        return regenerate;
    }

    // auto=if scry then
    static String processOracleTextRaid(String oracleText) {
        String raid = "";
        try {
            String incidence = "Raid ";

            if (oracleText.contains(incidence)) {

                raid = "auto=if raid then";

            }
        } catch (Exception e) {
        }
        return raid;
    }

    //auto={W}{U}:transforms((Bird Artifact Creature,setpower=2,settoughness=2,blue,white,flying)) ueot
    //text={T}: Add {W} or {U} to your mana pool. -- {W}{U}: Azorius Keyrune becomes a 2/2 white and blue Bird artifact creature with flying until end of turn.
    protected static String processOracleTextTransforms(String oracleText, String name) {
        String transforms = "";
        String transformCost;
        String pow;
        String tho;
        String color;
        String color2;

        try {
            if (oracleText.contains("becomes a")) {
                transformCost = oracleText.substring(oracleText.indexOf("{4"), oracleText.indexOf(name));
                pow = oracleText.substring(oracleText.indexOf("/") - 1, oracleText.indexOf("/"));
                tho = oracleText.substring(oracleText.indexOf("/") + 1, oracleText.indexOf("/") + 2);
                color = oracleText.substring(oracleText.indexOf("/") + 3, oracleText.indexOf(" and"));
                color2 = oracleText.substring(oracleText.indexOf("and") + 4, oracleText.indexOf(" Dragon"));
                transforms = "auto=" + transformCost.trim() + "transforms((Dragon Creature,setpower=" + pow + ",settoughness=" + tho + "," + color + "," + color2 + ",flying)) ueot";
            }
        } catch (Exception ex) {

        }
        return transforms;
    }

}

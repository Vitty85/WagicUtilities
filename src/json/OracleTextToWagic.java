package json;

import java.io.FileWriter;
import java.io.IOException;
import org.json.simple.JSONArray;

// @author Eduardo
public class OracleTextToWagic {

    static void parseOracleText(JSONArray keywords, String oracleText, String cardName, String type, String subtype, String power, String manaCost, FileWriter myWriter) throws IOException{
        String abilities = "";
        // Remove remainder text        
        oracleText = oracleText.replaceAll("\\(.*?\\)", "").replace("()", "");
        // Remove comma from Legendaries
        oracleText = oracleText.replaceAll(cardName.replace(",", ""), cardName.replace(",", ""));
        // Keywords present in the dataset to populate abilities=
        if (keywords != null) {
            abilities = Abilities.processKeywordsAbilities(keywords, oracleText);
        }
        // Special abilities for creatures and vehicles
        if (keywords == null && (type.contains("Creature") || subtype.contains("Vehicle"))) {
            abilities = Abilities.processCustomAbilities(oracleText, "");
        }
        // abilities=
        if (abilities.length() > 0) {
            myWriter.append("abilities=" + abilities.trim() + "\n");
        }
        // Auto lines
        for (String oracleBit : oracleText.split("\n")) {
            // Permanents, exclude this types
            if (!(type.contains("Instant") || type.contains("Sorcery") || subtype.contains("Aura") || subtype.contains("Equipment"))) {
                // Evergreen mechanics
                autoLineExists(myWriter,AutoLine.Corrupted(oracleBit, cardName));
                autoLineExists(myWriter,AutoEffects.DetermineActivatedAbility(oracleBit, cardName, type, subtype));
                autoLineExists(myWriter,AutoLine.ManaAbility(oracleBit, subtype));
                autoLineExists(myWriter,AutoLine.Cast(oracleBit, cardName));
                autoLineExists(myWriter,AutoLine.CombatDamage(oracleBit));
                autoLineExists(myWriter,AutoLine.OppCasts(oracleBit));
                autoLineExists(myWriter,AutoLine.Weak(oracleBit));
                autoLineExists(myWriter,AutoLine.Discarded(oracleBit));
                autoLineExists(myWriter,AutoLine.TakeControl(oracleBit));
                autoLineExists(myWriter,AutoLine.CantBeBlockedBy(oracleBit));
                autoLineExists(myWriter,AutoLine.Lord(oracleBit, type));
                autoLineExists(myWriter,AutoLine.Triggers(oracleBit, cardName, type, subtype, power));
                autoLineExists(myWriter,AutoLine.processAsLongAs(oracleBit, cardName));
                autoLineExists(myWriter,AutoLine.processForEach(oracleBit, type));
                autoLineExists(myWriter,AutoLine.CostReduction(oracleBit));
                autoLineExists(myWriter,AutoLineGRN.Surveil(oracleBit));
                autoLineExists(myWriter,AutoLine.Prowess(oracleBit));

                autoLineExists(myWriter,AutoLineGRN.Proliferate(oracleBit));

                autoLineExists(myWriter,AutoLine.Unearth(oracleBit));
                autoLineExists(myWriter,AutoLine.Prototype(oracleBit));
//                autoLineExists(AutoLine.Blitz(oracleBit));
//                autoLineExists(AutoLineGRN.Kicker(oracleBit, cardName));
//                autoLineExists(AutoLine.Ninjutsu(oracleBit));
//                autoLineExists(AutoLineGRN.Undergrowth(oracleBit));
//                autoLineExists(AutoLineGRN.Convoke(oracleBit));
//                autoLineExists(AutoLineGRN.Addendum(oracleBit));
//                autoLineExists(AutoLineGRN.Riot(oracleBit));
//                autoLineExists(AutoLineGRN.Spectacle(oracleBit));
//                autoLineExists(AutoLineGRN.Ascend(oracleBit));
//                autoLineExists(AutoLineGRN.Partner(oracleBit));
//                autoLineExists(AutoLineGRN.Amass(oracleBit));                
            }
            //INSTANT, SORCERY
            if (type.contains("Instant") || type.contains("Sorcery")) {
                autoLineExists(myWriter,AutoLine.Corrupted(oracleBit, cardName));
                autoLineExists(myWriter,AutoLine.ChooseOneOrBoth(oracleBit));
                autoLineExists(myWriter,AutoLine.MyTarget(oracleBit, "InstantOrSorcery", subtype));
                autoLineExists(myWriter,AutoEffects.ProcessEffect(oracleBit, type));
                autoLineExists(myWriter,AutoLine.ExileDestroyDamage(oracleBit, type));
                autoLineExists(myWriter,AutoLine.ReplacerAuraEquipBonus(oracleBit, "InstantOrSorcery"));
                autoLineExists(myWriter,AutoLine.Create(oracleBit));
                autoLineExists(myWriter,AutoLine.PutA(oracleBit, type));

                //autoLineExists(AutoLineGRN.Kicker(oracleBit, cardName));
                //autoLineExists(AutoLineGRN.JumpStart(oracleBit, manaCost));
            }
            // AURA
            if (subtype.contains("Aura")) {
                autoLineExists(myWriter,AutoLine.Corrupted(oracleBit, cardName));
                autoLineExists(myWriter,AutoLine.MyTarget(oracleBit, subtype, subtype));
                //autoLineExists(AutoLine.Triggers(oracleBit, cardName, type, subtype, power));
                String auraEquipBonus = AutoLine.ReplacerAuraEquipBonus(oracleBit, subtype);
                if (!auraEquipBonus.isEmpty()) {
                    autoLineExists(myWriter,auraEquipBonus);
                }
            }
            // EQUIPMENT
            if (subtype.contains("Equipment")) {
                //autoLineExists(AutoLine.Triggers(oracleBit, cardName, type, subtype, power));
                autoLineExists(myWriter,AutoLine.AuraEquipBonus(oracleBit, "Equipment"));
                autoLineExists(myWriter,AutoLine.ForMirrodin(oracleBit, "Equipment"));
                if (oracleBit.contains("Equip ")) {
                    autoLineExists(myWriter,AutoLine.EquipCost(oracleBit));
                }
            }
            // SAGA
//            if (subtype.contains("Saga")) {
//                autoLineExists(AutoLine.EpicSaga(oracleBit, subtype));
//            }
        }
    }

    static void autoLineExists(FileWriter myWriter, String outcome, boolean... needAuto) throws IOException{
        if (outcome.length() > 0) {
            if (needAuto.length > 0) {
                outcome = "auto=" + outcome;
            }
            outcome = outcome.replace("\n", " -- ");
            outcome = outcome.replace("—", "-");
            outcome = outcome.replace("•", "");
            outcome += "\n";
            myWriter.append(outcome);
        }
    }
}

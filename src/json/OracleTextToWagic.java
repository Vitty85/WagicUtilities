package json;

import org.json.simple.JSONArray;

// @author Eduardo
public class OracleTextToWagic {

    static void parseOracleText(JSONArray keywords, String oracleText, String cardName, String type, String subtype, String power, String manaCost) {
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
            System.out.println("abilities=" + abilities.trim());
        }
        // Auto lines
        for (String oracleBit : oracleText.split("\n")) {
            // Permanents, exclude this types
            if (!(type.contains("Instant") || type.contains("Sorcery") || subtype.contains("Aura") || subtype.contains("Equipment"))) {
                // Evergreen mechanics
                autoLineExists(AutoLine.Corrupted(oracleBit, cardName));
                autoLineExists(AutoEffects.DetermineActivatedAbility(oracleBit, cardName, type, subtype));
                autoLineExists(AutoLine.ManaAbility(oracleBit, subtype));
                autoLineExists(AutoLine.Cast(oracleBit, cardName));
                autoLineExists(AutoLine.CombatDamage(oracleBit));
                autoLineExists(AutoLine.OppCasts(oracleBit));
                autoLineExists(AutoLine.Weak(oracleBit));
                autoLineExists(AutoLine.Discarded(oracleBit));
                autoLineExists(AutoLine.TakeControl(oracleBit));
                autoLineExists(AutoLine.CantBeBlockedBy(oracleBit));
                autoLineExists(AutoLine.Lord(oracleBit, type));
                autoLineExists(AutoLine.Triggers(oracleBit, cardName, type, subtype, power));
                autoLineExists(AutoLine.processAsLongAs(oracleBit, cardName));
                autoLineExists(AutoLine.processForEach(oracleBit, type));
                autoLineExists(AutoLine.CostReduction(oracleBit));
                autoLineExists(AutoLineGRN.Surveil(oracleBit));
                autoLineExists(AutoLine.Prowess(oracleBit));

                autoLineExists(AutoLineGRN.Proliferate(oracleBit));

                autoLineExists(AutoLine.Unearth(oracleBit));
                autoLineExists(AutoLine.Prototype(oracleBit));
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
                autoLineExists(AutoLine.Corrupted(oracleBit, cardName));
                autoLineExists(AutoLine.ChooseOneOrBoth(oracleBit));
                autoLineExists(AutoLine.MyTarget(oracleBit, "InstantOrSorcery", subtype));
                autoLineExists(AutoEffects.ProcessEffect(oracleBit, type));
                autoLineExists(AutoLine.ExileDestroyDamage(oracleBit, type));
                autoLineExists(AutoLine.ReplacerAuraEquipBonus(oracleBit, "InstantOrSorcery"));
                autoLineExists(AutoLine.Create(oracleBit));
                autoLineExists(AutoLine.PutA(oracleBit, type));

                //autoLineExists(AutoLineGRN.Kicker(oracleBit, cardName));
                //autoLineExists(AutoLineGRN.JumpStart(oracleBit, manaCost));
            }
            // AURA
            if (subtype.contains("Aura")) {
                autoLineExists(AutoLine.Corrupted(oracleBit, cardName));
                autoLineExists(AutoLine.MyTarget(oracleBit, subtype, subtype));
                //autoLineExists(AutoLine.Triggers(oracleBit, cardName, type, subtype, power));
                String auraEquipBonus = AutoLine.ReplacerAuraEquipBonus(oracleBit, subtype);
                if (!auraEquipBonus.isEmpty()) {
                    autoLineExists(auraEquipBonus);
                }
            }
            // EQUIPMENT
            if (subtype.contains("Equipment")) {
                //autoLineExists(AutoLine.Triggers(oracleBit, cardName, type, subtype, power));
                autoLineExists(AutoLine.AuraEquipBonus(oracleBit, "Equipment"));
                autoLineExists(AutoLine.ForMirrodin(oracleBit, "Equipment"));
                if (oracleBit.contains("Equip ")) {
                    autoLineExists(AutoLine.EquipCost(oracleBit));
                }
            }
            // SAGA
//            if (subtype.contains("Saga")) {
//                autoLineExists(AutoLine.EpicSaga(oracleBit, subtype));
//            }
        }
    }

    static void autoLineExists(String outcome, boolean... needAuto) {
        if (outcome.length() > 0) {
            outcome += "\n";
            if (needAuto.length > 0) {
                outcome = "auto=" + outcome;
            }
        }

        System.out.print(outcome);
    }
}

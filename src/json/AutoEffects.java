package json;

// @author Eduardo
import java.util.HashMap;
import java.util.Map;

public class AutoEffects {

    static Map<String, Integer> numberMap = new HashMap<>();

    // Determine if the card has an activated ability
    static String DetermineActivatedAbility(String oracleBit, String cardName, String type, String subtype) {
        String[] activatedAbililty = oracleBit.split(":");
        if (activatedAbililty.length > 1) {
            return ActivatedAbililtyCost(activatedAbililty, cardName, type);
        } else {
            return "";
        }
    }

    // Activated ability cost
    private static String ActivatedAbililtyCost(String[] actAbil, String cardName, String type) {
        String actAbilCost;
        String actAbilEffect;

        numberMap.put("one", 1);
        numberMap.put("two", 2);
        numberMap.put("three", 3);
        numberMap.put("four", 4);
        numberMap.put("five", 5);
        numberMap.put("six", 6);
        numberMap.put("seven", 7);
        numberMap.put("eight", 8);
        numberMap.put("nine", 9);

        actAbilCost = actAbil[0];

        actAbilCost = actAbilCost.replaceAll("\\{[WUBRG]\\/P\\}", "{P($0)}");
        actAbilCost = actAbilCost.replace("Channel ", "autohand=");
        actAbilCost = actAbilCost.replace("discard your hand" + cardName, "reject all(*|myhand)");
        actAbilCost = actAbilCost.replace(", Discard " + cardName, "{discard}");
        actAbilCost = actAbilCost.replace("Discard a card", "{D(*|myhand)}");
        actAbilCost = actAbilCost.replace("Discard a land card", "{D(land|myhand)}");
        actAbilCost = actAbilCost.replace("Discard three cards", "{D(*|myhand)}{D(*|myhand)}{D(*|myhand)}");
        actAbilCost = actAbilCost.replace("Discard a creature card", "{D(creature|myhand)}");
        actAbilCost = actAbilCost.replace("Sacrifice a creature with defender", "{S(creature[defender]|myBattlefield)}");
        actAbilCost = actAbilCost.replace("Sacrifice " + cardName, "{S}");
        actAbilCost = actAbilCost.replace("Exile " + cardName, "{E}");
        actAbilCost = actAbilCost.replace("Sacrifice a creature", "{S(creature|myBattlefield)}");
        actAbilCost = actAbilCost.replace("Sacrifice two creatures", "{S(creature|myBattlefield)}{S(creature|myBattlefield)}");
        actAbilCost = actAbilCost.replace("Sacrifice an artifact", "{S(artifact|myBattlefield)}");
        actAbilCost = actAbilCost.replace("Sacrifice an artifact creature", "{S(creature[artifact]|myBattlefield)}");
        actAbilCost = actAbilCost.replace("Sacrifice a land", "{S(land|myBattlefield)}");
        actAbilCost = actAbilCost.replaceAll("Sacrifice another ([a-zA-Z]+)", "{S(other $1|mybattlefield)}");
        actAbilCost = actAbilCost.replaceAll("Remove (an?|a) ([a-zA-Z]+) counter from " + cardName, "{C(0/0,-1,$2)}");
        actAbilCost = actAbilCost.replace("Remove two oil counters from ", "{C(0/0,-2,oil)}");
        actAbilCost = actAbilCost.replace("Remove three oil counters from ", "{C(0/0,-3,oil)}");
        actAbilCost = actAbilCost.replace("Exile " + cardName + " from your graveyard", "{E}");
        actAbilCost = actAbilCost.replace("Pay ", "{L:");
        actAbilCost = actAbilCost.replace(" life", "}");
        actAbilCost = actAbilCost.replace(cardName, "");
        actAbilCost = actAbilCost.replace(", ", "");
        actAbilCost = actAbilCost.replace(" ", "");
        actAbilCost = actAbilCost.replace(".", "");

        if (type.contains("Planeswalker")) {
            actAbilCost = actAbilCost.replace("[", "");
            actAbilCost = actAbilCost.replace("]", "");
            actAbilCost = "{C(0/0," + actAbilCost + ",Loyalty)}";
        }

        actAbilCost = actAbilCost.concat(":");
        actAbilCost = "auto=" + actAbilCost;

        actAbilEffect = actAbil[1];
        actAbilEffect = ProcessEffect(actAbilEffect, cardName);

        return actAbilCost + actAbilEffect;
    }

    static String ProcessEffect(String oracleBit, String cardName) {
        String effect;
        effect = oracleBit;
        String manaOfAnyColor = "\nAdd{W}\n"
                + "auto={T}:Add{U}\n"
                + "auto={T}:Add{B}\n"
                + "auto={T}:Add{R}\n"
                + "auto={T}:Add{G}";
        effect = effect.toLowerCase();
        cardName = cardName.toLowerCase();

        if (effect.contains("create ")) {
            return AutoLine.Create(effect);
        }
        if (effect.contains("scry ")) {
            return AutoLine.Scry(effect);
        }

        effect = effect.replace("from your graveyard the battlefield","moveTo(battlefield) from(myGraveyard)");
        effect = effect.replace("exile the top card of your library. you may play it this turn","__PLAY_TOP_FROM_EXILE__");
        effect = effect.replaceAll("ward \\{(\\d+)\\}", "_WARD_($1)");
        effect = effect.replace("choose one or both -", "");
        effect = effect.replaceAll("amass (\\d+)", "_AMASS_($1)");
        effect = effect.replace("until end of turn", "ueot");
        effect = effect.replace("until your next turn", "uynt");
        effect = effect.replace("activate only once each turn.", "limit:1");
        effect = effect.replace("activate only as a sorcery", "asSorcery");
        effect = effect.replace("as long as an opponent has three or more poison counters,", "this(variable{opponentpoisoncount}>2)");
        effect = effect.replace("has three or more poison counters", "this(variable{opponentpoisoncount}>2)");
        effect = effect.replace("enlist ", "_ENLIST_");
        effect = effect.replace("kicker ", "kicker=");
        effect = effect.replace("if it was kicked,", "if paid(kicker) then ");
        effect = effect.replace("proliferate", "_PROLIFERATE_");
        effect = effect.replace("exile this saga, then return it to the battlefield transformed under your control", "moveto(exile) and!( moveto(mybattlefield) and!( transforms((,newability[flip(backside)])) oneshot )! )!");

        effect = effect.replace("you win the game", "wingame");
        effect = effect.replace("this ability triggers only once each turn.", "turnlimited:");
        effect = effect.replace("activate only during your turn", "myturnonly");
        effect = effect.replace("that was dealt damage this turn", "[damaged]");
        effect = effect.replace("+1/+1 counter", "counter(1/1)");
        effect = effect.replace("loyalty counter", "counter(0/0,1,Loyalty)");
        effect = effect.replace("on the bottom of its owner's library", "bottomoflibrary");
        effect = effect.replaceAll("creature with power ([0-9]+) or greater", "creature[power>=$1]");
        effect = effect.replaceAll("creature with power ([0-9]+) or less", "creature[power<=$1]");
        effect = effect.replaceAll("creature with toughness ([0-9]+) or greater", "creature[toughness>=$1]");
        effect = effect.replaceAll("creature with toughness ([0-9]+) or less", "creature[toughness<=$1]");
        effect = effect.replaceAll("with mana value ([0-9]+) or more", "[manacost>=$1]");
        effect = effect.replaceAll("with mana value ([0-9]+) or less", "[manacost<=$1]");
        effect = effect.replaceAll("with mana value ([0-9]+)", "[manacost=$1]");

        effect = effect.replace("return target nonland permanent you don't control to its owner's hand", "target(-land|opponentBattlefield) moveTo(ownerHand)");
        effect = effect.replace("to its owner's hand", "moveto(ownerhand)");
        effect = effect.replace("historic", "artifact,*[legendary],enchantment[saga]");
        effect = effect.replace("return it to the battlefield tapped under its owner's control at the beginning of the next end step", "(blink)ueot");
        effect = effect.replace("creatures", "creature");
        effect = effect.replace("activate this ability only once each turn", "limit:1");
        effect = effect.replace("sacrifice " + cardName, "{S}:");
        effect = effect.replace("sacrifice another creature", "{S(other creature|mybattlefield)}");
        effect = effect.replace("defending player controls", ")|opponentBattlefield)");
        effect = effect.replace("your opponents control", ")|opponentBattlefield)");
        effect = effect.replace("to any target", "target(creature,player)");
        effect = effect.replace("if you do, ", "");
        effect = effect.replace("return target creature an opponent controls to its owner's hand", "moveto(ownerHand) target(creature|opponentBattlefield)");
        effect = effect.replace("return target creature to its owner's hand", "moveto(ownerHand) target(creature)");
        effect = effect.replace("exile target nonland permanent an opponent controls until " + cardName + " leaves the battlefield.", "(blink)forsrc target(*[-land]|opponentbattlefield)");
        effect = effect.replace("you may search your library for a basic land card or gate card, reveal it, put it into your hand, then shuffle your library.", "target(land[basic;gate]|mylibrary) moveto(myhand)");
        effect = effect.replace("you may have it fight target creature you don't control", "may target(creature|opponentbattlefield) dynamicability<!powerstrike eachother!>");
        effect = effect.replace("activate this ability only any time you could cast a sorcery", "asSorcery");
        effect = effect.replace(cardName + " from your graveyard to your hand", "moveto(myhand) all(this)");
        effect = effect.replace("you may cast an instant or sorcery card from your hand without paying its mana cost", "may castcard(normal) notatarget(instant,sorcery|myhand)");
        effect = effect.replace(cardName + " enters the battlefield with a +1/+1 counter on it for each color of mana spent to cast it.", "counter(1/1,sunburst)");
        effect = effect.replace("as long as you've cast an instant or sorcery spell this turn, " + cardName, "auto=aslongas(instant,sorcery|mystack) >1");
        effect = effect.replace("legendary permanent that's an artifact, creature, or enchantment.", "artifact[legendary], creature[legendary], enchantment[legendary])");
        effect = effect.replace("each opponent discards a card", "ability$!name(discard) reject notatarget(*|myhand)!$ opponent");
        effect = effect.replace("each opponent loses 1 life", "life:-1 opponent");
        effect = effect.replace("to your hand", "moveto(ownerhand)");
        effect = effect.replace("whenever this creature attacks, ", "_ATTACKING_");
        effect = effect.replace("with an oil counter on it", "counter(0/0,1,oil)");
        effect = effect.replace("put a +1/+1 counter", "counter(1/1)");

        effect = effect.replaceAll("(\\d+) (.*?) counters on it", "(0/0,$1,$2)");
        effect = effect.replace("enters the battlefield under your control, ", "@movedTo(|myBattlefield):");
        effect = effect.replace("attacking creature without flying", "creature[attacking;-flying]");
        effect = effect.replace("can't be blocked this turn", ") unblockable");
        effect = effect.replace("can't be blocked", "unblockable");
        effect = effect.replace("can't block this turn", "cantblock");
        effect = effect.replace("another target ", "target(other ");
        effect = effect.replace("another ", "other ");
        effect = effect.replace("target ", "target(");
        effect = effect.replace("prevent the next", "prevent:");
        effect = effect.replace("to the battlefield.", "moveTo(mybattlefield)");
        effect = effect.replace("creature doesn't untap during its controller's next untap step", "freeze");

        effect = effect.replace("you don't control", "|opponentBattlefield)");
        effect = effect.replace("creature you control", "creature|myBattlefield)");
        effect = effect.replace("each white creature", "(creature[white]");
        effect = effect.replace(" attacking creature with lesser power", "creature[attacking;power<=])");
        effect = effect.replace("nonland permanent an opponent controls", "*[-land]|opponentbattlefield)");
        effect = effect.replace("if you attacked with a creature this turn, ", "if raid then ");
        effect = effect.replace(" an opponent controls", "|opponentBattlefield)");
        effect = effect.replace(" you control", "|myBattlefield)");
        effect = effect.replace("loses all abilities", "loseabilities");
        effect = effect.replace("discard a card", "ability$!name(discard) reject notatarget(*|myhand)!$");
        effect = effect.replace("discards a card", "ability$!name(discard) reject notatarget(*|myhand)!$");
        effect = effect.replace("discards two cards", "ability$!name(discard) reject notatarget(<2>*|myhand)!$");

        effect = effect.replace("sacrifices a creature", "ability$!sacrifice notatarget(creature|mybattlefield)!$ targetedplayer");
        effect = effect.replace("exile " + cardName, "\nexiledeath");
        effect = effect.replace(" card from your graveyard", "|mygraveyard)");
        effect = effect.replace("take an extra turn after this one", "turns:+1");
        // Deplete (Mill)
        effect = effect.replace("mills a card", "deplete:1");

        for (Map.Entry<String, Integer> entry : numberMap.entrySet()) {
            effect = effect.replace("with " + entry.getKey() + " oil counters on it", "counter(0/0," + entry.getValue() + ",oil)");
            effect = effect.replace("put " + entry.getKey() + " +1/+1 counters on ", "counter(1/1," + entry.getValue() + ")");
            effect = effect.replace("mills " + entry.getKey() + " cards", "deplete:" + entry.getValue());
            effect = effect.replace("mill " + entry.getKey() + " cards", "deplete:" + entry.getValue());
            effect = effect.replace("draw " + entry.getKey() + " cards", "draw:" + entry.getValue());
            effect = effect.replace("draws " + entry.getKey() + " cards", "draw:" + entry.getValue());
            effect = effect.replace("up to " + entry.getKey(), "<upto:" + entry.getValue() + ">");
        }
        effect = effect.replace("mill a card", "deplete:1");

        effect = effect.replaceAll("you gain ([0-9]+) life", "life:$1");
        effect = effect.replaceAll("you lose ([0-9]+) life", "life:-$1");
        // Draw Monsta Cardo!
        effect = effect.replace("draw a card", "draw:1");
        effect = effect.replace("for each creature ", "type:creature:");
        effect = effect.replace("each ", "all(");
        effect = effect.replace(cardName + " enters the battlefield tapped.", "auto=tap(noevent)");
        effect = effect.replace(cardName + " gains ", "");
        effect = effect.replace(cardName + " gets ", "");
        effect = effect.replace(cardName + " has ", "");

        effect = effect.replaceAll("deals ([0-9]+|x) damage", "damage:$1");

        effect = effect.replace("if you do, ", ":");
        effect = effect.replace("when you do, ", ":");
        effect = effect.replace("you may pay", "may pay(");
        effect = effect.replace("you may", "may");
        effect = effect.replace("add one mana of any color", manaOfAnyColor);

        effect = effect.replaceAll(", and that creature gains| and gains | and gain | and gets | and has | and you | and ", " && ");

        effect = effect.replace(" with a ", "");
        effect = effect.replace(" put a ", " ");
        effect = effect.replace("return ", "");
        effect = effect.replaceAll(" gains | gain | gets | get | has ", ") ");
        effect = effect.replace(", then ", " && ");

        effect = effect.replace(" it) ", "");
        effect = effect.replace(" it ", "");
        effect = effect.replace(" on ", "");
        effect = effect.replace(" or ", ",");
        effect = effect.replace(" to ", " ");

        effect = effect.replace("} {", "}{");
        effect = effect.replace(":|", ":");
        effect = effect.replace("+", "");
        effect = effect.replace(".", "");
        effect = effect.replace(cardName, "");

        effect = effect.trim();

        return effect;
    }
}

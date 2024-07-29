package guandan;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public enum HandType {
    Single(1,"High card"), 
    Pair(1,"Pair"),
    Three(1,"Three of a kind"),
    FullHouse(1,"Full house"),
    Straight(1,"Flush"),
    GangBan(1,"GangBan"),
    LianDui(1,"LianDui"),
    BombFour(2,"Four of a kind"),
    BombFive(3,"Five of a kind"),
    StraightFlush(4,"Royal flush"),
    BombSix(5,"Six of a kind"),
    BombSeven(6,"Seven of a kind"),
    BombEight(7,"Eight of a kind"),
    BombNine(8,"Nine of a kind"),
    BombTen(9,"Ten of a kind"),
    JokerBomb(10, "Joker bomb");

    private int handVal;
    private String handName;

    HandType(int val, String name){
        handVal = val;
        handName = name;
    }

    public int getValue() {return handVal;}
    public String getName() {return handName;}

    public static HandType fromVal(int value){
        for (HandType handtype : HandType.values()) {
            if (handtype.getValue() == value) {
                return handtype;
            }
        }
        throw new IllegalArgumentException("No enum constant with value " + value);
    }

    public static HandType findHandType(HashMap<Kind,Integer> cardKinds, int suitCount, int wildCount, int cardCount, int kindCount) throws InvalidHandTypeException{
        switch (cardCount) {
            case 1:
                return Single;
            case 2:
                if (kindCount <= 1) {
                    return Pair;
                }
                throw new InvalidHandTypeException("Invalid hand type");
            case 3:
                if (kindCount == 1) {
                    return Three;
                }
                throw new InvalidHandTypeException("Invalid hand type");
            case 4:
                if (kindCount == 1) {
                    return BombFour;
                }
                if (cardKinds.getOrDefault(Kind.RedJoker, 0) == 2 && cardKinds.getOrDefault(Kind.BlackJoker,0) == 2) {
                    return JokerBomb;
                }
                throw new InvalidHandTypeException("Invalid hand type");
            case 5:
                if (kindCount == 1) {
                    return BombFive;
                }
                else if (kindCount == 2) {
                    if (wildCount != 0 || cardKinds.containsValue(2)) {
                        return FullHouse;
                    }
                }
                else if (kindCount+wildCount == 5) {
                    if (cardKinds.containsKey(Kind.RedJoker)||cardKinds.containsKey(Kind.BlackJoker)) {
                        throw new InvalidHandTypeException("Invalid hand type");
                    }
                    List<Integer> sortedKinds = new ArrayList<>(cardKinds.keySet()).stream()
                                                    .map(kind -> kind.getValue())
                                                    .sorted().collect(Collectors.toList());
                    int kindBase = sortedKinds.get(0);
                    for (int i = 1; i <= 4; i++) {
                        if (sortedKinds.contains(kindBase+i)) {
                            continue;
                        }
                        else if (wildCount != 0) {
                            wildCount--;
                            continue;
                        }
                        else{
                            if (kindBase == 2 && i == 4) {
                                if (sortedKinds.contains(Kind.Ace.getValue())) {
                                    continue;
                                }
                            }
                            throw new InvalidHandTypeException("Invalid hand type");
                        }
                    }
                    if (suitCount == 1) {
                        return StraightFlush;
                    }
                    else{
                        return Straight;
                    }
                }
                throw new InvalidHandTypeException("Invalid hand type");
            case 6:
                //AABBWildWild will be classified as gangban
                if (kindCount == 1) {
                    return BombSix;
                }
                if (kindCount == 2) {
                    ArrayList<Kind> kinds = new ArrayList<>(cardKinds.keySet());
                    int diff = Math.abs(kinds.get(1).getValue() - kinds.get(0).getValue());
                    if (diff == 1 || diff == 12) {
                        if (wildCount == 2) {
                            return GangBan;
                        }
                        if (wildCount == 1 && cardKinds.containsValue(2)) {
                            return GangBan;
                        }
                        if (wildCount == 0 && cardKinds.containsValue(3)) {
                            return GangBan;
                        }
                    }
                }
                if (kindCount == 3) {
                    List<Integer> sortedKinds = new ArrayList<>(cardKinds.keySet()).stream()
                                                    .map(kind -> kind.getValue())
                                                    .sorted().collect(Collectors.toList());
                    int diff1 = sortedKinds.get(1)-sortedKinds.get(0);
                    int diff2 = sortedKinds.get(2)-sortedKinds.get(0);
                    if (diff1 == 1 && (diff2 == 2 || diff2 == 12)) {
                        if (wildCount == 2) {
                            return LianDui;
                        }
                        if (!cardKinds.containsValue(3) && !cardKinds.containsValue(4)) {
                            return LianDui;
                        }
                    }
                }
                throw new InvalidHandTypeException("Invalid hand type");
            case 7:
                //AABBWildWild will be classified as gangban
                if (kindCount == 1) {
                    return BombSeven;
                }
                throw new InvalidHandTypeException("Invalid hand type");
            case 8:
                //AABBWildWild will be classified as gangban
                if (kindCount == 1) {
                    return BombEight;
                }
                throw new InvalidHandTypeException("Invalid hand type");
            case 9:
                //AABBWildWild will be classified as gangban
                if (kindCount == 1) {
                    return BombNine;
                }
                throw new InvalidHandTypeException("Invalid hand type");
            case 10:
                //AABBWildWild will be classified as gangban
                if (kindCount == 1) {
                    return BombTen;
                }
                throw new InvalidHandTypeException("Invalid hand type");
            default:
                throw new InvalidHandTypeException("Invalid hand type");
        }
    }

    public static int findHandValue(HandType type, ArrayList<Card> cards,HashMap<Kind,Integer> cardKinds, Kind level){
        int kindMax = 0;
        switch (type) {
            case Single:
            case Pair:
            case Three:
            case BombFour:
            case BombFive:
            case BombSix:
            case BombSeven:
            case BombEight:
            case BombNine:
            case BombTen:
            case JokerBomb:
                return (cards.get(0).cardVal.cardKind == level)? 15:cards.get(0).cardVal.cardKind.getValue();  
            case FullHouse:
                for(Map.Entry<Kind, Integer> entry : cardKinds.entrySet()){
                    kindMax = Math.max(kindMax, entry.getKey().getValue());
                    if (entry.getValue() == 3) {
                        return (entry.getKey() == level)? 15:entry.getKey().getValue();
                    }
                }
                return kindMax;
            case Straight:
            case StraightFlush:
                if (cardKinds.containsKey(Kind.Two) && cardKinds.containsKey(Kind.Ace) ) {
                    return 1;
                }
                return Math.min(cards.get(0).cardVal.cardKind.getValue(), 10);
            case GangBan:
                if (cardKinds.containsKey(Kind.Two) && cardKinds.containsKey(Kind.Ace) ) {
                    return 1;
                }
                return cards.get(0).cardVal.cardKind.getValue();
            case LianDui:
                if (cardKinds.containsKey(Kind.Two) && cardKinds.containsKey(Kind.Ace) ) {
                    return 1;
                }
                return Math.min(cards.get(0).cardVal.cardKind.getValue(),12);
            default:
                return 0;
        }
    }

}


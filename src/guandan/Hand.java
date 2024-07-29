package guandan;

import java.util.ArrayList;
import java.util.HashMap;

public class Hand {
    public int value;
    public HandType type;
    public Kind level;
    public ArrayList<Card> cards;

    private HashMap<Kind,Integer> cardKinds = new HashMap<>();
    private HashMap<Suit,Integer> cardSuits = new HashMap<>();
    private int suitCount = 0;
    private int wildCount = 0;
    private int cardCount = 0;
    private int kindCount = 0;
    
    public Hand(ArrayList<Card> cardsIn, Kind levelIn) throws InvalidHandTypeException{
        level = levelIn;
        cards = cardsIn;
        cardCount = cards.size();
        getHash(new ArrayList<>(cards));
        suitCount = cardSuits.keySet().size();
        kindCount = cardKinds.keySet().size();

        type = HandType.findHandType(cardKinds,suitCount,wildCount,cardCount,kindCount);
        value = HandType.findHandValue(type,cards,cardKinds,level);
    }

    public Boolean isHandBigger(HandType lastType, int lastVal){
        if (lastType == null) {
            return true;
        }
        else{
            if (type == lastType && value > lastVal) {
                return true;
            }
            else if (type.getValue() > lastType.getValue() ) {
                return true;
            }
        }
        return false;
    }
    
    private void getHash(ArrayList<Card> cards){
        if (cards.size() != 0 && CardVal.isWildCard(cards.get(cards.size()-1).cardVal, level)) {
            wildCount++;
            cards.remove(cards.get(cards.size()-1));
            if (cards.size() != 0 && CardVal.isWildCard(cards.get(cards.size()-1).cardVal, level)) {
                wildCount++;
                cards.remove(cards.get(cards.size()-1));
            }
        }
        for (Card card : cards) {
            if (cardSuits.containsKey(card.cardVal.cardSuit)) {
                cardSuits.put(card.cardVal.cardSuit, cardSuits.get(card.cardVal.cardSuit)+1);
            }
            else{
                cardSuits.put(card.cardVal.cardSuit, 1);
            }
            if (cardKinds.containsKey(card.cardVal.cardKind)) {
                cardKinds.put(card.cardVal.cardKind, cardKinds.get(card.cardVal.cardKind)+1);
            }
            else{
                cardKinds.put(card.cardVal.cardKind, 1);
            }
        }
    }

    public int getValue(){
        return value;
    }
}

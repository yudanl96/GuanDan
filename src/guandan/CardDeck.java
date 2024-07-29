package guandan;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Random;

public class CardDeck {
    int cardCount;
    ArrayList<Card> cardLeft;
    ArrayList<Card> wildCards = new ArrayList<>();
    //do not keep wildCards in these HashMaps
    HashMap<Kind, ArrayList<Card>> cardsByKind = new HashMap<Kind, ArrayList<Card>>();
    HashMap<Suit, ArrayList<Card>> cardsBySuit = new HashMap<Suit, ArrayList<Card>>();
    Kind level;
    int wildCount = 0;
    
    private static Random random = new Random();

    public CardDeck(Kind levelIn){
        // default getting a full deck
        level = levelIn;
        cardLeft = getFullDeck(level);
        cardLeft.addAll(getFullDeck(level));
        cardCount = 108;
        wildCount = 2;
    }

    public CardDeck(CardDeck deck){
        // copy a deck
        wildCards = new ArrayList<Card>(deck.wildCards);
        wildCount = deck.wildCount;
        level = deck.level;
        cardCount = deck.cardCount;
        cardLeft = new ArrayList<Card>(deck.cardLeft);
        cardsByKind = new HashMap<Kind, ArrayList<Card>>(deck.cardsByKind);
        cardsBySuit = new HashMap<Suit, ArrayList<Card>>(deck.cardsBySuit);
    }

    public CardDeck(int initialCardCount,Kind levelIn){
        // create empty deck
        cardCount = initialCardCount;
        cardLeft = new ArrayList<Card>(initialCardCount);
        level = levelIn;
    }

    public static ArrayList<Card> getFullDeck(Kind level){
        ArrayList<Card> fullDeck = new ArrayList<Card>(54);
        for (Kind kind : Kind.values()) {
            for (Suit suit : Suit.values()) {
                if (kind != Kind.BlackJoker && kind != Kind.RedJoker && suit != Suit.None) {
                    fullDeck.add(new Card(new CardVal(kind,suit),level));
                }
                else if ((kind == Kind.BlackJoker || kind == Kind.RedJoker ) && suit == Suit.None) {
                    fullDeck.add(new Card(new CardVal(kind,suit),level));
                }
            }
        }
        return fullDeck;
    }

    public Card drawRandom(){
        // remove one random card out of the card deck
        // return the removed card
        int index = random.nextInt(cardCount);
        cardCount--;
        return cardLeft.remove(index);
    }

    public Card findRandom(){
        // locate one random card out of the card deck
        // without removing the card
        int index = random.nextInt(cardCount);
        return cardLeft.get(index);
    }

    public void addCard(Card card){
        if (CardVal.isWildCard(card.cardVal, level)) {
            wildCount++;
            wildCards.add(card);
        }
        else{
            cardsByKind.computeIfAbsent(card.cardVal.cardKind,  k -> new ArrayList<>()).add(card);
            cardsBySuit.computeIfAbsent(card.cardVal.cardSuit,  k -> new ArrayList<>()).add(card);
        }
        cardLeft.add(card);
   
    }

    public void removeCard(Card card){
        if (CardVal.isWildCard(card.cardVal, level)) {
            wildCount--;
            wildCards.remove(card);
        }
        else{
            cardsByKind.get(card.cardVal.cardKind).remove(card);
            cardsBySuit.get(card.cardVal.cardSuit).remove(card);
        }
        cardLeft.remove(card);
    }

    public static void printCardsLeft(ArrayList<Card> cardIn){
        //display cards left in the deck to terminal
        for (Card card : cardIn) {
            System.out.print(card.cardVal.cardKind.getName());
            System.out.println(card.cardVal.cardSuit.getName());
        }
    }

    public static ArrayList<Card> arrangeCards(Kind levelKind, ArrayList<Card> cardIn){
        ArrayList<Card> arranged = new ArrayList<Card>(cardIn.size());
        cardLeftLoop:
        for (Card card : cardIn) {
            for (int i = 0; i < arranged.size(); i++) {
                if (card.cardVal.cardKind.compareTo(arranged.get(i).cardVal.cardKind,levelKind) == -1) {
                    arranged.add(i,card);
                    continue cardLeftLoop;
                }
                else if (card.cardVal.cardKind.compareTo(arranged.get(i).cardVal.cardKind,levelKind) == 0) {
                    if (card.cardVal.cardSuit.compareTo(arranged.get(i).cardVal.cardSuit) <= 0) {
                        arranged.add(i,card);
                        continue cardLeftLoop;
                    }
                }
            }
            arranged.add(arranged.size(),card);
        }
        return arranged;
    }

    public static void main(String[] args) {
        // CardDeck deck = new CardDeck();
        // CardDeck.printCardsLeft(deck.cardLeft);
        // deck.cardLeft = CardDeck.arrangeCards(Kind.Eight,deck.cardLeft);
        // deck.shuffleCards();
        // deck.printCardsLeft();
    }
}

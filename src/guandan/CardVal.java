package guandan;

import java.util.Random;

public class CardVal {
    Kind cardKind;
    Suit cardSuit;
   
    static Random randomGen = new Random();

    public CardVal(Kind cardKind, Suit cardSuit){
        this.cardKind = cardKind;
        this.cardSuit = cardSuit;
    }

    public void printCard(){
        System.out.println(cardKind.getName()+cardSuit.getName());
    }

    public static Boolean isWildCard(CardVal card, Kind level){
        if (card.cardKind == level && card.cardSuit == Suit.Heart) {
            return true;
        }
        return false;
    }

    public static void main(String[] args) {
        //getFullDeck();
        Kind test = Kind.Eight;
        System.out.println(test.compareTo(Kind.Ace, Kind.Two));
        System.out.println(test.compareTo(Kind.Ace, Kind.Eight));
        System.out.println(test.compareTo(Kind.BlackJoker, Kind.Eight));
        System.out.println(test.compareTo(Kind.Eight, Kind.Eight));
        test = Kind.BlackJoker;
        System.out.println(test.compareTo(Kind.RedJoker, Kind.Eight));

        Suit test1 = Suit.Club;
        System.out.println(test1.compareTo(Suit.Diamond));
        System.out.println(test1.compareTo(Suit.Club));
        System.out.println(test1.compareTo(Suit.Heart));
        Kind.kindOrder(Kind.Eight);
        //Suit.compareTo()
    }
}

package guandan;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

public class FullHands {
    HashMap<HandType,ArrayList<Hand>> handsByType = new HashMap<HandType,ArrayList<Hand>> ();  
    HashMap<Integer,ArrayList<Hand>>  handsByValue = new HashMap<Integer,ArrayList<Hand>> ();
    
    private CardDeck tempDeck; //only used to hold cards to process

    public FullHands(CardDeck deck){
        tempDeck = new CardDeck(deck);

        addJokerBomb();
        addSameBombs(6);
        addStraightFlush();
        if (tempDeck.wildCount >=1) {
            //will try to use up all wildcards
            addStraightFlush(1);
        }
        if (tempDeck.wildCount >=2) {
            addStraightFlush(2);
        }
        addSameBombs(5);
        addSameBombs(4);

        //find all kinds with 2/3 same cards
        List<Kind> allKinds = new ArrayList<>(tempDeck.cardsByKind.keySet());
        Collections.sort(allKinds,Kind.getComparator(Kind.Ace));
        ArrayList<Kind> pairKinds = new ArrayList<Kind>();
        ArrayList<Kind> threeKinds = new ArrayList<Kind>();
        for (int i = 0; i < allKinds.size(); i++) {
            if (allKinds.get(i) == Kind.BlackJoker || allKinds.get(i) == Kind.RedJoker) {
                //never add joker as pairs
                continue;
            }
            if (tempDeck.cardsByKind.get(allKinds.get(i)).size() == 2) {
                pairKinds.add(allKinds.get(i));
            }
            else if (tempDeck.cardsByKind.get(allKinds.get(i)).size() == 3) {
                threeKinds.add(allKinds.get(i));
            }
        }
        //get all pairs & Liandui
        addSameCards(2, pairKinds);
        //get all threes & GangBan
        addSameCards(3, threeKinds);

        addStraights();
        //rest are treated as singles
        while (tempDeck.cardLeft.size()!= 0) {
           addHand(new ArrayList<Card>(Arrays.asList(tempDeck.cardLeft.get(0))));
        }
        //arrange elements in hashmap -- ascending
        for (HandType type : handsByType.keySet()) {
            Collections.sort(handsByType.get(type),Comparator.comparing(Hand::getValue));
        }

        System.out.println("initializing completed");
    }

    private void addSameCards(int twoOrThree, ArrayList<Kind> sameKinds){
        ArrayList<Card> handCards = new ArrayList<Card>();
        int countPairs = sameKinds.size();
        while (countPairs >= 6/twoOrThree) {
            handCards.addAll(tempDeck.cardsByKind.get(sameKinds.get(0)));
            for (int i = 1; i < 6/twoOrThree; i++) {
                if (sameKinds.get(i).getValue() != sameKinds.get(0).getValue()+i) {
                    handCards = tempDeck.cardsByKind.get(sameKinds.get(0));
                    continue;
                }
                else{
                    handCards.addAll(tempDeck.cardsByKind.get(sameKinds.get(i)));
                }
            }
            for (int i = 0; i < handCards.size() / twoOrThree; i++) {
                sameKinds.remove(sameKinds.get(0));
            }
            addHand(handCards);
            countPairs = sameKinds.size();
            handCards = new ArrayList<Card>();
        }
        for (Kind kind : sameKinds) {
            addHand(tempDeck.cardsByKind.get(kind));
        }
    }

    private void addSameBombs(int count){
        List<Kind> allKinds = new ArrayList<>(tempDeck.cardsByKind.keySet());
        Collections.sort(allKinds,Kind.getComparator(tempDeck.level));
        for (int i = allKinds.size()-1; i >= 0 ; i--) {
            ArrayList<Card> handCards = new ArrayList<Card>(tempDeck.cardsByKind.get(allKinds.get(i)));
            if (handCards.size() + tempDeck.wildCount == count ) {
                //find count-card bomb
                int wildNeeded = count - handCards.size();
                while (wildNeeded != 0) {
                    handCards.add(tempDeck.wildCards.get(wildNeeded-1));
                    wildNeeded--;
                }
                addHand(handCards);
            }
        }
    }

    private void addStraightFlush(){
        for (Suit suit : Suit.values()) {
            // finding straight flush w no wildcards
            // using 0 wild card is better than using 1 than using 2 
            if (suit != Suit.None) {
                ArrayList<Card> handCards;
                do{
                    ArrayList<Card> cardsWSuit = new ArrayList<Card>(tempDeck.cardsBySuit.get(suit));
                    Collections.sort(cardsWSuit,Card.getComparator(Kind.Ace));
                    handCards = findBiggestStraight(cardsWSuit, tempDeck.wildCards, 0);
                    if (handCards != null) {
                        addHand(handCards);
                    }
                }while(handCards != null);
            }
        }
    }

    private void addStraightFlush(int wildCardsUsed){
        //find the biggest possible straighflush within each suit
        //keep the biggest one
        //will try to use up all wild cards 
        while(tempDeck.wildCount >= wildCardsUsed){
            ArrayList<Card> keep = null;
            for (Suit suit : Suit.values()) {
                // finding biggest straight flush w wildCardsUsed wildcards
                if (suit != Suit.None) {
                    ArrayList<Card> handCards;
                    ArrayList<Card> cardsWSuit = new ArrayList<Card>(tempDeck.cardsBySuit.get(suit));
                    Collections.sort(cardsWSuit,Card.getComparator(Kind.Ace));
                    
                    handCards = findBiggestStraight(cardsWSuit, tempDeck.wildCards, wildCardsUsed);
                    if(handCards != null){
                        handCards = CardDeck.arrangeCards(tempDeck.level, handCards);
                        if (keep == null) {
                            keep = handCards;
                        }
                        else{
                            keep = (keep.get(0).cardVal.cardKind.getValue() >=handCards.get(0).cardVal.cardKind.getValue())?
                                    keep: handCards;
                        }
                    }
                }
            }
            if (keep != null) {
                addHand(keep);
            }
            else{
                // no need to loop again if nothing is found
                break;
            }
        }
    }

    private void addJokerBomb(){
        //add joker bomb if any
        ArrayList<Card> handCards = new ArrayList<Card>();
        if (tempDeck.cardsByKind.getOrDefault(Kind.RedJoker,new ArrayList<Card>()).size() == 2 
            && tempDeck.cardsByKind.getOrDefault(Kind.BlackJoker,new ArrayList<Card>()).size() == 2 ) {
            handCards.addAll(tempDeck.cardsByKind.get(Kind.RedJoker));
            handCards.addAll(tempDeck.cardsByKind.get(Kind.BlackJoker));
            addHand(handCards);
        }
    }

    
    private void addStraights(){
        tempDeck.cardLeft = CardDeck.arrangeCards(Kind.Ace, tempDeck.cardLeft);
        ArrayList<Card> handCards;
        do {
            handCards = findSmallestSraight(tempDeck.cardLeft, tempDeck.wildCards);
            if (handCards != null) {
                addHand(handCards);
            }
        } while (handCards != null);
    }

    private static ArrayList<Card> findSmallestSraight(ArrayList<Card> allCards, ArrayList<Card> wildCards){
        //allCards should be sorted ascening, allCards.size + wildCount should >=5
        //used to find smallest straight, will use wildcards if there are any left (although very unlikely)
        int curWildCount = wildCards.size();
        if (allCards == null || allCards.size() + curWildCount < 5) {
            return null;
        }

        ArrayList<Card> handCards = new ArrayList<Card>();
        int diff;
        Boolean[] hasA2345 = new Boolean[5];
        Arrays.fill(hasA2345, Boolean.FALSE);
        ArrayList<Card> A2345 = new ArrayList<>();
        for (int i = 1; i < allCards.size(); i++) {
            //val is used to determine whether A2345 exists
            int val = (allCards.get(i).cardVal.cardKind.getValue() == 14)? 1:allCards.get(i).cardVal.cardKind.getValue();
            if (val<=5 && hasA2345[val-1]==false) {
                A2345.add(allCards.get(i));
                hasA2345[val-1] = true;
            }
            diff = allCards.get(i).cardVal.cardKind.getValue() - allCards.get(i-1).cardVal.cardKind.getValue(); 
            
            if (diff == 1) {
                handCards.add(allCards.get(i-1));
                if (handCards.size() == 4) {
                    handCards.add(allCards.get(i));
                    i++;
                }
            }
            else if (diff > 1 && (Math.min(diff-1 , 4-handCards.size()) <= curWildCount)) {
                handCards.add(allCards.get(i-1));
                curWildCount--;
                for (int j = 0; j < Math.min(diff-1 , 5-handCards.size()); i++) {
                    handCards.add(wildCards.get(curWildCount - j)); 
                }
                if (handCards.size() == 4) {
                    handCards.add(allCards.get(i));
                    i++;
                }
            }
            else if (diff != 0) {
                handCards = new ArrayList<Card>();
                curWildCount = wildCards.size();
            }

            if (handCards.size() == 5) {
                return handCards;
            }
        }

        if (A2345.size() + wildCards.size() >= 5) {
            for (int i = 0; i < 5 - A2345.size(); i++) {
                A2345.add(wildCards.get(i));
            }
            return A2345;
        }
        //if not straights then return null
        return null;
    }

    private static ArrayList<Card> findBiggestStraight(ArrayList<Card> allCards,ArrayList<Card> wildCards, int wildCount){
        //allCards should be sorted ascending , allCards.size + wildCount should >=5;
        // used to find straight flush
        // therefore assume maximum of 2 cards for each kind type 
        if (allCards == null || allCards.size() + wildCount < 5) {
            return null;
        }

        ArrayList<Card> handCards = new ArrayList<Card>();

        int diff;
        int curWildCount = wildCount;
        
        //need to consider A2345 if contains Ace / contains wild card
        Boolean hasAce = (allCards.get(allCards.size()-1).cardVal.cardKind == Kind.Ace)? true: false;

        Card priorCard = new Card();
        Card curCard = new Card();
        for (int j = allCards.size()-1; (j > 0) || (j==0 && (hasAce || curWildCount >= 1)); j--) {
            if (j > 0) {
                diff = allCards.get(j).cardVal.cardKind.getValue()-allCards.get(j-1).cardVal.cardKind.getValue();
                curCard = allCards.get(j);
                priorCard = allCards.get(j-1);
            }
            else{
                //A2345
                diff = allCards.get(j).cardVal.cardKind.getValue() - 1;
                curCard = allCards.get(j);
                if (hasAce) {
                    priorCard = allCards.get(allCards.size()-1);
                }
                else{
                    priorCard = wildCards.get(curWildCount-1);
                }
            }
            if (diff == 1) {
                handCards.add(curCard);
                if (handCards.size() == 4 ) {
                    handCards.add(priorCard);
                    j--;
                }                               
            }
            else if (diff > 1 && (Math.min(diff-1 , 4-handCards.size()) <= curWildCount)) {
                // max of wildcards needed = min(cards needed to get 5 cards, cards needed to fill the gap)
                handCards.add(curCard);
                curWildCount--;
                for (int i = 0; i < Math.min(diff-1 , 5-handCards.size()); i++) {
                    handCards.add(wildCards.get(curWildCount - i)); 
                }
                if (handCards.size() == 4 ) {
                    handCards.add(priorCard);
                    j--;
                }    
            }
            else if (diff != 0) {
                handCards = new ArrayList<Card>();
                curWildCount = wildCount;
            }
            if (handCards.size() == 5) {
                return handCards;
            }
        }

        if (handCards.size() == 3 && handCards.get(0).cardVal.cardKind == Kind.Four && curWildCount>=1) {
            // 432 with a & wildcard, 432ww is better than 432awild if allowed
            if (curWildCount >= 2) {
                handCards.add(wildCards.get(curWildCount - 2)); 
            } else{
            handCards.add(priorCard);
            }
            handCards.add(wildCards.get(curWildCount - 1)); 
            return handCards;
        }
        if (handCards.size() == 2 && handCards.get(0).cardVal.cardKind == Kind.Three) {
            if (hasAce && curWildCount >= 2) {
                handCards.add(priorCard);
                handCards.add(wildCards.get(curWildCount - 1)); 
                handCards.add(wildCards.get(curWildCount - 2));
                return handCards; 
            }
        }

        //if not straights then return null
        return null;
    }

    private void addHand(ArrayList<Card> handCards){
        handCards=CardDeck.arrangeCards(tempDeck.level, handCards);
        // System.out.println("showing hands");
        // for (Card card : handCards) {
        //     System.out.println(card.cardVal.cardKind+" "+card.cardVal.cardSuit);
        // }
        // System.out.println("showing result after hands");
        Hand hand = null;
        try {
            hand = new Hand(handCards, tempDeck.level);
        } catch (Exception e) {
            System.err.println("not a valid hand");
            System.exit(1);
        }
        handsByType.computeIfAbsent(hand.type,  k -> new ArrayList<Hand>()).add(hand);
        handsByValue.computeIfAbsent(hand.value,  k -> new ArrayList<Hand>()).add(hand);
        for (Card card : handCards) {
            tempDeck.removeCard(card);
        }
    }


}

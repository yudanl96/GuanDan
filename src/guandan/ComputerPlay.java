package guandan;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CountDownLatch;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

public class ComputerPlay implements PlayTimerListener, Runnable{
    HandType lastType = null;
    int lastVal=0;
    Hand playHand;
    PlayTimer timer = new PlayTimer(this);
    ArrayList<Card> selected = new ArrayList<>(6);
    PlayerPage gui;
    Player player;
    Kind level;
    FullHands myHands;
    int myIndex;
    Boolean skipped = true;
    Boolean isFirstPlay = true;

    private CountDownLatch latch;
    private JPanel sidePanel;
    private Font skipFont = new Font("Bebas",Font.BOLD,30);
    private JLabel skipLabel = new JLabel("Skip",JLabel.CENTER);
    private JPanel selectedPanel;

    public ComputerPlay(PlayerPage guiIn, Player playerIn, int order){
        gui = guiIn;
        level = guiIn.levelKind;
        player =playerIn;
        myIndex = order;

        skipLabel.setFont(skipFont);

        switch (myIndex) {
            case 0:
                sidePanel = ((CenterPanel) gui.fullDeck.playerPanel).west;
                break;
            case 1:
                sidePanel = ((CenterPanel) gui.fullDeck.playerPanel).north;
                break;
            case 2:
                sidePanel = ((CenterPanel) gui.fullDeck.playerPanel).east;
                break;
            default:
                break;
        }

    }

    public void startPlay(Hand handIn, int sourceOfHand, CountDownLatch latchIn){
        SwingUtilities.invokeLater(() -> {
            sidePanel.removeAll();
            sidePanel.revalidate();
            sidePanel.repaint();
        });

        latch = latchIn;
        selected = new ArrayList<>(6);
        playHand = null;
        skipped = true;
        Boolean isTeammate = (Math.abs(sourceOfHand - myIndex) == 2) ? true : false;

        timer = new PlayTimer(this);
        timer.showTimer(false, sidePanel);

        try {
            Thread.sleep(2000);
        } catch (Exception e) {
            // TODO: handle exception
        }

        if (sourceOfHand == myIndex) {
            // initializing a hand
            initialAPlay();
        }
        else{
            respondToPlays(handIn.type, handIn.value, isTeammate);
        }
        whenTimeExpired();
    }

    public void run(){
        //allow computers to sort hands on different thread
        myHands = new FullHands(player.playerCards);
    }
    
    public void intialStartPlay(Thread thread,Hand handIn, int sourceOfHand, CountDownLatch latch) {
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        startPlay(handIn, sourceOfHand, latch);
    }

    public void initialAPlay(){
        //play hands with lower value first
        //prioritizing playing hands with maximum card count
        //except bombs
        List<Integer> values = new ArrayList<>(myHands.handsByValue.keySet());
        Collections.sort(values);
        for(int val : values){
            ArrayList<Hand> handsOfValue = myHands.handsByValue.get(val);
            int maxCount = 0;
            Hand toBePlayed = null;
            //to be used when playing full house
            Hand pair = null;
            Hand three = null;
            Boolean isFullHouseMax = false;
            for (Hand hand : handsOfValue) {
                if (hand.type.getValue() == 1) {
                    Boolean isFullHouse = false;
                    int cardCount = hand.cards.size();
                    ArrayList<Card> fullHouse = hand.cards;
                    if (hand.type == HandType.Three) {
                        ArrayList<Hand> handsOfTwo = myHands.handsByType.getOrDefault(HandType.Pair, null);
                        if (handsOfTwo != null && handsOfTwo.size()!=0) {
                            cardCount = 5;
                            pair = handsOfTwo.get(0);
                            three = hand;
                            fullHouse.addAll(handsOfTwo.get(0).cards);
                            isFullHouse = true;   
                        }
                    }
                    if(cardCount > maxCount){
                        if (isFullHouse) {
                            try {
                                toBePlayed = new Hand(fullHouse, level);
                           } catch (Exception e) {
                               System.err.println("not valid hand");
                               System.exit(1);
                           }
                           isFullHouseMax = true;
                        }
                        else{
                            toBePlayed = hand;
                            isFullHouseMax = false;
                        }
                    }
                }
            }
            if (toBePlayed != null) {
                if (isFullHouseMax) {
                    playCards(pair);
                    playCards(three);
                    playHand = toBePlayed;
                    selected = toBePlayed.cards;
                }
                else{
                    playCards(toBePlayed);
                }
                return;
            }
        }
        //if no normal cards left
        for (int handval = 2; handval < 11; handval++) {
            ArrayList<Hand> handsOfType = myHands.handsByType.getOrDefault(HandType.fromVal(handval), null);
            if (handsOfType != null && handsOfType.size()!=0) {
                playCards(handsOfType.get(0));
                return;
            }
        }
    }

    public void respondToPlays(HandType lastTypeIn, int lastValIn, Boolean isTeammate){
        lastType = lastTypeIn;
        lastVal = lastValIn;

        if (isTeammate && lastValIn >= 15) {
            //if the hand is played by a teammate & its a big hand
            skipped = true;
        }
        else if (lastTypeIn.getValue() == 1) {
            // normal hands, not bomb
            if (lastTypeIn == HandType.FullHouse) {
                //full house is stored as threes and twos seperated
                ArrayList<Hand> handsOfThree = myHands.handsByType.getOrDefault(HandType.Three, null);
                ArrayList<Hand> handsOfTwo = myHands.handsByType.getOrDefault(HandType.Pair, null);
                if (handsOfThree != null && handsOfTwo != null && handsOfThree.size()!=0 && handsOfTwo.size()!=0) {
                    for (Hand hand : handsOfThree) {
                        if (hand.value > lastValIn) {
                            selected = hand.cards;
                            selected.addAll(handsOfTwo.get(0).cards);
                            try {
                                playHand = new Hand(selected, level);
                            } catch (Exception e) {
                                System.err.println("not valid hand");
                                System.exit(1);
                            }
                            if (playHand.type != HandType.FullHouse) {
                                continue;
                            }
                            for (Card cardRe : selected) {
                                //remove card from cardLeft
                                player.playerCards.removeCard(cardRe);
                                //remove card display
                                player.playerPanel.removeCard(cardRe);
                            }
                            handsOfThree.remove(hand);
                            myHands.handsByValue.get(hand.value).remove(hand);
                            myHands.handsByValue.get(handsOfTwo.get(0).value).remove(handsOfTwo.get(0));
                            handsOfTwo.remove(handsOfTwo.get(0));
                            skipped = false;
                            break;
                        }
                    }
                }   
            }
            else{
                ArrayList<Hand> handsOfType = myHands.handsByType.getOrDefault(lastTypeIn, null);
                if(handsOfType != null && handsOfType.size()!=0){
                    for (Hand hand : handsOfType) {
                        if (hand.value > lastValIn) {
                            playCards(hand);
                            // playHand = hand;
                            // selected = hand.cards;
                            // for (Card cardRe : selected) {
                            //     //remove card from cardLeft
                            //     player.playerCards.removeCard(cardRe);
                            //     //remove card display
                            //     player.playerPanel.removeCard(cardRe);
                            // }
                            // handsOfType.remove(hand);
                            // myHands.handsByValue.get(hand.value).remove(hand);
                            // skipped = false;
                            break;
                        }
                    }
                }
            }
            // if computer does not have that hand -- consider using bomb
            // only when its a big value hand not played by a teammate
            if (lastValIn >= 15 && !isTeammate) {
                for (int handval = 2; handval < 11; handval++) {
                    ArrayList<Hand> handsOfType = myHands.handsByType.getOrDefault(HandType.fromVal(handval), null);
                    if (handsOfType != null && handsOfType.size()!=0) {
                        playCards(handsOfType.get(0));
                    }
                }
            }
        }
        else if (lastTypeIn.getValue()>1 && !isTeammate) {
        // if responding to a bomb played by opponent 
            bigLoop:
            for (int handval = lastTypeIn.getValue(); handval < 11; handval++) {
                ArrayList<Hand> handsOfType = myHands.handsByType.getOrDefault(HandType.fromVal(handval), null);
                if (handsOfType != null && handsOfType.size() !=0) {
                    if (handval > lastTypeIn.getValue()) {
                        playCards(handsOfType.get(0));
                        break;
                    }
                    for (Hand hand : handsOfType) {
                        if (hand.value > lastValIn) {
                            playCards(hand);
                            break bigLoop;
                        }
                    }
                }
            }
        }
    }

    // public void playCards(Hand hand, ArrayList<Hand> handsOfType){
    //     playHand = hand;
    //     selected = hand.cards;
    //     for (Card cardRe : selected) {
    //         //remove card from cardLeft
    //         player.playerCards.removeCard(cardRe);
    //         //remove card display
    //         player.playerPanel.removeCard(cardRe);
    //         System.out.print(cardRe.cardVal.cardKind+" "+cardRe.cardVal.cardSuit);
    //     }
    //     System.out.println("");
    //     handsOfType.remove(hand);
    //     myHands.handsByValue.get(hand.value).remove(hand);
    //     skipped = false;
    // }

    public void playCards(Hand hand){
        playHand = hand;
        selected = hand.cards;
        for (Card cardRe : selected) {
            //remove card from cardLeft
            player.playerCards.removeCard(cardRe);
            //remove card display
            player.playerPanel.removeCard(cardRe);
        }
        myHands.handsByValue.get(hand.value).remove(hand);
        myHands.handsByType.get(hand.type).remove(hand);
        skipped = false;
    }

    public void whenTimeExpired(){
        SwingUtilities.invokeLater(new Runnable (){
            public void run(){
                sidePanel.remove(timer.timerBox);
                if (skipped) {
                    sidePanel.add(skipLabel);
                }
                else{
                    JPanel selectedPanel = new SelectedCards(selected,false);
                    sidePanel.add(selectedPanel);
                    switch (myIndex) {
                        case 1:
                            sidePanel.setPreferredSize(new Dimension(
                            selectedPanel.getPreferredSize().width+5,
                            selectedPanel.getPreferredSize().height+5));
                            break;
                        default:
                            sidePanel.setPreferredSize(new Dimension(300,50));
                            break;
                    }
                }
                sidePanel.revalidate();
                sidePanel.repaint();
                latch.countDown();
            }
        });
        
    }
}

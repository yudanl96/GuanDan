package guandan;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.concurrent.CountDownLatch;

import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.SwingUtilities;
import javax.swing.Timer;
import javax.swing.border.Border;

public class Main {

    public static void main(String[] args) {
        CountDownLatch latch = new CountDownLatch(2);

        EnterPageEvents enter = new EnterPageEvents(new EnterPage(),latch);

        try {
            latch.await(); // Wait until latch counts down to 0
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.err.println("Interrupted while waiting for guiPlayer initialization.");
            return;
        }
        try {
            Thread.sleep(500);
        } catch (Exception e) {
            // TODO: handle exception
        }

        latch = new CountDownLatch(1);
        new DealCard(enter.guiPlayer, enter.guiPlayer.human, enter.guiPlayer.fullDeck, enter.guiPlayer.computers,latch);
        
        try {
            latch.await(); // Wait until latch counts down to 0
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.err.println("Interrupted while waiting for guiPlayer initialization.");
            return;
        }

        Thread[] gos = new Thread[3];
        ComputerPlay[] computers = new ComputerPlay[3];
        for (int i =0; i < 3; i++) {
            computers[i] = new ComputerPlay(enter.guiPlayer, enter.guiPlayer.computers[i],i);
            gos[i] = new Thread(computers[i]);
            gos[i].start();
        }

        HumanPlay human= new HumanPlay(enter.guiPlayer,enter.guiPlayer.human);

        int curPlayer = (int)Math.random()*4;
        //for debugging
        //curPlayer = 3;
    
        Hand hand = null;
        int sourceOfHand = curPlayer;
        
        wholeLoop:
        while (true) {

            latch = new CountDownLatch(1);
            switch (curPlayer) {
                case 3:
                    human.startPlay(hand,sourceOfHand, latch);
                    break;
                default:
                    if(computers[curPlayer].isFirstPlay){
                        //start play only after run is completed
                        computers[curPlayer].intialStartPlay(gos[curPlayer],hand,sourceOfHand,latch);
                        computers[curPlayer].isFirstPlay = false;
                    }
                    else{
                        computers[curPlayer].startPlay(hand,sourceOfHand,latch);
                    }       
                    break;
            }
            
            try {
                latch.await(); // Wait until latch counts down to 0
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                System.err.println("Interrupted while waiting for guiPlayer initialization.");
                return;
            }
            if (hand!= null) {
                System.out.println(hand.type);
                for (Card c : hand.cards) {
                    System.out.print(c.cardVal.cardKind+""+c.cardVal.cardSuit+ " ");
                }
                System.out.println("");
            }

            switch (curPlayer) {
                case 3:
                    if (!human.skipped) {
                        hand = human.playHand;
                        sourceOfHand = curPlayer;
                    }
                    break;
                default:
                    if (!computers[curPlayer].skipped) {
                        hand = computers[curPlayer].playHand;
                        sourceOfHand = curPlayer;
                    }
                    break;
            }
            
            switch (curPlayer) {
                case 3:
                    if (!human.skipped) {
                        hand = human.playHand;
                        sourceOfHand = curPlayer;
                    }
                    if (human.player.playerCards.cardLeft.size() <= 0 ) {
                        break wholeLoop;
                    }
                    break;
                default:
                    if (!computers[curPlayer].skipped) {
                        hand = computers[curPlayer].playHand;
                        sourceOfHand = curPlayer;
                    }
                    if (computers[curPlayer].player.playerCards.cardLeft.size() <= 0 ) {
                        break wholeLoop;
                    }
                    break;
            }
            curPlayer = (curPlayer+1) % 4;
        }
        if (curPlayer == 1 || curPlayer == 3) {
            popUpPanel(enter,"You win!");
        }
        else{
            popUpPanel(enter,"You lose");
        }
    }
    
    private static void printcards(ComputerPlay[] computers, HumanPlay human){
        for (Card c: human.player.playerCards.cardLeft) {
            System.out.print(c.cardVal.cardKind+""+c.cardVal.cardSuit+" ");
        }
        System.out.println("");
        for (ComputerPlay comp : computers) {
            for (Card c1: comp.player.playerCards.cardLeft) {
                System.out.print(c1.cardVal.cardKind+""+c1.cardVal.cardSuit+" ");
            }
            System.out.println("");
        }
    }

    private static void popUpPanel(EnterPageEvents enter, String message){
    // Create a JDialog
        final JDialog dialog = new JDialog(enter.guiPlayer, "Popup", true);
        JLabel result = new JLabel(message, JLabel.CENTER);
        
        dialog.setLayout(new BorderLayout());
        dialog.add(result, BorderLayout.CENTER);
        dialog.setSize(300, 50);
        dialog.setLocationRelativeTo(enter.guiPlayer); // Center the dialog relative to the frame

        dialog.setVisible(true);
    }
}


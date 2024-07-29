package guandan;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Flow;

import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.Timer;

public class HumanPlay implements CardClickListener, ActionListener, PlayTimerListener{
    
    HandType lastType = null;
    int lastVal = 0;
    Hand playHand;
    ArrayList<Card> selected = new ArrayList<>(6);
    PlayerPage gui;
    Player player;
    Kind level;
    PlayTimer timer = new PlayTimer(this);
    Boolean skipped = true;

    private CountDownLatch latch;
    private JLabel illegalHand = new JLabel("Illegal hand selected! Please try again. ", JLabel.CENTER);
    private JPanel sidePanel;
    private Font skipFont = new Font("Bebas",Font.BOLD,30);
    private JLabel skipLabel = new JLabel("Skip",JLabel.CENTER);
    private JPanel selectedPanel;
    
    public HumanPlay(PlayerPage guiIn, Player playerIn){
        gui = guiIn;
        level = guiIn.levelKind;
        player =playerIn;
        skipLabel.setFont(skipFont);

        sidePanel = ((CenterPanel) gui.fullDeck.playerPanel).south;

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

        SwingUtilities.invokeLater(() -> {
            timer = new PlayTimer(this);
            timer.showTimer(true, sidePanel);
            timer.play.addActionListener(this);
            timer.skip.addActionListener(this);
            if (sourceOfHand == 3) {
                timer.skip.setEnabled(false);
            }
            else{
                timer.skip.setEnabled(true);
            }
        });

        for (Card card : player.playerCards.cardLeft) {
            card.addCardClickListener(this);
        }
                
        if (sourceOfHand == 3) {
            respondToPlays(null,0);
        }
        else{
            respondToPlays(handIn.type,handIn.value);
        }
    }

    public void respondToPlays(HandType lastTypeIn, int lastValIn){
        lastType = lastTypeIn;
        lastVal = lastValIn;
        selected = new ArrayList<>(6);
        if (lastType != null) {
            timer.skip.setEnabled(true);
        }
    }

    public void actionPerformed(ActionEvent e){
        Boolean handLegal = true;
        if (e.getSource() == timer.play) {
            if (selected.size() !=0) {
                selected = CardDeck.arrangeCards(level, selected);
                try {
                    playHand = new Hand(selected,level);
                } catch (InvalidHandTypeException exception) {
                    handLegal = false;
                }
                if (handLegal && playHand.isHandBigger(lastType, lastVal)) {
                    for (Card cardRe : selected) {
                        //remove card from cardLeft
                        player.playerCards.removeCard(cardRe);
                        //remove card display
                        player.playerPanel.removeCard(cardRe);
                    }
                    skipped = false;
                    timer.timeLeft=0;
                }
                else{
                    SwingUtilities.invokeLater(new Runnable() {
                        @Override
                        public void run() {
                            popUpPanel();
                        }
                    });
                }
            }
        } else if (e.getSource() == timer.skip) {
            timer.timeLeft = 0;
        }
    }

    public void whenTimeExpired(){
        SwingUtilities.invokeLater(new Runnable (){
            public void run(){
                sidePanel.remove(timer.timerBox);
                if (skipped) {
                    sidePanel.add(skipLabel);
                }
                else{
                    selectedPanel = new SelectedCards(selected, true);
                    sidePanel.add(selectedPanel);
                    sidePanel.setPreferredSize(new Dimension(
                        selectedPanel.getPreferredSize().width+5,
                        selectedPanel.getPreferredSize().height+5));
                }
                sidePanel.revalidate();
                sidePanel.repaint();
            }
        });
        for (Card card : player.playerCards.cardLeft) {
            card.removeCardClickListener(this);
        }
        latch.countDown();
    }

    private void popUpPanel(){
        // Create a JDialog
        final JDialog dialog = new JDialog(gui, "Popup", true);
        dialog.setLayout(new BorderLayout());
        dialog.add(illegalHand, BorderLayout.CENTER);
        dialog.setSize(300, 50);
        dialog.setLocationRelativeTo(gui); // Center the dialog relative to the frame
        
        // Create a Timer to close the dialog after 1 second
        Timer timer = new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dialog.setVisible(false);
                dialog.dispose();
            }
        });
        timer.setRepeats(false); // Ensure the timer only runs once
        timer.start();

        // Show the dialog
        dialog.setVisible(true);
    }
    
    public void cardClicked(Card card){
        card.setCardUI();
        if (card.isSelected) {
            selected.remove(card);   
        }
        else{
            selected.add(card);
        }
        card.isSelected = !card.isSelected;
    }
}

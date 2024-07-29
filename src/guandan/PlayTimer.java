package guandan;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

public class PlayTimer implements Runnable{
    int timeLeft;
    JLabel timer = new JLabel("", JLabel.CENTER);
    Font timerFont = new Font("Bebas",Font.BOLD,20);
    JButton play = new JButton("Play");
    JButton skip = new JButton("Skip");
    JPanel buttonBox = new JPanel();
    JPanel timerBox = new JPanel();
    PlayTimerListener listener;

    public PlayTimer(PlayTimerListener listenerIn){
        listener = listenerIn;
        buttonBox.add(play);
        buttonBox.add(skip);
        timer.setPreferredSize(new Dimension(100,20));
        buttonBox.setPreferredSize(new Dimension(100,25));
        skip.setEnabled(false);
        GridLayout grid = new GridLayout(2,1);
        timerBox.setLayout(grid);
        timerBox.setPreferredSize(new Dimension(100,50));
    }

    public void showTimer(Boolean isHuman, JPanel sidePanel){
        timeLeft=60;
        timer.setFont(timerFont);
        timer.setText("Time left: "+timeLeft);
        
        if (isHuman) {
            timerBox.add(timer);
            timerBox.add(buttonBox);
        }
        else{
            timerBox.removeAll();
            timerBox.add(timer);
        }
        sidePanel.setLayout(new GridLayout(1,2));
        sidePanel.add(timerBox);
       
        Thread go = new Thread(PlayTimer.this);
        go.start();
    }
    
    public void run(){
        while(timeLeft > 0){
            timer.setText("Time left: "+timeLeft);
            try {
                Thread.sleep(1000);
            } catch (Exception e) {
                // TODO: handle exception
            }
            timeLeft--;
        }
        listener.whenTimeExpired();
    }

}

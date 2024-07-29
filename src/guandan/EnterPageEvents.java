package guandan;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.concurrent.CountDownLatch;

public class EnterPageEvents implements ActionListener{
    EnterPage guiEnter;
    PlayerPage guiPlayer;
    CountDownLatch latch;

    public EnterPageEvents(EnterPage in, CountDownLatch latchIn){
        guiEnter = in;
        latch = latchIn;
        guiEnter.newGame.addActionListener(this);
    }

    public void actionPerformed(ActionEvent event){
        Object source = event.getSource();
        if (source == guiEnter.newGame) {
            guiEnter.dispose();
            Kind levelKind = Kind.drawKind();
            Player human = new Player("Human",new HumanPanel(levelKind),levelKind);
            Player full = new Player(new CenterPanel(),levelKind);
            Player[] computers = new Player[3];
            computers[0] = new Player("Computer", new ComputerPanel(true),levelKind);
            computers[2] = new Player("Computer", new ComputerPanel(true),levelKind);
            computers[1] = new Player("Computer", new ComputerPanel(false),levelKind);
            guiPlayer = new PlayerPage(levelKind,human,full,computers,latch);
            latch.countDown();
        }
    }

    public static void main(String[] args) {
        //new EnterPageEvents(new EnterPage());
    }
}

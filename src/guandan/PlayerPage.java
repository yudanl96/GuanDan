package guandan;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.concurrent.CountDownLatch;

import javax.swing.JFrame;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import javax.swing.Timer;

public class PlayerPage extends JFrame{
    Player fullDeck;
    Player[] computers;
    Player human;
    
    //south panel components
    JLayeredPane[] cardsByKind = new JLayeredPane[14];
    Kind[] kindOrder = new Kind[14];

    SetLevel setLevel;
    Kind levelKind;

    CountDownLatch latch;

    public PlayerPage(Kind level, Player player, Player full, Player[] computersIn, CountDownLatch latchIn){
        super("Guan Dan");

        levelKind = level;
        human = player;
        fullDeck = full;
        computers = computersIn;
        latch = latchIn;

        setSize(1400,800);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        JLayeredPane layeredPane = new JLayeredPane();
        setLayeredPane(layeredPane);

        setLevel = new SetLevel(levelKind);

        Dimension frameSize = getSize();
        Dimension panelSize = setLevel.getPreferredSize();
        int x = (frameSize.width - panelSize.width) / 2;
        int y = (frameSize.height - panelSize.height) / 2;

        setLevel.setBounds(x, y, panelSize.width+10, panelSize.height);
        layeredPane.add(setLevel, JLayeredPane.PALETTE_LAYER);

        JPanel defaultLayer = new JPanel();
        BorderLayout border = new BorderLayout(20, 20);
        defaultLayer.setLayout(border);
        defaultLayer.setBounds(20, 20, frameSize.width-40, frameSize.height-40);

        //middle
        defaultLayer.add((JPanel)fullDeck.playerPanel,BorderLayout.CENTER);
        //west
        defaultLayer.add((JPanel)computers[0].playerPanel,BorderLayout.WEST);
        //east
        defaultLayer.add((JPanel)computers[2].playerPanel,BorderLayout.EAST);
        //north
        defaultLayer.add((JPanel)computers[1].playerPanel,BorderLayout.NORTH);
        //south
        defaultLayer.add((JPanel)human.playerPanel,BorderLayout.SOUTH);
        layeredPane.add(defaultLayer,JLayeredPane.DEFAULT_LAYER);
        
        setVisible(true);

        Timer timer = new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                layeredPane.remove(setLevel);
                setLevel.cornerPanel.setBounds(5,5, setLevel.cornerPanel.getPreferredSize().width, setLevel.cornerPanel.getPreferredSize().height);
                layeredPane.add(setLevel.cornerPanel, JLayeredPane.PALETTE_LAYER);
                layeredPane.revalidate();
                layeredPane.repaint();
                latch.countDown();
            }
        });

        // Start the timer (the Timer will execute the actionPerformed method once after 2 seconds)
        timer.setRepeats(false); // Ensure the Timer only runs once
        timer.start();
    }
    
    // public static void main(String[] args) {
    //     Kind levelKind = Kind.drawKind();
    //     Player human = new Player("test", new ComputerPanel(false));
    //     Player full = new Player(new CenterPanel(),levelKind);
    //     Player[] computers = new Player[3];
    //     computers[0] = new Player("computer", new ComputerPanel(true));
    //     computers[2] = new Player("computer", new ComputerPanel(true));
    //     computers[1] = new Player("computer", new ComputerPanel(false));
    //     new PlayerPage(levelKind,human,full,computers,new CountDownLatch(1));
    // }

    // public PlayerPage(){
    //     //default level is Ace
    //     Player test = new Player("test player");
    //     Player[] testcomp = new Player[3];
    //     test.playerCards = new CardDeck();
    //     this(Kind.Ace, test, new CardDeck(),testcomp);
    // }
}

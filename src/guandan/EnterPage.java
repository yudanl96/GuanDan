package guandan;

import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class EnterPage extends JFrame{
    JPanel row1 = new JPanel();
    JPanel row2 = new JPanel();
    JPanel row3 = new JPanel();
    JLabel title = new JLabel("Let's Play 掼蛋 (Guan Dan)");
    JCheckBox cardRecBox = new JCheckBox("Record Cards Played");
    JButton newGame = new JButton("New Game");
    JButton loadGame = new JButton("Load Game");

    public EnterPage(){
        super("Guan Dan");
        setSize(1400,800);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        
        GridLayout grid = new GridLayout(3,1,100,80);
        setLayout(grid);

        FlowLayout flo = new FlowLayout(FlowLayout.CENTER, 80, 100);
        row1.setLayout(flo);
        title.setFont(new Font("Arial", Font.BOLD,25));
        row1.add(title);
        add(row1);

        FlowLayout flo2 = new FlowLayout(FlowLayout.CENTER,20,10);
        row2.setLayout(flo2);
        // Kind[] allkinds = Kind.values();
        // for (Kind kind : allkinds) {
        //     if (kind != Kind.Joker) {
        //         levelBox.addItem(kind.getName());
        //     }
        // }
        // row2.add(levelLabel);
        // row2.add(levelBox);
        row2.add(cardRecBox);
        add(row2);

        row3.setLayout(flo2);
        row3.add(newGame);
        row3.add(loadGame);
        loadGame.setEnabled(false);
        add(row3);

        setVisible(true);
    }

    public static void main(String[] args) {
        new EnterPage();
    }
    
}

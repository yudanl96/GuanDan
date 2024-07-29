package guandan;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.util.concurrent.Flow;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.Border;

public class CardBack extends JPanel{
    static int cardWidth = 55;
    static int cardHeight = 77;
    static int cardStackHeight = 8;
    static int cardStackWidth = 12;
    
    JLabel logo;
    
    public CardBack(){
        super();
        setPreferredSize(new java.awt.Dimension(cardWidth,cardHeight));

        BorderLayout bord = new BorderLayout();
        setLayout(bord);

        Border lineBorder = BorderFactory.createLineBorder(Color.gray, 1);
        setBorder(lineBorder);

        ImageIcon icon = new ImageIcon("/Users/liuyudan/Documents/Study/Java/GuanDan/pics/logo.png");
        logo = new JLabel(icon, JLabel.CENTER);
        add(logo,BorderLayout.CENTER);

        setBackground(Color.white);
        setVisible(true);

    }
}

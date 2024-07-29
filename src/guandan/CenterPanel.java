package guandan;

import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.Border;

public class CenterPanel extends JPanel implements PlayerPanel{
    JPanel panel;
    JPanel center = new JPanel();
    JLayeredPane cardPane = new JLayeredPane();
    JPanel centerPane = new JPanel();
    int cardCount = 0;
    JPanel north = new JPanel();
    JPanel south = new JPanel();
    JPanel east = new JPanel();
    JPanel west = new JPanel();

    public CenterPanel(){
        panel = new JPanel();

        BorderLayout border = new BorderLayout();
        center.setLayout(border);
        center.setPreferredSize(new Dimension(1200,300));

        centerPane.setPreferredSize(new Dimension(350,100));

        cardPane.setPreferredSize(new Dimension(CardBack.cardWidth, CardBack.cardHeight));
        int x = (centerPane.getPreferredSize().width - cardPane.getPreferredSize().width) / 2;
        int y = (centerPane.getPreferredSize().height - cardPane.getPreferredSize().height) / 2;
        cardPane.setBounds(x, y, cardPane.getPreferredSize().width, cardPane.getPreferredSize().height);
        
        centerPane.add(cardPane);
        center.add(centerPane,BorderLayout.CENTER);

        south.setPreferredSize(new Dimension(350,100));
        north.setPreferredSize(new Dimension(350,50));
        west.setPreferredSize(new Dimension(350,50));
        east.setPreferredSize(new Dimension(350,50));

        center.add(north,BorderLayout.NORTH);
        center.add(south,BorderLayout.SOUTH);
        center.add(west,BorderLayout.WEST);
        center.add(east,BorderLayout.EAST);
        
        add(center);
    }
    
    public void addCard(Card card){
        cardCount++;
        cardPane.add(card.back,BorderLayout.CENTER);
        int x = (cardPane.getWidth() - CardBack.cardWidth) / 2;
        int y = 0;
        card.back.setBounds(x,y, CardBack.cardWidth, CardBack.cardHeight);
        cardPane.revalidate();
        cardPane.repaint();
    }

    public void removeCard(Card card){
        cardPane.remove(card.back);
        cardPane.revalidate();
        cardPane.repaint();
    }

}

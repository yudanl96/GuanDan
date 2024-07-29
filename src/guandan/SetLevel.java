package guandan;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.MouseListener;
import java.util.Set;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

public class SetLevel extends JPanel {
    
    JLabel someText = new JLabel("Playing Level:", JLabel.CENTER);
    PlayerPage gui;
    Font labelFont = new Font("Bebas",Font.PLAIN,30);
    Font labelFontSmall = new Font("Bebas",Font.PLAIN,20);
    int windowWidth = CardFront.cardWidth*3;
    int windowHeight = CardFront.cardHeight + 50;
    int cardBottomPadding = 10;

    JPanel cornerPanel;
    JLabel levelText = new JLabel("Level", JLabel.CENTER);
    
    public SetLevel(Kind level){
        
        super();
        Card levelCard = new Card(new CardVal(level, Suit.Heart),level);
        JPanel levelCardSmall = smallCard(new CardFront(new CardVal(level, Suit.Heart),level));
        
        for (MouseListener ml : levelCardSmall.getMouseListeners()) {
            levelCardSmall.removeMouseListener(ml);
        }
        for (MouseListener ml : levelCard.front.getMouseListeners()) {
            levelCard.front.removeMouseListener(ml);
        }

        someText.setFont(labelFont);
        someText.setPreferredSize(new Dimension(windowWidth,windowHeight - CardFront.cardHeight-cardBottomPadding));
        setPreferredSize(new Dimension(windowWidth,windowHeight));
        setBackground(Color.WHITE);

        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        someText.setAlignmentX(CENTER_ALIGNMENT);
        add(someText);

        levelCard.front.setAlignmentX(CENTER_ALIGNMENT);
        int hor = (int) ((windowWidth-CardFront.cardWidth)*0.5);

        Border emptyBorder = BorderFactory.createEmptyBorder(0,hor,cardBottomPadding,hor);
        Border lineBorder = levelCard.front.getBorder();
        Border compoundBorder = BorderFactory.createCompoundBorder(emptyBorder, lineBorder);

        levelCard.front.setBorder(compoundBorder);
        add(levelCard.front);

        cornerPanel = new JPanel();
        cornerPanel.add(levelCardSmall);

        levelText.setFont(labelFontSmall);
        levelText.setAlignmentX(CENTER_ALIGNMENT);
        levelText.setPreferredSize(new Dimension(CardFront.cardWidth/4*3, 20));
        cornerPanel.setPreferredSize(new Dimension(CardFront.cardWidth/4*3+1, CardFront.cardHeight/3*2+21));
        cornerPanel.setBackground(Color.WHITE);
        cornerPanel.add(levelText);
        cornerPanel.setLayout(new BoxLayout(cornerPanel, BoxLayout.Y_AXIS));

    }

    private CardFront smallCard(CardFront front){
        front.setPreferredSize(new Dimension(CardFront.cardWidth/3*2,CardFront.cardHeight/3*2));
        front.kindLabel.setForeground(Color.BLACK);
        front.top.remove(front.suitLabel);
        front.top.setPreferredSize(new Dimension(CardFront.cardWidth/3*2,CardFront.cardHeight/3*2));
        front.remove(front.bottom);
        front.setBorder(new LineBorder(Color.GRAY, 1));
        return front;
    }
    
}

package guandan;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.util.ArrayList;

import javax.swing.JLayeredPane;
import javax.swing.JPanel;

public class SelectedCards extends JPanel{

    public SelectedCards(ArrayList<Card> selected, Boolean isHuman){
        Dimension paneDimension;
        int cardY;
        int cardX;
        int cardCount = 0;
        JLayeredPane stackedCards = new JLayeredPane();
        //setLayout(new FlowLayout(FlowLayout.CENTER));
        for (Card card : selected) {
            cardCount++;
            paneDimension = new java.awt.Dimension((cardCount-1) * CardFront.cardStackWidth+CardFront.cardWidth, CardFront.cardHeight);
            cardY = 0;
            cardX = (cardCount-1) * CardFront.cardStackWidth;
            if (isHuman) {
                card.setCardUI();
            }
            stackedCards.add(card.front);
            stackedCards.setLayer(card.front, cardCount);
            stackedCards.setPreferredSize(paneDimension);
            card.front.setBounds(cardX,cardY, CardBack.cardWidth, CardBack.cardHeight);
        }
        add(stackedCards);
        //setPreferredSize(new Dimension((CardFront.cardWidth+5)*selected.size()+5,CardFront.cardHeight+5));
    }

}

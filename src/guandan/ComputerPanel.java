package guandan;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.util.concurrent.Flow;

import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

public class ComputerPanel extends JPanel implements PlayerPanel{
    
    JPanel panel;
    JLayeredPane verticalPane = new JLayeredPane();
    int cardCount = 0;
    boolean isVertical;

    public ComputerPanel(boolean isVerticalIn){
        panel = new JPanel();
        isVertical = isVerticalIn;
        verticalPane.setPreferredSize(new java.awt.Dimension(CardFront.cardWidth, CardBack.cardHeight));
        //FlowLayout flow = new FlowLayout(FlowLayout.CENTER);
        //setLayout(flow);
        add(verticalPane);
    }


    public void addCard(Card card){
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                cardCount++;
                Dimension paneDimension;
                int cardX;
                int cardY;
                if (isVertical) {
                    paneDimension = new java.awt.Dimension(CardFront.cardWidth, (cardCount-1) * CardBack.cardStackHeight+CardBack.cardHeight);
                    cardX = 0;
                    cardY = (cardCount-1) * CardBack.cardStackHeight;
                }
                else{
                    paneDimension = new java.awt.Dimension((cardCount-1) * CardBack.cardStackWidth+CardBack.cardWidth, CardBack.cardHeight);
                    cardY = 0;
                    cardX = (cardCount-1) * CardBack.cardStackWidth;
                }
                
                verticalPane.setPreferredSize(paneDimension);
                verticalPane.add(card.back,cardCount);
                card.back.setBounds(cardX,cardY, CardBack.cardWidth, CardBack.cardHeight);
                verticalPane.moveToFront(card.back);
                verticalPane.revalidate();
                verticalPane.repaint();
        
            };});
        
    }

    public void removeCard(Card card){
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run(){
                
                for (Component comp : verticalPane.getComponents()) {
                    if (comp == card.back) {
                        break;
                    }
                    if (isVertical) {
                        comp.setBounds(comp.getX(),comp.getY()-CardBack.cardStackHeight,comp.getWidth(),comp.getHeight());
                    }
                    else{
                        comp.setBounds(comp.getX()-CardBack.cardStackWidth,comp.getY(),comp.getWidth(),comp.getHeight());
                    }
                }
                cardCount--;
                verticalPane.remove(card.back);
                verticalPane.revalidate();
                verticalPane.repaint();
        }});
    }
}

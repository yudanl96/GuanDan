package guandan;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.util.concurrent.Flow;

import javax.swing.BoxLayout;
import javax.swing.JComponent;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

public class HumanPanel extends JPanel implements PlayerPanel{
    JPanel panel;
    JLayeredPane[] cardsByKind = new JLayeredPane[15];
    Kind[] kindOrder = new Kind[15];
    Kind kindLevel;
    int[] cardCount = new int[15];
    boolean isVertical;

    public HumanPanel(Kind kindLevelIn){
        panel = new JPanel();
        kindLevel = kindLevelIn;

        kindOrder = Kind.kindOrder(kindLevel);
        for (int i = 0; i < cardsByKind.length; i++) {
            cardsByKind[i] = new JLayeredPane();
            cardCount[i] = 0;
        }
        FlowLayout flow = new FlowLayout(FlowLayout.CENTER);
        setLayout(flow);
        
        add(panel);
    }

    public void addCard(Card card){
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                int index = 0;
                //int countMax = 0;
                for (int i = 0; i < cardsByKind.length; i++) {
                    //panel.remove(cardsByKind[i]);
                    index = i;
                    if (kindOrder[index] == card.cardVal.cardKind){// || (card.cardVal.cardKind == Kind.RedJoker && kindOrder[index] == Kind.BlackJoker)) {
                        cardCount[index]++;
                        
                        int suitVal = (card.cardVal.cardSuit.getValue() == 0) ? 1: card.cardVal.cardSuit.getValue();

                        cardsByKind[index].add(card.front, suitVal);
                        cardsByKind[index].setLayer(card.front, suitVal);
                        setCardBounds(cardsByKind[i],suitVal,card.front);
                        int layeredPaneHeight = CardFront.cardHeight+CardFront.cardStackHeight * (cardCount[index]);
                        cardsByKind[index].setPreferredSize(new java.awt.Dimension(CardFront.cardWidth, layeredPaneHeight));
                        cardsByKind[index].moveToFront(card.front);
                        cardsByKind[index].revalidate();
                        cardsByKind[index].repaint();
                    }
                    if (cardCount[index] != 0) {
                        panel.add(cardsByKind[index]);
                        //cardsByKind[index]setBounds(0,0,panel.getPreferredSize().width,panel.getPreferredSize().height);
                    }
                }      
            };});
    }

    private void setCardBounds(JLayeredPane cardsByKindi, int suitVal, CardFront card){
        int cardX = 0;
        int cardY = 0;
        
        int count = 0;
        for (int i = 1; i <= suitVal ; i++) {
            count += cardsByKindi.getComponentCountInLayer(i);
        }
        //cardY = (count == 0)? 0 : CardFront.cardHeight + CardFront.cardStackHeight *(count - 1);
        cardY = CardFront.cardStackHeight * count;
        card.setBounds(cardX,cardY,CardFront.cardWidth,CardFront.cardHeight);
        for (int i = suitVal+1; i <= 4; i++) {
            for (Component comp : cardsByKindi.getComponentsInLayer(i)) {
                comp.setBounds(comp.getX(),comp.getY()+CardFront.cardStackHeight,comp.getWidth(),comp.getHeight());
            }
        }
        
    }

    public void removeCard(Card card){
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run(){
                outer:
                for (int i = 0; i < kindOrder.length; i++) {
            if (card.cardVal.cardKind == kindOrder[i]) {
                int suitVal = card.cardVal.cardSuit.getValue();
                
                //adjust other card position
                for(int layer = suitVal; layer <= 4; layer++){
                    Component[] comps = cardsByKind[i].getComponentsInLayer(layer);
                    loop:
                    for (int j = 0; j <comps.length ; j++) {
                        if (layer == suitVal && comps[j] == card.front) {
                            break loop;
                        }
                        comps[j].setBounds(comps[j].getX(),comps[j].getY()-CardFront.cardStackHeight,comps[j].getWidth(),comps[j].getHeight());
                    }
                }
                //remove card
                cardCount[i]--;
                cardsByKind[i].remove(card.front);

                //adjust layeredpane position
                int layeredPaneHeight = CardFront.cardHeight+CardFront.cardStackHeight * (cardCount[i]);
                cardsByKind[i].setPreferredSize(new java.awt.Dimension(CardFront.cardWidth, layeredPaneHeight));

                //update gui
                cardsByKind[i].revalidate();
                cardsByKind[i].repaint();
                if (cardCount[i] == 0 ) {
                    panel.remove(cardsByKind[i]);
                }
                break outer;
            }
        }};});
    }

}

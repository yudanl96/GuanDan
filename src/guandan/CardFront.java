package guandan;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;


public class CardFront extends JPanel{
    CardVal card;

    static int cardWidth = 55;
    static int cardHeight = 77;
    static int cardStackHeight = 25;
    //static int cardStackHeight = 15;
    static int cardStackWidth = 30;

    JPanel top = new JPanel();
    JPanel bottom = new JPanel();

    //set up top
    Font kindFont = new Font("Bebas",Font.BOLD,18);
    Font kindFontJ = new Font("Bebas",Font.BOLD,15);
    Font suitFont = new Font("Bebas",Font.BOLD,13);
    Font suitFontBottom = new Font("Aptos",Font.BOLD,25);
    JLabel kindLabel = new JLabel("",JLabel.RIGHT);
    JLabel suitLabel = new JLabel("",JLabel.LEFT);

    JLabel suitLabelBottom = new JLabel("",JLabel.RIGHT);

    Color normalColor = Color.white;
    Color clickedColor = new Color(230, 230, 230);

    public CardFront(CardVal in, Kind level){
        super();
        card = in;
        setPreferredSize(new java.awt.Dimension(cardWidth, cardHeight));
 
        GridLayout grid = new GridLayout(2,1);
        setLayout(grid);

        Color cardColor = setCardColor(card,level);

        String[] cardText = setCardText(card);

        Border lineBorder = BorderFactory.createLineBorder(Color.gray, 1);
        setBorder(lineBorder);
        
        //set up top panel
        FlowLayout flo = new FlowLayout(FlowLayout.LEFT);
        top.setLayout(flo);
        kindLabel.setText(cardText[0]);
        kindLabel.setFont(kindFont);
        kindLabel.setForeground(cardColor);
        suitLabel.setText(cardText[1]);
        suitLabel.setFont(suitFont);
        suitLabel.setForeground(cardColor);
        top.add(kindLabel);
        top.add(suitLabel);
        top.setBackground(normalColor);
        top.setPreferredSize(new java.awt.Dimension(cardWidth,(int) cardHeight/4));
        add(top);

        //set up bottom panel
        FlowLayout flo1 = new FlowLayout(FlowLayout.RIGHT);
        bottom.setLayout(flo1);
        if (cardText[1] == "" && card.cardKind == Kind.BlackJoker) {
            ImageIcon icon = new ImageIcon("/Users/liuyudan/Documents/Study/Java/GuanDan/pics/jokerBlack.png");
            suitLabelBottom.setIcon(icon);
            bottom.setBorder(new EmptyBorder(0,0,5,0));
        } 
        else if (cardText[1] == "" && card.cardKind == Kind.RedJoker) {
            ImageIcon icon = new ImageIcon("/Users/liuyudan/Documents/Study/Java/GuanDan/pics/jokerRed.png");
            suitLabelBottom.setIcon(icon);
            bottom.setBorder(new EmptyBorder(0,0,5,0));
        }
        else{
            suitLabelBottom.setText(cardText[1]);
            suitLabelBottom.setFont(suitFontBottom);
            suitLabelBottom.setForeground(cardColor);
            bottom.setBorder(new EmptyBorder(0,0,0,0));
        }
        bottom.add(suitLabelBottom);
        bottom.setBackground(normalColor);
        bottom.setPreferredSize(new java.awt.Dimension(cardWidth, (int) cardHeight/4*3));
        add(bottom);

        setBackground(normalColor);
        setVisible(true);
        
    }

    public Color setCardColor(CardVal card, Kind level){
        if(card.cardSuit == Suit.Heart && card.cardKind == level){
            return Color.ORANGE;
        }
        else if (card.cardSuit == Suit.Diamond || card.cardSuit == Suit.Heart || card.cardKind == Kind.RedJoker) {
            return Color.red;
        }
        else {
            return Color.black;
        }
    }

    public String[] setCardText(CardVal card){
        String[] resultStr = new String[2];
        if (card.cardKind != Kind.BlackJoker && card.cardKind != Kind.RedJoker) {
            resultStr[0] = card.cardKind.getName();
            resultStr[1] = card.cardSuit.getName();
        }
        else{
            resultStr[0] = "Joker";
            resultStr[1] = "";
        }
        return resultStr;
    }
    
    public static void main(String[] args) {
        //new CardFront(new CardVal(Kind.Queen, Suit.Club));
    }    

}

package guandan;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;

import javax.swing.SwingUtilities;


public class Card {
    CardFront front;
    CardBack back;
    CardVal cardVal;
    boolean isSelected = false;
    Kind level;

    private List<CardClickListener> listeners = new ArrayList<>();
    private LinkedBlockingQueue<MouseEvent> eventQueue = new LinkedBlockingQueue<MouseEvent>();

    public Card(CardVal card, Kind levelIn){
        level = levelIn;
        this.cardVal = card;
        front = new CardFront(this.cardVal,level);
        back = new CardBack();
        

        front.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                eventQueue.add(e);
            }
        });

        Thread eventProcessorThread = new Thread(() -> {
            try {
                while (true) {
                    MouseEvent e = eventQueue.take(); // Take an event from the queue
                    notifyCardClickListeners(Card.this);
                }
            } catch (InterruptedException ex) {
                Thread.currentThread().interrupt();
            }
        });
        eventProcessorThread.start();
    }

    public void addCardClickListener(CardClickListener listener) {
        listeners.add(listener);
    }

    public void removeCardClickListener(CardClickListener listener) {
        listeners.remove(listener);
    }

    private synchronized void notifyCardClickListeners(Card card) {
        for (CardClickListener listener : listeners) {
            listener.cardClicked(card);
        }
    }

    public Card(){
        this.cardVal = new CardVal(Kind.Ace, Suit.Club); 
        //front = new CardFront(this.cardVal);
        back = new CardBack();
    }

    public void setCardUI(){
        if (this.isSelected) {
            SwingUtilities.invokeLater(() -> {
                this.front.setBackground(this.front.normalColor);
                this.front.top.setBackground(this.front.normalColor);
                this.front.bottom.setBackground(this.front.normalColor);
            });
        }
        else{
            SwingUtilities.invokeLater(() -> {
                this.front.setBackground(this.front.clickedColor);
                this.front.top.setBackground(this.front.clickedColor);
                this.front.bottom.setBackground(this.front.clickedColor);
            });
        }
        
    }

    public static Comparator<Card> getComparator(Kind levelKind){
        return new Comparator<Card>() {
            @Override
            public int compare(Card c1, Card c2){
                return c1.cardVal.cardKind.compareTo(c2.cardVal.cardKind,levelKind);
            }
        };
    }

    

}

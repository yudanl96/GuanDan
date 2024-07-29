package guandan;

import javax.swing.JPanel;

public class Player {
    String name;
    CardDeck playerCards;
    PlayerPanel playerPanel;

    public Player(PlayerPanel playerPanelIn, Kind level){
        playerCards = new CardDeck(level);
        playerPanel = playerPanelIn;
        for (Card card : playerCards.cardLeft) {
            playerPanel.addCard(card);
        }
    }

    public Player(String name, PlayerPanel playerPanelIn, Kind level){
        playerCards = new CardDeck(27,level);
        this.name = name;
        playerPanel = playerPanelIn;
    }

    public static void main(String[] args) {
        //Player test = new Player(new ComputerPanels(true));
        //test.playerPanel.addCard(new Card());
    }
}

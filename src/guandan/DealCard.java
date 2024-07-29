package guandan;

import java.util.concurrent.CountDownLatch;

public class DealCard {
    PlayerPage gui;
    Player[] computers;
    Player human;
    Player fullDeck;

    Thread go;

    public DealCard(PlayerPage guiIn, Player humanIn, Player fullDeckIn, Player[] comptersIn,CountDownLatch latch){
        gui = guiIn;
        human = humanIn;
        computers = comptersIn;
        fullDeck = fullDeckIn;

        Card draw;
        int i =0;
        // Card level1 = new Card(new CardVal(guiIn.levelKind, Suit.Heart),guiIn.levelKind);
        // Card level2 = new Card(new CardVal(guiIn.levelKind, Suit.Heart),guiIn.levelKind);
        // Card level3 = new Card(new CardVal(Kind.BlackJoker, Suit.None),guiIn.levelKind);
        // Card level4 = new Card(new CardVal(Kind.BlackJoker, Suit.None),guiIn.levelKind);
        // Card level5 = new Card(new CardVal(Kind.RedJoker, Suit.None),guiIn.levelKind);
        // Card level6 = new Card(new CardVal(Kind.RedJoker, Suit.None),guiIn.levelKind);
        // human.playerPanel.addCard(level1);
        // //human.playerPanel.addCard(level2);
        // human.playerCards.addCard(level1);
        // //human.playerCards.addCard(level2);
        while (fullDeck.playerCards.cardCount != 0) {
            draw = fullDeck.playerCards.drawRandom();
            fullDeck.playerPanel.removeCard(draw);
            
            try {
                Thread.sleep(20);
            } catch (Exception e) {
                // TODO: handle exception
            }
            
            if (i == 4) {
                i = 0;
            }
            if (i == 0) {
                human.playerPanel.addCard(draw);
                human.playerCards.addCard(draw);
            }
            else{
                computers[i-1].playerPanel.addCard(draw);
                computers[i-1].playerCards.addCard(draw);
            }
            i++;
        }
        human.playerCards.cardLeft = CardDeck.arrangeCards(gui.levelKind, human.playerCards.cardLeft);
        for (Player player : comptersIn) {
            player.playerCards.cardLeft = CardDeck.arrangeCards(gui.levelKind, player.playerCards.cardLeft);
        }
        latch.countDown();
    }


    // public static void main(String[] args) {
    //     Kind levelKind = Kind.drawKind();
    //     Player human = new Player("test", new HumanPanel(levelKind));
    //     Player full = new Player(new CenterPanel(),levelKind);
    //     Player[] computers = new Player[3];
    //     computers[0] = new Player("computer", new ComputerPanel(true));
    //     computers[2] = new Player("computer", new ComputerPanel(true));
    //     computers[1] = new Player("computer", new ComputerPanel(false));
    //     CountDownLatch latch = new CountDownLatch(1);
    //     PlayerPage gui = new PlayerPage(levelKind,human,full,computers,latch);
    //     //new DealCard(gui, human, full,computers);
    // }
}

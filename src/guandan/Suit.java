package guandan;

public enum Suit {
    Spade(1,"♠"),
    Diamond(2,"♦"),
    Club(3,"♣"),
    Heart(4,"♥"),
    None(0,"None");

    private int val;
    private String nameStr;

    Suit(int val, String str){
        this.val = val;
        this.nameStr = str;
    }

    public int getValue() {return val;}
    public String getName() {return nameStr;}

    public static Suit fromString(String nameStr){
        for(Suit suit : Suit.values()){
            if(suit.nameStr == nameStr){
                return suit;
            }
        }
        throw new IllegalArgumentException ("No Suit with string: "+ nameStr);
    }

    public int compare(Suit anotherSuit) {
        // this < anotherSuit return -1
        // this = anotherSuit return 0
        // this > anotherSu8it return 1
        int thisVal = this.val;
        int anotherVal = anotherSuit.getValue();

        return (thisVal == anotherVal)? 0 : ((thisVal < anotherVal)?  -1 :  1);
    }
}

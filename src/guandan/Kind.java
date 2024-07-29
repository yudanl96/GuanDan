package guandan;

import java.util.Comparator;

public enum Kind {
    Two(2,"2"),
    Three(3,"3"),
    Four(4,"4"),
    Five(5,"5"),
    Six(6,"6"),
    Seven(7,"7"),
    Eight(8,"8"),
    Nine(9,"9"),
    Ten(10,"10"),
    Jack(11,"J"),
    Queen(12,"Q"),
    King(13,"K"),
    Ace(14,"A"),
    BlackJoker(100,"Black Joker"),
    RedJoker(101,"Red Joker");

    private final int value;
    private final String nameStr;
    
    Kind(int i, String str){
        this.value = i;
        this.nameStr = str;
    }

    public int getValue() { return value;}
    public String getName() { return nameStr;}

    public int compareTo(Kind anotherKind, Kind level) {
        // this < anotherKind return -1
        // this = anotherKind return 0
        // this > anotherKind return 1
        int thisVal = this.value;
        int anotherVal = anotherKind.getValue();
        if (this == level) {
            thisVal = 15; // setting the level card to be biggest except none
        }
        if (anotherKind == level){
            anotherVal = 15;
        }
        return (thisVal == anotherVal)? 0 : ((thisVal < anotherVal)?  -1 :  1);
    }

    public static Comparator<Kind> getComparator(Kind levelKind){
        return new Comparator<Kind>() {
            @Override
            public int compare(Kind k1, Kind k2){
                return k1.compareTo(k2,levelKind);
            }
        };
    }

    public static Kind fromValue(int val){
        for(Kind kind: Kind.values()){
            if(kind.value == val){
                return kind;
            }
        }
        throw new IllegalArgumentException ("No Kind with value "+ val);
    }

    public static Kind[] kindOrder(Kind kindLevel){
        Kind[] order = Kind.values();
        Boolean isLevelReached = false;
        for(int i=0; i < order.length-2; i++){
            if(isLevelReached){
                order[i-1] = order[i];
            }
            if(kindLevel == order[i]){
                isLevelReached = true;
            }
        }
        order[order.length-3] = kindLevel;
        return order;
    }

    public static Kind drawKind(){
        int val;
        do{
            val = 2+(int) (Math.random() * 13);
        }while(Kind.fromValue(val) == BlackJoker || Kind.fromValue(val) == RedJoker);
        return Kind.fromValue(val);
    }

    public static void main(String[] args) {
    }
}

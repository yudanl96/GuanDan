package guandan;

import java.util.EventListener;

public interface CardClickListener extends EventListener {
    void cardClicked(Card card);
}

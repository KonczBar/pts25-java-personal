package sk.uniba.fmph.dcs.terra_futura;

import java.util.Map;

public interface GameObserver {
    void notifyAll(Map<Integer, String> newState);
}

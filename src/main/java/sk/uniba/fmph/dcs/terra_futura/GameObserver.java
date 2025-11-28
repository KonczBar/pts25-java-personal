package sk.uniba.fmph.dcs.terra_futura;

import sk.uniba.fmph.dcs.terra_futura.Exceptions.InvalidObserverException;

import java.util.HashMap;
import java.util.Map;

public class GameObserver {
    private Map<Integer, Observer> observers = new HashMap<>();

    GameObserver(Map<Integer, Observer> observers){
        this.observers = observers;
    }

    public void notifyAll(Map<Integer, String> newState){
        for (Integer id : newState.keySet()){
            try{
                observers.get(id).notify(newState.get(id));
            }
            catch (Exception e) {
                throw new InvalidObserverException("Given nonexistent observer id");
            }
        }
    }
}

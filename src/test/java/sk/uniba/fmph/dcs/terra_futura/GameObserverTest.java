package sk.uniba.fmph.dcs.terra_futura;

import org.junit.Before;
import org.junit.Test;
import sk.uniba.fmph.dcs.terra_futura.Exceptions.InvalidObserverException;

import static org.junit.Assert.*;

import java.util.*;


class FakeIdObserver implements Observer {
    public int Id;
    public int notifyCount = 0;
    public String lastMessage = null;

    FakeIdObserver(Integer Id){
        this.Id = Id;
    }

    @Override
    public void notify(String GameState) {
        notifyCount++;
        lastMessage = GameState;
    }
}

public class GameObserverTest {
    private Map<Integer, Observer> map;
    private GameObserver gameObserver;
    private FakeIdObserver obs1;
    private FakeIdObserver obs2;

    @Before
    public void setUp(){
        map = new HashMap<>();
        obs1 = new FakeIdObserver(1);
        map.put(1, obs1);
        obs2 = new FakeIdObserver(2);
        map.put(2, obs2);
        this.gameObserver = new GameObserver(map);
    }

    @Test
    public void AllCorrectObservers() {
        Map<Integer, String> messages = new HashMap<>();
        String message1 = "message to Observer number 1";
        String message2 = "message to Observer number 2";
        messages.put(1, message1);
        gameObserver.notifyAll(messages);
        assertEquals(1, obs1.notifyCount);
        assertEquals(message1, obs1.lastMessage);


        messages.put(2, message2);
        gameObserver.notifyAll(messages);

        assertEquals(2, obs1.notifyCount);
        assertEquals(1, obs2.notifyCount);

        assertEquals(message1, obs1.lastMessage);
        assertEquals(message2, obs2.lastMessage);
    }

    @Test(expected = InvalidObserverException.class)
    public void MissingObserverException(){
        Map<Integer, String> messages = new HashMap<>();
        String message10 = "message to nonexistant Observer";
        messages.put(10, message10);
        gameObserver.notifyAll(messages);
    }
}
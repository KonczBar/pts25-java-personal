package sk.uniba.fmph.dcs.terra_futura;

import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;

import java.util.Map;

import static org.junit.Assert.*;

public class CardTest {
    private Card startingCard;
    private Card pollutedCard;
    private Card richCard;

    @Before
    public void setUp(){
        Map<Resource, Integer> resources = Map.of(
                Resource.GREEN, 0,
                Resource.RED, 1,
                Resource.YELLOW, 1,
                Resource.BULB, 0,
                Resource.GEAR, 0,
                Resource.CAR, 0,
                Resource.MONEY, 1,
                Resource.POLLUTION, 0
        );
        startingCard = new Card(resources, 0);

        resources = Map.of(
                Resource.GREEN, 0,
                Resource.RED, 1,
                Resource.YELLOW, 0,
                Resource.BULB, 0,
                Resource.GEAR, 0,
                Resource.CAR, 0,
                Resource.MONEY, 0,
                Resource.POLLUTION, 2
        );
        pollutedCard = new Card(resources, 1);

        resources = Map.of(
                Resource.GREEN, 0,
                Resource.RED, 0,
                Resource.YELLOW, 0,
                Resource.BULB, 4,
                Resource.GEAR, 3,
                Resource.CAR, 4,
                Resource.MONEY, 0,
                Resource.POLLUTION, 1
        );
        richCard = new Card(resources, 1);
    }

    private void checkStateString(Card testedCard, String expectedMap, int expectedPollutionMax){
        System.out.println("Nas JSON:\n");
        System.out.println(startingCard.state());
        JSONObject obj = new JSONObject(testedCard.state());
        JSONArray arr =  obj.getJSONArray("Resources");
        StringBuilder s = new StringBuilder();
        for (int i = 0; i < arr.length(); i++) {
            JSONObject pair = arr.getJSONObject(i);
            s.append(String.format("(%s, %s)", pair.get("Resource"), pair.getInt("Amount")));
        }

        assertEquals(expectedMap, s.toString());
        assertEquals(expectedPollutionMax, obj.getInt("Max pollution"));
    }

    @Test
    public void checkStates(){
        checkStateString(pollutedCard, "(GREEN, 0)(RED, 1)(YELLOW, 0)(BULB, 0)" +
                "(GEAR, 0)(CAR, 0)(MONEY, 0)(POLLUTION, 2)", 1);
    }

    @Test
    public void testPuttingOnPollutedCard(){
        Map<Resource, Integer> resources = Map.of(
                Resource.GREEN, 1,
                Resource.RED, 1,
                Resource.YELLOW, 1,
                Resource.BULB, 0,
                Resource.GEAR, 0,
                Resource.CAR, 0,
                Resource.MONEY, 0,
                Resource.POLLUTION, 0
        );
        assertFalse(pollutedCard.canPutResources(resources));
        try{
            pollutedCard.putResources(resources);
            fail("Put resources on polluted card");
        } catch (InvalidMoveException ignored){}
    }

    @Test
    public void testTakingFromPollutedCard(){
        Map<Resource, Integer> resources = Map.of(
                Resource.GREEN, 0,
                Resource.RED, 1,
                Resource.YELLOW, 0,
                Resource.BULB, 0,
                Resource.GEAR, 0,
                Resource.CAR, 0,
                Resource.MONEY, 0,
                Resource.POLLUTION, 0
        );
        assertFalse(pollutedCard.canGetResources(resources));
        try{
            pollutedCard.getResources(resources);
            fail("Took resources from polluted card");
        } catch (InvalidMoveException ignored){}
    }

    @Test
    public void testTakingTooManyResources(){
        Map<Resource, Integer> resources = Map.of(
                Resource.GREEN, 1,
                Resource.RED, 0,
                Resource.YELLOW, 0,
                Resource.BULB, 0,
                Resource.GEAR, 0,
                Resource.CAR, 0,
                Resource.MONEY, 0,
                Resource.POLLUTION, 0
        );
        assertFalse(startingCard.canGetResources(resources));
        try{
            startingCard.getResources(resources);
            fail("Took resources from card, which were not available");
        } catch (InvalidMoveException ignored){}
    }

    @Test
    public void testTakingResources(){
        checkStateString(richCard, "(GREEN, 0)(RED, 0)(YELLOW, 0)(BULB, 4)" +
                "(GEAR, 3)(CAR, 4)(MONEY, 0)(POLLUTION, 1)", 1);
        Map<Resource, Integer> resources = Map.of(
                Resource.GREEN, 0,
                Resource.RED, 0,
                Resource.YELLOW, 0,
                Resource.BULB, 1,
                Resource.GEAR, 0,
                Resource.CAR, 1,
                Resource.MONEY, 0,
                Resource.POLLUTION, 0
        );
        richCard.getResources(resources);
        checkStateString(richCard, "(GREEN, 0)(RED, 0)(YELLOW, 0)(BULB, 3)" +
                "(GEAR, 3)(CAR, 3)(MONEY, 0)(POLLUTION, 1)", 1);

        checkStateString(startingCard, "(GREEN, 0)(RED, 1)(YELLOW, 1)(BULB, 0)" +
                "(GEAR, 0)(CAR, 0)(MONEY, 1)(POLLUTION, 0)", 0);
        resources = Map.of(
                Resource.GREEN, 0,
                Resource.RED, 1,
                Resource.YELLOW, 1,
                Resource.BULB, 0,
                Resource.GEAR, 0,
                Resource.CAR, 0,
                Resource.MONEY, 0,
                Resource.POLLUTION, 0
        );
        startingCard.getResources(resources);
        checkStateString(startingCard, "(GREEN, 0)(RED, 0)(YELLOW, 0)(BULB, 0)" +
                "(GEAR, 0)(CAR, 0)(MONEY, 1)(POLLUTION, 0)", 0);

    }

    @Test
    public void testPuttingResources(){
        checkStateString(richCard, "(GREEN, 0)(RED, 0)(YELLOW, 0)(BULB, 4)" +
                "(GEAR, 3)(CAR, 4)(MONEY, 0)(POLLUTION, 1)", 1);
        Map<Resource, Integer> resources = Map.of(
                Resource.GREEN, 0,
                Resource.RED, 0,
                Resource.YELLOW, 0,
                Resource.BULB, 1,
                Resource.GEAR, 0,
                Resource.CAR, 1,
                Resource.MONEY, 0,
                Resource.POLLUTION, 0
        );
        richCard.putResources(resources);
        checkStateString(richCard, "(GREEN, 0)(RED, 0)(YELLOW, 0)(BULB, 5)" +
                "(GEAR, 3)(CAR, 5)(MONEY, 0)(POLLUTION, 1)", 1);

        checkStateString(startingCard, "(GREEN, 0)(RED, 1)(YELLOW, 1)(BULB, 0)" +
                "(GEAR, 0)(CAR, 0)(MONEY, 1)(POLLUTION, 0)", 0);
        resources = Map.of(
                Resource.GREEN, 0,
                Resource.RED, 0,
                Resource.YELLOW, 0,
                Resource.BULB, 1,
                Resource.GEAR, 0,
                Resource.CAR, 0,
                Resource.MONEY, 1,
                Resource.POLLUTION, 0
        );
        startingCard.putResources(resources);
        checkStateString(startingCard, "(GREEN, 0)(RED, 1)(YELLOW, 1)(BULB, 1)" +
                "(GEAR, 0)(CAR, 0)(MONEY, 2)(POLLUTION, 0)", 0);

    }

    @Test
    public void testTakingPollutionAndSomethingElse(){
        Map<Resource, Integer> resources = Map.of(
                Resource.GREEN, 0,
                Resource.RED, 0,
                Resource.YELLOW, 0,
                Resource.BULB, 1,
                Resource.GEAR, 0,
                Resource.CAR, 1,
                Resource.MONEY, 0,
                Resource.POLLUTION, 1
        );
        try{
            richCard.canGetResources(resources);
            fail("Can't get both normal resources and pollution");
        } catch (InvalidMoveException ignored) {}
    }

    @Test
    public void testActuallyGetResourcesIsNewInstance(){
        checkStateString(startingCard, "(GREEN, 0)(RED, 1)(YELLOW, 1)(BULB, 0)" +
                "(GEAR, 0)(CAR, 0)(MONEY, 1)(POLLUTION, 0)", 0);
        Map<Resource, Integer> resources = startingCard.actuallyGetResources();
        for (Resource resource : Resource.values()){
            resources.replace(resource, 0);
        }
        checkStateString(startingCard, "(GREEN, 0)(RED, 1)(YELLOW, 1)(BULB, 0)" +
                "(GEAR, 0)(CAR, 0)(MONEY, 1)(POLLUTION, 0)", 0);
    }
}

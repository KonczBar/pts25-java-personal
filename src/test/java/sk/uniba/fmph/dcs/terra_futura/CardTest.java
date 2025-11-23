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
                Resource.Green, 0,
                Resource.Red, 1,
                Resource.Yellow, 1,
                Resource.Bulb, 0,
                Resource.Gear, 0,
                Resource.Car, 0,
                Resource.Money, 1,
                Resource.Pollution, 0
        );
        startingCard = new Card(resources, 0);

        resources = Map.of(
                Resource.Green, 0,
                Resource.Red, 1,
                Resource.Yellow, 0,
                Resource.Bulb, 0,
                Resource.Gear, 0,
                Resource.Car, 0,
                Resource.Money, 0,
                Resource.Pollution, 2
        );
        pollutedCard = new Card(resources, 1);

        resources = Map.of(
                Resource.Green, 0,
                Resource.Red, 0,
                Resource.Yellow, 0,
                Resource.Bulb, 4,
                Resource.Gear, 3,
                Resource.Car, 4,
                Resource.Money, 0,
                Resource.Pollution, 1
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
        checkStateString(pollutedCard, "(Green, 0)(Red, 1)(Yellow, 0)(Bulb, 0)" +
                "(Gear, 0)(Car, 0)(Money, 0)(Pollution, 2)", 1);
    }

    @Test
    public void testPuttingOnPollutedCard(){
        Map<Resource, Integer> resources = Map.of(
                Resource.Green, 1,
                Resource.Red, 1,
                Resource.Yellow, 1,
                Resource.Bulb, 0,
                Resource.Gear, 0,
                Resource.Car, 0,
                Resource.Money, 0,
                Resource.Pollution, 0
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
                Resource.Green, 0,
                Resource.Red, 1,
                Resource.Yellow, 0,
                Resource.Bulb, 0,
                Resource.Gear, 0,
                Resource.Car, 0,
                Resource.Money, 0,
                Resource.Pollution, 0
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
                Resource.Green, 1,
                Resource.Red, 0,
                Resource.Yellow, 0,
                Resource.Bulb, 0,
                Resource.Gear, 0,
                Resource.Car, 0,
                Resource.Money, 0,
                Resource.Pollution, 0
        );
        assertFalse(startingCard.canGetResources(resources));
        try{
            startingCard.getResources(resources);
            fail("Took resources from card, which were not available");
        } catch (InvalidMoveException ignored){}
    }

    @Test
    public void testTakingResources(){
        checkStateString(richCard, "(Green, 0)(Red, 0)(Yellow, 0)(Bulb, 4)" +
                "(Gear, 3)(Car, 4)(Money, 0)(Pollution, 1)", 1);
        Map<Resource, Integer> resources = Map.of(
                Resource.Green, 0,
                Resource.Red, 0,
                Resource.Yellow, 0,
                Resource.Bulb, 1,
                Resource.Gear, 0,
                Resource.Car, 1,
                Resource.Money, 0,
                Resource.Pollution, 0
        );
        richCard.getResources(resources);
        checkStateString(richCard, "(Green, 0)(Red, 0)(Yellow, 0)(Bulb, 3)" +
                "(Gear, 3)(Car, 3)(Money, 0)(Pollution, 1)", 1);

        checkStateString(startingCard, "(Green, 0)(Red, 1)(Yellow, 1)(Bulb, 0)" +
                "(Gear, 0)(Car, 0)(Money, 1)(Pollution, 0)", 0);
        resources = Map.of(
                Resource.Green, 0,
                Resource.Red, 1,
                Resource.Yellow, 1,
                Resource.Bulb, 0,
                Resource.Gear, 0,
                Resource.Car, 0,
                Resource.Money, 0,
                Resource.Pollution, 0
        );
        startingCard.getResources(resources);
        checkStateString(startingCard, "(Green, 0)(Red, 0)(Yellow, 0)(Bulb, 0)" +
                "(Gear, 0)(Car, 0)(Money, 1)(Pollution, 0)", 0);

    }

    @Test
    public void testPuttingResources(){
        checkStateString(richCard, "(Green, 0)(Red, 0)(Yellow, 0)(Bulb, 4)" +
                "(Gear, 3)(Car, 4)(Money, 0)(Pollution, 1)", 1);
        Map<Resource, Integer> resources = Map.of(
                Resource.Green, 0,
                Resource.Red, 0,
                Resource.Yellow, 0,
                Resource.Bulb, 1,
                Resource.Gear, 0,
                Resource.Car, 1,
                Resource.Money, 0,
                Resource.Pollution, 0
        );
        richCard.putResources(resources);
        checkStateString(richCard, "(Green, 0)(Red, 0)(Yellow, 0)(Bulb, 5)" +
                "(Gear, 3)(Car, 5)(Money, 0)(Pollution, 1)", 1);

        checkStateString(startingCard, "(Green, 0)(Red, 1)(Yellow, 1)(Bulb, 0)" +
                "(Gear, 0)(Car, 0)(Money, 1)(Pollution, 0)", 0);
        resources = Map.of(
                Resource.Green, 0,
                Resource.Red, 0,
                Resource.Yellow, 0,
                Resource.Bulb, 1,
                Resource.Gear, 0,
                Resource.Car, 0,
                Resource.Money, 1,
                Resource.Pollution, 0
        );
        startingCard.putResources(resources);
        checkStateString(startingCard, "(Green, 0)(Red, 1)(Yellow, 1)(Bulb, 1)" +
                "(Gear, 0)(Car, 0)(Money, 2)(Pollution, 0)", 0);

    }

    @Test
    public void testTakingPollutionAndSomethingElse(){
        Map<Resource, Integer> resources = Map.of(
                Resource.Green, 0,
                Resource.Red, 0,
                Resource.Yellow, 0,
                Resource.Bulb, 1,
                Resource.Gear, 0,
                Resource.Car, 1,
                Resource.Money, 0,
                Resource.Pollution, 1
        );
        try{
            richCard.canGetResources(resources);
            fail("Can't get both normal resources and pollution");
        } catch (InvalidMoveException ignored) {}
    }

    @Test
    public void testActuallyGetResourcesIsNewInstance(){
        checkStateString(startingCard, "(Green, 0)(Red, 1)(Yellow, 1)(Bulb, 0)" +
                "(Gear, 0)(Car, 0)(Money, 1)(Pollution, 0)", 0);
        Map<Resource, Integer> resources = startingCard.actuallyGetResources();
        for (Resource resource : Resource.values()){
            resources.replace(resource, 0);
        }
        checkStateString(startingCard, "(Green, 0)(Red, 1)(Yellow, 1)(Bulb, 0)" +
                "(Gear, 0)(Car, 0)(Money, 1)(Pollution, 0)", 0);
    }
}

package sk.uniba.fmph.dcs.terra_futura;

import org.junit.Before;
import org.junit.Test;
import sk.uniba.fmph.dcs.terra_futura.Effect.Effect;
import sk.uniba.fmph.dcs.terra_futura.Effect.EffectArbitrary;
import sk.uniba.fmph.dcs.terra_futura.Effect.EffectArbitraryBasic;
import sk.uniba.fmph.dcs.terra_futura.Effect.EffectTransformationFixed;

import java.util.Map;

import static org.junit.Assert.assertFalse;

public class EffectTest {
    private Effect basicToAdvanced;
    private Effect advancedToAdvanced;
    private Effect createBasic;

    private Effect arbitraryBasic;

    private Effect fixedNonPolluted;
    private Effect fixedPolluted;

    @Before
    public void setUp(){
        basicToAdvanced = new EffectArbitrary(false, 2, true, 1, false, 1);
        advancedToAdvanced = new EffectArbitrary(true, 1, true, 1, false, 0);
        createBasic = new EffectArbitrary(false, 0, false, 1, true, 0);
        Map<Resource, Integer> input = Map.of(Resource.RED, 1, Resource.YELLOW, 2);
        Map<Resource, Integer> output = Map.of(Resource.CAR, 1);
        fixedNonPolluted = new EffectTransformationFixed(input, output, 0);
        input = Map.of(Resource.RED, 1, Resource.YELLOW, 1);
        output = Map.of(Resource.CAR, 1);
        fixedPolluted = new EffectTransformationFixed(input, output, 1);
        output = Map.of(Resource.BULB, 1);
        arbitraryBasic = new EffectArbitraryBasic(2, output, 1);
    }

    @Test
    public void testBasicToAdvanced(){
        Map<Resource, Integer> input = Map.of(Resource.RED, 1, Resource.YELLOW, 1);
        Map<Resource, Integer> output = Map.of(Resource.CAR, 1);
        assert(basicToAdvanced.check(input, output, 1));

        input = Map.of(Resource.GREEN, 1, Resource.YELLOW, 1);
        output = Map.of(Resource.BULB, 1);
        assert(basicToAdvanced.check(input, output, 1));
        assertFalse(basicToAdvanced.check(input, output, 0));

        input = Map.of(Resource.GREEN, 2, Resource.YELLOW, 2);
        assertFalse(basicToAdvanced.check(input, output, 1));

        input = Map.of(Resource.GREEN, 1, Resource.YELLOW, 1);
        output = Map.of(Resource.BULB, 2);
        assertFalse(basicToAdvanced.check(input, output, 1));
    }

    @Test
    public void testAdvancedToAdvanced(){
        Map<Resource, Integer> input = Map.of(Resource.CAR, 1);
        Map<Resource, Integer> output = Map.of(Resource.CAR, 1);
        assert(advancedToAdvanced.check(input, output, 0));
        assertFalse(advancedToAdvanced.check(input, output, 1));

        input = Map.of(Resource.BULB, 1);
        assert(advancedToAdvanced.check(input, output, 0));
        assertFalse(advancedToAdvanced.check(input, output, 1));

        input = Map.of(Resource.RED, 1);
        output = Map.of(Resource.CAR, 1);
        assertFalse(advancedToAdvanced.check(input, output, 0));
    }

    @Test
    public void testCreateBasic(){
        Map<Resource, Integer> input = Map.of(Resource.CAR, 1);
        Map<Resource, Integer> output = Map.of(Resource.RED, 1);
        assertFalse(createBasic.check(input, output, 0));

        input = Map.of();
        assert(createBasic.check(input, output, 0));
        assertFalse(createBasic.check(input, output, 1));

        output = Map.of(Resource.MONEY, 1);
        assert(createBasic.check(input, output, 0));

        output = Map.of(Resource.MONEY, 1, Resource.YELLOW, 1);
        assertFalse(createBasic.check(input, output, 0));
    }

    @Test
    public void testArbitraryBasic(){
        Map<Resource, Integer> input = Map.of(Resource.RED, 1, Resource.YELLOW, 1);
        Map<Resource, Integer> output = Map.of(Resource.BULB, 1);
        assert(arbitraryBasic.check(input, output, 1));
        assertFalse(arbitraryBasic.check(input, output, 2));

        input = Map.of(Resource.RED, 2);
        output = Map.of(Resource.BULB, 1);
        assert(arbitraryBasic.check(input, output, 1));

        output = Map.of(Resource.CAR, 1);
        assertFalse(arbitraryBasic.check(input, output, 1));
    }

    @Test
    public void testFixedNonPolluted(){
        Map<Resource, Integer> input = Map.of(Resource.RED, 1, Resource.YELLOW, 2);
        Map<Resource, Integer> output = Map.of(Resource.CAR, 1);
        assert(fixedNonPolluted.check(input, output, 0));
        assertFalse(fixedNonPolluted.check(input, output, 1));

        output = Map.of(Resource.BULB, 1);
        assertFalse(fixedNonPolluted.check(input, output, 0));
    }

    @Test
    public void testFixedPolluted(){
        Map<Resource, Integer> input = Map.of(Resource.RED, 1, Resource.YELLOW, 1);
        Map<Resource, Integer> output = Map.of(Resource.CAR, 1);
        assert(fixedPolluted.check(input, output, 1));
        assertFalse(fixedPolluted.check(input, output, 2));

        input = Map.of(Resource.RED, 1, Resource.YELLOW, 1);
        output = Map.of(Resource.GEAR, 1);
        assertFalse(fixedPolluted.check(input, output, 1));
    }
}

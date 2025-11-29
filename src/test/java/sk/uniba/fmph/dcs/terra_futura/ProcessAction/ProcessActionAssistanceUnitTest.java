package sk.uniba.fmph.dcs.terra_futura.ProcessAction;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import sk.uniba.fmph.dcs.terra_futura.Card;
import sk.uniba.fmph.dcs.terra_futura.Grid;
import sk.uniba.fmph.dcs.terra_futura.GridPosition;
import sk.uniba.fmph.dcs.terra_futura.Resource;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.fail;
import static org.junit.Assert.*;

public class ProcessActionAssistanceUnitTest {
    @Test
    public void activateCardTest(){
        ProcessActionAssistance paa = new ProcessActionAssistance();
        Card cardTaker = Mockito.mock(Card.class);
        Card cardGiver = Mockito.mock(Card.class);
        Card cardPolluted = Mockito.mock(Card.class);
        Card cardAssister = Mockito.mock(Card.class);
        // add hasAssistance, change check to checkLower
        Mockito.when(cardTaker.canPutResources(Mockito.any())).thenReturn(true);
        Mockito.when(cardTaker.canGetResources(Mockito.any())).thenReturn(false);
        Mockito.when(cardTaker.checkLower(Mockito.any(), Mockito.any(), Mockito.anyInt())).thenReturn(true);
        Mockito.when(cardTaker.hasAssistance()).thenReturn(true);

        Mockito.when(cardGiver.canPutResources(Mockito.any())).thenReturn(false);
        Mockito.when(cardGiver.canGetResources(Mockito.any())).thenReturn(true);
        Mockito.when(cardGiver.hasAssistance()).thenReturn(false);

        Mockito.when(cardPolluted.canPutResources(Mockito.any())).thenReturn(false);
        Mockito.when(cardPolluted.canGetResources(Mockito.any())).thenReturn(false);
        Mockito.when(cardPolluted.checkLower(Mockito.any(), Mockito.any(), Mockito.anyInt())).thenReturn(false);
        Mockito.when(cardPolluted.hasAssistance()).thenReturn(false);

        Mockito.when(cardAssister.canPutResources(Mockito.any())).thenReturn(true);
        Mockito.when(cardAssister.canGetResources(Mockito.any())).thenReturn(false);
        Mockito.when(cardAssister.checkLower(Mockito.any(), Mockito.any(), Mockito.anyInt())).thenReturn(true);
        Mockito.when(cardAssister.hasAssistance()).thenReturn(false);

        Grid grid = Mockito.mock(Grid.class);
        GridPosition takerPos = new GridPosition(1, 1);
        GridPosition giverPos = new GridPosition(0, 0);
        GridPosition pollutionPos = new GridPosition(0, 1);
        Mockito.when(grid.getCard(giverPos)).thenReturn(Optional.of(cardGiver));
        Mockito.when(grid.getCard(takerPos)).thenReturn(Optional.of(cardTaker));
        Mockito.when(grid.getCard(pollutionPos)).thenReturn(Optional.of(cardPolluted));

        ArrayList<Pair<Resource, GridPosition>> inputs = new ArrayList<>();
        inputs.add(new ImmutablePair<>(Resource.GREEN, giverPos));
        inputs.add(new ImmutablePair<>(Resource.GREEN, giverPos));
        ArrayList<Pair<Resource, GridPosition>> outputs = new ArrayList<>();
        outputs.add(new ImmutablePair<>(Resource.CAR, takerPos));
        ArrayList<GridPosition> pollution = new ArrayList<>(List.of(takerPos));
        pollution.add(takerPos);

        try {
            paa.activateCard(cardTaker, grid, cardAssister, inputs, outputs, pollution);
            System.out.println("Performed successfully");
        } catch (RuntimeException e) {
            System.out.println(e.getMessage());
            fail();
        }

        inputs.add(new ImmutablePair<>(Resource.RED, pollutionPos));
        try {
            paa.activateCard(cardTaker, grid, cardAssister, inputs, outputs, pollution);
        } catch (RuntimeException e) {
            assertEquals("There is not enough resources on card at (0, 1)", e.getMessage());
            System.out.println(e.getMessage());
        }

        inputs.removeLast();
        outputs.add(new ImmutablePair<>(Resource.GREEN, pollutionPos));
        try {
            paa.activateCard(cardTaker, grid, cardAssister, inputs, outputs, pollution);
        } catch (RuntimeException e) {
            assertEquals("Gain resources are not being put on the card with assistance", e.getMessage());
            System.out.println(e.getMessage());
        }

        outputs.removeFirst();
        try {
            paa.activateCard(cardPolluted, grid, cardAssister, inputs, outputs, pollution);
        } catch (RuntimeException e) {
            assertEquals("The given card cannot perform assistance", e.getMessage());
            System.out.println(e.getMessage());
        }
    }
}
package sk.uniba.fmph.dcs.terra_futura.ProcessAction;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.junit.Test;
import org.mockito.Mockito;
import sk.uniba.fmph.dcs.terra_futura.Card;
import sk.uniba.fmph.dcs.terra_futura.Grid;
import sk.uniba.fmph.dcs.terra_futura.GridPosition;
import sk.uniba.fmph.dcs.terra_futura.Resource;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static junit.framework.TestCase.*;

public class ProcessActionUnitTest {
    @Test
    public void activateCardTest(){
        ProcessAction processAction = new ProcessAction();
        Card cardTaker = Mockito.mock(Card.class);
        Card cardGiver = Mockito.mock(Card.class);
        Card cardPolluted = Mockito.mock(Card.class);
//        needs canPutResources, canGetResources and check
        Mockito.when(cardTaker.canPutResources(Mockito.any())).thenReturn(true);
        Mockito.when(cardTaker.canGetResources(Mockito.any())).thenReturn(false);
        Mockito.when(cardTaker.check(Mockito.any(), Mockito.any(), Mockito.anyInt())).thenReturn(true);

        Mockito.when(cardGiver.canPutResources(Mockito.any())).thenReturn(false);
        Mockito.when(cardGiver.canGetResources(Mockito.any())).thenReturn(true);

        Mockito.when(cardPolluted.canPutResources(Mockito.any())).thenReturn(false);
        Mockito.when(cardPolluted.canGetResources(Mockito.any())).thenReturn(false);
        Mockito.when(cardPolluted.check(Mockito.any(), Mockito.any(), Mockito.anyInt())).thenReturn(false);

        Grid grid = Mockito.mock(Grid.class);
//        needs getCard
        GridPosition takerPosition = new GridPosition(1, 1);
        GridPosition giverPosition = new GridPosition(0, 0);
        GridPosition pollutionPosition = new GridPosition(0, 1);
        Mockito.when(grid.getCard(giverPosition)).thenReturn(Optional.of(cardGiver));
        Mockito.when(grid.getCard(takerPosition)).thenReturn(Optional.of(cardTaker));
        Mockito.when(grid.getCard(pollutionPosition)).thenReturn(Optional.of(cardPolluted));

        ArrayList<Pair<Resource, GridPosition>> inputs = new ArrayList<>();
        inputs.add(new ImmutablePair<>(Resource.GREEN, giverPosition));
        ArrayList<Pair<Resource, GridPosition>> outputs = new ArrayList<>();
        outputs.add(new ImmutablePair<>(Resource.CAR, takerPosition));
        ArrayList<GridPosition> pollution = new ArrayList<>(List.of(takerPosition));
        pollution.add(takerPosition);

        try {
            processAction.activateCard(cardTaker, grid, inputs, outputs, pollution);
            System.out.println("Performed successfully");
        } catch (RuntimeException e) {
            System.out.println(e.getMessage());
            fail();
        }

        inputs.add(new ImmutablePair<>(Resource.RED, pollutionPosition));
        try {
            processAction.activateCard(cardTaker, grid, inputs, outputs, pollution);
        } catch (RuntimeException e) {
            assertEquals("There is not enough resources on card at (0, 1)", e.getMessage());
            System.out.println(e.getMessage());
        }

        inputs.removeLast();
        outputs.add(new ImmutablePair<>(Resource.GREEN, pollutionPosition));
        try {
            processAction.activateCard(cardTaker, grid, inputs, outputs, pollution);
        } catch (RuntimeException e) {
            assertEquals("Gain resources are not being put on the activated card", e.getMessage());
            System.out.println(e.getMessage());
        }

        outputs.removeFirst();
        try {
            processAction.activateCard(cardPolluted, grid, inputs, outputs, pollution);
        } catch (RuntimeException e) {
            assertEquals("That effect cannot be performed on this card", e.getMessage());
            System.out.println(e.getMessage());
        }
    }
}
package sk.uniba.fmph.dcs.terra_futura;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.junit.Test;
import org.mockito.Mockito;
import sk.uniba.fmph.dcs.terra_futura.ProcessAction.ProcessAction;

import java.util.ArrayList;
import java.util.Optional;

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
        GridPosition takerPos = new GridPosition(1, 1);
        GridPosition giverPos = new GridPosition(0, 0);
        GridPosition pollutionPos = new GridPosition(0, 1);
        Mockito.when(grid.getCard(giverPos)).thenReturn(Optional.of(cardGiver));
        Mockito.when(grid.getCard(takerPos)).thenReturn(Optional.of(cardTaker));
        Mockito.when(grid.getCard(pollutionPos)).thenReturn(Optional.of(cardPolluted));

        ArrayList<Pair<Resource, GridPosition>> inputs = new ArrayList<>();
        inputs.add(new ImmutablePair<>(Resource.GREEN, giverPos));
        ArrayList<Pair<Resource, GridPosition>> outputs = new ArrayList<>();
        outputs.add(new ImmutablePair<>(Resource.CAR, takerPos));
        ArrayList<GridPosition> pollution = new ArrayList<>();
        pollution.add(takerPos);

        try {
            processAction.activateCard(cardTaker, grid, inputs, outputs, pollution);
            System.out.println("Performed successfully");
        } catch (RuntimeException e) {
            System.out.println(e.getMessage());
        }

        inputs.add(new ImmutablePair<>(Resource.RED, pollutionPos));
        try {
            processAction.activateCard(cardTaker, grid, inputs, outputs, pollution);
        } catch (RuntimeException e) {
            System.out.println(e.getMessage());
        }

        inputs.removeLast();
        outputs.add(new ImmutablePair<>(Resource.GREEN, pollutionPos));
        try {
            processAction.activateCard(cardTaker, grid, inputs, outputs, pollution);
        } catch (RuntimeException e) {
            System.out.println(e.getMessage());
        }

        outputs.removeFirst();
        try {
            processAction.activateCard(cardPolluted, grid, inputs, outputs, pollution);
        } catch (RuntimeException e) {
            System.out.println(e.getMessage());
        }
    }
}
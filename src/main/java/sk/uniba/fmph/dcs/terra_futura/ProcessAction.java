package sk.uniba.fmph.dcs.terra_futura;

import org.apache.commons.lang3.tuple.Pair;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProcessAction {
    private void moveResources(Grid grid, List<Pair<Resource, GridPosition>> inputs, List<Pair<Resource, GridPosition>> outputs, List<GridPosition> pollution){
        for (Pair<Resource, GridPosition> i : inputs){
            grid.getCard(i.getValue()).ifPresentOrElse((cardGiver) -> {
                        if (cardGiver.canGetResources(Map.of(i.getKey(), 1))){
                            cardGiver.getResources(Map.of(i.getKey(), 1));
                        }
                        else
                            throw new InvalidMove("Not enough resources");
                    },
                    () -> {throw new InvalidMove("No card on such position");});
        }
        for (Pair<Resource, GridPosition> i : outputs){
            grid.getCard(i.getValue()).ifPresentOrElse((cardTaker) -> {
                        if (cardTaker.canPutResources(Map.of(i.getKey(), 1))){
                            cardTaker.putResources(Map.of(i.getKey(), 1));
                        }
                        else
                            throw new InvalidMove("Can't put resources on this card");
                    },
                    () -> {throw new InvalidMove("No card on such position");});
        }
        for (GridPosition i : pollution){
            grid.getCard(i).ifPresentOrElse((cardPolluted) -> {
                        if (cardPolluted.canPutPollution()){
                            cardPolluted.putPollution(1);
                        }
                        else
                            throw new InvalidMove("Can't put pollution on this card");
                    },
                    () -> {throw new InvalidMove("No card on such position");});
        }
    }

    public boolean activateCard(Card card, Grid grid, List<Pair<Resource, GridPosition>> inputs, List<Pair<Resource, GridPosition>> outputs, List<GridPosition> pollution){
        Map<Resource, Integer> inputResources = new HashMap<>();
        Map<Resource, Integer> outputResources = new HashMap<>();

        for(Pair<Resource, GridPosition> i : inputs){
            if (inputResources.containsKey(i.getKey()))
                inputResources.replace(i.getKey(), inputResources.get(i.getKey()) + 1);
            else
                inputResources.put(i.getKey(), 1);
        }

        for(Pair<Resource, GridPosition> i : outputs){
            if (outputResources.containsKey(i.getKey()))
                outputResources.replace(i.getKey(), outputResources.get(i.getKey()) + 1);
            else
                outputResources.put(i.getKey(), 1);
        }

        if (card.check(inputResources, outputResources, pollution.size()) || card.checkLower(inputResources, outputResources, pollution.size())){
            try {
                moveResources(grid, inputs, outputs, pollution);
            } catch (InvalidMove e) {
                throw new RuntimeException(e);
            }
            return true;
        }
        return false;
    }
}

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
                            throw new InvalidMoveException("There is not enough " + i.getKey() + " resource on card at (" + i.getValue().getX() + ", " + i.getValue().getY() + ")");
                    },
                    () -> {throw new InvalidMoveException("No card on position (" + i.getValue().getX() + ", " + i.getValue().getY() + ")");});
        }
        for (Pair<Resource, GridPosition> i : outputs){
            grid.getCard(i.getValue()).ifPresentOrElse((cardTaker) -> {
                        if (cardTaker.canPutResources(Map.of(i.getKey(), 1))){
                            cardTaker.putResources(Map.of(i.getKey(), 1));
                        }
                        else
                            throw new InvalidMoveException("Can't put " + i.getKey() +  " resource on card at (" + i.getValue().getX() + ", " + i.getValue().getY() + ")");
                    },
                    () -> {throw new InvalidMoveException("No card on position (" + i.getValue().getX() + ", " + i.getValue().getY() + ")");});
        }
        for (GridPosition i : pollution){
            grid.getCard(i).ifPresentOrElse((cardPolluted) -> {
                        if (cardPolluted.canPutResources(Map.of(Resource.POLLUTION, 1))){
                            cardPolluted.putResources(Map.of(Resource.POLLUTION, 1));
                        }
                        else
                            throw new InvalidMoveException("Can't put POLLUTION on the card at (" + i.getX() + ", " + i.getY() + ")");
                    },
                    () -> {throw new InvalidMoveException("No card on position (" + i.getX() + ", " + i.getY() + ")");});
        }
    }

    public void activateCard(Card card, Grid grid, List<Pair<Resource, GridPosition>> inputs, List<Pair<Resource, GridPosition>> outputs, List<GridPosition> pollution){
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

        if (card.check(inputResources, outputResources, pollution.size())){
            moveResources(grid, inputs, outputs, pollution);
        }
        else{
            throw new InvalidMoveException("That effect cannot be performed on this card");
        }
    }
}

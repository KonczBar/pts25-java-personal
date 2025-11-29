package sk.uniba.fmph.dcs.terra_futura.ProcessAction;

import org.apache.commons.lang3.tuple.Pair;
import sk.uniba.fmph.dcs.terra_futura.*;
import sk.uniba.fmph.dcs.terra_futura.Exceptions.InvalidMoveException;

import java.util.*;

public class GeneralProcessAction {
    private void moveResources(Grid grid, List<Pair<Resource, GridPosition>> inputs,
                               List<Pair<Resource, GridPosition>> outputs, List<GridPosition> pollution){
        // Check on presence of resources
        Set<GridPosition> inputPoses = new HashSet<>();
        for (Pair<Resource, GridPosition> i : inputs){
            inputPoses.add(i.getValue());
        }
        for (GridPosition j : inputPoses) {
            Map<Resource, Integer> giverCardResources = new HashMap<>();
            for (Pair<Resource, GridPosition> i : inputs) {
                if (i.getValue().equals(j)){
                    if (giverCardResources.containsKey(i.getKey()))
                        giverCardResources.replace(i.getKey(), giverCardResources.get(i.getKey()) + 1);
                    else
                        giverCardResources.put(i.getKey(), 1);
                }
            }

            grid.getCard(j).ifPresentOrElse((cardGiver) -> {
                        if (!cardGiver.canGetResources(giverCardResources)) {
                            throw new InvalidMoveException("There is not enough resources on card at " +
                                    "(" + j.getX() + ", " + j.getY() + ")");
                        }
                    },
                    () -> {
                        throw new InvalidMoveException("No card on position (" + j.getX() + ", " + j.getY() + ")");
                    });
        }

        Set<GridPosition> outputPoses = new HashSet<>();
        for (Pair<Resource, GridPosition> i : outputs){
            outputPoses.add(i.getValue());
        }
        for (GridPosition j : outputPoses){
            Map<Resource, Integer> takerCardResources = new HashMap<>();
            for (Pair<Resource, GridPosition> i : outputs) {
                if (i.getValue().equals(j)){
                    if (takerCardResources.containsKey(i.getKey()))
                        takerCardResources.replace(i.getKey(), takerCardResources.get(i.getKey()) + 1);
                    else
                        takerCardResources.put(i.getKey(), 1);
                }
            }

            grid.getCard(j).ifPresentOrElse((cardGiver) -> {
                        if (!cardGiver.canPutResources(takerCardResources)) {
                            throw new InvalidMoveException("Can't put resources on card at " +
                                    "(" + j.getX() + ", " + j.getY() + ")");
                        }
                    },
                    () -> {
                        throw new InvalidMoveException("No card on position (" + j.getX() + ", " + j.getY() + ")");
                    });
        }

        Map<GridPosition, Integer> pollutionMap = new HashMap<>();
        for (GridPosition i : pollution){
            if (pollutionMap.containsKey(i))
                pollutionMap.replace(i, pollutionMap.get(i) + 1);
            else
                pollutionMap.put(i, 1);
        }
        for (GridPosition i : pollutionMap.keySet()){
            grid.getCard(i).ifPresentOrElse((cardPolluted) -> {
                        if (!cardPolluted.canPutResources(Map.of(Resource.POLLUTION, pollutionMap.get(i)))){
                            throw new InvalidMoveException("Can't put " + pollutionMap.get(i) +
                                    " POLLUTION on the card at (" + i.getX() + ", " + i.getY() + ")");
                        }
                    },
                    () -> {throw new InvalidMoveException("No card on position (" + i.getX() + ", " + i.getY() + ")");});
        }

        // Actually take the resources
        for (Pair<Resource, GridPosition> i : inputs){
            grid.getCard(i.getValue()).ifPresentOrElse((cardGiver) ->
                            cardGiver.getResources(Map.of(i.getKey(), 1)),
                    () -> {});
        }
        for (Pair<Resource, GridPosition> i : outputs){
            grid.getCard(i.getValue()).ifPresentOrElse((cardTaker) ->
                            cardTaker.putResources(Map.of(i.getKey(), 1)),
                    () -> {});
        }
        for (GridPosition i : pollution){
            grid.getCard(i).ifPresentOrElse((cardPolluted) ->
                            cardPolluted.putResources(Map.of(Resource.POLLUTION, 1)),
                    () -> {});
        }
    }

    public void activateCard(Card card, Grid grid, List<Pair<Resource, GridPosition>> inputs,
                             List<Pair<Resource, GridPosition>> outputs,
                             List<GridPosition> pollution, boolean isAssistance) throws InvalidMoveException{
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
            if (i.getKey().equals(Resource.POLLUTION)) {
                pollution.add(i.getValue());
                outputs.remove(i);
            }
        }

        if (isAssistance){
            if (card.checkLower(inputResources, outputResources, pollution.size())) {
                moveResources(grid, inputs, outputs, pollution);
            } else {
                if (card.check(inputResources, outputResources, pollution.size())){
                    throw new InvalidMoveException("The non-bottom effect cannot be performed via assistance");
                }
                else
                    throw new InvalidMoveException("That effect cannot be performed on this card ");
            }
        }
        else {
            if (card.check(inputResources, outputResources, pollution.size())) {
                moveResources(grid, inputs, outputs, pollution);
            } else {
                throw new InvalidMoveException("That effect cannot be performed on this card");
            }
        }
    }
}

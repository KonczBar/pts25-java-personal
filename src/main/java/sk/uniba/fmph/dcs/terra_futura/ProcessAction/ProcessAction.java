package sk.uniba.fmph.dcs.terra_futura.ProcessAction;

import org.apache.commons.lang3.tuple.Pair;
import sk.uniba.fmph.dcs.terra_futura.*;
import sk.uniba.fmph.dcs.terra_futura.Exceptions.InvalidMoveException;

import java.util.List;
import java.util.Optional;

public class ProcessAction {
    public void activateCard(Card card, Grid grid, List<Pair<Resource, GridPosition>> inputs,
                             List<Pair<Resource, GridPosition>> outputs,
                             List<GridPosition> pollution) throws InvalidMoveException {
        for(Pair<Resource, GridPosition> i : inputs){
            if (i.getKey().equals(Resource.POLLUTION)){
                for (GridPosition j : pollution){
                    if (!grid.getCard(j).equals(Optional.of(card)))
                        throw new InvalidMoveException("Putting pollution not on card with pollution transfer");
                }
            }
        }

        for(Pair<Resource, GridPosition> i : outputs){
            if (!grid.getCard(i.getValue()).equals(Optional.of(card)) && !i.getKey().equals(Resource.POLLUTION))
                throw new InvalidMoveException("Gain resources are not being put on the activated card");
        }

        GeneralProcessAction gpa = new GeneralProcessAction();
        gpa.activateCard(card, grid, inputs, outputs, pollution, false);
    }
}

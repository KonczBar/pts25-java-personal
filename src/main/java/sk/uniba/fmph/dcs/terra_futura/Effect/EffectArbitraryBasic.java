package sk.uniba.fmph.dcs.terra_futura.Effect;

import sk.uniba.fmph.dcs.terra_futura.Resource;

import java.util.HashMap;
import java.util.Map;

/**
 * Makes basic arbitrary resources into output
 */
public class EffectArbitraryBasic implements Effect {
    private final int input;
    private final Map<Resource, Integer> output;
    private final int pollution;

    public EffectArbitraryBasic(int input, Map<Resource, Integer> output, int pollution){
        this.output = new HashMap<>(output);
        for (Resource resource : Resource.values()){
            if (!output.containsKey(resource)){
                this.output.put(resource, 0);
            }
        }
        this.input = input;
        this.pollution = pollution;
    }

    @Override
    public boolean check(Map<Resource, Integer> input, Map<Resource, Integer> output, int pollution) {
        int inputAmount = 0;
        for (Resource resource : Resource.values()){
            if (input.containsKey(resource)){
                if (input.get(resource) > 0 && resource != Resource.GREEN && resource != Resource.RED && resource != Resource.YELLOW){
                    return false;
                }
                inputAmount += input.get(resource);
            }

            if (!Effect.checkResource(resource, this.output, output)){
                return false;
            }
        }
        if (this.input != inputAmount){
            return false;
        }
        return this.pollution == pollution;
    }

    @Override
    public boolean hasAssistance() {
        return false;
    }
}

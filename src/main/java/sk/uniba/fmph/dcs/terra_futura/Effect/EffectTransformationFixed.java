package sk.uniba.fmph.dcs.terra_futura.Effect;

import sk.uniba.fmph.dcs.terra_futura.Resource;

import java.util.HashMap;
import java.util.Map;

/**
 * Specific resources into other specific resources, input can be empty
 */
public class EffectTransformationFixed implements Effect {
    private final Map<Resource, Integer> input;
    private final Map<Resource, Integer> output;
    private final int pollution;

    public EffectTransformationFixed(Map<Resource, Integer> input, Map<Resource, Integer> output, int pollution){
        this.input = new HashMap<>(input);
        this.output = new HashMap<>(output);
        for (Resource resource : Resource.values()){
            if (!input.containsKey(resource)){
                this.input.put(resource, 0);
            }
            if (!output.containsKey(resource)){
                this.output.put(resource, 0);
            }
        }
        this.pollution = pollution;
    }

    @Override
    public boolean check(Map<Resource, Integer> input, Map<Resource, Integer> output, int pollution) {
        for (Resource resource : Resource.values()){
            if (!Effect.checkResource(resource, this.input, input)){
                return false;
            }
            if (!Effect.checkResource(resource, this.output, output)){
                return false;
            }
        }
        return this.pollution == pollution;
    }

    @Override
    public boolean hasAssistance() {
        return false;
    }
}

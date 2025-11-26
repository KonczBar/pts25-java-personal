package sk.uniba.fmph.dcs.terra_futura;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class EffectArbitraryBasic implements Effect{
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

    /*@Override
    public String state() {
        JSONArray effectInput = new JSONArray();
        JSONArray effectOutput = new JSONArray();
        pair.put("Resource", "Arbitrary Basic");
        pair.put("Amount", input);
        effectInput.put(pair);
        for (Resource resource : Resource.values()) {
            JSONObject pair = new JSONObject();
            pair.put("Resource", resource);
            amount = input.getOrDefault(resource, 0);
            pair.put("Amount", amount);
            effectInput.put(pair);
            pair = new JSONObject();
            pair.put("Resource", resource);
            int amount = output.getOrDefault(resource, 0);
            pair.put("Amount", amount);
            effectOutput.put(pair);
        }
        JSONObject result = new JSONObject();
        result.put("Input", input);
        result.put("Output", effectOutput);
        result.put("Pollution", pollution);
        return result.toString();
    }*/
}

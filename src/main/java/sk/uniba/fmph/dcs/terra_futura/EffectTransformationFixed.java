package sk.uniba.fmph.dcs.terra_futura;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class EffectTransformationFixed implements Effect{
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
            if (!input.containsKey(resource)){
                if (this.input.get(resource) > 0){
                    return false;
                }
            }
            else{
                if (!Objects.equals(this.input.get(resource), input.get(resource))){
                    return false;
                }
            }

            if (!output.containsKey(resource)){
                if (this.output.get(resource) > 0){
                    return false;
                }
            }
            else{
                if (resource == Resource.POLLUTION){
                    throw new InvalidMoveException("Pollution in output - put it in pollution int");
                }
                if (!Objects.equals(this.output.get(resource), output.get(resource))){
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public boolean hasAssistance() {
        return false;
    }

    @Override
    public String state() {
        JSONArray effectInput = new JSONArray();
        JSONArray effectOutput = new JSONArray();
        for (Resource resource : Resource.values()) {
            JSONObject pair = new JSONObject();
            pair.put("Resource", resource);
            int amount = input.getOrDefault(resource, 0);
            pair.put("Amount", amount);
            effectInput.put(pair);
            pair = new JSONObject();
            pair.put("Resource", resource);
            amount = output.getOrDefault(resource, 0);
            pair.put("Amount", amount);
            effectInput.put(pair);
        }
        JSONObject result = new JSONObject();
        result.put("Input", effectInput);
        result.put("Output", effectOutput);
        result.put("Pollution", pollution);
        return result.toString();
    }
}

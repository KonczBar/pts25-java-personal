package sk.uniba.fmph.dcs.terra_futura;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Map;

public class EffectCreateBasic implements Effect{
    private final Resource resource;

    public EffectCreateBasic(Resource resource){
        this.resource = resource;
    }

    @Override
    public boolean check(Map<Resource, Integer> input, Map<Resource, Integer> output, int pollution) {
        for (Resource resource : Resource.values()) {
            if (input.containsKey(resource) && input.get(resource) > 0) {
                return false;
            }
            if (resource == this.resource && (!output.containsKey(resource) || output.get(resource) != 1)){
                return false;
            }
            if (output.containsKey(resource)) {
                if (resource != this.resource && input.get(resource) > 0){
                    return false;
                }
            }
        }
        return pollution == 0;
    }

    @Override
    public boolean hasAssistance() {
        return false;
    }

    /*@Override
    public String state() {
        JSONArray effectInput = new JSONArray();
        JSONArray effectOutput = new JSONArray();
        JSONObject pair = new JSONObject();
        pair.put("Resource", resource);
        pair.put("Amount", 1);
        effectOutput.put(pair);
        JSONObject result = new JSONObject();
        result.put("Input", effectInput);
        result.put("Output", effectOutput);
        result.put("Pollution", 0);
        return result.toString();
    }*/
}

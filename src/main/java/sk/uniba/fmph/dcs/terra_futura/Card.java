package sk.uniba.fmph.dcs.terra_futura;

import org.json.JSONArray;
import org.json.JSONObject;
import java.util.HashMap;
import java.util.Map;

public class Card {
    private final Map<Resource, Integer> resources;
    private final int pollutionSpacesL;
    private Effect upperEffect;
    private Effect lowerEffect;

    //for testing purposes only
    public Card(int pollutionMax){
        resources = new HashMap<>();
        for (Resource resource : Resource.values()){
            resources.put(resource, 0);
        }
        pollutionSpacesL = pollutionMax;
    }

    //for testing purposes only
    public Card(Map<Resource, Integer> resources, int pollutionMax){
        this.resources = new HashMap<>(resources);
        pollutionSpacesL = pollutionMax;
    }

    //for testing purposes only
    public Card(Map<Resource, Integer> resources, int pollutionMax, Effect upperEffect, Effect lowerEffect){
        this.resources = new HashMap<>(resources);
        for (Resource resource : Resource.values()){
            if (!this.resources.containsKey(resource)){
                this.resources.put(resource, 0);
            }
        }
        pollutionSpacesL = pollutionMax;
        this.upperEffect = upperEffect;
        this.lowerEffect = lowerEffect;
    }

    public Card(int pollutionMax, Effect upperEffect, Effect lowerEffect){
        resources = new HashMap<>();
        for (Resource resource : Resource.values()){
            resources.put(resource, 0);
        }
        pollutionSpacesL = pollutionMax;
        this.upperEffect = upperEffect;
        this.lowerEffect = lowerEffect;
    }

    //You are free to modify returned map
    public Map<Resource, Integer> actuallyGetResources(){
        return new HashMap<>(this.resources);
    }

    public boolean isPolluted(){
        return resources.get(Resource.POLLUTION) > pollutionSpacesL;
    }

    public boolean canGetResources(Map<Resource, Integer> resources) throws InvalidMoveException{
        if (resources.containsKey(Resource.POLLUTION) && resources.get(Resource.POLLUTION) > 0){
            for (Resource resource : resources.keySet()){
                if (resources.get(resource) != 0 && resource != Resource.POLLUTION){
                    throw new InvalidMoveException("Checking if removing pollution and" +
                            " other resources is possible at once (it's not)\nPOLLUTION: "
                            + resources.get(Resource.POLLUTION) + "\n" + resource + ": "
                            + resources.get(resource) + "\n");
                }
            }
            return this.resources.get(Resource.POLLUTION) >= resources.get(Resource.POLLUTION);
        }
        if (this.resources.containsKey(Resource.POLLUTION) && pollutionSpacesL < this.resources.get(Resource.POLLUTION)){
            return false;
        }
        for (Resource resource : resources.keySet()){
            if (this.resources.containsKey(resource) && resources.get(resource) > this.resources.get(resource)){
                return false;
            }
            else if (!this.resources.containsKey(resource) && resources.get(resource) > 0){
                return false;
            }
        }
        return true;
    }

    public void getResources(Map<Resource, Integer> resources) throws InvalidMoveException{
        if (!canGetResources(resources)){
            throw new InvalidMoveException("Taking resources from card, which cannot give those resources");
        }
        for (Resource resource : resources.keySet()){
            this.resources.replace(resource, this.resources.get(resource) - resources.get(resource));
            if (this.resources.get(resource) < 0){
                throw new InvalidMoveException("Tried to remove too many resources\n" +
                        "Resource: " + resource + "\n" +
                        "On card: " + this.resources.get(resource) + resources.get(resource) + "\n" +
                        "Tried to remove: " + resources.get(resource));
            }
        }
    }

    public boolean canPutResources(Map<Resource, Integer> resources){
        if (resources.containsKey(Resource.POLLUTION) && resources.get(Resource.POLLUTION) > 0){
            return this.resources.get(Resource.POLLUTION) + resources.get(Resource.POLLUTION) <= pollutionSpacesL + 1;
        }
        if (!this.resources.containsKey(Resource.POLLUTION)){
            return true;
        }
        return this.resources.get(Resource.POLLUTION) <= pollutionSpacesL;
    }

    public void putResources(Map<Resource, Integer> resources) throws InvalidMoveException{
        if (!canPutResources(resources)){
            throw new InvalidMoveException("Cannot put resources on this card");
        }
        for (Resource resource : resources.keySet()){
            if (!this.resources.containsKey(resource)){
                this.resources.put(resource, resources.get(resource));
                continue;
            }
            this.resources.replace(resource, this.resources.get(resource) + resources.get(resource));
        }
    }

    public boolean check(Map<Resource, Integer> input, Map<Resource, Integer> output, int pollution){
        if (upperEffect != null) {
            if (upperEffect.check(input, output, pollution)){
                return true;
            }
        }
        if (lowerEffect != null){
            if (lowerEffect.check(input, output, pollution)){
                return true;
            }
        }
        return false;
    }

    public boolean hasAssistance(){
        if (upperEffect != null && upperEffect.hasAssistance()){
            return true;
        }
        if (lowerEffect != null && lowerEffect.hasAssistance()){
            return true;
        }
        return false;
    }

    public String state(){
        JSONArray resourceList = new JSONArray();
        for (Resource resource : Resource.values()) {
            JSONObject pair = new JSONObject();
            pair.put("Resource", resource);
            int amount = resources.getOrDefault(resource, 0);
            pair.put("Amount", amount);
            resourceList.put(pair);
        }
        JSONObject result = new JSONObject();
        result.put("Resources", resourceList);
        result.put("Max pollution", pollutionSpacesL);
        return result.toString();
    }
}

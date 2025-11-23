package sk.uniba.fmph.dcs.terra_futura;

/*
resources:Resource[]
pollutionSpacesL int
new: upperEffect Effect
new: lowerEffect Effect
new: actuallyGetResources(): Map<Resource, Integer>
canGetResources(resources: Map<Resource, Integer>): bool
getResources(resources: Map<Resource, Integer>)
canPutResources(resources: Map<Resource, Integer>): bool
putResources(resources: Map<Resource, Integer>)
check(input: Map<Resource, Integer>, output: Map<Resource, Integer>, pollution:int): bool
checkLower(input: Map<Resource, Integer>, output: Map<Resource, Integer>, pollution:int): bool
hasAssistance():bool
state(): string
 */
import org.json.JSONArray;
import org.json.JSONObject;
import java.util.HashMap;
import java.util.Map;

public class Card {
    private Map<Resource, Integer> resources;
    private final int pollutionSpacesL;
    private Effect upperEffect;
    private Effect lowerEffect;

    //for testing purposes
    public Card(int pollutionMax){
        resources = new HashMap<>();
        for (Resource resource : Resource.values()){
            resources.put(resource, 0);
        }
        pollutionSpacesL = pollutionMax;
    }

    //for testing purposes
    public Card(Map<Resource, Integer> resources, int pollutionMax){
        this.resources = new HashMap<>(resources);
        pollutionSpacesL = pollutionMax;
    }

    //for testing purposes
    public Card(Map<Resource, Integer> resources, int pollutionMax, Effect upperEffect, Effect lowerEffect){
        this.resources = new HashMap<>(resources);
        pollutionSpacesL = pollutionMax;
        this.upperEffect = upperEffect;
        this.lowerEffect = lowerEffect;
    }

    public Card(int pollutionMax, Effect upperEffect, Effect lowerEffect){
        pollutionSpacesL = pollutionMax;
        this.upperEffect = upperEffect;
        this.lowerEffect = lowerEffect;
    }

    //You are free to modify returned map
    public Map<Resource, Integer> actuallyGetResources(){
        return new HashMap<>(this.resources);
    }

    public boolean canGetResources(Map<Resource, Integer> resources) throws InvalidMoveException{
        if (resources.get(Resource.Pollution) > 0){
            for (Resource resource : Resource.values()){
                if (resources.get(resource) != 0 && resource != Resource.Pollution){
                    throw new InvalidMoveException("Checking if removing pollution and" +
                            " other resources is possible at once (it's not)\nPollution: "
                            + resources.get(Resource.Pollution) + "\n" + resource + ": "
                            + resources.get(resource) + "\n");
                }
            }
            return this.resources.get(Resource.Pollution) >= resources.get(Resource.Pollution);
        }
        if (pollutionSpacesL < this.resources.get(Resource.Pollution)){
            return false;
        }
        for (Resource resource : Resource.values()){
            if (resources.get(resource) > this.resources.get(resource)){
                return false;
            }
        }
        return true;
    }

    public void getResources(Map<Resource, Integer> resources) throws InvalidMoveException{
        if (!canGetResources(resources)){
            throw new InvalidMoveException("Taking resources from card, which cannot give those resources");
        }
        for (Resource resource : Resource.values()){
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
        if (resources.get(Resource.Pollution) > 0){
            return this.resources.get(Resource.Pollution) + resources.get(Resource.Pollution) <= pollutionSpacesL + 1;
        }
        return this.resources.get(Resource.Pollution) <= pollutionSpacesL;
    }

    public void putResources(Map<Resource, Integer> resources) throws InvalidMoveException{
        if (!canPutResources(resources)){
            throw new InvalidMoveException("Cannot put resources on this card");
        }
        for (Resource resource : Resource.values()){
            this.resources.replace(resource, this.resources.get(resource) + resources.get(resource));
        }
    }

    public boolean check(Map<Resource, Integer> input, Map<Resource, Integer> output, int pollution){
        return upperEffect.check(input, output, pollution);
    }

    public boolean checkLower(Map<Resource, Integer> input, Map<Resource, Integer> output, int pollution){
        return lowerEffect.check(input, output, pollution);
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
            pair.put("Amount", resources.get(resource));
            resourceList.put(pair);
        }
        JSONObject result = new JSONObject();
        result.put("Resources", resourceList);
        result.put("Max pollution", pollutionSpacesL);
        return result.toString();
    }
}

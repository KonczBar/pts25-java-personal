package sk.uniba.fmph.dcs.terra_futura.Effect;

import sk.uniba.fmph.dcs.terra_futura.InvalidMoveException;
import sk.uniba.fmph.dcs.terra_futura.Resource;

import java.util.Map;
import java.util.Objects;


public interface Effect {
    boolean check(Map<Resource, Integer> input, Map<Resource, Integer> output, int pollution);
    boolean hasAssistance();
    //String state();

    public static boolean checkResource(Resource resource, Map<Resource, Integer> effectMap, Map<Resource, Integer> map){
        if (!map.containsKey(resource)){
            return effectMap.get(resource) <= 0;
        }
        else{
            if (resource == Resource.POLLUTION){
                throw new InvalidMoveException("Pollution in output - put it in pollution int");
            }
            return Objects.equals(effectMap.get(resource), map.get(resource));
        }
    }
}

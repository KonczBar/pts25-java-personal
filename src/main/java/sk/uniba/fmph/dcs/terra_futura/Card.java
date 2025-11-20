package sk.uniba.fmph.dcs.terra_futura;

import java.util.List;
import java.util.Map;

public interface Card {
    // resources:Map<Resource, Integer>
    // pollutionSpacesL: int
    void putPollution(int pollution);
    boolean canPutPollution();
    boolean hasPollution();
    Map<Resource, Integer> actuallyGetResources();
    boolean canGetResources(Map<Resource, Integer> resources);
    void getResources(Map<Resource, Integer> resources); // remove resources, actually
    boolean canPutResources(Map<Resource, Integer> resources);
    void putResources(Map<Resource, Integer> resources);
    boolean check(Map<Resource, Integer> input, Map<Resource, Integer> output, int pollution);
    boolean checkLower(Map<Resource, Integer> input, Map<Resource, Integer> output, int pollution);
    boolean hasAssistance();
    String state();
}

package sk.uniba.fmph.dcs.terra_futura;

import java.util.Map;

public interface Effect {
    boolean check(Map<Resource, Integer> input, Map<Resource, Integer> output, int pollution);
    boolean hasAssistance();
    String state();

}

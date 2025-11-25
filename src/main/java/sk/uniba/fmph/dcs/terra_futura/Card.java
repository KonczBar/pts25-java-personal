package sk.uniba.fmph.dcs.terra_futura;

import java.util.Map;

public interface Card {
    boolean isPolluted();
    Map<Resource, Integer> actuallyGetResources();
}
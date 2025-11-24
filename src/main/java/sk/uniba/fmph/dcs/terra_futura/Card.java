package sk.uniba.fmph.dcs.terra_futura;

import java.util.List;
import java.util.Map;

public interface Card {
    boolean hasPollution();
    boolean isPolluted();
    Map<Resource, Integer> actuallyGetResources();
}
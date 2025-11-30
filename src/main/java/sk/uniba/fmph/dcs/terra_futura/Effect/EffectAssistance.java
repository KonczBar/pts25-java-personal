package sk.uniba.fmph.dcs.terra_futura.Effect;

import sk.uniba.fmph.dcs.terra_futura.Resource;

import java.util.Map;

/*
Effect Assistance. Exists in one shape only, mostly handled via ProcessActionAssistance
 */
public class EffectAssistance implements Effect{
    @Override
    public boolean check(Map<Resource, Integer> input, Map<Resource, Integer> output, int pollution) {
        return false;
    }

    @Override
    public boolean hasAssistance() {
        return true;
    }
}

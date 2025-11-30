package sk.uniba.fmph.dcs.terra_futura.Effect;

import sk.uniba.fmph.dcs.terra_futura.Resource;

import java.util.Map;

public class EffectPollutionTransfer implements Effect {
    @Override
    public boolean check(Map<Resource, Integer> input, Map<Resource, Integer> output, int pollution) {
        int inputPollution = 0;

        if(!output.isEmpty()) return false;

        for(Resource resource : input.keySet()) {
            if(resource != Resource.POLLUTION && input.get(resource) > 0) return false;
            if(resource == Resource.POLLUTION) inputPollution = input.get(resource);
        }

        return pollution == inputPollution;
    }

    @Override
    public boolean hasAssistance() {
        return false;
    }
}

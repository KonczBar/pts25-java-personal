package sk.uniba.fmph.dcs.terra_futura.Effect;

import sk.uniba.fmph.dcs.terra_futura.Resource;

import java.util.Map;

public class EffectCreateArbitraryBasic implements Effect{
    private final int input;
    private final boolean includeMoney;

    public EffectCreateArbitraryBasic(int amount, boolean includeMoney){
        this.input = amount;
        this.includeMoney = includeMoney;
    }

    @Override
    public boolean check(Map<Resource, Integer> input, Map<Resource, Integer> output, int pollution) {
        int inputAmount = 0;
        for (Resource resource : Resource.values()){
            if (input.containsKey(resource) && input.get(resource) > 0){
                return false;
            }
            if (output.containsKey(resource)){
                if (resource == Resource.MONEY && includeMoney){
                    inputAmount += output.get(resource);
                }
                else if(resource == Resource.GREEN || resource == Resource.RED || resource == Resource.YELLOW){
                    inputAmount += output.get(resource);
                }
                else if(output.get(resource) > 0){
                    return false;
                }
            }
        }
        if (inputAmount != this.input){
            return false;
        }
        return pollution == 0;
    }

    @Override
    public boolean hasAssistance() {
        return false;
    }
}

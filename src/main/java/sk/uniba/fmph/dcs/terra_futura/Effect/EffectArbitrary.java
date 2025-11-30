package sk.uniba.fmph.dcs.terra_futura.Effect;

import sk.uniba.fmph.dcs.terra_futura.Resource;

import java.util.Map;

/**
 * Effect, which has both input and output as arbitrary (or no input and arbitrary output)
 */
public class EffectArbitrary implements Effect{
    private final boolean inputAdvanced;
    private final int input;
    private final boolean outputAdvanced;
    private final int output;
    private final int pollution;
    private final boolean moneyOutput;

    private int inputAmount;
    private int outputAmount;

    public EffectArbitrary(boolean inputAdvanced, int input, boolean outputAdvanced, int output, boolean moneyOutput,  int pollution){
        this.input = input;
        this.inputAdvanced = inputAdvanced;
        this.output = output;
        this.outputAdvanced = outputAdvanced;
        this.pollution = pollution;
        this.moneyOutput = moneyOutput;
    }

    private boolean checkMap(Map<Resource, Integer> map, boolean input, Resource resource){
        if (map.containsKey(resource)){
            if ((input && inputAdvanced) || (!input && outputAdvanced)){
                if (resource == Resource.BULB || resource == Resource.CAR || resource == Resource.GEAR){
                    if (input){
                        inputAmount += map.get(resource);
                    }
                    else{
                        outputAmount += map.get(resource);
                    }
                }
                else{
                    return map.get(resource) <= 0;
                }
            }
            else{
                if (resource == Resource.GREEN || resource == Resource.RED || resource == Resource.YELLOW || (moneyOutput && resource == Resource.MONEY)){
                    if (input){
                        inputAmount += map.get(resource);
                    }
                    else{
                        outputAmount += map.get(resource);
                    }
                }
                else{
                    return map.get(resource) <= 0;
                }
            }
        }
        return true;
    }

    @Override
    public boolean check(Map<Resource, Integer> input, Map<Resource, Integer> output, int pollution) {
        inputAmount = 0;
        outputAmount = 0;
        for (Resource resource : Resource.values()){
            if (!checkMap(input, true, resource)){
                return false;
            }
            if (!checkMap(output, false, resource)){
                return false;
            }
        }
        if (inputAmount != this.input){
            return false;
        }
        if (outputAmount != this.output){
            return false;
        }
        return pollution == this.pollution;
    }

    @Override
    public boolean hasAssistance() {
        return false;
    }
}

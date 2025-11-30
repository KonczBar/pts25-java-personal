package sk.uniba.fmph.dcs.terra_futura;

import java.util.List;
import java.util.Optional;

public class SelectReward {
    private Optional<Integer> player;
    private List<Resource> selection;

    public void setReward(int player, Card card, List<Resource> reward) {
        return;
    }

    public boolean canSelectReward(Resource resource) {
        return false;
    }

    public void selectReward() {
        return;
    }

    public String state() {
        return "";
    }
}

package sk.uniba.fmph.dcs.terra_futura;

import java.util.*;

public class SelectReward {
    private Optional<Integer> player = Optional.empty();
    private List<Resource> selection = new ArrayList<>();
    private Card card;
    private boolean done = false;

    public void setReward(int player, Card card, List<Resource> reward) {
        this.player = Optional.of(player);
        this.selection = new ArrayList<>(reward);
        this.card = card;
        this.done = false;
    }

    public boolean canSelectReward(Resource resource) {
        // nonexistent player can't select reward
        if (player.isEmpty()) {
            return false;
        }

        // cannot select a resource that isn't available
        if (!selection.contains(resource)) {
            return false;
        }

        // cannot select reward twice
        if (done) {
            return false;
        }

        // cannot put resource on overpolluted card
        Map<Resource, Integer> request = new HashMap<>();
        request.put(resource, 1);
        if (!card.canPutResources(request)) {
            return false;
        }

        return true;
    }

    public void selectReward(Resource resource) {
        if (!canSelectReward(resource)) {
            throw new IllegalArgumentException("Resource not available: " + resource);
        }

        Map<Resource, Integer> request = new HashMap<>();
        request.put(resource, 1);
        card.putResources(request);
        done = true;
    }

    public String state() {
        return "Player: " + player + " Selection: " + selection + " Done: " + done;
    }
}

package sk.uniba.fmph.dcs.terra_futura;

import java.util.*;

/* Scoring Method:
    resources - which resources earn the player extra points
    pointsPerCombination - how many points each combination of these resources is worth
 */

/* Changes:
    Points class replaced with Integer
    Added Optional<Integer> getCalculatedTotal
    Added Grid to constructor
 */

public class ScoringMethod {

    private final List<Resource> resources;
    private final Integer pointsPerCombination;
    private Optional<Integer> calculatedTotal;
    private final Grid grid;

    public ScoringMethod(List<Resource> resources, Integer pointsPerCombination, Grid grid) {
        this.resources = List.copyOf(resources);
        this.pointsPerCombination = pointsPerCombination;
        this.grid = grid;
        this.calculatedTotal = Optional.empty();
    }

    public void selectThisMethodAndCalculate() {
        int points = 0;

        Map<Resource, Integer> totalResources = new HashMap<>();
        for (Resource resource : Resource.values()) {
            totalResources.put(resource, 0);
        }

        // getting all player resources
        for (int i = -2; i <= 2; i++) {
            for (int j = -2; j <= 2; j++) {
                Optional<Card> c = grid.getCard(new GridPosition(i, j));

                if (c.isPresent()) {
                    // pollution is worth -1 regardless of quantity
                    if (c.get().hasPollution()) {
                        totalResources.put(Resource.POLLUTION, totalResources.get(Resource.POLLUTION) + 1);
                    }

                    // over-polluted cards yield no resources
                    if (!c.get().isPolluted()) {
                        Map<Resource, Integer> newResources = c.get().actuallyGetResources();
                        for (Resource resource : Resource.values()) {
                            if (resource == Resource.POLLUTION) {
                                continue;
                            }
                            Integer val = newResources.get(resource);
                            totalResources.put(resource, totalResources.get(resource) + val);

                        }
                    }
                }
            }
        }

        // calculating basic score
        for (Resource resource : Resource.values()) {
            points += resource.getValue() * totalResources.get(resource);
        }

        // calculating score specific to score card
        while (true) {
            for (Resource resource : resources) {
                if (totalResources.get(resource) == 0) {
                    calculatedTotal = Optional.of(points);
                    return;
                }

                totalResources.put(resource, totalResources.get(resource) - 1);
            }

            points += pointsPerCombination;
        }

    }

    public Optional<Integer> getCalculatedTotal() {
        return calculatedTotal;
    }

    public String state() {
        if (calculatedTotal.isPresent()) {
            return resources.toString() + pointsPerCombination.toString() + calculatedTotal.toString();
        }

        return resources.toString() + pointsPerCombination.toString();
    }
}

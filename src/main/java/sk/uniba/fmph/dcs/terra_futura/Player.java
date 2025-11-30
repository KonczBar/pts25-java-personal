package sk.uniba.fmph.dcs.terra_futura;

public class Player {
    private Grid grid;
    private ActivationPattern activationPattern1;
    private ActivationPattern activationPattern2;
    private ScoringMethod scoringMethod1;
    private ScoringMethod scoringMethod2;


    public Player(Grid grid, ActivationPattern activationPattern1, ActivationPattern activationPattern2,
                  ScoringMethod scoringMethod1, ScoringMethod scoringMethod2) {
        this.grid = grid;
        this.activationPattern1 = activationPattern1;
        this.activationPattern2 = activationPattern2;
        this.scoringMethod1 = scoringMethod1;
        this.scoringMethod2 = scoringMethod2;
    }

    public Grid getGrid() {
        return grid;
    }

    public ActivationPattern getActivationPattern1() {
        return activationPattern1;
    }

    public ActivationPattern getActivationPattern2() {
        return activationPattern2;
    }

    public ScoringMethod getScoringMethod1() {
        return scoringMethod1;
    }

    public ScoringMethod getScoringMethod2() {
        return scoringMethod2;
    }
}

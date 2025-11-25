package sk.uniba.fmph.dcs.terra_futura;

public enum Resource{
    GREEN,
    RED,
    YELLOW,
    BULB,
    GEAR,
    CAR,
    MONEY,
    POLLUTION;

    public int getValue() {
        return switch (this) {
            case GREEN, RED, YELLOW -> 1;
            case BULB, GEAR -> 5;
            case CAR -> 6;
            case MONEY -> 0;
            case POLLUTION -> -1;
        };
    }
}

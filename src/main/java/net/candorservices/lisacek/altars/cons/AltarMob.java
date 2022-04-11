package net.candorservices.lisacek.altars.cons;

public class AltarMob {

    private final String name;

    private final double chance;

    private final int min;

    private final int max;

    public AltarMob(String name, double chance, int min, int max) {
        this.name = name;
        this.chance = chance;
        this.min = min;
        this.max = max;
    }

    public String getName() {
        return name;
    }

    public double getChance() {
        return chance;
    }

    public int getMin() {
        return min;
    }

    public int getMax() {
        return max;
    }
}

package net.candorservices.lisacek.altars.cons;

import org.bukkit.Location;

public class AltarMob {

    private final String name;

    private final double chance;

    private final Location location;

    private final int min;

    private final int max;

    public AltarMob(String name, double chance, Location location, int min, int max) {
        this.name = name;
        this.chance = chance;
        this.location = location;
        this.min = min;
        this.max = max;
    }

    public Location getLocation() {
        return location;
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

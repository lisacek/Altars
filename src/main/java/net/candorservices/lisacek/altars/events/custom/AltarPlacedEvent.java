package net.candorservices.lisacek.altars.events.custom;

import net.candorservices.lisacek.altars.cons.AltarEvent;
import org.bukkit.Sound;

import java.util.List;

public class AltarPlacedEvent implements AltarEvent {

    private final Sound sound;

    private final double pitch;

    private final double volume;

    private final List<String> commands;

    public AltarPlacedEvent(Sound sound, double pitch, double volume, List<String> commands) {
        this.sound = sound;
        this.commands = commands;
        this.pitch = pitch;
        this.volume = volume;
    }

    @Override
    public boolean isSoundEnabled() {
        return sound != null;
    }

    @Override
    public Sound getSound() {
        return this.sound;
    }

    @Override
    public double getPitch() {
        return this.pitch;
    }

    @Override
    public double getVolume() {
        return this.volume;
    }

    @Override
    public List<String> getCommands() {
        return this.commands;
    }
}

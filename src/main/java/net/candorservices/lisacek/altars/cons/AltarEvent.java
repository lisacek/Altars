package net.candorservices.lisacek.altars.cons;

import org.bukkit.Sound;

import java.util.List;

public interface AltarEvent {

    boolean isSoundEnabled();

    Sound getSound();

    double getPitch();

    double getVolume();

    List<String> getCommands();

}

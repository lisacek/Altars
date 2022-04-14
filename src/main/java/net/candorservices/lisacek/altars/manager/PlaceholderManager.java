package net.candorservices.lisacek.altars.manager;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import net.candorservices.lisacek.altars.Altars;
import net.candorservices.lisacek.altars.cons.Altar;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class PlaceholderManager extends PlaceholderExpansion {

    @Override
    public boolean canRegister() {
        return true;
    }

    @Override
    public @NotNull
    String getIdentifier() {
        return "altars";
    }

    @Override
    public @NotNull
    String getAuthor() {
        return "LISACEK";
    }

    @Override
    public @NotNull
    String getVersion() {
        return "1.0.0";
    }

    /**
     * This is the method called when a placeholder with our identifier
     * is found and needs a value.
     * <br>We specify the value identifier in this method.
     * <br>Since version 2.9.1 can you use OfflinePlayers in your requests.
     *
     * @param player     A {@link org.bukkit.OfflinePlayer OfflinePlayer}.
     * @param identifier A String containing the identifier/value.
     * @return Possibly-null String of the requested identifier.
     */
    @Override
    public String onRequest(OfflinePlayer player, @NotNull String identifier) {
        return getPlaceholders(player, identifier);
    }

    @Override
    public String onPlaceholderRequest(Player player, @NotNull String params) {
        return getPlaceholders(player, params);
    }

    @Nullable
    private String getPlaceholders(OfflinePlayer player, String identifier) {
        String[] args = identifier.split("_");
        if (args.length > 0) {
            int id;
            try {
                id = Integer.parseInt(args[0]);
            } catch (Exception e) {
                return null;
            }
            Altar altar = Altars.getInstance().getManager().getAltarById(id);
            if (altar == null) return null;
            switch (args[1]) {
                case "placed":
                    return "" + altar.getPlaced();
                case "left":
                    return "" + (altar.getTotal() - altar.getPlaced());
                case "total":
                    return "" + altar.getTotal();
                case "mobspawned":
                    return "" + altar.isFight();
                case "mobname":
                    return "" + altar.getMobName();
            }
        }
        return null;
    }

}
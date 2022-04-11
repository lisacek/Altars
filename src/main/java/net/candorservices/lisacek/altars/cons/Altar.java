package net.candorservices.lisacek.altars.cons;

import io.lumine.xikage.mythicmobs.MythicMobs;
import io.lumine.xikage.mythicmobs.api.bukkit.BukkitAPIHelper;
import io.lumine.xikage.mythicmobs.api.exceptions.InvalidMobTypeException;
import net.candorservices.lisacek.altars.Altars;
import net.candorservices.lisacek.altars.enums.ConsoleColor;
import net.candorservices.lisacek.altars.events.custom.AltarFightEndEvent;
import net.candorservices.lisacek.altars.events.custom.AltarFightEvent;
import net.candorservices.lisacek.altars.events.custom.AltarPlacedEvent;
import net.candorservices.lisacek.altars.enums.EventType;
import net.candorservices.lisacek.altars.utils.Colors;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

public class Altar {

    private final int id;

    private final ConfigurationSection config;

    private final BukkitAPIHelper mythicMobs = MythicMobs.inst().getAPIHelper();

    private final Map<EventType, AltarEvent> events = new EnumMap<>(EventType.class);

    private final List<AltarMob> mobs = new ArrayList<>();

    private final HashMap<Player, Double> damages = new HashMap<>();

    private final ConsoleOutput console = new ConsoleOutput(ConsoleColor.CYAN + "Altars " + ConsoleColor.DARK_GRAY_BOLD + "| " + ConsoleColor.RESET);

    private Location location;

    private boolean isFight = false;

    private int total = 0;

    private int placed = 0;

    private String name;

    private Material originalMaterial;

    private Material replaceMaterial;

    private String activationItem;

    private int mobId = 0;
    private String mobName = "";

    public Altar(int id, ConfigurationSection config) {
        this.id = id;
        this.config = config;
    }

    public int getId() {
        return id;
    }

    public String getActivationItem() {
        return activationItem;
    }

    public int getMobId() {
        return mobId;
    }

    public String getMobName() {
        return mobName;
    }

    public HashMap<Player, Double> getDamages() {
        return damages;
    }

    public void loadAltar() {
        try {
            location = new Location(Bukkit.getWorld(config.getString("location.world")),
                    config.getDouble("location.x"),
                    config.getDouble("location.y"),
                    config.getDouble("location.z"));
            originalMaterial = Material.getMaterial(config.getString("block-to-activate"));
            replaceMaterial = Material.valueOf(config.getString("activated-block"));
            activationItem = config.getString("activation-item-name");
            name = config.getString("name");

            ConfigurationSection eventsSection = config.getConfigurationSection("events");
            eventsSection.getKeys(false).forEach(eventType -> {
                ConfigurationSection event = eventsSection.getConfigurationSection(eventType);
                boolean sound = event.getBoolean("sound.enabled");
                switch (eventType) {
                    case "eye-place":
                        AltarPlacedEvent eyePlaceEvent = new AltarPlacedEvent(sound ? Sound.valueOf(event.getString("sound.sound")) : null,
                                event.getDouble("sound.volume"), event.getDouble("sound.pitch"), event.getStringList("commands"));
                        events.put(EventType.ALTAR_PLACED, eyePlaceEvent);
                        break;
                    case "fight-start":
                        AltarFightEvent fightEvent = new AltarFightEvent(sound ? Sound.valueOf(event.getString("sound.sound")) : null,
                                event.getDouble("sound.volume"), event.getDouble("sound.pitch"), event.getStringList("commands"));
                        events.put(EventType.ALTAR_FIGHT, fightEvent);
                        break;
                    case "fight-end":
                        AltarFightEndEvent fightEndedEvent = new AltarFightEndEvent(sound ? Sound.valueOf(event.getString("sound.sound")) : null,
                                event.getDouble("sound.volume"), event.getDouble("sound.pitch"), event.getStringList("commands"));
                        events.put(EventType.ALTAR_END, fightEndedEvent);
                        break;
                }
            });
            config.getConfigurationSection("mobs").getKeys(false).forEach(mob -> {
                ConfigurationSection mobSection = config.getConfigurationSection("mobs." + mob);
                AltarMob altarMob = new AltarMob(mobSection.getString("name"), mobSection.getDouble("chance"), mobSection.getInt("min"), mobSection.getInt("max"));
                mobs.add(altarMob);
            });
        } catch (Exception e) {
            console.warn("&cAltar id: " + id + " has incorrect configuration!");
        }
    }


    public Material getReplaceMaterial() {
        return replaceMaterial;
    }

    public Map<EventType, AltarEvent> getEvents() {
        return events;
    }

    public List<AltarMob> getMobs() {
        return mobs;
    }

    public Player lastPlacedBy = null;

    public void startTask() {
        total = getBlocks(location.getBlock(), 5, false).size();
        Bukkit.getScheduler().runTaskTimerAsynchronously(Altars.getInstance(), () -> {
            Bukkit.getScheduler().runTask(Altars.getInstance(), () -> {
                if (!isFight) {
                    try {
                        List<Block> missing = getBlocks(location.getBlock(), 5, false);
                        List<Block> filled = getBlocks(location.getBlock(), 5, true);
                        if (missing.size() == 0 && filled.size() > 0) {
                            isFight = true;
                            restore();
                            spawnMobs();
                            AltarEvent event = events.get(EventType.ALTAR_FIGHT);
                            if (event.isSoundEnabled()) {
                                location.getWorld().playSound(location, event.getSound(), (float) event.getVolume(), (float) event.getPitch());
                            }
                        }
                    } catch (Exception e) {
                        console.warn("Altar id: " + id + " has wrong configuration!");
                    }
                }
            });
        }, 0, 5);
    }

    public void distributeRewards() {
        try {
            Map<Player, Double> rewardPlayers = sortByComparator(damages, false);
            Object[] players = rewardPlayers.keySet().toArray();
            String mobIdConfig = "null";
            for (String mob : config.getConfigurationSection("mobs").getKeys(false)) {
                if (config.getConfigurationSection("mobs." + mob).getString("name", "null").equals(mobName)) {
                    mobIdConfig = mob;
                    break;
                }
            }

            ConfigurationSection mobSection = config.getConfigurationSection("mobs." + mobIdConfig + ".damages");

            AtomicInteger i = new AtomicInteger(0);
            for (String key : mobSection.getKeys(false)) {
                mobSection.getStringList(key + ".commands").forEach(cmd -> {
                    if (players.length > i.get()) {
                        Player player = (Player) players[i.getAndIncrement()];
                        if (player != null) {
                            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), cmd.replace("%player%", player.getName()));
                        }
                    }
                });
            }
        } catch (Exception e) {
            console.warn("Altar id: " + id + " has wrong configuration!");
        }
    }

    public String getName() {
        return name;
    }

    public Player getLastPlacedBy() {
        return lastPlacedBy;
    }

    public void setLastPlacedBy(Player player) {
        this.lastPlacedBy = player;
    }

    private void spawnMobs() {
        try {
            AltarMob altarMob = getRandomMob();
            int random = (int) (Math.random() * (altarMob.getMax() - altarMob.getMin())) + altarMob.getMin();
            mobName = altarMob.getName();
            for (int i = 0; i < random; i++) {
                Entity entity = mythicMobs.spawnMythicMob(altarMob.getName(), location);
                mobId = entity.getEntityId();
                Altars.getInstance().getManager().getInFight().put(mobId, this);

                if (Altars.getInstance().getConfig().getBoolean("altars.messages.fight-started.enabled")) {
                    Bukkit.getOnlinePlayers().forEach(player -> {
                        Colors.translateColors(Altars.getInstance().getConfig().getStringList("altars.messages.fight-started.message")).forEach(msg -> {
                            player.sendMessage(msg
                                    .replace("%boss%", entity.getCustomName())
                            );
                        });
                    });
                }
            }
        } catch (InvalidMobTypeException e) {
            console.info(ConsoleColor.GRAY + "Invalid MythicMobs type: " + ConsoleColor.RED + mobName);
        }
    }

    public void restore() {
        try {
            List<Block> filled = getBlocks(location.getBlock(), 5, true);
            filled.forEach(block -> {
                block.setType(originalMaterial);
            });
        } catch (Exception e) {
            console.warn("Altar id: " + id + " has wrong configuration!");
        }
    }

    public Location getLocation() {
        return location;
    }

    public void setInFight(boolean inFight) {
        this.isFight = inFight;
    }

    public boolean isFight() {
        return isFight;
    }

    public Material getOriginalMaterial() {
        return originalMaterial;
    }

    private AltarMob getRandomMob() {
        double p = Math.random();
        double cumulativeProbability = 0.0;
        for (AltarMob mob : mobs) {
            cumulativeProbability += mob.getChance();
            if (p <= cumulativeProbability) {
                return mob;
            }
        }
        return mobs.get(0);
    }

    private List<Block> getBlocks(Block start, int radius, boolean filled) {
        if (radius < 0) {
            return new ArrayList<>(0);
        }
        int iterations = (radius * 2) + 1;
        List<Block> blocks = new ArrayList<>(iterations * iterations * iterations);
        for (int x = -radius; x <= radius; x++) {
            for (int y = -radius; y <= radius; y++) {
                for (int z = -radius; z <= radius; z++) {
                    Block block = start.getRelative(x, y, z);

                    if (block.getType() == originalMaterial && !filled && block.getType() != Material.ENDER_PORTAL_FRAME) {
                        blocks.add(block);
                    }
                    if (block.getType() == replaceMaterial && filled && block.getType() != Material.ENDER_PORTAL_FRAME) {
                        blocks.add(block);
                    }

                    if (block.getType() == Material.ENDER_PORTAL_FRAME && originalMaterial == Material.ENDER_PORTAL_FRAME) {
                        byte data = block.getData();
                        if (!filled && data == (byte) 2) {
                            blocks.add(block);
                        }
                        if (filled && data != (byte) 2) {
                            blocks.add(block);
                        }
                    }

                    if (block.getType() == replaceMaterial && filled && block.getType() != Material.ENDER_PORTAL_FRAME) {
                        blocks.add(block);
                    }
                }
            }
        }
        return blocks;
    }

    private Map<Player, Double> sortByComparator(Map<Player, Double> unsortMap, final boolean order) {
        List<Map.Entry<Player, Double>> list = new LinkedList<>(unsortMap.entrySet());

        list.sort((o1, o2) -> {
            if (order) {
                return o1.getValue().compareTo(o2.getValue());
            } else {
                return o2.getValue().compareTo(o1.getValue());

            }
        });

        Map<Player, Double> sortedMap = new LinkedHashMap<>();
        for (Map.Entry<Player, Double> entry : list) {
            sortedMap.put(entry.getKey(), entry.getValue());
        }

        return sortedMap;
    }

    public int getTotal() {
        return this.total;
    }

    public int getPlaced() {
        return this.placed;
    }

    public void incrementPlaced() {
        placed++;
    }

}

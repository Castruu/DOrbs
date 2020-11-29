package com.draxy.orbs.database;

import com.draxy.orbs.manager.DataManager;
import com.draxy.orbs.manager.PlayerManager;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.UUID;

public class YAMLDatabase extends Database {

    private static final YAMLDatabase instance = new YAMLDatabase();
    public HashMap<UUID, ConfigurationSection> playerConfigSections = new HashMap<>();
    private FileConfiguration configuration;
    @Override
    public void startConnection() {
        configuration = DataManager.getInstance().getConfig();
    }

    @Override
    public void insertPlayer(Player player) {
        ConfigurationSection section;
        if(isSaved(player)) {
            section = configuration.getConfigurationSection(player.getUniqueId().toString());
        } else section = configuration.createSection(player.getUniqueId().toString());
        playerConfigSections.put(player.getUniqueId(), section);
        DataManager.getInstance().saveConfig();
    }

    @Override
    public void addOrb(Player player, int newOrbs) {
        int amount = playerConfigSections.get(player.getUniqueId()).getInt("orbs");
        playerConfigSections.get(player.getUniqueId()).set("orbs", amount + newOrbs);
        PlayerManager.getPlayerManagerByUUID.get(player.getUniqueId()).setOrbAmount(amount + newOrbs);
        DataManager.getInstance().saveConfig();
    }

    @Override
    public void removeOrb(Player player, int takedOrbs) {
        int amount = playerConfigSections.get(player.getUniqueId()).getInt("orbs");
        playerConfigSections.get(player.getUniqueId()).set("orbs", amount - takedOrbs);
        PlayerManager.getPlayerManagerByUUID.get(player.getUniqueId()).setOrbAmount(amount - takedOrbs);
        DataManager.getInstance().saveConfig();
    }

    @Override
    public int getOrbs(Player player) {
        return playerConfigSections.get(player.getUniqueId()).getInt("orbs");
    }

    @Override
    public void close() {
        configuration = null;
    }

    private boolean isSaved(Player player) {
        return configuration.isConfigurationSection(player.getUniqueId().toString());
    }

   public static YAMLDatabase getInstance() {
        return instance;
   }

}

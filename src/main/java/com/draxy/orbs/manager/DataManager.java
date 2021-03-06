package com.draxy.orbs.manager;

import com.draxy.orbs.DOrbs;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.logging.Level;

public class DataManager {



    private final static DataManager instance = new DataManager();
    private final DOrbs plugin = DOrbs.getInstance();
    private FileConfiguration dataConfig = null;
    private File configFile = null;

    public void reloadConfig() {
        if(this.configFile == null) this.configFile = new File(this.plugin.getDataFolder(), "players.yml");
        this.dataConfig = YamlConfiguration.loadConfiguration(this.configFile);

        InputStream defaultStream = this.plugin.getResource("players.yml");
        if(defaultStream != null) {
            YamlConfiguration defaultConfig = YamlConfiguration.loadConfiguration(new InputStreamReader(defaultStream));
            this.dataConfig.setDefaults(defaultConfig);
        }
    }

    public FileConfiguration getConfig(){
        if(this.dataConfig == null) reloadConfig();

        return this.dataConfig;
    }

    public void saveConfig(){
        if(this.dataConfig == null || this.configFile == null) return;
        try{
            this.getConfig().save(this.configFile);
        } catch (IOException e) {
            plugin.getLogger().log(Level.SEVERE, "Could not save config to " + configFile, e);
            e.printStackTrace();
        }
    }

    public void saveDefaultConfig() {
        if(this.configFile == null) this.configFile = new File(this.plugin.getDataFolder(), "players.yml");
        if(!this.configFile.exists()) this.plugin.saveResource("players.yml", false);
          }

    public static DataManager getInstance() {
        return instance;
    }

}

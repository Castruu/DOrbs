package com.draxy.orbs.config;

import com.draxy.orbs.DOrbs;
import com.draxy.orbs.utils.ItemBuilder;
import com.draxy.orbs.utils.Utils;
import org.bukkit.configuration.ConfigurationSection;

import java.util.List;

public class ConfigHandler {

    private static final ConfigHandler instance = new ConfigHandler();

    public String[] lore;


    private boolean tradeSystem;


    private int TP;
    private ConfigurationSection orbSection;
    public ItemBuilder builder;

    public void setValues() {
        orbSection = DOrbs.getInstance().getConfig().getConfigurationSection("orb");
        tradeSystem = orbSection.getBoolean("allowtrading");
        int id = orbSection.getInt("item");
        int data = orbSection.getInt("data");
        TP = orbSection.getInt("tpamount");
        String name = orbSection.getString("name");
        builder = new ItemBuilder(id, 1, (byte) data).name(Utils.color(name));
        setLore();
    }


    private void setLore() {
        List<String> itemLore = orbSection.getStringList("lore");
        lore = new String[itemLore.size()];
        int i = 0;
        for(String s : itemLore) {
            lore[i] = Utils.color(s);
            i++;
        }
    }

    public int getTP() {
        return TP;
    }

    public boolean isTradeSystemOn() {
        return tradeSystem;
    }

    public static ConfigHandler getInstance() {
        return instance;
    }
}

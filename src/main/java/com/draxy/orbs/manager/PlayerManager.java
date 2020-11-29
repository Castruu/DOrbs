package com.draxy.orbs.manager;

import com.draxy.orbs.DOrbs;
import com.draxy.orbs.config.ConfigHandler;
import com.draxy.orbs.database.YAMLDatabase;
import com.draxy.orbs.utils.ItemBuilder;
import com.draxy.orbs.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.*;

public class PlayerManager {

    public static HashMap<UUID, PlayerManager> getPlayerManagerByUUID = new HashMap<>();

    private final Player player;
    private int OrbAmount;
    private final Inventory inventory;
    private String[] orbList = ConfigHandler.getInstance().lore.clone();
    private ItemStack orb;

    public PlayerManager(Player player) {
        this.player = player;
        OrbAmount = DOrbs.getInstance().connection.getOrbs(player);
        inventory = Bukkit.createInventory(player, 27, Utils.color("&6Orbs -> &a" + player.getName()));
        replacement();
        orb = ConfigHandler.getInstance().builder.lore(orbList).build();
        setInventory();
    }

    private void setInventory() {
        final ItemStack glass = new ItemBuilder(Material.STAINED_GLASS_PANE, 1, 7).name(Utils.color("&b&kADS")).build();
        for(int i = 0; i < inventory.getSize(); i++) {
            inventory.setItem(i, glass);
        }
        inventory.setItem(13, orb);
    }

    public void updateOrb() {
        replacement();
        orb = ConfigHandler.getInstance().builder.lore(orbList).build();
        inventory.setItem(13, orb);
    }

    public Inventory getInventory() {
        return inventory;
    }

    public void setOrbAmount(int orbAmount) {
        this.OrbAmount = orbAmount;
        updateOrb();
    }

    public int getOrbAmount() {
        return this.OrbAmount;
    }

    private void replacement() {
        int i = 0;
        orbList = ConfigHandler.getInstance().lore.clone();
        for(String s : orbList) {
             orbList[i] = s.replaceAll("%orbamount%", "" + getOrbAmount());
             i++;
         }
    }


}

package com.draxy.orbs;

import com.draxy.orbs.commands.OrbCommand;
import com.draxy.orbs.config.ConfigHandler;
import com.draxy.orbs.database.Database;
import com.draxy.orbs.database.MySQLDatabase;
import com.draxy.orbs.database.SQLiteDatabase;
import com.draxy.orbs.database.YAMLDatabase;
import com.draxy.orbs.events.JoinEvent;
import com.draxy.orbs.events.TradeOrbsEvent;
import com.draxy.orbs.manager.DataManager;
import com.draxy.orbs.manager.PlayerManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;


public class DOrbs extends JavaPlugin {

    public Database connection;
    @Override
    public void onEnable() {
        setDatabaseUsed();
        saveDefaultConfig();
        DataManager.getInstance().saveDefaultConfig();
        Bukkit.getPluginManager().registerEvents(new JoinEvent(), this);
        Bukkit.getPluginManager().registerEvents(new TradeOrbsEvent(), this);
        getCommand("orbs").setExecutor(new OrbCommand());
        ConfigHandler.getInstance().setValues();
        for(Player player : Bukkit.getServer().getOnlinePlayers()) {
            connection.insertPlayer(player);
            PlayerManager.getPlayerManagerByUUID.put(player.getUniqueId(), new PlayerManager(player));
        }
    }

    @Override
    public void onDisable() {
        connection.close();
    }


    public void setDatabaseUsed() {
        if(getConfig().getBoolean("usemysql")) {
            String database = getConfig().getString("mysql.database"), host = getConfig().getString("mysql.host"),
                    username = getConfig().getString("mysql.username"), password = getConfig().getString("mysql.password");
            int port = getConfig().getInt("mysql.port");
            MySQLDatabase.setInstance(new MySQLDatabase(database, host, username, password, port));
            connection = MySQLDatabase.getInstance();
        } else if(getConfig().getBoolean("usesqlite")) {
            connection = SQLiteDatabase.getInstance();
        } else {
            connection = YAMLDatabase.getInstance();
        }
        connection.startConnection();
    }

    public static DOrbs getInstance() {
        return DOrbs.getPlugin(DOrbs.class);
    }
}

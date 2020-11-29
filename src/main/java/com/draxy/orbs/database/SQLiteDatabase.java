package com.draxy.orbs.database;

import com.draxy.orbs.DOrbs;
import com.draxy.orbs.manager.PlayerManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.io.File;
import java.sql.*;

public class SQLiteDatabase extends SQLDB {


    private static SQLiteDatabase instance = new SQLiteDatabase();

    public Connection con;

    File file = new File(DOrbs.getInstance().getDataFolder(), "orbs.db");
    String URL = "jdbc:sqlite:" + file;

    @Override
    public void startConnection() {

        try {
            Class.forName("org.sqlite.JDBC");
            con = DriverManager.getConnection(URL);
            createTable(con);
            System.out.print("A conexao SQLite foi iniciada!");
        } catch (SQLException | ClassNotFoundException e) {
            System.out.println("Na tentativa de conectar com SQLite, um erro aconteceu!");
            DOrbs.getInstance().getPluginLoader().disablePlugin(DOrbs.getInstance());
            e.printStackTrace();
        }
    }

    @Override
    public void insertPlayer(Player player) {
        try {
            PreparedStatement stm = con.prepareStatement("INSERT INTO player (uuid, orbs) VALUES (?, 0)");
            stm.setString(1, player.getUniqueId().toString());
            stm.execute();
            stm.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void addOrb(Player player, int newOrbs) {
        Bukkit.getScheduler().runTaskAsynchronously(DOrbs.getInstance(), () -> {
            int amount = PlayerManager.getPlayerManagerByUUID.get(player.getUniqueId()).getOrbAmount();
            try {
                PreparedStatement stm = con.prepareStatement("UPDATE player SET orbs = ? WHERE uuid = ?");
                stm.setInt(1, amount + newOrbs);
                stm.setString(2, player.getUniqueId().toString());
                stm.executeUpdate();
                stm.close();
                PlayerManager.getPlayerManagerByUUID.get(player.getUniqueId()).setOrbAmount(amount + newOrbs);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
    }

    @Override
    public void removeOrb(Player player, int takedOrbs) {
        Bukkit.getScheduler().runTaskAsynchronously(DOrbs.getInstance(), () -> {
            int amount = PlayerManager.getPlayerManagerByUUID.get(player.getUniqueId()).getOrbAmount();
        try {
            PreparedStatement stm = con.prepareStatement("UPDATE player SET orbs = ? WHERE uuid = ?");
            stm.setInt(1, amount - takedOrbs);
            stm.setString(2, player.getUniqueId().toString());
            stm.executeUpdate();
            stm.close();
            PlayerManager.getPlayerManagerByUUID.get(player.getUniqueId()).setOrbAmount(amount - takedOrbs);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        });
    }

    @Override
    public int getOrbs(Player player) {
        int orbs = 0;
        try {
            PreparedStatement stm = con.prepareStatement("SELECT orbs FROM player WHERE uuid = ?");
            stm.setString(1, player.getUniqueId().toString());
            ResultSet set = stm.executeQuery();
            if(set != null && set.next()) {
                orbs = set.getInt("orbs");
            }
            set.close();
            stm.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return orbs;
    }

    @Override
    public void close() {
        if(con != null) {
            try {
                con.close();
                System.out.println("Connection closed with DATABASE");
            } catch (SQLException e) {
                e.printStackTrace();
                System.out.println("Error!!");
            }
        }
    }

    public static SQLiteDatabase getInstance() {
        return instance;
    }



}

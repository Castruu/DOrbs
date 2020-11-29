package com.draxy.orbs.database;

import com.draxy.orbs.DOrbs;
import com.draxy.orbs.manager.PlayerManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.sql.*;

public class MySQLDatabase extends SQLDB {



    private static MySQLDatabase instance;

    private Connection con;

    private final String database;
    private final String host;
    private final String username;
    private final String password;
    private final int port;

    public MySQLDatabase(String database, String host, String username, String password, int port) {
        this.database = database;
        this.host = host;
        this.username = username;
        this.password = password;
        this.port = port;
    }

    @Override
    public void startConnection() {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            con =  DriverManager.getConnection("jdbc:mysql://" + this.host + ":" + this.port + "/" + this.database, this.username, this.password);
            createTable(con);
            System.out.println("Conexao com MySQL iniciada!");
        } catch (SQLException | ClassNotFoundException e) {
            System.out.println("Na tentativa de conectar com MYSQL, um erro aconteceu!");
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

    public static MySQLDatabase getInstance() {
        return instance;
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

    public static void setInstance(MySQLDatabase instance) {
        MySQLDatabase.instance = instance;
    }

}

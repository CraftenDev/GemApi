package me.mickyjou.plugins.gems.api;

import me.mickyjou.plugins.gems.api.commands.GemCommands;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.ServicePriority;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;

public class GemApi extends JavaPlugin implements GemProvider {
    private File gemsFile;
    private YamlConfiguration gems;

    @Override
    public void onEnable() {
        gemsFile = new File(getDataFolder(), "gems.yml");
        gems = YamlConfiguration.loadConfiguration(gemsFile);

        Bukkit.getServicesManager().register(GemProvider.class, this, this, ServicePriority.Normal);
        getCommand("gems").setExecutor(new GemCommands(this));
    }

    private ConfigurationSection getPlayerData(OfflinePlayer player) {
        ConfigurationSection playerSection = gems.getConfigurationSection(player.getUniqueId().toString());
        if (playerSection == null) {
            playerSection = gems.createSection(player.getUniqueId().toString());
        }
        return playerSection;
    }

    @Override
    public int getGems(OfflinePlayer player) {
        return getPlayerData(player).getInt("gems", 0);
    }

    @Override
    public void addGems(OfflinePlayer player, int amount) {
        setGems(player, getGems(player) + amount);
    }

    @Override
    public boolean removeGems(OfflinePlayer player, int amount) {
        if (getGems(player) >= amount) {
            setGems(player, getGems(player) - amount);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void setGems(OfflinePlayer player, int amount) {
        getPlayerData(player).set("gems", getPlayerData(player).getInt("gems") + amount);
        try {
            gems.save(gemsFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

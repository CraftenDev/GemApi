package me.mickyjou.plugins.gems.api.providers;

import me.mickyjou.plugins.gems.api.GemProvider;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

/**
 * A gem provider that uses a yaml file.
 */
public class YamlGemProvider implements GemProvider {
    private final File gemsFile;
    private final YamlConfiguration gems;

    public YamlGemProvider(File gemsFile) {
        this.gemsFile = gemsFile;
        gems = YamlConfiguration.loadConfiguration(gemsFile);
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
        ConfigurationSection playerSection = getPlayerData(player);
        playerSection.set("gems", amount);
        gems.set(player.getUniqueId().toString(), playerSection);
        try {
            gems.save(gemsFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

package me.mickyjou.plugins.gems.api;

import org.bukkit.OfflinePlayer;

/**
 * A provider for Gems.
 */
public interface GemProvider {
    /**
     * Gets the gems of the given player.
     *
     * @param player player
     * @return gems
     */
    int getGems(OfflinePlayer player);

    /**
     * Adds gems to the given player.
     *
     * @param player player
     * @param amount amount of gems to add
     */
    void addGems(OfflinePlayer player, int amount);

    /**
     * Removes gems from the given player.
     *
     * @param player player
     * @param amount amount of gems to remove
     */
    void removeGems(OfflinePlayer player, int amount);

    /**
     * Sets the gems of the given player.
     *
     * @param player player
     * @param amount amount of gems to set
     */
    void setGems(OfflinePlayer player, int amount);
}

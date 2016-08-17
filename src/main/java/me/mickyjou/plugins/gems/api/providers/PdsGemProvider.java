package me.mickyjou.plugins.gems.api.providers;

import de.craften.plugins.playerdatastore.api.PlayerDataStore;
import de.craften.plugins.playerdatastore.api.PlayerDataStoreService;
import me.mickyjou.plugins.gems.api.GemProvider;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;

/**
 * A gem provider that uses the {@link de.craften.plugins.playerdatastore.api.PlayerDataStoreService}.
 */
public class PdsGemProvider implements GemProvider {
    private static PlayerDataStore getStore(OfflinePlayer player) {
        return Bukkit.getServicesManager().getRegistration(PlayerDataStoreService.class).getProvider().getStore(player);
    }

    @Override
    public int getGems(OfflinePlayer player) {
        String gems = getStore(player).get("gemapi.gems");
        if (gems != null) {
            try {
                return Integer.parseInt(gems);
            } catch (NumberFormatException e) {
                //ignore
            }
        }
        return 0;
    }

    @Override
    public void addGems(OfflinePlayer player, int amount) {
        // TODO locking
        getStore(player).put("gemapi.gems", "" + (getGems(player) + amount));
    }

    @Override
    public boolean removeGems(OfflinePlayer player, int amount) {
        // TODO locking
        int gems = getGems(player);
        if (gems >= amount) {
            getStore(player).put("gemapi.gems", String.valueOf(gems - amount));
            return true;
        }
        return false;
    }

    @Override
    public void setGems(OfflinePlayer player, int amount) {
        getStore(player).put("gemapi.gems", String.valueOf(amount));
    }

    public static boolean isAvailable() {
        return Bukkit.getServicesManager().isProvidedFor(PlayerDataStoreService.class);
    }
}

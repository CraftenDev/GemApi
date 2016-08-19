package me.mickyjou.plugins.gems.api.providers;

import de.craften.plugins.playerdatastore.api.PlayerDataStore;
import de.craften.plugins.playerdatastore.api.PlayerDataStoreService;
import me.mickyjou.plugins.gems.api.GemProvider;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * A gem provider that uses the {@link de.craften.plugins.playerdatastore.api.PlayerDataStoreService}.
 */
public class PdsGemProvider implements GemProvider {
    private static PlayerDataStore getStore(OfflinePlayer player) {
        return Bukkit.getServicesManager().getRegistration(PlayerDataStoreService.class).getProvider().getStore(player);
    }

    @Override
    public int getGems(OfflinePlayer player) {
        return tryParseInt(getStore(player).get("gemapi.gems"), 0);
    }

    @Override
    public void addGems(OfflinePlayer player, int amount) {
        getStore(player).update("gemapi.gems", (gems) -> String.valueOf(tryParseInt(gems, 0) + amount));
    }

    @Override
    public boolean removeGems(OfflinePlayer player, final int amount) {
        final AtomicBoolean paid = new AtomicBoolean(true);
        getStore(player).update("gemapi.gems", (gemsString) -> {
            int gems = tryParseInt(gemsString, 0);
            if (gems >= amount) {
                return String.valueOf(gems - amount);
            } else {
                paid.set(false);
                return String.valueOf(gems);
            }
        });
        return paid.get();
    }

    @Override
    public void setGems(OfflinePlayer player, int amount) {
        getStore(player).put("gemapi.gems", String.valueOf(amount));
    }

    public static boolean isAvailable() {
        return Bukkit.getServicesManager().isProvidedFor(PlayerDataStoreService.class);
    }

    private static int tryParseInt(String number, int fallback) {
        if (number == null) {
            return fallback;
        }
        try {
            return Integer.parseInt(number);
        } catch (NumberFormatException e) {
            return fallback;
        }
    }
}

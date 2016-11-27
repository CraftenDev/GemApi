package me.mickyjou.plugins.gems.api;

import com.alecgorge.minecraft.jsonapi.JSONAPI;
import com.alecgorge.minecraft.jsonapi.api.APIMethodName;
import com.alecgorge.minecraft.jsonapi.api.JSONAPICallHandler;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Provider for some JSONAPI methods for the Gem API.
 */
public class JsonApiHandler {
    public void register() {
        JSONAPI jsonapi = (JSONAPI) Bukkit.getPluginManager().getPlugin("JSONAPI");
        jsonapi.registerAPICallHandler(new JSONAPICallHandler() {
            @Override
            public boolean willHandle(APIMethodName apiMethodName) {
                if (!apiMethodName.getNamespace().equals("gemapi"))
                    return false;

                if (apiMethodName.getMethodName().equals("get"))
                    return true;

                if (apiMethodName.getMethodName().equals("add"))
                    return true;

                if (apiMethodName.getMethodName().equals("remove"))
                    return true;

                return false;
            }

            @Override
            public Object handle(APIMethodName apiMethodName, Object[] objects) {
                GemProvider gemProvider = Bukkit.getServicesManager().getRegistration(GemProvider.class).getProvider();

                OfflinePlayer player;
                try {
                    player = Bukkit.getOfflinePlayer(UUID.fromString(objects[0].toString()));
                } catch (IllegalArgumentException e) {
                    player = Bukkit.getOfflinePlayer(objects[0].toString());
                }
                if (player == null) {
                    return null;
                }

                if (apiMethodName.getMethodName().equals("get")) {
                    Map<String, Object> result = new HashMap<>();
                    result.put("gems", gemProvider.getGems(player));
                    return result;
                } else if (apiMethodName.getMethodName().equals("add")) {
                    gemProvider.addGems(player, Math.max(0, Integer.parseInt(objects[1].toString())));
                    return true;
                } else if (apiMethodName.getMethodName().equals("remove")) {
                    return gemProvider.removeGems(player, Math.max(0, Integer.parseInt(objects[1].toString())));
                }

                return null;
            }
        });
    }
}

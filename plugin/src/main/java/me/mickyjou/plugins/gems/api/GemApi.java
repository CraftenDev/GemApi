package me.mickyjou.plugins.gems.api;

import me.mickyjou.plugins.gems.api.commands.GemCommands;
import me.mickyjou.plugins.gems.api.providers.PdsGemProvider;
import de.craften.plugins.bkcommandapi.SubCommandHandler;
import me.mickyjou.plugins.gems.api.providers.YamlGemProvider;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.ServicePriority;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

public class GemApi extends JavaPlugin {
    @Override
    public void onEnable() {
        saveDefaultConfig();

        GemProvider provider;
        if (getConfig().getString("provider", "yaml").equals("pds")) {
            if (PdsGemProvider.isAvailable()) {
                provider = new PdsGemProvider();
                getLogger().info("Using PlayerDataStore provider");
            } else {
                getLogger().warning("PlayerDataStore isn't available");
                getServer().getPluginManager().disablePlugin(this);
                return;
            }
        } else {
            provider = new YamlGemProvider(new File(getDataFolder(), "gems.yml"));
            getLogger().info("Using yaml file provider");
        }

        Bukkit.getServicesManager().register(GemProvider.class, provider, this, ServicePriority.Normal);

        SubCommandHandler gemCommandHandler = new SubCommandHandler("gems") {
            @Override
            protected void onInvalidCommand(CommandSender commandSender) {
                commandSender.sendMessage(ChatColor.RED + "Unknown command.");
            }

            @Override
            protected void onPermissionDenied(CommandSender commandSender, Command command, String[] strings) {
                commandSender.sendMessage(ChatColor.RED + "You don't have permission to use this command.");
            }
        };
        gemCommandHandler.addHandlers(new GemCommands(provider));
        getCommand("gems").setExecutor(gemCommandHandler);

        if(getServer().getPluginManager().isPluginEnabled("JSONAPI")) {
            new JsonApiHandler().register();
        }
    }
}

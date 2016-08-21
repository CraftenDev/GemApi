package me.mickyjou.plugins.gems.api.commands;

import de.craften.plugins.bkcommandapi.Command;
import de.craften.plugins.bkcommandapi.CommandHandler;
import me.mickyjou.plugins.gems.api.GemProvider;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class GemCommands implements CommandHandler {
    private final GemProvider gemProvider;

    public GemCommands(GemProvider gemProvider) {
        this.gemProvider = gemProvider;
    }

    @Command(
            value = "show",
            max = 1,
            allowFromConsole = true,
            description = "Show the gems of a player.",
            usage = "show [player]"
    )
    public Result showGems(CommandSender sender, String[] args) {
        if (args.length == 0) {
            if (sender instanceof Player) {
                sender.sendMessage(ChatColor.GRAY + "You've got " + ChatColor.GOLD + gemProvider.getGems((Player) sender) + ChatColor.GRAY + " Gems.");
            } else {
                sender.sendMessage(ChatColor.RED + "You need to specify a player.");
            }
            return Result.Done;
        } else if (sender.hasPermission("gems.show.any")) {
            OfflinePlayer player = Bukkit.getOfflinePlayer(args[0]);
            if (player != null) {
                sender.sendMessage(ChatColor.GOLD + player.getName() + ChatColor.GRAY + " has got " + ChatColor.GOLD + gemProvider.getGems(player) + ChatColor.GRAY + " Gems.");
            } else {
                sender.sendMessage(ChatColor.RED + "Unknown player: " + args[0]);
            }
            return Result.Done;
        } else {
            return Result.NoPermission;
        }
    }

    @Command(
            value = "",
            description = "Show your gems"
    )
    public Result showPlayerGems(Player player) {
        return showGems(player, new String[0]);
    }

    @Command(
            value = "add",
            permission = "gems.add",
            min = 2,
            max = 2,
            allowFromConsole = true,
            description = "Give gems to a player.",
            usage = "add <player> <gems>"
    )
    public void addGems(CommandSender sender, String[] args) {
        OfflinePlayer player = Bukkit.getOfflinePlayer(args[0]);
        if (player != null) {
            try {
                Integer amount = Integer.valueOf(args[1]);
                if (amount > 0) {
                    gemProvider.addGems(player, amount);
                    sender.sendMessage(ChatColor.GRAY + player.getName() + " received " + ChatColor.GOLD + amount + ChatColor.GRAY + " Gems.");

                    if (player.isOnline()) {
                        player.getPlayer().sendMessage(ChatColor.GRAY + "You've got " + ChatColor.GOLD + amount + ChatColor.GRAY + " Gems from " + sender.getName() + "!");
                    }
                } else {
                    sender.sendMessage(ChatColor.RED + "The amount of gems must be greater than zero.");
                }
            } catch (NumberFormatException e) {
                sender.sendMessage(ChatColor.RED + "Invalid amount of gems: " + args[1]);
            }
        } else {
            sender.sendMessage(ChatColor.RED + "Unknown player: " + args[0]);
        }
    }

    @Command(
            value = "remove",
            permission = "gems.remove",
            min = 2,
            max = 2,
            allowFromConsole = true,
            description = "Take gems from a player.",
            usage = "remove <player> <gems>"
    )
    public void removeGems(CommandSender sender, String[] args) {
        OfflinePlayer player = Bukkit.getOfflinePlayer(args[0]);
        if (player != null) {
            try {
                Integer amount = Integer.valueOf(args[1]);
                if (amount > 0) {
                    if (amount <= gemProvider.getGems(player)) {
                        gemProvider.removeGems(player, amount);
                        sender.sendMessage(ChatColor.GRAY + player.getName() + " lost " + ChatColor.GOLD + amount + ChatColor.GRAY + " Gems.");

                        if (player.isOnline()) {
                            player.getPlayer().sendMessage(ChatColor.GRAY + "You were taken " + ChatColor.GOLD + amount + ChatColor.GRAY + " Gems by " + sender.getName() + ".");
                        }
                    } else {
                        sender.sendMessage(ChatColor.RED + "The player has less than " + amount + " Gems.");
                    }
                } else {
                    sender.sendMessage(ChatColor.RED + "The amount of gems must be greater than zero.");
                }
            } catch (NumberFormatException e) {
                sender.sendMessage(ChatColor.RED + "Invalid amount of gems: " + args[1]);
            }
        } else {
            sender.sendMessage(ChatColor.RED + "Unknown player: " + args[0]);
        }
    }

    @Command(
            value = "set",
            permission = "gems.set",
            min = 2,
            max = 2,
            allowFromConsole = true,
            description = "Set the gems of a player.",
            usage = "set <player> <gems>"
    )
    public void setGems(CommandSender sender, String[] args) {
        OfflinePlayer player = Bukkit.getOfflinePlayer(args[0]);
        if (player != null) {
            try {
                Integer amount = Integer.valueOf(args[1]);
                if (amount >= 0) {
                    gemProvider.setGems(player, amount);
                    sender.sendMessage(ChatColor.GRAY + player.getName() + " now has " + ChatColor.GOLD + amount + ChatColor.GRAY + " Gems.");

                    if (player.isOnline()) {
                        player.getPlayer().sendMessage(ChatColor.GRAY + "Your gems were set to " + ChatColor.GOLD + amount + ChatColor.GRAY + " Gems by " + sender.getName() + ".");
                    }
                } else {
                    sender.sendMessage(ChatColor.RED + "The amount of gems must be greater than or equal to zero.");
                }
            } catch (NumberFormatException e) {
                sender.sendMessage(ChatColor.RED + "Invalid amount of gems: " + args[1]);
            }
        } else {
            sender.sendMessage(ChatColor.RED + "Unknown player: " + args[0]);
        }
    }

    @Command(
            value = "pay",
            permission = "gems.pay",
            min = 2,
            max = 2,
            description = "Pay gems to a player.",
            usage = "pay <player> <gems>"
    )
    public void payGems(Player player, String[] args) {
        OfflinePlayer receiver = Bukkit.getOfflinePlayer(args[0]);
        if (receiver != null) {
            try {
                Integer amount = Integer.valueOf(args[1]);
                if (amount > 0) {
                    if (gemProvider.removeGems(player, amount)) {
                        gemProvider.addGems(receiver, amount);
                        if (receiver.isOnline()) {
                            receiver.getPlayer().sendMessage(ChatColor.GRAY + player.getName() + " sent you " + ChatColor.GOLD + amount + " Gems" + ChatColor.GRAY + "!");
                        }
                        player.sendMessage(ChatColor.GRAY + "You sent " + ChatColor.GOLD + amount + " Gems" + ChatColor.GRAY + " to " + receiver.getName() + ".");
                    } else {
                        player.sendMessage(ChatColor.RED + "You don't have enough gems.");
                    }
                } else {
                    player.sendMessage(ChatColor.RED + "The amount of gems must be greater than zero.");
                }
            } catch (NumberFormatException e) {
                player.sendMessage(ChatColor.RED + "Invalid amount of gems: " + args[1]);
            }
        } else {
            player.sendMessage(ChatColor.RED + "Unknown player: " + args[0]);
        }
    }
}

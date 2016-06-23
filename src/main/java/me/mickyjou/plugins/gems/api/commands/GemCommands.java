package me.mickyjou.plugins.gems.api.commands;

import me.mickyjou.plugins.gems.api.GemProvider;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class GemCommands implements CommandExecutor {
    private final GemProvider gemProvider;

    public GemCommands(GemProvider gemProvider) {
        this.gemProvider = gemProvider;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String cmdlabel, String[] args) {
        if (args.length == 0) {
            if (sender instanceof Player) {
                sender.sendMessage(ChatColor.GRAY + "You've got " + ChatColor.GOLD + gemProvider.getGems((Player) sender) + " Gems" + ChatColor.GRAY + ".");
                return true;
            }
        } else if (args.length == 3) {
            if (args[0].equals("pay") && sender.hasPermission("gems.pay")) {
                if (sender instanceof Player) {
                    OfflinePlayer receiver = Bukkit.getOfflinePlayer(args[1]);
                    Integer amount = Integer.valueOf(args[2]);
                    if (receiver != null) {
                        if (amount != null && amount > 0) {
                            if (gemProvider.removeGems((Player) sender, amount)) {
                                gemProvider.addGems(receiver, amount);
                                if (receiver.isOnline()) {
                                    receiver.getPlayer().sendMessage(sender.getName() + " sent you " + ChatColor.GOLD + " Gems" + ChatColor.RESET + "!");
                                }
                            } else {
                                sender.sendMessage(ChatColor.RED + "You don't have enough gems.");
                            }
                        } else {
                            sender.sendMessage(ChatColor.RED + "Amount of gems to pay must be a positive number.");
                        }
                    } else {
                        sender.sendMessage(ChatColor.RED + "The player " + args[1] + " could not be found.");
                    }
                    return true;
                }
                return false;
            } else if (args[0].equals("add") && sender.hasPermission("gems.add")) {
                OfflinePlayer receiver = Bukkit.getOfflinePlayer(args[1]);
                Integer amount = Integer.valueOf(args[2]);
                if (receiver != null) {
                    if (amount != null && amount >= 0) {
                        gemProvider.addGems(receiver, amount);
                        sender.sendMessage(ChatColor.GRAY + receiver.getName() + " got " + ChatColor.GOLD + args[2] + " Gems" + ChatColor.GRAY + ".");
                        if (receiver.isOnline()) {
                            receiver.getPlayer().sendMessage(ChatColor.GRAY + "You just got " + ChatColor.GOLD + args[2] + " Gems" + ChatColor.GRAY + " by " + sender.getName() + "!");
                        }
                    } else {
                        sender.sendMessage(ChatColor.RED + "Amount of gems to add must be a positive number.");
                    }
                } else {
                    sender.sendMessage(ChatColor.RED + "The player " + args[1] + " could not be found.");
                }
                return true;
            } else if (args[0].equals("remove") && sender.hasPermission("gems.remove")) {
                OfflinePlayer receiver = Bukkit.getOfflinePlayer(args[1]);
                Integer amount = Integer.valueOf(args[2]);
                if (receiver != null) {
                    if (amount != null && amount > 0) {
                        if (amount <= gemProvider.getGems((OfflinePlayer) sender)) {
                            gemProvider.removeGems(receiver, amount);
                            sender.sendMessage(ChatColor.GOLD + receiver.getName() + ChatColor.GRAY + " lost " + ChatColor.GOLD + amount + ChatColor.GRAY + " Gems!");
                            if (receiver.getPlayer() != null) {
                                receiver.getPlayer().sendMessage(ChatColor.GRAY + "You lost " + ChatColor.GOLD + args[2] + ChatColor.GRAY + " Gems by " + ChatColor.GOLD + sender.getName() + ChatColor.GRAY + "!");
                            }
                        } else {
                            sender.sendMessage(ChatColor.RED + "The Player §6" + ChatColor.GOLD + receiver + ChatColor.RED + " don't have enough Gems for that!");
                        }
                    } else {
                        sender.sendMessage(ChatColor.RED + "Amount of gems to remove must be a positive number.");
                    }
                } else {
                    sender.sendMessage(ChatColor.RED + "The player " + args[1] + " could not be found.");
                }
                return true;
            } else if (args[0].equals("set") && sender.hasPermission("gems.set")) {
                OfflinePlayer receiver = Bukkit.getOfflinePlayer(args[1]);
                Integer amount = Integer.valueOf(args[2]);
                if (receiver != null) {
                    if (amount != null && amount >= 0) {
                        gemProvider.setGems(receiver, amount);
                        sender.sendMessage(ChatColor.GRAY + "The Gems of " + ChatColor.GOLD + receiver + ChatColor.GRAY + " were set to §6" + ChatColor.GOLD + args[2] + ChatColor.GRAY + "!");
                        if (receiver.getPlayer() != null) {
                            receiver.getPlayer().sendMessage(ChatColor.GRAY + "Your Gems was set to " + ChatColor.GOLD + args[2] + ChatColor.GRAY + " by " + ChatColor.GOLD + sender.getName() + ChatColor.GRAY + "!");
                        }
                    } else {
                        sender.sendMessage(ChatColor.RED + "Amount of gems to set must be greater or equal to zero.");
                    }
                } else {
                    sender.sendMessage(ChatColor.RED + "The player " + args[1] + " could not be found.");
                }
                return true;
            }
        }
        return false;
    }
}
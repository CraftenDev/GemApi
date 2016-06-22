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
        if (args.length == 0 && sender instanceof Player) {
            sender.sendMessage(ChatColor.GRAY + "You've got " + ChatColor.GOLD + gemProvider.getGems((Player) sender) + ChatColor.GRAY + " Gems!");
        } else if (args.length == 3 && sender instanceof Player) {
            if (sender.hasPermission("gems.add")) {
                // Admin-Commands

                if (args.length == 3) {
                    if (sender.hasPermission("gems.add")) {
                        if (args[0].equalsIgnoreCase("add")) {
                            OfflinePlayer receiver = Bukkit.getOfflinePlayer(args[1]);
                            if (receiver != null) {
                                Integer amount = Integer.valueOf(args[2]);
                                gemProvider.addGems(receiver, amount);
                                sender.sendMessage("§6" + receiver + " §7got §6" + args[2] + " §7Gems!");
                                if (receiver.getPlayer() != null) {
                                    receiver.getPlayer().sendMessage(ChatColor.GRAY + "You've got " + ChatColor.GOLD + args[2] + ChatColor.GRAY + " by Mickyjou!");
                                }
                            } else {
                                sender.sendMessage(ChatColor.RED + "Player " + args[1] + " not found.");
                            }
                        }
                    } else {
                        sender.sendMessage(ChatColor.RED + "You dont have permissions for that!");
                    }

                } else {
                    sender.sendMessage(ChatColor.RED + "Wrong usage!");
                }

                if (args.length == 3) {
                    if (sender.hasPermission("gems.remove")) {
                        if (args[0].equalsIgnoreCase("remove")) {
                            OfflinePlayer receiver = Bukkit.getOfflinePlayer(args[1]);
                            Integer amount = Integer.valueOf(args[2]);
                            if (amount != 0) {
                                if (amount >= 0) {
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
                                    sender.sendMessage(ChatColor.RED + "The amount can't be under 0!");
                                }
                            } else {
                                sender.sendMessage(ChatColor.RED + "The amount can't be 0!");
                            }
                        }
                    } else {
                        sender.sendMessage(ChatColor.RED + "You dont have permissions for that!");
                    }
                } else {
                    sender.sendMessage(ChatColor.RED + "Wrong usage!");
                }
                if (args.length == 3) {
                    if (sender.hasPermission("gems.set")) {
                        if (args[0].equalsIgnoreCase("set")) {
                            OfflinePlayer receiver = Bukkit.getOfflinePlayer(args[1]);
                            Integer amount = Integer.valueOf(args[2]);
                            if (receiver != null) {
                                gemProvider.setGems(receiver, amount);
                                if (amount >= 0) {

                                    sender.sendMessage(ChatColor.GRAY + "The Gems of " + ChatColor.GOLD + receiver + ChatColor.GRAY + " were set to §6" + ChatColor.GOLD + args[2] + ChatColor.GRAY + "!");
                                    if (receiver.getPlayer() != null) {
                                        receiver.getPlayer().sendMessage(ChatColor.GRAY + "Your Gems was set to " + ChatColor.GOLD + args[2] + ChatColor.GRAY + " by " + ChatColor.GOLD + sender.getName() + ChatColor.GRAY + "!");
                                    }
                                } else {
                                    sender.sendMessage(ChatColor.RED + "The amount can't be under 0!");
                                }
                            } else {
                                sender.sendMessage(ChatColor.GRAY + "The player " + ChatColor.GOLD + receiver + ChatColor.GRAY + " isn't online!");
                            }
                        }
                    } else {
                        sender.sendMessage(ChatColor.RED + "You dont have permissions for that!");
                    }
                } else {
                    sender.sendMessage(ChatColor.RED + "Wrong usage!");
                }
            } else {
                sender.sendMessage(ChatColor.RED + "You dont have Permissions for that!");
            }

        } else {
            sender.sendMessage(ChatColor.RED + "Wrong usage!");
        }

        return true;
    }
}

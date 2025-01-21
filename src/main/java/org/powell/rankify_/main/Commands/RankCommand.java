package org.powell.rankify_.main.Commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.powell.rankify_.main.Enums.Rank;
import org.powell.rankify_.main.Main;

public class RankCommand implements CommandExecutor {
    private Main main;

    public RankCommand(Main main) {
        this.main = main;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if (sender instanceof Player){
            Player player = (Player) sender;
            if (player.isOp()){
                if (args.length == 2){
                    if (Bukkit.getOfflinePlayer(args[0]) != null){
                        OfflinePlayer target = Bukkit.getOfflinePlayer(args[0]);

                        for (Rank rank : Rank.values()){
                            if (rank.name().equalsIgnoreCase(args[1])){
                                main.getRankManager().setRank(target.getUniqueId(), rank, false);

                                player.sendMessage(ChatColor.AQUA + "You Changed " + target.getName() + "'s rank to "+ rank.getDisplay() + ChatColor.AQUA + ".");
                            if (target.isOnline()){
                                target.getPlayer().sendMessage(ChatColor.DARK_AQUA + player.getName() + " set your rank to " + rank.getDisplay() + ChatColor.DARK_AQUA + ".");
                            }
                                return false;
                            }
                        }
                        player.sendMessage(ChatColor.RED + "You did not specified which rank: Guest, Member, Admin");
                    } else {
                        player.sendMessage(ChatColor.RED + "This user has never joined the server");
                    }
                } else {
                    player.sendMessage(ChatColor.RED + "Invalid Usage. Please use /rank <player> <rank>");
                }
            } else {
                player.sendMessage(ChatColor.RED + "You must be op to use this command");
            }
        }
        return false;
    }
}

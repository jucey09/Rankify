package org.powell.rankify.main.Commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.powell.rankify.main.Enums.Rank;
import org.powell.rankify.main.Main;
import org.powell.rankify.main.Managers.RankManager;

import java.util.ArrayList;
import java.util.Arrays;

public class RankCommand implements CommandExecutor {
    private Main main;
    private RankManager rankManager;

    public RankCommand(Main main, RankManager rankManager) {
        this.main = main;
        this.rankManager = rankManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if (sender instanceof Player){
            Player player = (Player) sender;
            if (player.isOp()) {
                if (args.length == 2){
                    if (Bukkit.getOfflinePlayer(args[0]) != null){
                        OfflinePlayer target = Bukkit.getOfflinePlayer(args[0]);

                        for (Rank rank : Rank.values()){
                            if (rank.name().equalsIgnoreCase(args[1])){
                                main.getRankManager().setRank(target.getUniqueId(), rank, false);

                                player.sendMessage(ChatColor.AQUA + "You changed " + target.getName() + "'s rank to "+ rank.getDisplay() + ChatColor.AQUA + ".");
                                if (target.isOnline()){
                                    target.getPlayer().sendMessage(ChatColor.DARK_AQUA + player.getName() + " set your rank to " + rank.getDisplay() + ChatColor.DARK_AQUA + ".");
                                }
                                return false;
                            }
                        }
                        player.sendMessage(ChatColor.RED + "You did not specify which rank: Guest, Member, Moderator, Admin, Youtube, Co_Owner, Owner");
                    } else {
                        player.sendMessage(ChatColor.RED + "This user has never joined the server");
                    }
                } else if (args[0].equalsIgnoreCase("gui")){
                    //ONLINE PLAYERS
                    Inventory inv = Bukkit.createInventory(player, 54, ChatColor.DARK_AQUA + "Rankify Menu");
                    for(Player online_player : Bukkit.getServer().getOnlinePlayers()) {
                        ItemStack skull = new ItemStack(Material.PLAYER_HEAD,1, (byte) 3);
                        SkullMeta skullMeta = (SkullMeta) skull.getItemMeta();
                        skullMeta.setOwner(online_player.getName());
                        skullMeta.setDisplayName(ChatColor.GOLD + online_player.getName());
                        skullMeta.setLore(Arrays.asList(ChatColor.GOLD + "Rank: " + main.getRankManager().getRank(online_player.getUniqueId())));
                        skull.setItemMeta(skullMeta);

                        inv.setItem(10, skull);
                    }
                    //CLOSE
                    ItemStack close = new ItemStack(Material.BARRIER);
                    ItemMeta closemeta = close.getItemMeta();
                    closemeta.setDisplayName(ChatColor.RED + "Close Button");
                    closemeta.setLore(Arrays.asList(""));
                    close.setItemMeta(closemeta);

                    inv.setItem(0, close);

                    //FRAME
                    ItemStack frame = new ItemStack(Material.GRAY_STAINED_GLASS_PANE);
                    ItemMeta fmeta = frame.getItemMeta();
                    fmeta.setDisplayName(ChatColor.GRAY + "_");
                    fmeta.setLore(Arrays.asList(""));
                    frame.setItemMeta(fmeta);
                    for (int i : new int[]{1,2,3,4,5,6,7,8,9,17,18,26,27,35,36,44,45,46,47,48,49,50,51,52,53}){
                        inv.setItem(i, frame);
                    }

                    player.openInventory(inv);

                } else {
                    player.sendMessage(ChatColor.RED + "Invalid Usage. Please use /rankify <player> <rank> or /rankify gui");
                }
            } else {
                player.sendMessage(ChatColor.RED + "You must be op to use this command");
            }
        }
        return false;
    }

    public RankManager getRankManager() { return rankManager; }
}

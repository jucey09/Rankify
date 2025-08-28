package org.powell.rankify.main.Commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.powell.rankify.main.Enums.Rank;
import org.powell.rankify.main.GUI.GUI;
import org.powell.rankify.main.Main;
import org.powell.rankify.main.Managers.RankManager;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.UUID;

public class RankCommand implements CommandExecutor {
    private final Main main;
    private final RankManager rankManager;

    public RankCommand(Main main, RankManager rankManager) {
        this.main = main;
        this.rankManager = rankManager;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        // Handle reload subcommand (allow console and players with permission)
        if (args.length > 0 && args[0].equalsIgnoreCase("reload")) {
            if (sender instanceof Player) {
                Player p = (Player) sender;
                if (!p.hasPermission("rankify.reload")) {
                    p.sendMessage(ChatColor.RED + "You don't have permission to reload Rankify.");
                    return true;
                }
            }
            main.reloadConfig();
            sender.sendMessage(ChatColor.GREEN + "Rankify configuration reloaded.");
            return true;
        }

        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "Only players can use this command.");
            return true;
        }

        Player player = (Player) sender;
        
        if (args.length == 0) {
            sendUsage(player);
            return true;
        }

        if (args[0].equalsIgnoreCase("gui")) {
            if (!player.hasPermission("rankify.gui")) {
                player.sendMessage(ChatColor.RED + "You don't have permission to use the GUI.");
                return true;
            }
            new GUI(main, player, 1);
            return true;
        }

        if (args.length < 2) {
            sendUsage(player);
            return true;
        }

        if (!player.hasPermission("rankify.setrank")) {
            player.sendMessage(ChatColor.RED + "You don't have permission to set ranks.");
            return true;
        }

        String targetName = args[0];
        String rankName = args[1];
        OfflinePlayer target = Bukkit.getOfflinePlayer(targetName);
        
        // Only block if the target has NEVER joined AND is not currently online.
        // This allows setting ranks for:
        //  - Online players on their first session
        //  - Offline players who have joined before
        if (!target.hasPlayedBefore() && !target.isOnline()) {
            player.sendMessage(ChatColor.RED + "Player " + targetName + " has never joined the server.");
            return true;
        }

        Rank rank = Rank.fromString(rankName);
        if (rank == null) {
            player.sendMessage(ChatColor.RED + "Invalid rank. Available ranks: " + getAvailableRanksFromConfig());
            return true;
        }

        UUID targetUuid = target.getUniqueId();
        Rank currentRank = rankManager.getRank(targetUuid);
        
        if (rank == currentRank) {
            player.sendMessage(ChatColor.YELLOW + targetName + " already has the " + rank.getDisplay() + ChatColor.YELLOW + " rank.");
            return true;
        }

        // Set the rank
        rankManager.setRank(targetUuid, rank, false);
        player.sendMessage(ChatColor.GREEN + "Set " + targetName + "'s rank to " + rank.getDisplay() + ChatColor.GREEN + ".");

        // Notify the target if they're online
        Player onlineTarget = target.getPlayer();
        if (onlineTarget != null) {
            onlineTarget.sendMessage(ChatColor.GREEN + "Your rank has been updated to " + rank.getDisplay() + ChatColor.GREEN + " by " + 
                                   player.getDisplayName() + ChatColor.GREEN + ".");
        }

        return true;
    }

    private void sendUsage(Player player) {
        player.sendMessage(ChatColor.RED + "Usage: /rankify <player> <rank>");
        player.sendMessage(ChatColor.YELLOW + "Available ranks: " + getAvailableRanksFromConfig());
        if (player.hasPermission("rankify.gui")) {
            player.sendMessage(ChatColor.YELLOW + "Or use: /rankify gui");
        }
        if (player.hasPermission("rankify.reload")) {
            player.sendMessage(ChatColor.YELLOW + "Admin: /rankify reload");
        }
    }

    public RankManager getRankManager() {
        return rankManager;
    }

    private String getAvailableRanksFromConfig() {
        // Read ranks list from config: ranks: - { arg: "..." }
        List<Map<?, ?>> list = main.getConfig().getMapList("ranks");
        if (list == null || list.isEmpty()) {
            return Rank.getAvailableRanks();
        }
        List<String> args = list.stream()
                .map(m -> m.get("arg"))
                .filter(v -> v instanceof String && !((String) v).isEmpty())
                .map(v -> ((String) v).toLowerCase())
                .collect(Collectors.toList());
        if (args.isEmpty()) {
            return Rank.getAvailableRanks();
        }
        return String.join(", ", args);
    }
}

package org.powell.rankify.main.Listeners;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.permissions.PermissionAttachment;
import org.powell.rankify.main.Enums.Rank;
import org.powell.rankify.main.Main;
import org.powell.rankify.main.Managers.RankManager;

import java.util.UUID;

public class RankListener implements Listener {
    private final Main main;
    private final RankManager rankManager;
    
    public RankListener(Main main) {
        this.main = main;
        this.rankManager = main.getRankManager();
    }
    
    @EventHandler(priority = EventPriority.HIGH)
    public void onJoin(PlayerJoinEvent e) {
        Player player = e.getPlayer();
        UUID playerId = player.getUniqueId();
        
        try {
            // Set default rank for new players
            if (!player.hasPlayedBefore()) {
                rankManager.setRank(playerId, Rank.GUEST, true);
            } 
            // Ensure existing players have a valid rank
            else if (rankManager.getRank(playerId) == null) {
                main.getLogger().warning("Player " + player.getName() + " had no rank, setting to GUEST");
                rankManager.setRank(playerId, Rank.GUEST, false);
            }
            
            // Update name tag and permissions
            updatePlayerRank(player);
            
        } catch (Exception ex) {
            main.getLogger().severe("Error handling player join for " + player.getName() + ": " + ex.getMessage());
            if (main.getConfig().getBoolean("debug", false)) {
                ex.printStackTrace();
            }
        }
    }
    
    private void updatePlayerRank(Player player) {
        UUID playerId = player.getUniqueId();
        
        try {
            // Update nametag
            if (main.getNametagManager() != null) {
                main.getNametagManager().setNametags(player);
                main.getNametagManager().newTag(player);
            }
            
            // Update permissions
            PermissionAttachment attachment = rankManager.getPerm().get(playerId);
            if (attachment == null) {
                attachment = player.addAttachment(main);
                rankManager.getPerm().put(playerId, attachment);
            }
            
            // Clear existing permissions
            for (String perm : attachment.getPermissions().keySet()) {
                attachment.unsetPermission(perm);
            }
            
            // Set new permissions
            Rank rank = rankManager.getRank(playerId);
            if (rank != null) {
                for (String perm : rank.getPerms()) {
                    if (perm != null && !perm.trim().isEmpty()) {
                        attachment.setPermission(perm, true);
                    }
                }
            }
            
            player.recalculatePermissions();
            
        } catch (Exception e) {
            main.getLogger().warning("Failed to update rank for " + player.getName() + ": " + e.getMessage());
            if (main.getConfig().getBoolean("debug", false)) {
                e.printStackTrace();
            }
        }
    }
    
    @EventHandler(priority = EventPriority.MONITOR)
    public void onQuit(PlayerQuitEvent e) {
        try {
            Player player = e.getPlayer();
            if (main.getNametagManager() != null) {
                main.getNametagManager().removeTag(player);
            }
            // Clean up permissions
            rankManager.getPerm().remove(player.getUniqueId());
        } catch (Exception ex) {
            main.getLogger().warning("Error handling player quit: " + ex.getMessage());
        }
    }
    
    @EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
    public void onChat(AsyncPlayerChatEvent e) {
        Player player = e.getPlayer();
        try {
            Rank rank = rankManager.getRank(player.getUniqueId());
            if (rank == null) {
                rank = Rank.GUEST;
                main.getLogger().warning(player.getName() + " had no rank, defaulting to GUEST for chat");
            }
            
            String format = String.format("%s %s: %s%s",
                rank.getDisplay(),
                player.getName(),
                ChatColor.GRAY,
                e.getMessage()
            );
            
            e.setFormat(format);
        } catch (Exception ex) {
            // Fallback to default format if something goes wrong
            e.setFormat("%s: %s");
            main.getLogger().warning("Error formatting chat for " + player.getName() + ": " + ex.getMessage());
        }
    }
}

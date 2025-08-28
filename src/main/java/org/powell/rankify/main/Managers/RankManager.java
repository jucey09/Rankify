package org.powell.rankify.main.Managers;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionAttachment;
import org.powell.rankify.main.Enums.Rank;
import org.powell.rankify.main.Main;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.UUID;

public class RankManager {
private File file;
private Main main;
private YamlConfiguration config;
private HashMap<UUID, PermissionAttachment> perm = new HashMap<>();
    private final PermissionsBridge permissionsBridge;

    public RankManager(Main main){
        this.main = main;
         if (!main.getDataFolder().exists()){
             main.getDataFolder().mkdir();
         }
         file = new File(main.getDataFolder(), "ranks.yml");
         if (!file.exists()){
             try {
                 file.createNewFile();
             } catch (IOException e) {
                e.printStackTrace();
             }
         }
        config = YamlConfiguration.loadConfiguration(file);
        this.permissionsBridge = new PermissionsBridge(main);
    }
    public void setRank(UUID uuid, Rank rank, boolean firstJoin) {
        if (uuid == null) {
            throw new IllegalArgumentException("UUID cannot be null");
        }
        if (rank == null) {
            throw new IllegalArgumentException("Rank cannot be null");
        }

        OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(uuid);
        
        // If this is a new player and no rank is specified, use default
        if (firstJoin && !config.contains(uuid.toString())) {
            rank = Rank.GUEST;
        }

        // Handle permission updates for online players
        if (offlinePlayer.isOnline()) {
            Player player = offlinePlayer.getPlayer();
            if (player != null) {
                // Remove existing permissions
                PermissionAttachment attachment = perm.get(uuid);
                if (attachment == null) {
                    attachment = player.addAttachment(main);
                    perm.put(uuid, attachment);
                }

                // Clear old permissions if not first join
                if (!firstJoin) {
                    Rank oldRank = getRank(uuid);
                    if (oldRank != null) {
                        for (String perm : oldRank.getPerms()) {
                            attachment.unsetPermission(perm);
                        }
                    }
                }

                // Add new permissions
                for (String perm : rank.getPerms()) {
                    if (perm != null && !perm.isEmpty()) {
                        attachment.setPermission(perm, true);
                    }
                }
                
                // Update player's permissions
                player.recalculatePermissions();
            }
        }

        // Save to config
        config.set(uuid.toString(), rank.name());
        try {
            config.save(file);
        } catch (IOException e) {
            main.getLogger().severe("Could not save rank data for " + uuid + ": " + e.getMessage());
            if (main.getConfig().getBoolean("debug", false)) {
                e.printStackTrace();
            }
        }

        // Apply external permissions group via supported plugins (LuckPerms/PEX)
        try {
            permissionsBridge.setGroup(uuid, rank);
        } catch (Exception e) {
            main.getLogger().warning("Failed to apply permissions group for " + uuid + ": " + e.getMessage());
        }

        // Update nametag if player is online
        if (offlinePlayer.isOnline()) {
            Player player = offlinePlayer.getPlayer();
            if (player != null) {
                try {
                    if (main.getNametagManager() != null) {
                        main.getNametagManager().removeTag(player);
                        main.getNametagManager().newTag(player);
                    }
                } catch (Exception e) {
                    main.getLogger().warning("Failed to update nametag for " + player.getName() + ": " + e.getMessage());
                }
            }
        }
    }
    /**
     * Get a player's rank
     * @param uuid The player's UUID
     * @return The player's rank, or GUEST if not found
     */
    public Rank getRank(UUID uuid) {
        if (uuid == null) {
            return Rank.GUEST;
        }

        String rankName = config.getString(uuid.toString());
        if (rankName == null || rankName.isEmpty()) {
            return Rank.GUEST;
        }

        try {
            return Rank.fromString(rankName);
        } catch (IllegalArgumentException e) {
            main.getLogger().warning("Invalid rank name '" + rankName + "' for UUID " + uuid);
            return Rank.GUEST;
        }
    }

    public HashMap<UUID, PermissionAttachment> getPerm() {return perm;}
}

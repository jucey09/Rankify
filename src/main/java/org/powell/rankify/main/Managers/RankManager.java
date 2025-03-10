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

    public RankManager(Main main){
        this.main = main;
         if (!main.getDataFolder().exists()){
             main.getDataFolder().mkdir();
         }
         file = new File(main.getDataFolder(), "config.yml");
         if (!file.exists()){
             try {
                 file.createNewFile();
             } catch (IOException e) {
                e.printStackTrace();
             }
         }
        config = YamlConfiguration.loadConfiguration(file);
    }
    public void setRank(UUID uuid, Rank rank, boolean firstJoin) {
        OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(uuid);

        if (!config.contains(uuid.toString())) {
            rank = Rank.Guest;
        }

        if (offlinePlayer.isOnline() && !firstJoin) {
            Player player = offlinePlayer.getPlayer();
            if (player == null) return;

            PermissionAttachment attachment = perm.getOrDefault(uuid, player.addAttachment(main));
            perm.put(uuid, attachment);

            Rank oldRank = getRank(uuid);
            if (oldRank != null) {
                for (String oldPerm : oldRank.getPerms()) {
                    attachment.unsetPermission(oldPerm);
                }
            }
            for (String newPerm : rank.getPerms()) {
                attachment.setPermission(newPerm, true);
            }
        }
        config.set(uuid.toString(), rank.name());
        try {
            config.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (offlinePlayer.isOnline()) {
            Player player = offlinePlayer.getPlayer();
            if (player != null) {
                main.getNametagManager().removeTag(player);
                main.getNametagManager().newTag(player);
            }
        }
    }
    public Rank getRank(UUID uuid) {
        String rankName = config.getString(uuid.toString());

        if (rankName == null || rankName.isEmpty()) {
            System.out.println("Rank is null or empty for player UUID: " + uuid);
            return Rank.Guest;
        }

        try {
            return Rank.valueOf(rankName);
        } catch (IllegalArgumentException e) {
            System.out.println("Invalid rank name for player UUID " + uuid + ": " + rankName);
            return Rank.Guest;
        }
    }

    public HashMap<UUID, PermissionAttachment> getPerm() {return perm;}
}

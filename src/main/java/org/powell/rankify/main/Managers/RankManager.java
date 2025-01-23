package org.powell.rankify.main.Managers;

import org.bukkit.Bukkit;
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
    public void setRank(UUID uuid, Rank rank, Boolean firstJoin){
        if(Bukkit.getOfflinePlayer(uuid).isOnline() && !firstJoin){
            PermissionAttachment attachment;
            Player player = Bukkit.getPlayer(uuid);
            if (perm.containsKey(uuid)){
                attachment = perm.get(uuid);
            } else {
                attachment = player.addAttachment(main);
                perm.put(uuid, attachment);
            }
        }
        config.set(uuid.toString(), rank.name());
        try {
            config.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (Bukkit.getOfflinePlayer(uuid).isOnline()){
            Player player = Bukkit.getPlayer(uuid);
            main.getNametagManager().removeTag(player);
            assert player != null;
            main.getNametagManager().newTag(player);
        }

    }
    public Rank getRank(UUID uuid) {
        return Rank.valueOf(config.getString(uuid.toString()));
    }
    public HashMap<UUID, PermissionAttachment> getPerm() {return perm;}


}

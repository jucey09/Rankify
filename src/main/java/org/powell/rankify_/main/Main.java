package org.powell.rankify_.main;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.powell.rankify_.main.Commands.RankCommand;
import org.powell.rankify_.main.Listeners.RankListener;
import org.powell.rankify_.main.Managers.NametagManager;
import org.powell.rankify_.main.Managers.RankManager;

public final class Main extends JavaPlugin {
private RankManager rankManager;
private NametagManager nametagManager;
    @Override
    public void onEnable() {
        // Plugin startup logic
        getCommand("rank").setExecutor(new RankCommand(this));

        rankManager = new RankManager(this);
        nametagManager = new NametagManager(this);

        Bukkit.getPluginManager().registerEvents(new RankListener(this), this);
    }

    public RankManager getRankManager() {return rankManager;}
    public NametagManager getNametagManager() {return nametagManager;}
}

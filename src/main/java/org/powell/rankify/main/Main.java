package org.powell.rankify.main;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.powell.rankify.main.Commands.RankCommand;
import org.powell.rankify.main.Listeners.RankGuiListener;
import org.powell.rankify.main.Listeners.RankListener;
import org.powell.rankify.main.Listeners.SetRankGuiListener;
import org.powell.rankify.main.Managers.NametagManager;
import org.powell.rankify.main.Managers.RankManager;

public final class Main extends JavaPlugin {
private RankManager rankManager;
private NametagManager nametagManager;
    @Override
    public void onEnable() {
        // Plugin startup logic
        getCommand("rankify").setExecutor(new RankCommand(this, rankManager));

        rankManager = new RankManager(this);
        nametagManager = new NametagManager(this);

        Bukkit.getPluginManager().registerEvents(new RankGuiListener(), this);
        Bukkit.getPluginManager().registerEvents(new SetRankGuiListener(), this);
        Bukkit.getPluginManager().registerEvents(new RankListener(this), this);
    }

    public RankManager getRankManager() {return rankManager;}
    public NametagManager getNametagManager() {return nametagManager;}

}

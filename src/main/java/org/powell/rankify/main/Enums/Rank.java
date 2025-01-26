package org.powell.rankify.main.Enums;

import org.bukkit.ChatColor;

public enum Rank {
    Owner(ChatColor.DARK_RED + "Owner"),
    CoOwner(ChatColor.DARK_GREEN + "CoOwner"),
    Youtube(ChatColor.GOLD + "Youtube"),
    Admin(ChatColor.DARK_PURPLE + "Admin"),
    Moderator(ChatColor.GREEN + "Moderator"),
    Member(ChatColor.AQUA + "Member"),
    Guest(ChatColor.GRAY + "Guest");

    private String display;
    Rank(String display){ this.display = display; }
    public String getDisplay() { return display;}
}

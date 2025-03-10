package org.powell.rankify.main.Enums;

import org.bukkit.ChatColor;

public enum Rank {
    Owner(ChatColor.DARK_RED + "Owner", new String[]{"rankify.setrank"}),
    CoOwner(ChatColor.DARK_GREEN + "CoOwner", new String[]{"rankify.setrank"}),
    Youtube(ChatColor.GOLD + "Youtube", new String[]{}),
    Admin(ChatColor.DARK_PURPLE + "Admin", new String[]{"rankify.setrank"}),
    Moderator(ChatColor.GREEN + "Moderator", new String[]{"rankify.setrank"}),
    Member(ChatColor.AQUA + "Member", new String[]{}),
    Guest(ChatColor.GRAY + "Guest", new String[]{}),;

    private String display;
    private String[] perms;

    Rank(String display, String[] perms) {
        this.display = display;
        this.perms = perms;
    }
    public String getDisplay() { return display;}
    public String[] getPerms() { return perms;}
}

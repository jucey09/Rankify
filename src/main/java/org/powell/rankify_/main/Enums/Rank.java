package org.powell.rankify_.main.Enums;

import org.bukkit.ChatColor;

public enum Rank {
    Owner(ChatColor.DARK_RED + "Owner"),
    Admin(ChatColor.RED + "Admin"),
    Member(ChatColor.AQUA + "Member"),
    Guest(ChatColor.GRAY + "Guest");

    private String display;
    Rank(String display){
        this.display = display;
    }
    public String getDisplay() {return display;}
}

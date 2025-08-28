package org.powell.rankify.main.Enums;

import org.bukkit.ChatColor;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public enum Rank {
    OWNER(ChatColor.DARK_RED + "Owner", new String[]{"rankify.setrank", "rankify.reload"}),
    COOWNER(ChatColor.DARK_GREEN + "CoOwner", new String[]{"rankify.setrank", "rankify.reload"}),
    YOUTUBE(ChatColor.GOLD + "YouTube", new String[]{}),
    ADMIN(ChatColor.DARK_PURPLE + "Admin", new String[]{"rankify.setrank", "rankify.reload"}),
    MODERATOR(ChatColor.GREEN + "Moderator", new String[]{"rankify.setrank", "rankify.reload"}),
    HELPER(ChatColor.GOLD + "Helper", new String[]{}),
    DONOR(ChatColor.AQUA + "Donor", new String[]{}),
    DISCORD(ChatColor.BLUE + "Discord", new String[]{}),
    STRONGEST(ChatColor.YELLOW + "Strongest", new String[]{}),
    EGIRL(ChatColor.LIGHT_PURPLE + "Egirl", new String[]{}),
    SUS(ChatColor.DARK_GREEN + "Sus", new String[]{}),
    MEMBER(ChatColor.AQUA + "Member", new String[]{}),
    GUEST(ChatColor.GRAY + "Guest", new String[]{});

    private static final Map<String, Rank> BY_NAME = new HashMap<>();
    
    static {
        for (Rank rank : values()) {
            BY_NAME.put(rank.name(), rank);
            BY_NAME.put(rank.name().toLowerCase(), rank);
            BY_NAME.put(rank.name().replace("_", ""), rank);
            BY_NAME.put(rank.name().replace("_", "").toLowerCase(), rank);
        }
    }

    private final String display;
    private final String[] perms;

    Rank(String display, String[] perms) {
        this.display = display;
        this.perms = perms != null ? perms : new String[0];
    }

    public String getDisplay() {
        return display;
    }

    public String[] getPerms() {
        return perms.clone();
    }
    
    @Nullable
    public static Rank fromString(String name) {
        if (name == null || name.isEmpty()) {
            return null;
        }
        return BY_NAME.get(name.toUpperCase().replace("-", "_").replace(" ", "_"));
    }
    
    public static String getAvailableRanks() {
        return String.join(", ", Arrays.stream(values())
                .map(Enum::name)
                .map(String::toLowerCase)
                .toArray(String[]::new));
    }
}

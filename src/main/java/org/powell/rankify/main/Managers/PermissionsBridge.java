package org.powell.rankify.main.Managers;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.ConsoleCommandSender;
import org.jetbrains.annotations.NotNull;
import org.powell.rankify.main.Enums.Rank;
import org.powell.rankify.main.Main;

import java.util.Locale;
import java.util.UUID;

/**
 * Lightweight integration layer that talks to permissions plugins via console commands.
 *
 * Supported:
 *  - LuckPerms: lp user <uuid> parent set <group>
 *  - PermissionsEx: pex user <name> group set <group>
 *
 * We avoid a compile-time dependency and still support offline players.
 */
public final class PermissionsBridge {
    private final Main main;

    public PermissionsBridge(Main main) {
        this.main = main;
    }

    public boolean isLuckPermsPresent() {
        return Bukkit.getPluginManager().getPlugin("LuckPerms") != null;
    }

    public boolean isPermissionsExPresent() {
        return Bukkit.getPluginManager().getPlugin("PermissionsEx") != null
                || Bukkit.getPluginManager().getPlugin("Permissions") != null; // some forks register like this
    }

    /**
     * Map a Rank to a permissions group name. Default is enum name lowercased.
     * You can adjust this if you want custom group ids.
     */
    @NotNull
    public String toGroup(Rank rank) {
        if (rank == null) return "guest";
        return rank.name().toLowerCase(Locale.ROOT);
    }

    /**
     * Set the player's group in the detected permissions plugin. If both LP and PEX exist,
     * LuckPerms is preferred.
     */
    public void setGroup(UUID uuid, Rank rank) {
        if (uuid == null || rank == null) return;
        String group = toGroup(rank);
        ConsoleCommandSender console = Bukkit.getConsoleSender();

        // Prefer LuckPerms
        if (isLuckPermsPresent()) {
            // Overwrite parents with the target group.
            String cmd = String.format("lp user %s parent set %s", uuid, group);
            Bukkit.dispatchCommand(console, cmd);

            // Optionally, set a nice display name/meta if you use it (commented out by default):
            // Bukkit.dispatchCommand(console, String.format("lp user %s meta setprefix \"%s\"", uuid, rank.getDisplay()));
            logApplied("LuckPerms", uuid, group);
            return;
        }

        // Fallback to PermissionsEx by name
        if (isPermissionsExPresent()) {
            OfflinePlayer off = Bukkit.getOfflinePlayer(uuid);
            String name = off != null && off.getName() != null ? off.getName() : uuid.toString();
            String cmd = String.format("pex user %s group set %s", name, group);
            Bukkit.dispatchCommand(console, cmd);
            logApplied("PermissionsEx", uuid, group);
            return;
        }

        // Neither plugin present
        main.getLogger().warning("No supported permissions plugin detected (LuckPerms/PermissionsEx). Skipping group apply for " + uuid);
    }

    private void logApplied(String platform, UUID uuid, String group) {
        if (main.getConfig().getBoolean("debug", false)) {
            main.getLogger().info("[PermissionsBridge] Applied group '" + group + "' to " + uuid + " via " + platform);
        }
    }
}

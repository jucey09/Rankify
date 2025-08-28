package org.powell.rankify.main.Listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import java.util.List;
import java.util.Map;

public class SetRankGuiListener implements Listener {
    private final org.powell.rankify.main.Main plugin;

    public SetRankGuiListener(org.powell.rankify.main.Main plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent e) {
        Inventory inv = e.getInventory();
        // Cancel all clicks if this is the SetRank GUI (size 36, and item 1 is the special pane)
        if (inv.getItem(1) != null && inv.getSize() == 36) {
            e.setCancelled(true);
            // Handle Close Button (slot 0, BARRIER) for this GUI
            if (e.getRawSlot() == 0 && e.getCurrentItem() != null && e.getCurrentItem().getType() == Material.BARRIER) {
                Player player = (Player) e.getWhoClicked();
                player.closeInventory();
                return;
            }
            if (inv.getItem(1).getAmount() == 90) {
                String pname = ChatColor.stripColor(inv.getItem(1).getItemMeta().getDisplayName());
                if (e.getCurrentItem() != null && ChatColor.stripColor(e.getView().getTitle()).equals(pname)) {
                    Player player = (Player) e.getWhoClicked();
                    String player_name = ChatColor.stripColor(e.getView().getTitle());
                    int clicked = e.getRawSlot();
                    // Read ranks from config and find matching slot
                    List<Map<?, ?>> cfgRanks = plugin.getConfig().getMapList("ranks");
                    for (Map<?, ?> sec : cfgRanks) {
                        Object slotObj = sec.get("slot");
                        if (!(slotObj instanceof Number)) continue;
                        int slot = ((Number) slotObj).intValue();
                        if (slot == clicked) {
                            Object argObj = sec.get("arg");
                            String arg = argObj == null ? "" : String.valueOf(argObj);
                            if (!arg.isEmpty()) {
                                player.performCommand("rankify " + player_name + " " + arg);
                                player.closeInventory();
                            }
                            return;
                        }
                    }
                }
            }
        }
    }
}

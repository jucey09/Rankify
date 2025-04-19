package org.powell.rankify.main.Listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;

public class SetRankGuiListener implements Listener {

    @EventHandler
    public void onInventoryClick(InventoryClickEvent e) {
        Inventory inv = e.getInventory();
        // Cancel all clicks if this is the SetRank GUI (size 36, and item 1 is the special pane)
        if (inv.getItem(1) != null && inv.getSize() == 36) {
            e.setCancelled(true);
            if (inv.getItem(1).getAmount() == 90) {
                String pname = inv.getItem(1).getItemMeta().getDisplayName();
                if (e.getCurrentItem() != null && e.getView().getTitle().equals(pname)) {
                    Player player = (Player) e.getWhoClicked();
                    String player_name = e.getView().getTitle();
                    switch (e.getRawSlot()) {
                        case 10:
                            player.performCommand("rankify " + player_name + " owner");
                            break;
                        case 11:
                            player.performCommand("rankify " + player_name + " coowner");
                            break;
                        case 12:
                            player.performCommand("rankify " + player_name + " youtube");
                            break;
                        case 13:
                            player.performCommand("rankify " + player_name + " admin");
                            break;
                        case 14:
                            player.performCommand("rankify " + player_name + " moderator");
                            break;
                        case 15:
                            player.performCommand("rankify " + player_name + " dono");
                            break;
                        case 16:
                            player.performCommand("rankify " + player_name + " discord");
                            break;
                        case 19:
                            player.performCommand("rankify " + player_name + " helper");
                            break;
                        case 20:
                            player.performCommand("rankify " + player_name + " strongest");
                            break;
                        case 21:
                            player.performCommand("rankify " + player_name + " egirl");
                            break;
                        case 22:
                            player.performCommand("rankify " + player_name + " sus");
                            break;
                        case 23:
                            player.performCommand("rankify " + player_name + " member");
                            break;
                        case 24:
                            player.performCommand("rankify " + player_name + " guest");
                            break;
                        default:
                            return;
                    }
                    player.closeInventory();
                }
            }
        }
    }
}

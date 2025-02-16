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
        if (inv.getItem(1) != null) {

            if (inv.getItem(1).getAmount() == 69) {
                String pname = inv.getItem(1).getItemMeta().getDisplayName();
                if (inv.getSize() == 27 && e.getCurrentItem() != null && e.getView().getTitle().equals(pname)) {
                    e.setCancelled(true);
                    Player player = (Player) e.getWhoClicked();
                    String player_name = e.getView().getTitle();
                    switch (e.getRawSlot()) {
                        case 0:
                            break;
                        case 10: //GUEST
                            player.performCommand("rankify " + player_name + " guest");
                            break;
                        case 11://MEMBER
                            player.performCommand("rankify " + player_name + " member");
                            break;
                        case 12://MODERATOR
                            player.performCommand("rankify " + player_name + " moderator");
                            break;
                        case 13://ADMIN
                            player.performCommand("rankify " + player_name + " admin");
                            break;
                        case 14://YOUTUBE
                            player.performCommand("rankify " + player_name + " youtube");
                            break;
                        case 15://CO-OWNER
                            player.performCommand("rankify " + player_name + " coowner");
                            break;
                        case 16://OWNER
                            player.performCommand("rankify " + player_name + " owner");
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

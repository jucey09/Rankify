package org.powell.rankify.main.Listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

public class SetRankGuiListener implements Listener {

    @EventHandler
    public void onInventoryClick(InventoryClickEvent e){

        if (e.getInventory().getSize() == 27 && e.getCurrentItem() != null){
            Player player = (Player) e.getWhoClicked();
            String player_name = e.getView().getTitle();
            switch (e.getRawSlot()){
                case 0:
                    break;
                case 10: //GUEST
                    player.performCommand("/rankify " + player_name + " guest");
                    player.sendMessage(player_name);
                    break;
                case 11://MEMBER
                    player.performCommand("/rankify " + player_name + " member");
                    break;
                case 12://MODERATOR
                    player.performCommand("/rankify " + player_name + " moderator");
                    break;
                case 13://ADMIN
                    player.performCommand("/rankify " + player_name + " admin");
                    break;
                case 14://YOUTUBE
                    player.performCommand("/rankify " + player_name + " youtube");
                    break;
                case 15://CO-OWNER
                    player.performCommand("/rankify " + player_name + " co_owner");
                    break;
                case 16://OWNER
                    player.performCommand("/rankify " + player_name + " owner");
                    break;
                default:
                    return;
            }
            player.closeInventory();
        }
    }

}

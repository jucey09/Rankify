package org.powell.rankify.main.Listeners;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;

public class RankGuiListener implements Listener {

    @EventHandler
    public void onInventoryClick(InventoryClickEvent e){
        if (e.getView().getTitle().equals(ChatColor.DARK_AQUA + "Rankify Menu") && e.getCurrentItem() != null){
            Player player = (Player) e.getWhoClicked();

            ItemStack clickedItem = e.getCurrentItem();
            if (clickedItem == null || clickedItem.getType() == Material.AIR
                    || !clickedItem.hasItemMeta()) {
                e.setCancelled(true);
                return;
            }
            e.setCancelled(true);
            if (e.getCurrentItem().getType() == Material.PLAYER_HEAD) {
                String player_name = clickedItem.getItemMeta().getDisplayName();
                Inventory inv = Bukkit.createInventory(player, 27, player_name);
                //RANKS\\

                //OWNER
                ItemStack owner = new ItemStack(Material.RED_CONCRETE);
                ItemMeta ownermeta = owner.getItemMeta();
                ownermeta.setDisplayName(ChatColor.DARK_RED + "Owner");
                owner.setItemMeta(ownermeta);

                inv.setItem(16, owner);
                //CO-OWNER
                ItemStack co_owner = new ItemStack(Material.GREEN_CONCRETE);
                ItemMeta co_ownermeta = co_owner.getItemMeta();
                co_ownermeta.setDisplayName(ChatColor.DARK_GREEN + "CoOwner");
                co_owner.setItemMeta(co_ownermeta);

                inv.setItem(15, co_owner);
                //YOUTUBE
                ItemStack yt = new ItemStack(Material.YELLOW_CONCRETE);
                ItemMeta ytmeta = yt.getItemMeta();
                ytmeta.setDisplayName(ChatColor.GOLD + "Youtube");
                yt.setItemMeta(ytmeta);

                inv.setItem(14, yt);
                //ADMIN
                ItemStack admin = new ItemStack(Material.PURPLE_CONCRETE);
                ItemMeta adminmeta = admin.getItemMeta();
                adminmeta.setDisplayName(ChatColor.DARK_PURPLE + "Admin");
                admin.setItemMeta(adminmeta);

                inv.setItem(13, admin);
                //MODERATOR
                ItemStack mod = new ItemStack(Material.LIME_CONCRETE);
                ItemMeta modmeta = mod.getItemMeta();
                modmeta.setDisplayName(ChatColor.GREEN + "Moderator");
                mod.setItemMeta(modmeta);

                inv.setItem(12, mod);
                //MEMBER
                ItemStack member = new ItemStack(Material.LIGHT_BLUE_CONCRETE);
                ItemMeta membermeta = owner.getItemMeta();
                membermeta.setDisplayName(ChatColor.AQUA + "Member");
                member.setItemMeta(membermeta);

                inv.setItem(11, member);
                //GUEST
                ItemStack guest = new ItemStack(Material.GRAY_CONCRETE);
                ItemMeta guestmeta = owner.getItemMeta();
                guestmeta.setDisplayName(ChatColor.GRAY + "Guest");
                guest.setItemMeta(guestmeta);

                inv.setItem(10, guest);

                //CLOSE
                ItemStack close = new ItemStack(Material.BARRIER);
                ItemMeta closemeta = close.getItemMeta();
                closemeta.setDisplayName(ChatColor.RED + "Close Button");

                close.setItemMeta(closemeta);

                inv.setItem(0, close);

                ItemStack frame = new ItemStack(Material.GRAY_STAINED_GLASS_PANE);
                ItemMeta fmeta = frame.getItemMeta();
                fmeta.setDisplayName(ChatColor.GRAY + "_");
                fmeta.setLore(Arrays.asList(""));
                frame.setItemMeta(fmeta);
                for (int i : new int[]{1,2,3,4,5,6,7,8,9,17,18,19,20,21,22,23,24,25,26}){
                    inv.setItem(i, frame);
                }

                player.openInventory(inv);
            }
            if (clickedItem.getType() == Material.BARRIER && clickedItem.hasItemMeta()){
                player.closeInventory();
            }
            if (clickedItem.getType() == Material.GRAY_STAINED_GLASS_PANE && clickedItem.hasItemMeta()){
                e.setCancelled(true);
            }

        }
    }
}

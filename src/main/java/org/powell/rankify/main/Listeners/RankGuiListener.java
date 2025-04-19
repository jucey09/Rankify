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
            e.setCancelled(true);
            if (e.getCurrentItem().getType() == Material.PLAYER_HEAD) {
                String player_name = clickedItem.getItemMeta().getDisplayName();
                Inventory inv = Bukkit.createInventory(player, 36, player_name);

                // CLOSE BUTTON at slot 0
                ItemStack close = new ItemStack(Material.BARRIER);
                ItemMeta closemeta = close.getItemMeta();
                closemeta.setDisplayName(ChatColor.RED + "Close Button");
                close.setItemMeta(closemeta);
                inv.setItem(0, close);

                // PLAYER INFO ITEM at slot 1
                ItemStack p = new ItemStack(Material.WHITE_STAINED_GLASS_PANE, 90);
                ItemMeta pmeta = p.getItemMeta();
                pmeta.setDisplayName(player_name);
                pmeta.setMaxStackSize(1);
                p.setItemMeta(pmeta);
                inv.setItem(1, p);

                // FRAME all around except rank slots, close, slot 1, and slot 25
                ItemStack frame = new ItemStack(Material.GRAY_STAINED_GLASS_PANE);
                ItemMeta fmeta = frame.getItemMeta();
                fmeta.setDisplayName(ChatColor.GRAY + "_");
                fmeta.setLore(Arrays.asList(""));
                frame.setItemMeta(fmeta);
                int[] rankSlots = {10, 11, 12, 13, 14, 15, 16, 19, 20, 21, 22, 23, 24};
                for (int i = 0; i < 36; i++) {
                    if (i == 0 || i == 1 || i == 25) continue;
                    boolean isRankSlot = false;
                    for (int r : rankSlots) {
                        if (i == r) { isRankSlot = true; break; }
                    }
                    if (!isRankSlot) {
                        inv.setItem(i, frame);
                    }
                }

                // RANKS in the specified slots, ordered as requested
                ItemStack[] rankItems = new ItemStack[13];
                // 0 Owner
                ItemStack owner = new ItemStack(Material.RED_CONCRETE);
                ItemMeta ownermeta = owner.getItemMeta();
                ownermeta.setDisplayName(ChatColor.DARK_RED + "Owner");
                owner.setItemMeta(ownermeta);
                rankItems[0] = owner;
                // 1 CoOwner
                ItemStack co_owner = new ItemStack(Material.GREEN_CONCRETE);
                ItemMeta co_ownermeta = co_owner.getItemMeta();
                co_ownermeta.setDisplayName(ChatColor.DARK_GREEN + "CoOwner");
                co_owner.setItemMeta(co_ownermeta);
                rankItems[1] = co_owner;
                // 2 Youtube
                ItemStack yt = new ItemStack(Material.YELLOW_CONCRETE);
                ItemMeta ytmeta = yt.getItemMeta();
                ytmeta.setDisplayName(ChatColor.GOLD + "Youtube");
                yt.setItemMeta(ytmeta);
                rankItems[2] = yt;
                // 3 Admin
                ItemStack admin = new ItemStack(Material.PURPLE_CONCRETE);
                ItemMeta adminmeta = admin.getItemMeta();
                adminmeta.setDisplayName(ChatColor.DARK_PURPLE + "Admin");
                admin.setItemMeta(adminmeta);
                rankItems[3] = admin;
                // 4 Moderator
                ItemStack mod = new ItemStack(Material.LIME_CONCRETE);
                ItemMeta modmeta = mod.getItemMeta();
                modmeta.setDisplayName(ChatColor.GREEN + "Moderator");
                mod.setItemMeta(modmeta);
                rankItems[4] = mod;
                // 5 DONO
                ItemStack dono = new ItemStack(Material.LIGHT_BLUE_CONCRETE);
                ItemMeta donometa = dono.getItemMeta();
                donometa.setDisplayName(ChatColor.AQUA + "DONO");
                dono.setItemMeta(donometa);
                rankItems[5] = dono;
                // 6 Discord
                ItemStack discord = new ItemStack(Material.BLUE_CONCRETE);
                ItemMeta discordmeta = discord.getItemMeta();
                discordmeta.setDisplayName(ChatColor.BLUE + "Discord");
                discord.setItemMeta(discordmeta);
                rankItems[6] = discord;
                // 7 Helper
                ItemStack helper = new ItemStack(Material.ORANGE_CONCRETE);
                ItemMeta helpermeta = helper.getItemMeta();
                helpermeta.setDisplayName(ChatColor.GOLD + "Helper");
                helper.setItemMeta(helpermeta);
                rankItems[7] = helper;
                // 8 STRONGEST
                ItemStack strongest = new ItemStack(Material.YELLOW_CONCRETE);
                ItemMeta strongestmeta = strongest.getItemMeta();
                strongestmeta.setDisplayName(ChatColor.YELLOW + "STRONGEST");
                strongest.setItemMeta(strongestmeta);
                rankItems[8] = strongest;
                // 9 Egirl
                ItemStack egirl = new ItemStack(Material.MAGENTA_CONCRETE);
                ItemMeta egirlmeta = egirl.getItemMeta();
                egirlmeta.setDisplayName(ChatColor.LIGHT_PURPLE + "Egirl");
                egirl.setItemMeta(egirlmeta);
                rankItems[9] = egirl;
                // 10 Sus
                ItemStack sus = new ItemStack(Material.GREEN_CONCRETE);
                ItemMeta susmeta = sus.getItemMeta();
                susmeta.setDisplayName(ChatColor.DARK_GREEN + "Sus");
                sus.setItemMeta(susmeta);
                rankItems[10] = sus;
                // 11 Member
                ItemStack member = new ItemStack(Material.LIGHT_BLUE_CONCRETE);
                ItemMeta membermeta = member.getItemMeta();
                membermeta.setDisplayName(ChatColor.AQUA + "Member");
                member.setItemMeta(membermeta);
                rankItems[11] = member;
                // 12 Guest
                ItemStack guest = new ItemStack(Material.GRAY_CONCRETE);
                ItemMeta guestmeta = guest.getItemMeta();
                guestmeta.setDisplayName(ChatColor.GRAY + "Guest");
                guest.setItemMeta(guestmeta);
                rankItems[12] = guest;
                // Place ranks in slots 10,11,12,13,14,15,16,19,20,21,22,23,24
                int[] rankSlotOrder = {10,11,12,13,14,15,16,19,20,21,22,23,24};
                for (int i = 0; i < rankItems.length; i++) {
                    inv.setItem(rankSlotOrder[i], rankItems[i]);
                }

                // Slot 25 left empty intentionally

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

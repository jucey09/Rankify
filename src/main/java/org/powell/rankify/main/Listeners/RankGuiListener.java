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
import java.util.List;
import java.util.Map;

public class RankGuiListener implements Listener {
    private final org.powell.rankify.main.Main plugin;

    public RankGuiListener(org.powell.rankify.main.Main plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent e){
        if (e.getView().getTitle().equals(ChatColor.DARK_AQUA + "Rankify Menu") && e.getCurrentItem() != null){
            Player player = (Player) e.getWhoClicked();

            ItemStack clickedItem = e.getCurrentItem();
            e.setCancelled(true);
            if (e.getCurrentItem().getType() == Material.PLAYER_HEAD) {
                String player_name = ChatColor.stripColor(clickedItem.getItemMeta().getDisplayName());
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
                // Determine rank slots from config to avoid placing frame there
                List<Map<?, ?>> cfgRanks = plugin.getConfig().getMapList("ranks");
                boolean[] rankSlotMask = new boolean[36];
                for (Map<?, ?> sec : cfgRanks) {
                    Object slotObj = sec.get("slot");
                    if (slotObj instanceof Number) {
                        int slot = ((Number) slotObj).intValue();
                        if (slot >= 0 && slot < 36) rankSlotMask[slot] = true;
                    }
                }
                for (int i = 0; i < 36; i++) {
                    if (i == 0 || i == 1 || i == 25) continue;
                    if (!rankSlotMask[i]) {
                        inv.setItem(i, frame);
                    }
                }

                // Build rank items from config
                for (Map<?, ?> sec : cfgRanks) {
                    Object nameObj = sec.get("name");
                    String name = nameObj == null ? "Rank" : String.valueOf(nameObj);
                    Object matObj = sec.get("material");
                    String materialStr = matObj == null ? "PAPER" : String.valueOf(matObj);
                    Object slotObj = sec.get("slot");
                    int slot = (slotObj instanceof Number) ? ((Number) slotObj).intValue() : -1;
                    Material mat;
                    try { mat = Material.valueOf(materialStr.toUpperCase()); }
                    catch (Exception ex) { mat = Material.PAPER; }
                    ItemStack item = new ItemStack(mat);
                    ItemMeta meta = item.getItemMeta();
                    if (meta != null) {
                        meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', name));
                        item.setItemMeta(meta);
                    }
                    if (slot >= 0 && slot < 36) {
                        inv.setItem(slot, item);
                    }
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

package org.powell.rankify.main.GUI;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.powell.rankify.main.Main;
import org.powell.rankify.main.Utils.PageUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GUI{
    public Main main;

    public GUI (Main main,Player player, int page) {
        this.main = main;

        //ONLINE PLAYERS
        Inventory inv = Bukkit.createInventory(player, 54, ChatColor.DARK_AQUA + "Rankify Menu");

        //CLOSE
        ItemStack close = new ItemStack(Material.BARRIER);
        ItemMeta closemeta = close.getItemMeta();
        closemeta.setDisplayName(ChatColor.RED + "Close Button");
        closemeta.setLore(Arrays.asList(""));
        close.setItemMeta(closemeta);

        inv.setItem(0, close);

        //FRAME
        ItemStack frame = new ItemStack(Material.GRAY_STAINED_GLASS_PANE);
        ItemMeta fmeta = frame.getItemMeta();
        fmeta.setDisplayName(ChatColor.GRAY + "_");
        fmeta.setLore(Arrays.asList(""));
        frame.setItemMeta(fmeta);
        for (int i : new int[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 17, 18, 26, 27, 35, 36, 44, 46, 47, 48, 50, 51, 52}) {
            inv.setItem(i, frame);
        }
        for (Player all : Bukkit.getServer().getOnlinePlayers()) {

            ItemStack players = new ItemStack(Material.PLAYER_HEAD);
            SkullMeta playersMeta = (SkullMeta) players.getItemMeta();
            playersMeta.setOwningPlayer(all);
            playersMeta.setDisplayName(all.getName());
            playersMeta.setLore(Arrays.asList(ChatColor.GOLD + "Rank: " + main.getRankManager().getRank(all.getUniqueId())));
            players.setItemMeta(playersMeta);

            inv.addItem(players);
        }
        ItemStack players = new ItemStack(Material.DIAMOND_AXE);

        List<ItemStack> heads = Arrays.asList((ItemStack) players);

        //PAGE NUMBER
        ItemStack page_number = new ItemStack(Material.WHITE_STAINED_GLASS_PANE);
        ItemMeta pmeta = page_number.getItemMeta();
        pmeta.setDisplayName(page + "");
        page_number.setItemMeta(pmeta);

        inv.setItem(49, page_number);

        ItemStack left;
        ItemMeta leftMeta;
        if (PageUtil.isPageValid(heads, page - 1, 28)) {
            left = new ItemStack(Material.LIME_STAINED_GLASS_PANE);
            leftMeta = left.getItemMeta();
            leftMeta.setDisplayName(ChatColor.GREEN + "---->");
        } else {
            left = new ItemStack(Material.RED_STAINED_GLASS_PANE);
            leftMeta = left.getItemMeta();
            leftMeta.setDisplayName(ChatColor.RED + "---->");
        }
        left.setItemMeta(leftMeta);
        inv.setItem(53, left);

        ItemStack right;
        ItemMeta rightMeta;
        if (PageUtil.isPageValid(heads, page - 1, 28)) {
            right = new ItemStack(Material.LIME_STAINED_GLASS_PANE);
            rightMeta = right.getItemMeta();
            rightMeta.setDisplayName(ChatColor.GREEN + "<----");
        } else {
            right = new ItemStack(Material.RED_STAINED_GLASS_PANE);
            rightMeta = right.getItemMeta();
            rightMeta.setDisplayName(ChatColor.RED + "<----");
        }
        right.setItemMeta(rightMeta);
        inv.setItem(45, right);

        List<ItemStack> allItems = new ArrayList<>();
        for (int i = 0; i < 135; i++) {
            allItems.add(new ItemStack(Material.DIAMOND_AXE));
        }
        for (ItemStack is : PageUtil.getPageItems(allItems, page, 50)) {
            inv.addItem(is);
        }

        player.openInventory(inv);
    }
}

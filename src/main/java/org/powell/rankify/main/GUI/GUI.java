package org.powell.rankify.main.GUI;

import dev.triumphteam.gui.builder.item.ItemBuilder;
import dev.triumphteam.gui.guis.Gui;
import dev.triumphteam.gui.guis.GuiItem;
import dev.triumphteam.gui.guis.PaginatedGui;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.powell.rankify.main.Main;
import java.util.Arrays;

public class GUI{
    public Main main;

    public GUI (Main main,Player player, int page) {
        this.main = main;

        //ONLINE PLAYERS
        PaginatedGui gui = Gui.paginated()
                .title(Component.text(ChatColor.DARK_AQUA + "Rankify Menu"))
                .rows(6)
                .pageSize(28)
                .create();

        //CLOSE
        ItemStack closebutton = new ItemStack(Material.BARRIER);
        ItemMeta closemeta = closebutton.getItemMeta();
        closemeta.setDisplayName(ChatColor.RED + "Close Button");
        closemeta.setLore(Arrays.asList(""));
        closebutton.setItemMeta(closemeta);

        GuiItem close = ItemBuilder.from(closebutton).asGuiItem();

        gui.setItem(0, close);

        //FRAME
        ItemStack frame = new ItemStack(Material.GRAY_STAINED_GLASS_PANE);
        ItemMeta fmeta = frame.getItemMeta();
        fmeta.setDisplayName(ChatColor.GRAY + "_");
        fmeta.setLore(Arrays.asList(""));
        frame.setItemMeta(fmeta);

        GuiItem frames = ItemBuilder.from(frame).asGuiItem();

        for (int i : new int[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 17, 18, 26, 27, 35, 36, 44, 46, 47, 48, 49, 50, 51, 52}) {
            gui.setItem(i, frames);
        }
        for (Player all : Bukkit.getServer().getOnlinePlayers()) {

            ItemStack players = new ItemStack(Material.PLAYER_HEAD);
            SkullMeta playersMeta = (SkullMeta) players.getItemMeta();
            playersMeta.setOwningPlayer(all);
            playersMeta.setDisplayName(all.getName());
            playersMeta.setLore(Arrays.asList(ChatColor.GOLD + "Rank: " + main.getRankManager().getRank(all.getUniqueId())));
            players.setItemMeta(playersMeta);

            GuiItem all_players = ItemBuilder.from(players).asGuiItem();
            gui.addItem(all_players);

        }

        //PAGE NUMBER
        ItemStack page_number = new ItemStack(Material.WHITE_STAINED_GLASS_PANE);
        ItemMeta pmeta = page_number.getItemMeta();
        pmeta.setDisplayName(page + "");
        page_number.setItemMeta(pmeta);

        //PAGE BUTTONS
        gui.setItem(45, ItemBuilder.from(Material.ARROW).setName("Previous").asGuiItem(event -> gui.previous()));
        gui.setItem(53, ItemBuilder.from(Material.ARROW).setName("Next").asGuiItem(event -> gui.next()));

        gui.open(player);
    }
}

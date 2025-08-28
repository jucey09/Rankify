package org.powell.rankify.main.GUI;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import java.util.*;
import java.util.function.Consumer;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.powell.rankify.main.Enums.Rank;
import org.powell.rankify.main.Main;


public class GUI implements InventoryHolder {
    private final Main main;
    private final Player player;
    private final int page;
    private final Map<Integer, Consumer<InventoryClickEvent>> clickHandlers = new HashMap<>();
    private Inventory inventory;

    public GUI(Main main, Player player, int page) {
        this.main = main;
        this.player = player;
        this.page = page;
        openRankMenu();
    }

    private void openRankMenu() {
        // Create a new GUI with 6 rows (54 slots)
        this.inventory = Bukkit.createInventory(this, 54, ChatColor.DARK_AQUA + "Rankify Menu");
        clickHandlers.clear();
        
        // Add frame items
        addFrameItems();
        
        // Add player heads
        addPlayerHeads();
        
        // Add navigation buttons
        addNavigationItems();
        
        // Open the GUI for the player
        player.openInventory(inventory);
    }
    
    private void addFrameItems() {
        ItemStack frame = new ItemStack(Material.GRAY_STAINED_GLASS_PANE);
        ItemMeta meta = frame.getItemMeta();
        if (meta != null) {
            meta.setDisplayName(" ");
            frame.setItemMeta(meta);
        }
        
        // Add frame items to the borders
        int[] frameSlots = {0,1,2,3,4,5,6,7,8,9,17,18,26,27,35,36,44,45,46,47,48,50,51,52,53};
        for (int slot : frameSlots) {
            inventory.setItem(slot, frame);
        }
    }
    
    private void addPlayerHeads() {
        int slot = 10; // Starting slot for player heads
        
        for (Player onlinePlayer : player.getServer().getOnlinePlayers()) {
            if (slot > 43) break; // Don't go beyond the last row of the inventory
            
            ItemStack playerHead = new ItemStack(Material.PLAYER_HEAD);
            SkullMeta meta = (SkullMeta) playerHead.getItemMeta();
            if (meta != null) {
                meta.setOwningPlayer(onlinePlayer);
                meta.setDisplayName(ChatColor.GOLD + onlinePlayer.getName());
                meta.setLore(Arrays.asList(
                    ChatColor.GRAY + "Rank: " + main.getRankManager().getRank(onlinePlayer.getUniqueId()),
                    ChatColor.YELLOW + "Click to manage rank"
                ));
                playerHead.setItemMeta(meta);
            }
            
            int finalSlot = slot++;
            inventory.setItem(finalSlot, playerHead);
            clickHandlers.put(finalSlot, event -> {
                event.setCancelled(true);
                // Handle player rank management here
                player.sendMessage(ChatColor.GOLD + "Managing rank for " + onlinePlayer.getName());
                
                // Open rank selection menu
                openRankSelectionMenu(onlinePlayer);
            });
            
            // Skip the last slot of each row
            if ((slot + 1) % 9 == 0) {
                slot += 2;
            }
        }
    }
    
    private void openRankSelectionMenu(Player targetPlayer) {
        this.inventory = Bukkit.createInventory(this, 27, ChatColor.DARK_AQUA + "Set rank for " + targetPlayer.getName());
        clickHandlers.clear();
        
        // Get all available ranks
        Rank[] ranks = Rank.values();
        // Sort ranks by their ordinal (order in the enum)
        Arrays.sort(ranks, (r1, r2) -> r2.ordinal() - r1.ordinal());
        
        // Map rank categories to materials for the GUI
        Map<Class<? extends Enum<?>>, Material> rankMaterials = new HashMap<>();
        rankMaterials.put(Rank.OWNER.getClass(), Material.RED_CONCRETE);
        rankMaterials.put(Rank.COOWNER.getClass(), Material.GREEN_CONCRETE);
        rankMaterials.put(Rank.ADMIN.getClass(), Material.YELLOW_CONCRETE);
        rankMaterials.put(Rank.MODERATOR.getClass(), Material.PURPLE_CONCRETE);
        rankMaterials.put(Rank.HELPER.getClass(), Material.LIME_CONCRETE);
        rankMaterials.put(Rank.YOUTUBE.getClass(), Material.LIGHT_BLUE_CONCRETE);
        rankMaterials.put(Rank.DONOR.getClass(), Material.BLUE_CONCRETE);
        rankMaterials.put(Rank.DISCORD.getClass(), Material.ORANGE_CONCRETE);
        rankMaterials.put(Rank.STRONGEST.getClass(), Material.YELLOW_CONCRETE);
        rankMaterials.put(Rank.EGIRL.getClass(), Material.PINK_CONCRETE);
        rankMaterials.put(Rank.SUS.getClass(), Material.GREEN_CONCRETE);
        rankMaterials.put(Rank.MEMBER.getClass(), Material.LIGHT_BLUE_CONCRETE);
        rankMaterials.put(Rank.GUEST.getClass(), Material.GRAY_CONCRETE);
        
        int slot = 10; // Starting slot for ranks
        // Get the current rank once before the loop
        final Rank currentRank = main.getRankManager().getRank(targetPlayer.getUniqueId());
        
        for (Rank rank : ranks) {
            if (slot > 16) break; // Don't go beyond the second row
            
            Material material = rankMaterials.getOrDefault(rank.getClass(), Material.PAPER);
            ItemStack rankItem = new ItemStack(material);
            ItemMeta meta = rankItem.getItemMeta();
            
            if (meta != null) {
                meta.setDisplayName(rank.getDisplay());
                
                List<String> lore = new ArrayList<>();
                lore.add(ChatColor.GRAY + "Click to set " + targetPlayer.getName());
                lore.add(ChatColor.GRAY + "to " + rank.getDisplay() + ChatColor.GRAY + " rank");
                lore.add("");
                lore.add(ChatColor.YELLOW + "Current rank: " + (currentRank != null ? currentRank.getDisplay() : "None"));
                
                meta.setLore(lore);
                rankItem.setItemMeta(meta);
            }
            
            int finalSlot = slot++;
            inventory.setItem(finalSlot, rankItem);
            clickHandlers.put(finalSlot, event -> {
                event.setCancelled(true);
                
                // Check if player has permission to set this rank
                if (!player.hasPermission("rankify.setrank")) {
                    player.sendMessage(ChatColor.RED + "You don't have permission to set ranks.");
                    player.closeInventory();
                    return;
                }
                
                // Don't update if it's the same rank
                if (rank == currentRank) {
                    player.sendMessage(ChatColor.YELLOW + targetPlayer.getName() + " already has the " + 
                                     rank.getDisplay() + ChatColor.YELLOW + " rank.");
                    player.closeInventory();
                    return;
                }
                
                // Set the rank
                try {
                    main.getRankManager().setRank(targetPlayer.getUniqueId(), rank, true);
                    player.sendMessage(ChatColor.GREEN + "Set " + targetPlayer.getName() + "'s rank to " + 
                                     rank.getDisplay() + ChatColor.GREEN + ".");
                    
                    // Notify the target player if online
                    if (targetPlayer.isOnline()) {
                        targetPlayer.sendMessage(ChatColor.GREEN + "Your rank has been updated to " + 
                                              rank.getDisplay() + ChatColor.GREEN + " by " + 
                                              player.getDisplayName() + ChatColor.GREEN + ".");
                    }
                    
                    // Update the GUI to show the new rank
                    openRankSelectionMenu(targetPlayer);
                } catch (Exception e) {
                    player.sendMessage(ChatColor.RED + "An error occurred while setting the rank: " + e.getMessage());
                    e.printStackTrace();
                }
            });
            
            // Move to next row if needed
            if (slot == 17) slot = 19; // Skip to next row with a gap
        }
        
        // Add back button
        ItemStack backButton = new ItemStack(Material.ARROW);
        ItemMeta backMeta = backButton.getItemMeta();
        if (backMeta != null) {
            backMeta.setDisplayName(ChatColor.YELLOW + "Go Back");
            backButton.setItemMeta(backMeta);
        }
        inventory.setItem(22, backButton);
        clickHandlers.put(22, event -> {
            event.setCancelled(true);
            openRankMenu();
        });
        
        player.openInventory(inventory);
    }
    
    private void addNavigationItems() {
        // Close button
        ItemStack closeButton = new ItemStack(Material.BARRIER);
        ItemMeta closeMeta = closeButton.getItemMeta();
        if (closeMeta != null) {
            closeMeta.setDisplayName(ChatColor.RED + "Close");
            closeMeta.setLore(Arrays.asList(ChatColor.GRAY + "Click to close"));
            closeButton.setItemMeta(closeMeta);
        }
        inventory.setItem(49, closeButton);
        clickHandlers.put(49, event -> player.closeInventory());
    }
    
    @Override
    public Inventory getInventory() {
        return inventory;
    }
    
    public void handleClick(InventoryClickEvent event) {
        int slot = event.getSlot();
        if (clickHandlers.containsKey(slot)) {
            clickHandlers.get(slot).accept(event);
        }
    }
}

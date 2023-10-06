package me.WalterWaves.Gambling;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class Shop implements CommandExecutor, Listener {

	private static Map<String, Inventory> categories = new HashMap<String, Inventory>();
	private static Map<String, Inventory> valuables = new HashMap<String, Inventory>();
	private static Map<String, Inventory> mobs = new HashMap<String, Inventory>();

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		Player player = (Player) sender;
		if (label.equalsIgnoreCase("shop")) {
			player.playSound(player, Sound.BLOCK_NOTE_BLOCK_PLING, 1f, 1f);
			if (categories.get(player.getName()) == null) {
				categories.put(player.getName(), Bukkit.createInventory(player, 54,
						ChatColor.AQUA + "" + ChatColor.BOLD + "" + ChatColor.UNDERLINE + "SELECT CATEGORY"));
			}

			ItemStack blank = new ItemStack(Material.BLACK_STAINED_GLASS_PANE);
			ItemMeta blank_meta = blank.getItemMeta();
			blank_meta.setDisplayName(" ");
			blank.setItemMeta(blank_meta);

			ItemStack valuables_category = new ItemStack(Material.DIAMOND);
			ItemMeta valuables_category_meta = valuables_category.getItemMeta();
			valuables_category_meta.setDisplayName(ChatColor.AQUA + "" + ChatColor.BOLD + "VALUABLES");
			valuables_category.setItemMeta(valuables_category_meta);

			ItemStack mobs_category = new ItemStack(Material.BONE);
			ItemMeta mobs_category_meta = valuables_category.getItemMeta();
			mobs_category_meta.setDisplayName(ChatColor.DARK_GRAY + "" + ChatColor.BOLD + "MOB DROPS");
			mobs_category.setItemMeta(mobs_category_meta);

			for (int i = 0; i < 54; i++) {
				categories.get(player.getName()).setItem(i, blank);
			}
			categories.get(player.getName()).setItem(10, valuables_category);
			categories.get(player.getName()).setItem(11, mobs_category);
			player.openInventory(categories.get(player.getName()));
		}
		return false;
	}

	@EventHandler
	public void shopInteract(InventoryClickEvent event) {
		Player player = (Player) event.getWhoClicked();
		if (event.getCurrentItem() != null && event.getCurrentItem().getType() == Material.BARRIER
				&& event.getCurrentItem().getItemMeta().getDisplayName()
						.equals(ChatColor.RED + "" + ChatColor.BOLD + "BACK TO CATEGORIES")) {
			player.openInventory(categories.get(player.getName()));
			player.playSound(player, Sound.BLOCK_NOTE_BLOCK_PLING, 1f, 1f);
		}

		for (ItemStack search : event.getInventory().getContents()) {
			if (search.getType() == Material.BARRIER) {
				ItemStack foundItem = search;
				if (foundItem.getItemMeta().getDisplayName()
						.equals(ChatColor.RED + "" + ChatColor.BOLD + "BACK TO CATEGORIES")) {
					if (event.getAction() == InventoryAction.PICKUP_ALL) {
						ItemStack item = event.getCurrentItem();
						if (item.hasItemMeta() && item.getItemMeta().hasLore()
								&& item.getItemMeta().getLore().get(0).contains("Buy")
								&& item.getItemMeta().getLore().get(1).contains("Sell")) {
							String buy_price = item.getItemMeta().getLore().get(0);
							int length = buy_price.length();
							String result = "";
							for (int i = 0; i < length; i++) {
								Character character = buy_price.charAt(i);
								if (Character.isDigit(character)) {
									result += character;
								}
							}
							if ((int) Main.data.getConfig().get(player.getName() + ".Credits") >= Integer
									.parseInt(result)) {
								Main.data.getConfig().set(player.getName() + ".Credits",
										(int) Main.data.getConfig().get(player.getName() + ".Credits")
												- Integer.parseInt(result));
								Main.data.saveConfig();
								player.sendMessage(ChatColor.GREEN + "✔ Purchase Successful! New Balance: "
										+ Main.data.getConfig().get(player.getName() + ".Credits") + " Credits.");
								player.playSound(player, Sound.BLOCK_NOTE_BLOCK_BELL, 1f, 1f);
								player.getInventory().addItem(new ItemStack(item.getType()));
								Main.logs.getConfig().set(
										player.getName() + "'s Logs" + ".Buy" + "." + Main.formatter.format(Main.date)
												+ ".TransactionID " + (Main.random(1000000) + 1000000),
										"Bought " + item.getType() + " for " + result + " Credits. Balance: "
												+ Main.data.getConfig().get(player.getName() + ".Credits")
												+ " Credits.");
								Main.logs.saveConfig();
							} else {
								player.sendMessage(ChatColor.RED + "⚠ Not Enough Credits! Balance: "
										+ Main.data.getConfig().get(player.getName() + ".Credits") + " Credits.");
							}
						}
					}

					if (event.getAction() == InventoryAction.PICKUP_HALF) {
						ItemStack item = event.getCurrentItem();
						if (item.hasItemMeta() && item.getItemMeta().hasLore()
								&& item.getItemMeta().getLore().get(0).contains("Buy")
								&& item.getItemMeta().getLore().get(1).contains("Sell")) {
							String sell_price = item.getItemMeta().getLore().get(1);
							int length = sell_price.length();
							String result = "";
							for (int i = 0; i < length; i++) {
								Character character = sell_price.charAt(i);
								if (Character.isDigit(character)) {
									result += character;
								}
							}
							for (ItemStack playerInv : player.getInventory().getContents()) {
								if (playerInv != null && playerInv.getType() == item.getType()) {
									ItemStack sellable = playerInv;
									sellable.setAmount(sellable.getAmount() - 1);
									Main.data.getConfig().set(player.getName() + ".Credits",
											(int) Main.data.getConfig().get(player.getName() + ".Credits")
													+ Integer.parseInt(result));
									Main.data.saveConfig();
									player.sendMessage(ChatColor.GREEN + "✔ Sale Successful! New Balance: "
											+ Main.data.getConfig().get(player.getName() + ".Credits") + " Credits.");
									player.playSound(player, Sound.BLOCK_NOTE_BLOCK_HARP, 1f, 1f);
									Main.logs.getConfig()
											.set(player.getName() + "'s Logs" + ".Sell" + "."
													+ Main.formatter.format(Main.date) + ".TransactionID "
													+ (Main.random(1000000) + 1000000),
													"Sold " + item.getType() + " for " + result + " Credits. Balance: "
															+ Main.data.getConfig().get(player.getName() + ".Credits")
															+ " Credits.");
									Main.logs.saveConfig();
								}
							}
						}
					}
				}
			}
		}

		if (event.getCurrentItem() != null && event.getCurrentItem().getType() == Material.DIAMOND
				&& event.getCurrentItem().getItemMeta().getDisplayName()
						.equals(ChatColor.AQUA + "" + ChatColor.BOLD + "VALUABLES")) {
			if (valuables.get(player.getName()) == null) {
				valuables.put(player.getName(), Bukkit.createInventory(player, 54,
						ChatColor.AQUA + "" + ChatColor.BOLD + "" + ChatColor.UNDERLINE + "VALUABLES"));
			}
			player.playSound(player, Sound.BLOCK_NOTE_BLOCK_PLING, 1f, 1f);

			ItemStack blank = new ItemStack(Material.BLACK_STAINED_GLASS_PANE);
			ItemMeta blank_meta = blank.getItemMeta();
			blank_meta.setDisplayName(" ");
			blank.setItemMeta(blank_meta);

			ItemStack back = new ItemStack(Material.BARRIER);
			ItemMeta back_meta = back.getItemMeta();
			back_meta.setDisplayName(ChatColor.RED + "" + ChatColor.BOLD + "BACK TO CATEGORIES");
			back.setItemMeta(back_meta);

			ItemStack item1 = new ItemStack(Material.COAL);
			ItemMeta item1_meta = item1.getItemMeta();
			item1_meta.setDisplayName(ChatColor.GRAY + "" + ChatColor.BOLD + "COAL");
			List<String> item1_lore = new ArrayList<String>();
			item1_lore.add(ChatColor.AQUA + "Buy " + ChatColor.GREEN + "10 Credits");
			item1_lore.add(ChatColor.AQUA + "Sell " + ChatColor.RED + "8 Credits");
			item1_lore.add("");
			item1_lore.add(ChatColor.DARK_GRAY + "" + ChatColor.ITALIC + "Left-Click To Buy");
			item1_lore.add(ChatColor.DARK_GRAY + "" + ChatColor.ITALIC + "Right-Click To Sell");
			item1_meta.setLore(item1_lore);
			item1.setItemMeta(item1_meta);

			ItemStack item2 = new ItemStack(Material.IRON_INGOT);
			ItemMeta item2_meta = item2.getItemMeta();
			item2_meta.setDisplayName(ChatColor.WHITE + "" + ChatColor.BOLD + "IRON INGOT");
			List<String> item2_lore = new ArrayList<String>();
			item2_lore.add(ChatColor.AQUA + "Buy " + ChatColor.GREEN + "55 Credits");
			item2_lore.add(ChatColor.AQUA + "Sell " + ChatColor.RED + "40 Credits");
			item2_lore.add("");
			item2_lore.add(ChatColor.DARK_GRAY + "" + ChatColor.ITALIC + "Left-Click To Buy");
			item2_lore.add(ChatColor.DARK_GRAY + "" + ChatColor.ITALIC + "Right-Click To Sell");
			item2_meta.setLore(item2_lore);
			item2.setItemMeta(item2_meta);

			ItemStack item3 = new ItemStack(Material.COPPER_INGOT);
			ItemMeta item3_meta = item3.getItemMeta();
			item3_meta.setDisplayName(ChatColor.GOLD + "" + ChatColor.BOLD + "COPPER INGOT");
			List<String> item3_lore = new ArrayList<String>();
			item3_lore.add(ChatColor.AQUA + "Buy " + ChatColor.GREEN + "10 Credits");
			item3_lore.add(ChatColor.AQUA + "Sell " + ChatColor.RED + "5 Credits");
			item3_lore.add("");
			item3_lore.add(ChatColor.DARK_GRAY + "" + ChatColor.ITALIC + "Left-Click To Buy");
			item3_lore.add(ChatColor.DARK_GRAY + "" + ChatColor.ITALIC + "Right-Click To Sell");
			item3_meta.setLore(item3_lore);
			item3.setItemMeta(item3_meta);

			ItemStack item4 = new ItemStack(Material.GOLD_INGOT);
			ItemMeta item4_meta = item4.getItemMeta();
			item4_meta.setDisplayName(ChatColor.YELLOW + "" + ChatColor.BOLD + "GOLD INGOT");
			List<String> item4_lore = new ArrayList<String>();
			item4_lore.add(ChatColor.AQUA + "Buy " + ChatColor.GREEN + "165 Credits");
			item4_lore.add(ChatColor.AQUA + "Sell " + ChatColor.RED + "140 Credits");
			item4_lore.add("");
			item4_lore.add(ChatColor.DARK_GRAY + "" + ChatColor.ITALIC + "Left-Click To Buy");
			item4_lore.add(ChatColor.DARK_GRAY + "" + ChatColor.ITALIC + "Right-Click To Sell");
			item4_meta.setLore(item4_lore);
			item4.setItemMeta(item4_meta);

			ItemStack item5 = new ItemStack(Material.REDSTONE);
			ItemMeta item5_meta = item5.getItemMeta();
			item5_meta.setDisplayName(ChatColor.DARK_RED + "" + ChatColor.BOLD + "REDSTONE");
			List<String> item5_lore = new ArrayList<String>();
			item5_lore.add(ChatColor.AQUA + "Buy " + ChatColor.GREEN + "10 Credits");
			item5_lore.add(ChatColor.AQUA + "Sell " + ChatColor.RED + "5 Credits");
			item5_lore.add("");
			item5_lore.add(ChatColor.DARK_GRAY + "" + ChatColor.ITALIC + "Left-Click To Buy");
			item5_lore.add(ChatColor.DARK_GRAY + "" + ChatColor.ITALIC + "Right-Click To Sell");
			item5_meta.setLore(item5_lore);
			item5.setItemMeta(item5_meta);

			ItemStack item6 = new ItemStack(Material.EMERALD);
			ItemMeta item6_meta = item6.getItemMeta();
			item6_meta.setDisplayName(ChatColor.GREEN + "" + ChatColor.BOLD + "EMERALD");
			List<String> item6_lore = new ArrayList<String>();
			item6_lore.add(ChatColor.AQUA + "Buy " + ChatColor.GREEN + "500 Credits");
			item6_lore.add(ChatColor.AQUA + "Sell " + ChatColor.RED + "450 Credits");
			item6_lore.add("");
			item6_lore.add(ChatColor.DARK_GRAY + "" + ChatColor.ITALIC + "Left-Click To Buy");
			item6_lore.add(ChatColor.DARK_GRAY + "" + ChatColor.ITALIC + "Right-Click To Sell");
			item6_meta.setLore(item6_lore);
			item6.setItemMeta(item6_meta);

			ItemStack item7 = new ItemStack(Material.LAPIS_LAZULI);
			ItemMeta item7_meta = item7.getItemMeta();
			item7_meta.setDisplayName(ChatColor.DARK_BLUE + "" + ChatColor.BOLD + "LAPIS LAZULI");
			List<String> item7_lore = new ArrayList<String>();
			item7_lore.add(ChatColor.AQUA + "Buy " + ChatColor.GREEN + "15 Credits");
			item7_lore.add(ChatColor.AQUA + "Sell " + ChatColor.RED + "10 Credits");
			item7_lore.add("");
			item7_lore.add(ChatColor.DARK_GRAY + "" + ChatColor.ITALIC + "Left-Click To Buy");
			item7_lore.add(ChatColor.DARK_GRAY + "" + ChatColor.ITALIC + "Right-Click To Sell");
			item7_meta.setLore(item7_lore);
			item7.setItemMeta(item7_meta);

			ItemStack item8 = new ItemStack(Material.DIAMOND);
			ItemMeta item8_meta = item8.getItemMeta();
			item8_meta.setDisplayName(ChatColor.AQUA + "" + ChatColor.BOLD + "DIAMOND");
			List<String> item8_lore = new ArrayList<String>();
			item8_lore.add(ChatColor.AQUA + "Buy " + ChatColor.GREEN + "900 Credits");
			item8_lore.add(ChatColor.AQUA + "Sell " + ChatColor.RED + "840 Credits");
			item8_lore.add("");
			item8_lore.add(ChatColor.DARK_GRAY + "" + ChatColor.ITALIC + "Left-Click To Buy");
			item8_lore.add(ChatColor.DARK_GRAY + "" + ChatColor.ITALIC + "Right-Click To Sell");
			item8_meta.setLore(item8_lore);
			item8.setItemMeta(item8_meta);

			for (int i = 0; i < 54; i++) {
				valuables.get(player.getName()).setItem(i, blank);
			}
			valuables.get(player.getName()).setItem(45, back);
			valuables.get(player.getName()).setItem(10, item1);
			valuables.get(player.getName()).setItem(11, item2);
			valuables.get(player.getName()).setItem(12, item3);
			valuables.get(player.getName()).setItem(13, item4);
			valuables.get(player.getName()).setItem(14, item5);
			valuables.get(player.getName()).setItem(15, item6);
			valuables.get(player.getName()).setItem(16, item7);
			valuables.get(player.getName()).setItem(19, item8);
			player.openInventory(valuables.get(player.getName()));
		}

		if (event.getCurrentItem() != null && event.getCurrentItem().getType() == Material.BONE
				&& event.getCurrentItem().getItemMeta().getDisplayName()
						.equals(ChatColor.DARK_GRAY + "" + ChatColor.BOLD + "MOB DROPS")) {
			if (mobs.get(player.getName()) == null) {
				mobs.put(player.getName(), Bukkit.createInventory(player, 54,
						ChatColor.GRAY + "" + ChatColor.BOLD + "" + ChatColor.UNDERLINE + "MOB DROPS"));
			}
			player.playSound(player, Sound.BLOCK_NOTE_BLOCK_PLING, 1f, 1f);

			ItemStack blank = new ItemStack(Material.BLACK_STAINED_GLASS_PANE);
			ItemMeta blank_meta = blank.getItemMeta();
			blank_meta.setDisplayName(" ");
			blank.setItemMeta(blank_meta);

			ItemStack back = new ItemStack(Material.BARRIER);
			ItemMeta back_meta = back.getItemMeta();
			back_meta.setDisplayName(ChatColor.RED + "" + ChatColor.BOLD + "BACK TO CATEGORIES");
			back.setItemMeta(back_meta);

			ItemStack item1 = new ItemStack(Material.ROTTEN_FLESH);
			ItemMeta item1_meta = item1.getItemMeta();
			item1_meta.setDisplayName(ChatColor.GOLD + "" + ChatColor.BOLD + "ROTTEN FLESH");
			List<String> item1_lore = new ArrayList<String>();
			item1_lore.add(ChatColor.AQUA + "Buy " + ChatColor.GREEN + "5 Credits");
			item1_lore.add(ChatColor.AQUA + "Sell " + ChatColor.RED + "3 Credits");
			item1_lore.add("");
			item1_lore.add(ChatColor.DARK_GRAY + "" + ChatColor.ITALIC + "Left-Click To Buy");
			item1_lore.add(ChatColor.DARK_GRAY + "" + ChatColor.ITALIC + "Right-Click To Sell");
			item1_meta.setLore(item1_lore);
			item1.setItemMeta(item1_meta);

			ItemStack item2 = new ItemStack(Material.GUNPOWDER);
			ItemMeta item2_meta = item2.getItemMeta();
			item2_meta.setDisplayName(ChatColor.GRAY + "" + ChatColor.BOLD + "GUNPOWDER");
			List<String> item2_lore = new ArrayList<String>();
			item2_lore.add(ChatColor.AQUA + "Buy " + ChatColor.GREEN + "105 Credits");
			item2_lore.add(ChatColor.AQUA + "Sell " + ChatColor.RED + "55 Credits");
			item2_lore.add("");
			item2_lore.add(ChatColor.DARK_GRAY + "" + ChatColor.ITALIC + "Left-Click To Buy");
			item2_lore.add(ChatColor.DARK_GRAY + "" + ChatColor.ITALIC + "Right-Click To Sell");
			item2_meta.setLore(item2_lore);
			item2.setItemMeta(item2_meta);

			ItemStack item3 = new ItemStack(Material.BONE);
			ItemMeta item3_meta = item3.getItemMeta();
			item3_meta.setDisplayName(ChatColor.DARK_GRAY + "" + ChatColor.BOLD + "BONE");
			List<String> item3_lore = new ArrayList<String>();
			item3_lore.add(ChatColor.AQUA + "Buy " + ChatColor.GREEN + "10 Credits");
			item3_lore.add(ChatColor.AQUA + "Sell " + ChatColor.RED + "5 Credits");
			item3_lore.add("");
			item3_lore.add(ChatColor.DARK_GRAY + "" + ChatColor.ITALIC + "Left-Click To Buy");
			item3_lore.add(ChatColor.DARK_GRAY + "" + ChatColor.ITALIC + "Right-Click To Sell");
			item3_meta.setLore(item3_lore);
			item3.setItemMeta(item3_meta);

			ItemStack item4 = new ItemStack(Material.SLIME_BALL);
			ItemMeta item4_meta = item4.getItemMeta();
			item4_meta.setDisplayName(ChatColor.GREEN + "" + ChatColor.BOLD + "SLIMEBALL");
			List<String> item4_lore = new ArrayList<String>();
			item4_lore.add(ChatColor.AQUA + "Buy " + ChatColor.GREEN + "150 Credits");
			item4_lore.add(ChatColor.AQUA + "Sell " + ChatColor.RED + "20 Credits");
			item4_lore.add("");
			item4_lore.add(ChatColor.DARK_GRAY + "" + ChatColor.ITALIC + "Left-Click To Buy");
			item4_lore.add(ChatColor.DARK_GRAY + "" + ChatColor.ITALIC + "Right-Click To Sell");
			item4_meta.setLore(item4_lore);
			item4.setItemMeta(item4_meta);

			ItemStack item5 = new ItemStack(Material.GHAST_TEAR);
			ItemMeta item5_meta = item5.getItemMeta();
			item5_meta.setDisplayName(ChatColor.GRAY + "" + ChatColor.BOLD + "GHAST TEAR");
			List<String> item5_lore = new ArrayList<String>();
			item5_lore.add(ChatColor.AQUA + "Buy " + ChatColor.GREEN + "400 Credits");
			item5_lore.add(ChatColor.AQUA + "Sell " + ChatColor.RED + "150 Credits");
			item5_lore.add("");
			item5_lore.add(ChatColor.DARK_GRAY + "" + ChatColor.ITALIC + "Left-Click To Buy");
			item5_lore.add(ChatColor.DARK_GRAY + "" + ChatColor.ITALIC + "Right-Click To Sell");
			item5_meta.setLore(item5_lore);
			item5.setItemMeta(item5_meta);

			ItemStack item6 = new ItemStack(Material.MAGMA_CREAM);
			ItemMeta item6_meta = item6.getItemMeta();
			item6_meta.setDisplayName(ChatColor.GOLD + "" + ChatColor.BOLD + "MAGMA CREAM");
			List<String> item6_lore = new ArrayList<String>();
			item6_lore.add(ChatColor.AQUA + "Buy " + ChatColor.GREEN + "100 Credits");
			item6_lore.add(ChatColor.AQUA + "Sell " + ChatColor.RED + "45 Credits");
			item6_lore.add("");
			item6_lore.add(ChatColor.DARK_GRAY + "" + ChatColor.ITALIC + "Left-Click To Buy");
			item6_lore.add(ChatColor.DARK_GRAY + "" + ChatColor.ITALIC + "Right-Click To Sell");
			item6_meta.setLore(item6_lore);
			item6.setItemMeta(item6_meta);

			ItemStack item7 = new ItemStack(Material.ENDER_PEARL);
			ItemMeta item7_meta = item7.getItemMeta();
			item7_meta.setDisplayName(ChatColor.DARK_PURPLE + "" + ChatColor.BOLD + "ENDER PEARL");
			List<String> item7_lore = new ArrayList<String>();
			item7_lore.add(ChatColor.AQUA + "Buy " + ChatColor.GREEN + "600 Credits");
			item7_lore.add(ChatColor.AQUA + "Sell " + ChatColor.RED + "185 Credits");
			item7_lore.add("");
			item7_lore.add(ChatColor.DARK_GRAY + "" + ChatColor.ITALIC + "Left-Click To Buy");
			item7_lore.add(ChatColor.DARK_GRAY + "" + ChatColor.ITALIC + "Right-Click To Sell");
			item7_meta.setLore(item7_lore);
			item7.setItemMeta(item7_meta);

			ItemStack item8 = new ItemStack(Material.BLAZE_ROD);
			ItemMeta item8_meta = item8.getItemMeta();
			item8_meta.setDisplayName(ChatColor.GOLD + "" + ChatColor.BOLD + "BLAZE ROD");
			List<String> item8_lore = new ArrayList<String>();
			item8_lore.add(ChatColor.AQUA + "Buy " + ChatColor.GREEN + "500 Credits");
			item8_lore.add(ChatColor.AQUA + "Sell " + ChatColor.RED + "140 Credits");
			item8_lore.add("");
			item8_lore.add(ChatColor.DARK_GRAY + "" + ChatColor.ITALIC + "Left-Click To Buy");
			item8_lore.add(ChatColor.DARK_GRAY + "" + ChatColor.ITALIC + "Right-Click To Sell");
			item8_meta.setLore(item8_lore);
			item8.setItemMeta(item8_meta);

			ItemStack item9 = new ItemStack(Material.SHULKER_SHELL);
			ItemMeta item9_meta = item9.getItemMeta();
			item9_meta.setDisplayName(ChatColor.LIGHT_PURPLE + "" + ChatColor.BOLD + "SHULKER SHELL");
			List<String> item9_lore = new ArrayList<String>();
			item9_lore.add(ChatColor.AQUA + "Buy " + ChatColor.GREEN + "5000 Credits");
			item9_lore.add(ChatColor.AQUA + "Sell " + ChatColor.RED + "150 Credits");
			item9_lore.add("");
			item9_lore.add(ChatColor.DARK_GRAY + "" + ChatColor.ITALIC + "Left-Click To Buy");
			item9_lore.add(ChatColor.DARK_GRAY + "" + ChatColor.ITALIC + "Right-Click To Sell");
			item9_meta.setLore(item9_lore);
			item9.setItemMeta(item9_meta);

			ItemStack item10 = new ItemStack(Material.TOTEM_OF_UNDYING);
			ItemMeta item10_meta = item10.getItemMeta();
			item10_meta.setDisplayName(ChatColor.GOLD + "" + ChatColor.BOLD + "TOTEM OF UNDYING");
			List<String> item10_lore = new ArrayList<String>();
			item10_lore.add(ChatColor.AQUA + "Buy " + ChatColor.GREEN + "15000 Credits");
			item10_lore.add(ChatColor.AQUA + "Sell " + ChatColor.RED + "2000 Credits");
			item10_lore.add("");
			item10_lore.add(ChatColor.DARK_GRAY + "" + ChatColor.ITALIC + "Left-Click To Buy");
			item10_lore.add(ChatColor.DARK_GRAY + "" + ChatColor.ITALIC + "Right-Click To Sell");
			item10_meta.setLore(item10_lore);
			item10.setItemMeta(item10_meta);

			ItemStack item11 = new ItemStack(Material.WITHER_SKELETON_SKULL);
			ItemMeta item11_meta = item11.getItemMeta();
			item11_meta.setDisplayName(ChatColor.DARK_GRAY + "" + ChatColor.BOLD + "WITHER SKELETON SKULL");
			List<String> item11_lore = new ArrayList<String>();
			item11_lore.add(ChatColor.AQUA + "Buy " + ChatColor.GREEN + "5000 Credits");
			item11_lore.add(ChatColor.AQUA + "Sell " + ChatColor.RED + "1000 Credits");
			item11_lore.add("");
			item11_lore.add(ChatColor.DARK_GRAY + "" + ChatColor.ITALIC + "Left-Click To Buy");
			item11_lore.add(ChatColor.DARK_GRAY + "" + ChatColor.ITALIC + "Right-Click To Sell");
			item11_meta.setLore(item11_lore);
			item11.setItemMeta(item11_meta);

			ItemStack item12 = new ItemStack(Material.PHANTOM_MEMBRANE);
			ItemMeta item12_meta = item12.getItemMeta();
			item12_meta.setDisplayName(ChatColor.GRAY + "" + ChatColor.BOLD + "PHANTOM MEMBRANE");
			List<String> item12_lore = new ArrayList<String>();
			item12_lore.add(ChatColor.AQUA + "Buy " + ChatColor.GREEN + "550 Credits");
			item12_lore.add(ChatColor.AQUA + "Sell " + ChatColor.RED + "220 Credits");
			item12_lore.add("");
			item12_lore.add(ChatColor.DARK_GRAY + "" + ChatColor.ITALIC + "Left-Click To Buy");
			item12_lore.add(ChatColor.DARK_GRAY + "" + ChatColor.ITALIC + "Right-Click To Sell");
			item12_meta.setLore(item12_lore);
			item12.setItemMeta(item12_meta);

			ItemStack item13 = new ItemStack(Material.SPONGE);
			ItemMeta item13_meta = item13.getItemMeta();
			item13_meta.setDisplayName(ChatColor.YELLOW + "" + ChatColor.BOLD + "SPONGE");
			List<String> item13_lore = new ArrayList<String>();
			item13_lore.add(ChatColor.AQUA + "Buy " + ChatColor.GREEN + "2500 Credits");
			item13_lore.add(ChatColor.AQUA + "Sell " + ChatColor.RED + "145 Credits");
			item13_lore.add("");
			item13_lore.add(ChatColor.DARK_GRAY + "" + ChatColor.ITALIC + "Left-Click To Buy");
			item13_lore.add(ChatColor.DARK_GRAY + "" + ChatColor.ITALIC + "Right-Click To Sell");
			item13_meta.setLore(item13_lore);
			item13.setItemMeta(item13_meta);

			ItemStack item14 = new ItemStack(Material.DRAGON_EGG);
			ItemMeta item14_meta = item14.getItemMeta();
			item14_meta.setDisplayName(ChatColor.DARK_PURPLE + "" + ChatColor.BOLD + "DRAGON EGG");
			List<String> item14_lore = new ArrayList<String>();
			item14_lore.add(ChatColor.AQUA + "Buy " + ChatColor.GREEN + "1000000 Credits");
			item14_lore.add(ChatColor.AQUA + "Sell " + ChatColor.RED + "15000 Credits");
			item14_lore.add("");
			item14_lore.add(ChatColor.DARK_GRAY + "" + ChatColor.ITALIC + "Left-Click To Buy");
			item14_lore.add(ChatColor.DARK_GRAY + "" + ChatColor.ITALIC + "Right-Click To Sell");
			item14_meta.setLore(item14_lore);
			item14.setItemMeta(item14_meta);

			for (int i = 0; i < 54; i++) {
				mobs.get(player.getName()).setItem(i, blank);
			}
			mobs.get(player.getName()).setItem(45, back);
			mobs.get(player.getName()).setItem(10, item1);
			mobs.get(player.getName()).setItem(11, item2);
			mobs.get(player.getName()).setItem(12, item3);
			mobs.get(player.getName()).setItem(13, item4);
			mobs.get(player.getName()).setItem(14, item5);
			mobs.get(player.getName()).setItem(15, item6);
			mobs.get(player.getName()).setItem(16, item7);
			mobs.get(player.getName()).setItem(19, item8);
			mobs.get(player.getName()).setItem(20, item9);
			mobs.get(player.getName()).setItem(21, item10);
			mobs.get(player.getName()).setItem(22, item11);
			mobs.get(player.getName()).setItem(23, item12);
			mobs.get(player.getName()).setItem(24, item13);
			mobs.get(player.getName()).setItem(25, item14);
			player.openInventory(mobs.get(player.getName()));
		}
	}
}

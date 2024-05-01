package me.WalterWaves.Gambling;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Blackjack implements CommandExecutor, Runnable {

	private static Map<String, Boolean> playing = new HashMap<String, Boolean>();
	private static Map<String, Integer> dealerCards = new HashMap<String, Integer>();
	private static Map<String, Integer> playerCards = new HashMap<String, Integer>();
	private static Map<String, Integer> bet = new HashMap<String, Integer>();
	String prefix = ChatColor.DARK_GRAY + "" + ChatColor.BOLD + "[" + ChatColor.DARK_RED + "" + ChatColor.BOLD
			+ "Black" + ChatColor.RED + "" + ChatColor.BOLD + "Jack" + ChatColor.DARK_GRAY + "" + ChatColor.BOLD + "] ";

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (label.equalsIgnoreCase("blackjack") || label.equalsIgnoreCase("bj")) {
			Player player = (Player) sender;
			if (args.length == 0) {
				if (playing.get(player.getName()) == false) {
					player.sendMessage(prefix + ChatColor.GREEN + "Use " + ChatColor.AQUA + "/blackjack <bet>"
							+ ChatColor.GREEN + " to start playing!");
				} else {
					player.sendMessage(prefix + ChatColor.GREEN + "Use " + ChatColor.AQUA + "/blackjack <hit/stand>"
							+ ChatColor.GREEN + " to choose your next move!");
				}
			}
			if (args.length == 1) {
				if (playing.get(player.getName()) == false) {
					if (Integer.parseInt(args[0]) > 0) {
						if ((int) Main.data.getConfig().get(player.getName() + ".Credits") >= Integer
								.parseInt(args[0])) {
							player.sendMessage(
									ChatColor.GREEN + "✔ Purchase Successful! Bet: " + args[0] + " Credits.");
							Main.data.getConfig().set(player.getName() + ".Credits",
									(int) Main.data.getConfig().get(player.getName() + ".Credits")
											- Integer.parseInt(args[0]));
							Main.data.saveConfig();
							Main.logs.getConfig()
							.set(player.getName() + "'s Logs" + ".Blackjack" + "." + Main.formatter.format(Main.date)
									+ ".TransactionID " + (Main.random(1000000) + 1000000),
									"Played Blackjack for " + args[0] + " Credits. Balance: "
											+ Main.data.getConfig().get(player.getName() + ".Credits") + " Credits.");
							Main.logs.saveConfig();
							bet.put(player.getName(), Integer.parseInt(args[0]));
							playing.put(player.getName(), true);
							playerCards.put(player.getName(), Main.random(11) + 1);
							playerCards.put(player.getName(), playerCards.get(player.getName()) + Main.random(11));
							dealerCards.put(player.getName(), Main.random(11) + 1);
							player.sendMessage(prefix + ChatColor.RED + "Dealer's Cards: " + ChatColor.AQUA
									+ dealerCards.get(player.getName()) + " + ?");
							for (int i = 0; i <= 10; i++) {
								if (dealerCards.get(player.getName()) < 13) {
									dealerCards.put(player.getName(),
											dealerCards.get(player.getName()) + Main.random(11) + 1);
								}
							}
							player.sendMessage(prefix + ChatColor.GREEN + "Your Cards: " + ChatColor.AQUA
									+ playerCards.get(player.getName()));
							if (playerCards.get(player.getName()) == 21) {
								playing.put(player.getName(), false);
								Main.data.getConfig().set(player.getName() + ".Credits",
										(int) Main.data.getConfig().get(player.getName() + ".Credits")
												+ bet.get(player.getName()) * 2);
								Main.data.saveConfig();
								player.sendMessage(prefix + ChatColor.DARK_RED + "" + ChatColor.BOLD + "BLACKJACK! "
										+ ChatColor.GREEN + "✔ You Won! New Balance: "
										+ Main.data.getConfig().get(player.getName() + ".Credits") + " Credits.");
								player.playSound(player, Sound.UI_TOAST_CHALLENGE_COMPLETE, 1f, 1f);
								Main.logs.getConfig()
								.set(player.getName() + "'s Logs" + ".Blackjack_Win" + "."
										+ Main.formatter.format(Main.date) + ".TransactionID "
										+ (Main.random(1000000) + 1000000),
										"Had a BLACKJACK for " + (bet.get(player.getName()) * 2)
												+ " Credits. Balance: "
												+ Main.data.getConfig().get(player.getName() + ".Credits")
												+ " Credits.");
								Main.logs.saveConfig();
							}
						} else {
							player.sendMessage(ChatColor.RED + "⚠ Not Enough Credits! Balance: "
									+ Main.data.getConfig().get(player.getName() + ".Credits") + " Credits.");
						}
					} else {
						player.sendMessage(ChatColor.RED + "⚠ Invalid Argument!");
					}
				} else {
					if (args[0].equalsIgnoreCase("hit")) {
						playerCards.put(player.getName(), playerCards.get(player.getName()) + Main.random(11) + 1);
						if (playerCards.get(player.getName()) > 21) {
							player.sendMessage(prefix + ChatColor.RED + "" + ChatColor.BOLD + "BUSTED! You Lost! "
									+ ChatColor.GREEN + "Your Cards: " + ChatColor.AQUA
									+ playerCards.get(player.getName()));
							playing.put(player.getName(), false);
							player.playSound(player, Sound.BLOCK_ANVIL_DESTROY, 1, 1);
						} else {
							player.sendMessage(prefix + ChatColor.GREEN + "Your Cards: " + ChatColor.AQUA
									+ playerCards.get(player.getName()));
						}
					}
					if (args[0].equalsIgnoreCase("stand")) {
						player.sendMessage(prefix + ChatColor.RED + "Dealer's Cards: " + ChatColor.AQUA
								+ dealerCards.get(player.getName()));
						player.sendMessage(prefix + ChatColor.GREEN + "Your Cards: " + ChatColor.AQUA
								+ playerCards.get(player.getName()));
						playing.put(player.getName(), false);
						if (playerCards.get(player.getName()) > dealerCards.get(player.getName())
								|| dealerCards.get(player.getName()) > 21) {
							Main.data.getConfig().set(player.getName() + ".Credits",
									(int) Main.data.getConfig().get(player.getName() + ".Credits")
											+ bet.get(player.getName()) * 2);
							Main.data.saveConfig();
							player.sendMessage(prefix + ChatColor.GREEN + "✔ You Won! New Balance: "
									+ Main.data.getConfig().get(player.getName() + ".Credits") + " Credits.");
							player.playSound(player, Sound.UI_TOAST_CHALLENGE_COMPLETE, 1f, 1f);
							Main.logs.getConfig()
							.set(player.getName() + "'s Logs" + ".Blackjack_Win" + "."
									+ Main.formatter.format(Main.date) + ".TransactionID "
									+ (Main.random(1000000) + 1000000),
									"Won at Blackjack " + (bet.get(player.getName()) * 2)
											+ " Credits. Balance: "
											+ Main.data.getConfig().get(player.getName() + ".Credits")
											+ " Credits.");
							Main.logs.saveConfig();
						} else {
							if (playerCards.get(player.getName()) == dealerCards.get(player.getName())) {
								player.sendMessage(prefix + ChatColor.GRAY + "" + ChatColor.BOLD + "Draw!");
								Main.data.getConfig().set(player.getName() + ".Credits",
										(int) Main.data.getConfig().get(player.getName() + ".Credits")
												+ bet.get(player.getName()));
								Main.data.saveConfig();
								Main.logs.getConfig()
								.set(player.getName() + "'s Logs" + ".Blackjack_Draw" + "."
										+ Main.formatter.format(Main.date) + ".TransactionID "
										+ (Main.random(1000000) + 1000000),
										"Won at Blackjack " + (bet.get(player.getName()))
												+ " Credits. Balance: "
												+ Main.data.getConfig().get(player.getName() + ".Credits")
												+ " Credits.");
								Main.logs.saveConfig();
							} else {
								player.sendMessage(prefix + ChatColor.RED + "" + ChatColor.BOLD + "You Lost!");
								player.playSound(player, Sound.BLOCK_ANVIL_DESTROY, 1, 1);
							}
						}
					}
				}
			}
		}
		return false;
	}

	@Override
	public void run() {
		for (Player players : Bukkit.getOnlinePlayers()) {
			if (playing.get(players.getName()) == null) {
				playing.put(players.getName(), false);
			}
		}
	}
}

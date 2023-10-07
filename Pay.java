package me.WalterWaves.Gambling;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Pay implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		Player player = (Player) sender;
		if (label.equalsIgnoreCase("pay")) {
			if (args.length == 2) {
				List<String> players = new ArrayList<String>();
				for (Player p : Bukkit.getOnlinePlayers()) {
					players.add(p.getName());
				}
				if ((int) Main.data.getConfig().get(player.getName() + ".Credits") >= Integer.parseInt(args[0])
						&& Integer.parseInt(args[0]) > 0) {
					if (players.contains(args[1])) {
						if (player == Bukkit.getPlayerExact(args[1])) {
							Main.data.getConfig().set(player.getName() + ".Credits",
									(int) Main.data.getConfig().get(player.getName() + ".Credits")
											- Integer.parseInt(args[0]));
							Main.data.getConfig().set(args[1] + ".Credits",
									(int) Main.data.getConfig().get(args[1] + ".Credits") + Integer.parseInt(args[0]));
							Main.data.saveConfig();
							Main.logs.getConfig().set(
									player.getName() + "'s Logs" + ".Payments" + "." + Main.formatter.format(Main.date)
											+ ".TransactionID " + (Main.random(1000000) + 1000000),
									"SENT " + args[0] + " Credits to " + args[1] + ". Balance: "
											+ Main.data.getConfig().get(player.getName() + ".Credits") + " Credits.");
							Main.logs.getConfig()
									.set(args[1] + "'s Logs" + ".Payments" + "." + Main.formatter.format(Main.date)
											+ ".TransactionID " + (Main.random(1000000) + 1000000),
											"RECEIVED " + args[0] + " Credits from " + player.getName() + ". Balance: "
													+ Main.data.getConfig()
															.get(args[1] + ".Credits")
													+ " Credits.");
							Main.logs.saveConfig();
							player.sendMessage(ChatColor.GREEN + "✔ Successfully sent " + ChatColor.AQUA + args[0]
									+ ChatColor.GREEN + " Credits to " + ChatColor.AQUA + args[1] + ChatColor.GREEN
									+ ". New Balance: " + Main.data.getConfig().get(player.getName() + ".Credits")
									+ " Credits.");
							Bukkit.getPlayerExact(args[1])
									.sendMessage(ChatColor.GREEN + "✔ Successfully received " + ChatColor.AQUA + args[0]
											+ ChatColor.GREEN + " Credits from " + ChatColor.AQUA + player.getName()
											+ ChatColor.GREEN + ". New Balance: "
											+ Main.data.getConfig().get(args[1] + ".Credits") + " Credits.");
						} else {
							player.sendMessage(ChatColor.RED + "⚠ You Can't Send Credits To Yourself!");
						}
					} else {
						player.sendMessage(ChatColor.RED + "⚠ Target Player Not Found!");
					}
				} else {
					player.sendMessage(ChatColor.RED + "⚠ Not Enough Credits Or Invalid Value! Balance: "
							+ Main.data.getConfig().get(player.getName() + ".Credits") + " Credits.");
				}
			} else {
				player.sendMessage(ChatColor.RED + "⚠ Invalid Arguments! Usage: /pay <amount> <player>");
			}
		}
		return false;
	}

}

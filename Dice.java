package me.WalterWaves.Gambling;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Dice implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		Player player = (Player) sender;
		if (label.equalsIgnoreCase("dice")) {
			if (args.length == 2 && Integer.parseInt(args[0]) <= 6) {
				if ((int) Main.data.getConfig().get(player.getName() + ".Credits") >= Integer.parseInt(args[1])) {
					Main.data.getConfig().set(player.getName() + ".Credits",
							(int) Main.data.getConfig().get(player.getName() + ".Credits") - Integer.parseInt(args[1]));
					Main.data.saveConfig();
					player.sendMessage(ChatColor.GREEN + "✔ Purchase Successful! Bet: " + args[1] + " Credits.");
					int random = 1 + Main.random(6);
					if (random == 1) {
						player.sendMessage(ChatColor.GREEN + "⚀");
					}
					if (random == 2) {
						player.sendMessage(ChatColor.GREEN + "⚁");
					}
					if (random == 3) {
						player.sendMessage(ChatColor.GREEN + "⚂");
					}
					if (random == 4) {
						player.sendMessage(ChatColor.GREEN + "⚃");
					}
					if (random == 5) {
						player.sendMessage(ChatColor.GREEN + "⚄");
					}
					if (random == 6) {
						player.sendMessage(ChatColor.GREEN + "⚅");
					}
					if (random == Integer.parseInt(args[0])) {
						Main.data.getConfig().set(player.getName() + ".Credits",
								(int) Main.data.getConfig().get(player.getName() + ".Credits")
										+ Integer.parseInt(args[1]) * 6);
						Main.data.saveConfig();
						player.sendMessage(ChatColor.GREEN + "✔ You Won! New Balance: "
								+ Main.data.getConfig().get(player.getName() + ".Credits") + " Credits.");
					} else {
						player.sendMessage(ChatColor.RED + "❌ You Lost! Balance: "
								+ Main.data.getConfig().get(player.getName() + ".Credits") + " Credits.");
					}
				} else {
					player.sendMessage(ChatColor.RED + "⚠ Not Enough Credits! Balance: "
							+ Main.data.getConfig().get(player.getName() + ".Credits") + " Credits.");
				}
			} else {
				player.sendMessage(ChatColor.RED + "⚠ Invalid Arguments! Usage: /dice <prediction (1-6)> <bet>");
			}
		}
		return false;
	}

}

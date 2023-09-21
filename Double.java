package me.WalterWaves.Gambling;

import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Double implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		Player player = (Player) sender;
		if (label.equalsIgnoreCase("double")) {
			if (args.length == 2 && Integer.parseInt(args[0]) > 0 && (args[1].equalsIgnoreCase("under") || args[1].equalsIgnoreCase("over"))) {
				if ((int) Main.data.getConfig().get(player.getName() + ".Credits") >= Integer.parseInt(args[0])) {
					Main.data.getConfig().set(player.getName() + ".Credits",
							(int) Main.data.getConfig().get(player.getName() + ".Credits") - Integer.parseInt(args[0]));
					Main.data.saveConfig();
					player.sendMessage(ChatColor.GREEN + "✔ Purchase Successful! Bet: " + args[0] + " Credits.");
					if (args[1].equalsIgnoreCase("under")) {
						int random = Main.random(100);
						if (random < 50) {
							Main.data.getConfig().set(player.getName() + ".Credits",
									(int) Main.data.getConfig().get(player.getName() + ".Credits")
											+ Integer.parseInt(args[0]) * 2);
							Main.data.saveConfig();
							player.sendMessage(ChatColor.GREEN + "✔ You Won! New Balance: "
									+ Main.data.getConfig().get(player.getName() + ".Credits") + " Credits. Roll: " + random + ".");
							player.playSound(player, Sound.UI_TOAST_CHALLENGE_COMPLETE, 1f, 1f);
						} else {
							player.sendMessage(ChatColor.RED + "❌ You Lost! Balance: "
									+ Main.data.getConfig().get(player.getName() + ".Credits") + " Credits. Roll: " + random + ".");
							player.playSound(player, Sound.BLOCK_ANVIL_DESTROY, 1f, 1f);
						}
					}
					if (args[1].equalsIgnoreCase("over")) {
						int random = Main.random(100);
						if (random >= 50) {
							Main.data.getConfig().set(player.getName() + ".Credits",
									(int) Main.data.getConfig().get(player.getName() + ".Credits")
											+ Integer.parseInt(args[0]) * 2);
							Main.data.saveConfig();
							player.sendMessage(ChatColor.GREEN + "✔ You Won! New Balance: "
									+ Main.data.getConfig().get(player.getName() + ".Credits") + " Credits. Roll: " + random + ".");
							player.playSound(player, Sound.UI_TOAST_CHALLENGE_COMPLETE, 1f, 1f);
						} else {
							player.sendMessage(ChatColor.RED + "❌ You Lost! Balance: "
									+ Main.data.getConfig().get(player.getName() + ".Credits") + " Credits. Roll: " + random + ".");
							player.playSound(player, Sound.BLOCK_ANVIL_DESTROY, 1f, 1f);
						}
					}
				} else {
					player.sendMessage(ChatColor.RED + "⚠ Not Enough Credits! Balance: "
							+ Main.data.getConfig().get(player.getName() + ".Credits") + " Credits.");
				}
			} else {
				player.sendMessage(ChatColor.RED + "⚠ Invalid Arguments! Usage: /double <bet> <over/under>");
			}
		}
		return false;
	}

}

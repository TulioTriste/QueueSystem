package me.tulio.queue.commands;

import org.bukkit.entity.Player;

import me.tulio.queue.QueueMain;
import me.tulio.queue.utilities.Color;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.CommandExecutor;

public class LeaveQueueCommand implements CommandExecutor {
	
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (!(sender instanceof Player)) {
			return true;
		}
		Player p = (Player) sender;
		if (QueueMain.getPlugin().getQueueManager().getQueue(p) == null) {
			for(String lines : QueueMain.getPlugin().getConfig().getStringList("NOT_IN_QUEUE")) {
				sender.sendMessage(Color.translate(lines));
			}
			return true;
		}
		if (QueueMain.getPlugin().getQueueManager().getQueue(p) != null) {
			for(String lines : QueueMain.getPlugin().getConfig().getStringList("QUEUE_LEFT")) {
				p.sendMessage(Color.translate(lines.replace("%server%",
						QueueMain.getPlugin().getQueueManager().getQueueName(p))));
			}
			QueueMain.getPlugin().getQueueManager().getQueue(p).removeEntry(p);
			return true;
		}
		return false;
	}
}

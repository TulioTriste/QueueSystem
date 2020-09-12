package me.tulio.queue.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import me.tulio.queue.QueueMain;
import me.tulio.queue.system.Queue;
import me.tulio.queue.utilities.Color;

import org.bukkit.command.CommandExecutor;

public class PauseQueueCommand implements CommandExecutor {

	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (!sender.hasPermission("hub.queue.pause")) {
			sender.sendMessage(Color.translate(QueueMain.getPlugin().getConfig().getString("NO_PERMISSION")));
			return true;
		}
		if (args.length != 1) {
			sender.sendMessage("§cUsage: /" + label + " <queue>");
			return true;
		}
		String server = args[0];
		if (QueueMain.getPlugin().getQueueManager().getQueue(server) == null) {
			for(String lines : QueueMain.getPlugin().getConfig().getStringList("INVALID_QUEUE")) {
				sender.sendMessage(Color.translate(lines));
			}
			return true;
		}
		Queue q = QueueMain.getPlugin().getQueueManager().getQueue(server);
		if (q.isPaused()) {
			for(String lines : QueueMain.getPlugin().getConfig().getStringList("QUEUE_UNPAUSED")) {
				sender.sendMessage(Color.translate(lines.replace("%server%", q.getServer())));
			}
		} else {
			for(String lines : QueueMain.getPlugin().getConfig().getStringList("QUEUE_PAUSED")) {
				sender.sendMessage(Color.translate(lines.replace("%server%", q.getServer())));
			}
		}
		q.setPaused(!q.isPaused());
		return true;
	}
}

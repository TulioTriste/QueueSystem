package me.tulio.queue.commands;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.tulio.queue.QueueMain;
import me.tulio.queue.utilities.Color;

public class PlayQueueCommand implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(!(sender instanceof Player)) {
			sender.sendMessage(Color.translate("&cNo console."));
			return true;
		}
		Player player = (Player) sender;
		List<String> servers = new ArrayList<>(QueueMain.getPlugin().getConfig().getStringList("queue.servers"));
		if(args.length == 0) {
			player.sendMessage(Color.translate("&cUse: /" + label + " " + servers.toString().replace("[", "").replace("]", "")));
			return true;
		}
		QueueMain.getPlugin().getQueueManager().getQueue("hcf").addEntry(player);
		return true;
	}

}

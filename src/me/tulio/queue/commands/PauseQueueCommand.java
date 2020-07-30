package me.tulio.queue.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import me.tulio.queue.QueueMain;
import me.tulio.queue.system.Queue;
import me.tulio.queue.utilities.Color;

import org.bukkit.command.CommandExecutor;

public class PauseQueueCommand implements CommandExecutor
{
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
            sender.sendMessage(Color.translate(QueueMain.getPlugin().getConfig().getString("INVALID_QUEUE")));
            return true;
        }
        Queue q = QueueMain.getPlugin().getQueueManager().getQueue(server);
        if (q.isPaused()) {
            sender.sendMessage(Color.translate(QueueMain.getPlugin().getConfig().getString("QUEUE_UNPAUSED").replace("%server%", q.getServer())));
        }
        else {
            sender.sendMessage(Color.translate(QueueMain.getPlugin().getConfig().getString("QUEUE_PAUSED").replace("%server%", q.getServer())));
        }
        q.setPaused(!q.isPaused());
        return true;
    }
}

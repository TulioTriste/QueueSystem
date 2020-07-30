package me.tulio.queue.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import com.google.common.primitives.Ints;

import me.tulio.queue.QueueMain;
import me.tulio.queue.system.Queue;
import me.tulio.queue.utilities.Color;

import org.bukkit.command.CommandExecutor;

public class SetLimitCommand implements CommandExecutor
{
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!sender.hasPermission("hub.queue.setlimit")) {
            sender.sendMessage(Color.translate(QueueMain.getPlugin().getConfig().getString("NO_PERMISSION")));
            return true;
        }
        if (args.length != 2) {
            sender.sendMessage("§cUsage: /" + label + " <amount>");
            return true;
        }
        String server = args[0];
        if (QueueMain.getPlugin().getQueueManager().getQueue(server) == null) {
            sender.sendMessage(Color.translate(QueueMain.getPlugin().getConfig().getString("INVALID_QUEUE")));
            return true;
        }
        Queue q = QueueMain.getPlugin().getQueueManager().getQueue(server);
        if (Ints.tryParse(args[1]) == null) {
            sender.sendMessage(Color.translate(QueueMain.getPlugin().getConfig().getString("INVALID_NUMBER")));
            return true;
        }
        int i = Ints.tryParse(args[1]);
        q.setLimit(i);
        sender.sendMessage(Color.translate(QueueMain.getPlugin().getConfig().getString("LIMIT_SET").replace("%server%", q.getServer()).replace("%amount%", String.valueOf(i))));
        return true;
    }
}

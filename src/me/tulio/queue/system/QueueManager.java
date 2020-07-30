package me.tulio.queue.system;

import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.OfflinePlayer;

import me.tulio.queue.QueueMain;
import me.tulio.queue.utilities.ServerUtils;

import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.Bukkit;
import java.util.ArrayList;
import java.util.List;
import org.bukkit.event.Listener;

public class QueueManager implements Listener
{
    private List<Queue> queues;
    
    public QueueManager() {
        this.queues = new ArrayList<Queue>();
        Bukkit.getPluginManager().registerEvents(this, QueueMain.getPlugin());
        for (String s : QueueMain.getPlugin().getConfig().getStringList("queue.servers")) {
            this.queues.add(new Queue(s));
        }
        new BukkitRunnable() {
            public void run() {
                for (Queue q : QueueManager.this.queues) {
                    int playerCount = ServerUtils.getPlayerCount(q.getServer());
                    if (!q.isPaused() && !q.getPlayers().isEmpty() && playerCount < q.getLimit()) {
                        if (q.getPlayerAt(0).isOnline()) {
                            q.sendFirst();
                        }
                        if (q.getPlayerAt(0).isOnline()) {
                            continue;
                        }
                        Player p = (Player)q.getPlayerAt(0);
                        if (q.getPlayers().contains(p)) {
                            q.getPlayers().remove(p);
                        }
                        if (!q.getTaskMap().containsKey(p)) {
                            continue;
                        }
                        q.getTaskMap().get(p).cancel();
                        q.getTaskMap().remove(p);
                    }
                }
            }
        }.runTaskTimer(QueueMain.getPlugin(), 20L, (long)QueueMain.getPlugin().getConfig().getInt("queue.send-delay"));
    }
    
    public Queue getQueue(OfflinePlayer p) {
        for (Queue q : this.queues) {
            if (q.getPlayers().contains(p)) {
                return q;
            }
        }
        return null;
    }
    
    public Queue getQueue(String s) {
        for (Queue q : this.queues) {
            if (q.getServer().equalsIgnoreCase(s)) {
                return q;
            }
        }
        return null;
    }
    
    public String getQueueName(OfflinePlayer p) {
        return this.getQueue(p).getServer();
    }
    
    public int getPriority(OfflinePlayer p) {
        return QueueMain.getPlugin().getConfig().getInt("list-ranks." + QueueMain.getPlugin().getPerms().getPrimaryGroup(null, p) + ".priority");
    }
    
    @EventHandler
    public void onQuit(PlayerQuitEvent e) {
        Player p = e.getPlayer();
        for (Queue q : this.queues) {
            if (q.getPlayers().contains(p)) {
                q.removeEntry((OfflinePlayer)p);
            }
        }
    }
}

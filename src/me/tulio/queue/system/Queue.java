package me.tulio.queue.system;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;

import me.tulio.queue.QueueMain;

import java.util.Collections;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import java.util.HashMap;
import java.util.ArrayList;
import org.bukkit.scheduler.BukkitTask;
import java.util.Map;
import org.bukkit.OfflinePlayer;
import java.util.List;

public class Queue
{
    private String server;
    private List<OfflinePlayer> list;
    private Map<OfflinePlayer, BukkitTask> taskMap;
    private boolean paused;
    private int limit;
    
    public Queue(String server) {
        this.server = server;
        this.list = new ArrayList<OfflinePlayer>();
        this.taskMap = new HashMap<OfflinePlayer, BukkitTask>();
        this.paused = false;
        this.limit = 400;
        new BukkitRunnable() {
            public void run() {
                for (OfflinePlayer o : Queue.this.list) {
                    if (o.isOnline()) {
                        Player p = (Player)o;
                        for (String s : QueueMain.getPlugin().getConfig().getStringList("QUEUE_MESSAGE")) {
                            p.sendMessage(ChatColor.translateAlternateColorCodes('&', s.replace("%position%", String.valueOf(Queue.this.getPosition(p))).replace("%total%", String.valueOf(Queue.this.getSize())).replace("%server%", server)));
                        }
                    }
                    else {
                        Queue.this.list.remove(o);
                    }
                }
            }
        }.runTaskTimer(QueueMain.getPlugin(), 300L, 300L);
    }
    
    public void addEntry(OfflinePlayer p) {
        if (this.list.contains(p)) {
            return;
        }
        if (QueueMain.getPlugin().getQueueManager().getPriority(p) == 0) {
            Player o = (Player)p;
            this.sendDirect(o);
            o.sendMessage(QueueMain.getPlugin().getConfig().getString("QUEUE_SENT").replace("%server%", this.server));
            return;
        }
        this.list.add(p);
        for (OfflinePlayer o2 : this.list) {
            int pos = this.list.indexOf(o2);
            if (p != o2 && QueueMain.getPlugin().getQueueManager().getPriority(p) < QueueMain.getPlugin().getQueueManager().getPriority(o2)) {
                Collections.swap(this.list, pos, this.list.size() - 1);
            }
        }
    }
    
    public List<OfflinePlayer> getPlayers() {
        return this.list;
    }
    
    public void removeEntry(OfflinePlayer p) {
        this.list.remove(p);
    }
    
    public int getSize() {
        return this.list.size();
    }
    
    public OfflinePlayer getPlayerAt(int i) {
        return this.list.get(i);
    }
    
    public int getPosition(Player p) {
        return this.list.indexOf(p) + 1;
    }
    
    public void sendFirst() {
        if (!this.list.isEmpty()) {
            Player p = this.list.get(0).getPlayer();
            ByteArrayDataOutput out = ByteStreams.newDataOutput();
            out.writeUTF("Connect");
            out.writeUTF(this.server);
            p.sendPluginMessage(QueueMain.getPlugin(), "BungeeCord", out.toByteArray());
        }
    }
    
    public void sendDirect(Player p) {
        ByteArrayDataOutput out = ByteStreams.newDataOutput();
        out.writeUTF("Connect");
        out.writeUTF(this.server);
        p.sendPluginMessage(QueueMain.getPlugin(), "BungeeCord", out.toByteArray());
    }
    
    public String getServer() {
        return this.server;
    }
    
    public List<OfflinePlayer> getList() {
        return this.list;
    }
    
    public Map<OfflinePlayer, BukkitTask> getTaskMap() {
        return this.taskMap;
    }
    
    public boolean isPaused() {
        return this.paused;
    }
    
    public int getLimit() {
        return this.limit;
    }
    
    public void setServer(String server) {
        this.server = server;
    }
    
    public void setList(List<OfflinePlayer> list) {
        this.list = list;
    }
    
    public void setTaskMap(Map<OfflinePlayer, BukkitTask> taskMap) {
        this.taskMap = taskMap;
    }
    
    public void setPaused(boolean paused) {
        this.paused = paused;
    }
    
    public void setLimit(int limit) {
        this.limit = limit;
    }
    
    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof Queue)) {
            return false;
        }
        Queue other = (Queue)o;
        if (!other.canEqual(this)) {
            return false;
        }
        Object this$server = this.getServer();
        Object other$server = other.getServer();
        Label_0065: {
            if (this$server == null) {
                if (other$server == null) {
                    break Label_0065;
                }
            }
            else if (this$server.equals(other$server)) {
                break Label_0065;
            }
            return false;
        }
        Object this$list = this.getList();
        Object other$list = other.getList();
        Label_0102: {
            if (this$list == null) {
                if (other$list == null) {
                    break Label_0102;
                }
            }
            else if (this$list.equals(other$list)) {
                break Label_0102;
            }
            return false;
        }
        Object this$taskMap = this.getTaskMap();
        Object other$taskMap = other.getTaskMap();
        if (this$taskMap == null) {
            if (other$taskMap == null) {
                return this.isPaused() == other.isPaused() && this.getLimit() == other.getLimit();
            }
        }
        else if (this$taskMap.equals(other$taskMap)) {
            return this.isPaused() == other.isPaused() && this.getLimit() == other.getLimit();
        }
        return false;
    }
    
    protected boolean canEqual(Object other) {
        return other instanceof Queue;
    }
    
    @Override
    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        Object $server = this.getServer();
        result = result * PRIME + (($server == null) ? 43 : $server.hashCode());
        Object $list = this.getList();
        result = result * PRIME + (($list == null) ? 43 : $list.hashCode());
        Object $taskMap = this.getTaskMap();
        result = result * PRIME + (($taskMap == null) ? 43 : $taskMap.hashCode());
        result = result * PRIME + (this.isPaused() ? 79 : 97);
        result = result * PRIME + this.getLimit();
        return result;
    }
    
    @Override
    public String toString() {
        return "Queue(server=" + this.getServer() + ", list=" + this.getList() + ", taskMap=" + this.getTaskMap() + ", paused=" + this.isPaused() + ", limit=" + this.getLimit() + ")";
    }
}

package me.tulio.queue.utilities;

import com.google.common.io.ByteArrayDataInput;
import org.bukkit.entity.Player;
import java.net.SocketAddress;
import java.net.Socket;
import java.net.InetSocketAddress;
import com.google.common.io.ByteArrayDataOutput;
import org.bukkit.Bukkit;
import com.google.common.io.ByteStreams;

import me.tulio.queue.QueueMain;

import java.util.List;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.TreeMap;
import java.util.Map;
import org.bukkit.plugin.messaging.PluginMessageListener;

public class ServerUtils implements PluginMessageListener
{
    public static Map<String, Integer> playerCounts;
    
    static {
        playerCounts = new TreeMap<String, Integer>(String.CASE_INSENSITIVE_ORDER);
    }
    
    public static void startPlayerCountTask() {
        List<String> servers = QueueMain.getPlugin().getConfig().getStringList("utilities.servers");
        new BukkitRunnable() {
            public void run() {
                for (String s : servers) {
                    ByteArrayDataOutput globalOut = ByteStreams.newDataOutput();
                    globalOut.writeUTF("PlayerCount");
                    globalOut.writeUTF(s);
                    Bukkit.getServer().sendPluginMessage(QueueMain.getPlugin(), "BungeeCord", globalOut.toByteArray());
                }
            }
        }.runTaskTimer(QueueMain.getPlugin(), 20L, 20L);
    }
    
    public static boolean isOnline(String server) {
        int port = QueueMain.getPlugin().getConfig().getInt("utilities.ports." + server.toLowerCase());
        try {
            SocketAddress a = new InetSocketAddress("localhost", port);
            Socket s = new Socket();
            s.connect(a, 1000);
            s.close();
            return true;
        }
        catch (Exception e) {
            return false;
        }
    }
    
    public static int getPlayerCount(String server) {
        return ServerUtils.playerCounts.containsKey(server) ? ServerUtils.playerCounts.get(server) : 0;
    }
    
    public void onPluginMessageReceived(String channel, Player player, byte[] message) {
        if (channel.equals("BungeeCord")) {
            try {
                if (message.length != 0) {
                    ByteArrayDataInput in = ByteStreams.newDataInput(message);
                    if (in.readUTF().equals("PlayerCount")) {
                        playerCounts.put(in.readUTF(), Integer.valueOf(in.readInt()));
                    }
                }
            } catch (Exception e) {
            }
        }
    }
}

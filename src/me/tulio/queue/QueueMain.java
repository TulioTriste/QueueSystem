package me.tulio.queue;

import org.bukkit.Bukkit;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import lombok.Getter;
import lombok.Setter;
import me.tulio.queue.commands.LeaveQueueCommand;
import me.tulio.queue.commands.PauseQueueCommand;
import me.tulio.queue.commands.PlayQueueCommand;
import me.tulio.queue.commands.SetLimitCommand;
import me.tulio.queue.system.QueueManager;
import me.tulio.queue.utilities.ServerUtils;
import net.milkbowl.vault.chat.Chat;
import net.milkbowl.vault.permission.Permission;

@Setter
@Getter
public class QueueMain extends JavaPlugin {
	
	@Getter
	private static QueueMain plugin;
    private QueueManager queueManager;
    private Permission perms;
	private Chat chat;
	
	@Override
	public void onEnable() {
        QueueMain.plugin = this;
        if (!getDataFolder().exists()) {
            getDataFolder().mkdirs();
        }
        saveDefaultConfig();
        queueManager = new QueueManager();
		setUpPermissions();
		setUpChat();
		registerCommands();
        Bukkit.getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");
        Bukkit.getMessenger().registerIncomingPluginChannel(this, "BungeeCord", new ServerUtils());
        ServerUtils.startPlayerCountTask();
	}
	
	@Override
	public void onDisable() {
        QueueMain.plugin = null;
	}
	
	public void registerCommands() {
		getCommand("leavequeue").setExecutor(new LeaveQueueCommand());
		getCommand("pausequeue").setExecutor(new PauseQueueCommand());
		getCommand("playqueue").setExecutor(new PlayQueueCommand());
		getCommand("setlimit").setExecutor(new SetLimitCommand());
	}
    
    private boolean setUpPermissions() {
        perms = (Permission) getServer().getServicesManager().getRegistration(Permission.class).getProvider();
        return perms != null;
    }
    
    private boolean setUpChat() {
        RegisteredServiceProvider<Chat> chatProvider = getServer().getServicesManager().getRegistration(Chat.class);
        if (chatProvider != null) {
            chat = (Chat) chatProvider.getProvider();
        }
        return chat != null;
    }
}

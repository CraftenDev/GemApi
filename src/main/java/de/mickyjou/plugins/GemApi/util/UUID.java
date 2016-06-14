package me.mickyjou.main;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
public class UUID {
	
	
	public static Player getPlayer(String playerName){
		Object[] playerObjects = Bukkit.getServer().getOnlinePlayers().toArray();
		
		for(Object playerObject : playerObjects){
			Player player = (Player) playerObject;
			if(player.getName().toLowerCase().equals(playerName.toLowerCase())){
				return player;
			}
		}
		
		return null;
	}
	public static OfflinePlayer getOfflinePlayer(String playerName){
		OfflinePlayer[] offlinePlayers = Bukkit.getServer().getOfflinePlayers();
		
		for(OfflinePlayer aktuellerPlayer : offlinePlayers){
			if(aktuellerPlayer.getName().toLowerCase().equals(playerName.toLowerCase())){
				return aktuellerPlayer;
			}
		}
		
		return null;
	}
	public static java.util.UUID getUUID(String name){
		Player p = getPlayer(name);
		
		if(p != null){
			return getPlayer(name).getUniqueId();
		}
		
		return null;
	}

}

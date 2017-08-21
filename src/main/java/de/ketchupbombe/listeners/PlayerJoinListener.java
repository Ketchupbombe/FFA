package de.ketchupbombe.listeners;

import de.ketchupbombe.FFA;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

/**
 * @author Ketchupbombe
 * @version 1.0
 */
public class PlayerJoinListener implements Listener {

    private FFA ffa = FFA.getInstance();

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {
        Player p = e.getPlayer();
        p.teleport(ffa.getLocationManager().getSpawnLocationByWorld(ffa.getMapManager().getCurrentMap()));
        p.getInventory().setContents(ffa.getKitManager().getKitContents(ffa.getKitManager().getCurrentKit()));
        p.getInventory().setArmorContents(ffa.getKitManager().getKitArmorKontents(ffa.getKitManager().getCurrentKit()));
    }
}

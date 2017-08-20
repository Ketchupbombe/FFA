package de.ketchupbombe.manager;

import de.ketchupbombe.FFA;
import de.ketchupbombe.utils.variables;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class MapChangeManager {

    private int mapChangeSchedulerTime = FFA.getInstance().getConfig().getInt("MapChange.repatingTime");
    private boolean canChangeMap = true;
    private String followingMap = null;
    private int scheduler;

    /**
     * Change the map
     *
     * @param mapname map to switch
     */
    public void ChangeMapTo(String mapname) {
        if (!FFA.getInstance().getMapManager().getOnlineMapCache().isEmpty() && Bukkit.getOnlinePlayers().size() != 0) {
            if (followingMap != null) {
                FFA.getInstance().getMapManager().setCurrentMap(getFollowingMap());
                for (Player all : Bukkit.getOnlinePlayers()) {
                    all.teleport(FFA.getInstance().getLocationManager().getSpawnLocationByWorld(getFollowingMap()));

                }

                setFollowingMap(null);
            } else {
                FFA.getInstance().getMapManager().setCurrentMap(mapname);
                for (Player all : Bukkit.getOnlinePlayers()) {
                    // if (!MapManager.getOnlineMapCache().contains(all.getWorld().getName())) {
                    all.teleport(FFA.getInstance().getLocationManager().getSpawnLocationByWorld(FFA.getInstance().getMapManager().getCurrentMap()));
                    //}
                }

            }
            System.out.println("Changed map to: " + FFA.getInstance().getMapManager().getCurrentMap());
        } else
            Bukkit.broadcastMessage(variables.getPrefix() + "§4§lNo map found or no online players!");
    }

    /**
     * Start the scheduler for mapchanges
     * After the time it change to a random map
     */
    public void startMapChangeScheduler() {
        scheduler = Bukkit.getScheduler().scheduleSyncRepeatingTask(FFA.getInstance(), new Runnable() {
            @Override
            public void run() {
                mapChangeSchedulerTime--;
                if (mapChangeSchedulerTime <= FFA.getInstance().getConfig().getInt("MapChange.cancleForcemapTime")) {
                    if (canChangeMap == true) {
                        canChangeMap = false;
                    }

                }
                if (mapChangeSchedulerTime == 0) {
                    ChangeMapTo(FFA.getInstance().getMapManager().getRandomMap());
                    setMapChangeSchedulerTime(FFA.getInstance().getConfig().getInt("MapChange.repatingTime"));
                    canChangeMap = true;
                }
            }
        }, 0, 20);
    }

    /**
     * Set the time for map change scheduler in seconds!
     *
     * @param mapChangeSchedulerTime time in secons
     */
    public void setMapChangeSchedulerTime(int mapChangeSchedulerTime) {
        this.mapChangeSchedulerTime = mapChangeSchedulerTime;
    }

    /**
     * Get remaning time
     *
     * @return remaing time of map change scheduler
     */
    public int getMapChangeSchedulerTime() {
        return mapChangeSchedulerTime;
    }

    /**
     * Check if mapchange is allowed
     *
     * @return boolean true if mapchange is allowed
     */
    public boolean isCanChangeMap() {
        return canChangeMap;
    }

    /**
     * Set the map after the current map
     * Useful for ex. /forcemap
     *
     * @param followingMap
     */
    public void setFollowingMap(String followingMap) {
        this.followingMap = followingMap;
    }

    /**
     * Get the map which comes after the current map
     * Result is null if the map is random
     *
     * @return name of following map
     */
    public String getFollowingMap() {
        return followingMap;
    }

    /**
     * Stop map change scheduler
     */
    public void cancleMapChangeScheduler() {
        Bukkit.getScheduler().cancelTask(scheduler);
    }

    /**
     * Check if map change is enabled!
     *
     * @return boolean true if map change is enabled
     */
    public boolean isMapChangeEnabled() {
        return FFA.getInstance().getConfig().getBoolean("MapChange.enable");
    }

}

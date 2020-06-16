/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hvh.org;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import org.bukkit.entity.Player;
import org.bukkit.World;
import org.bukkit.ChatColor;
import org.bukkit.Location;

/**
 * @author Leee Leee
 */
class Game {
    
    public final static int SPAWN_RADIUS = 1000;
    public final static int MAX_DISTANCE = 100;
    public final static int NUM_HUNTED = 1;
    public final static int NUM_HUNTER = 0;
    public static int COUNT = 0;
    public static List<Location> STARTS = new ArrayList();

    public boolean inLobby;
    public boolean waitingToStart;
    public boolean playing;
    public boolean done;
    
    private int id;
    private World world;
    private Location hubSpawn;
    private List<HvHPlayer> hunters;
    private List<HvHPlayer> hunteds;
    
    public Game(World world, Location hubSpawn) {
        this.world = world;
        this.hubSpawn = hubSpawn;
        id = COUNT++;
        
        hunters = new ArrayList();
        hunteds = new ArrayList();
        
        resetState();
        inLobby = true;
    }

    private void resetState() {
        inLobby = false;
        waitingToStart = false;
        playing = false;
        done = false;
    }

    public void start() {
        resetState();
        playing = true;
        
        // start the game
	    announce("The game is starting");
       
        // find new spawn location
        Location loc = findStart();
        STARTS.add(loc);

        // tp players there
        tpGroup(hunters, loc);
        tpGroup(hunteds, loc);

        for (HvHPlayer hunter : hunters) {
            // set respawn point
            hunter.player.setBedSpawnLocation(loc);
        }

        // GameEndEvent will call game.end() once hunted dies or wins
    }

    public void end() {
        resetState();
        done = true;

        // announce the winner
        announce("The Hunters have won the game!");
        tellGroup(hunters, ChatColor.GREEN + "You won!");
        tellGroup(hunteds, ChatColor.RED + "You lost!");
        
        // tp players to hub
        tpGroup(hunters, hubSpawn);
        tpGroup(hunteds, hubSpawn);
    }

    /**
     * Find a new game start location
     */
    private Location findStart() {
        Location loc = randomStart();

        // found one
        if (isGoodStart(loc)) {
            return loc;
        }

        // keep searching
        return findStart();
    }

    /**
     * Check if a location has players nearby
     */
    private boolean isGoodStart(Location loc) {
        for (Location gameStart : STARTS) {
            if (gameStart.distance(loc) < MAX_DISTANCE) {
                return false;
            }
        }

        return true;
    }

    /**
     * Find a random spawn point in the world
     */
    private Location randomStart() {
        int x = ThreadLocalRandom.current().nextInt(-SPAWN_RADIUS, SPAWN_RADIUS+1);
        int z = ThreadLocalRandom.current().nextInt(-SPAWN_RADIUS, SPAWN_RADIUS+1);
        int y = world.getHighestBlockYAt(x, z);

        return new Location(world, x, y, z);
    }

    private void checkReady() {
    	if (!needsHunted() && !needsHunter()) {
	        waitingToStart = true;
	    }
    }
    
    public boolean needsHunted() {
        return hunteds.size() < NUM_HUNTED;
    }
    
    public boolean needsHunter() {
        return hunters.size() < NUM_HUNTER;
    }
    
    public boolean addPlayer(HvHPlayer player) {
        switch(player.team) {
            case "hunter":
                if (needsHunter()) {
                    hunters.add(player);
	                checkReady();
                    return true;
                }
                break;
            case "hunted":
                if (needsHunted()) {
                    hunteds.add(player);
	                checkReady();
                    return true;
                }
                break;
        }

        return false;
    }

    public boolean remove(Player player) {
        HvHPlayer hplayer = getHvHPlayer(player);
        switch(hplayer.team) {
            case "hunter":
                return hunters.remove(hplayer);
            case "hunted":
                return hunteds.remove(hplayer);
        }
        return false;
    }

    public HvHPlayer getHvHPlayer(Player player) {
        // check hunters
        for (HvHPlayer hunter : hunters) {
            if (player.equals(hunter.player)) {
                return hunter;
            }
        }
        
        // check hunteds
        for (HvHPlayer hunted : hunteds) {
            if (player.equals(hunted.player)) {
                return hunted;
            }
        }

        return null;
    }

    public String getTeam(Player player) {
        return getHvHPlayer(player).team;
    }

    public void announce(String message) {
        message = "" + ChatColor.BOLD + ChatColor.UNDERLINE + message;
	    tellGroup(hunters, message);
	    tellGroup(hunteds, message);
    }

    public void tellGroup(List<HvHPlayer> group, String message) {
    	for (HvHPlayer hplayer : group) {
	        hplayer.player.sendMessage(ChatColor.GOLD + "[HvH Game] " + ChatColor.LIGHT_PURPLE + message);
	    }
    }

    public void tpGroup(List<HvHPlayer> group, Location loc) {
        for (HvHPlayer hplayer : group) {
            hplayer.player.teleport(loc);
        }
    }

    
    // HELPRS
    // 
    
    public String toString() {
        return "#" + id;
    }
}

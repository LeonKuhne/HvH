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
    public final static int NUM_HUNTER = 1;
    public static int COUNT = 0;
    public static List<Location> STARTS = new ArrayList();

    public boolean inLobby;
    public boolean waitingToStart;
    public boolean playing;
    public boolean done;
    
    private int id;
    private World world;
    private Location hubSpawn;
    private Location gameSpawn;
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
        gameSpawn = findStart();
        STARTS.add(gameSpawn);

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
        announce("The Hunters Won!");
        tellGroup(hunters, ChatColor.GREEN + "You won!");
        tellGroup(hunteds, ChatColor.RED + "You lost!");
        
        // tp players to hub
        tpGroup(hunters, hubSpawn);
        tpGroup(hunteds, hubSpawn);
    }

    public void close() {
        announce(ChatColor.RED + "Game lobby closed " + ChatColor.GREEN + this);
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
        System.out.println("found highest y of " + y + " at (" + x + ", " + y + ")");

        return new Location(world, x, y+2, z);
    }

    private void checkReady() {
    	if (!needsHunted() && !needsHunter()) {
	        waitingToStart = true;
	    }
    }
    

    // INFO
    // 

    public boolean needsHunted() {
        return hunteds.size() < NUM_HUNTED;
    }
    
    public boolean needsHunter() {
        return hunters.size() < NUM_HUNTER;
    }

    // PLAYER ACTIONS
    
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

    public boolean remove(HvHPlayer hplayer) {
        return remove(hplayer.player);
    }
    public boolean remove(Player player) {
        HvHPlayer hplayer = getHvHPlayer(player);
        if (hplayer != null) {
            switch(hplayer.team) {
                case "hunter":
                    return hunters.remove(hplayer);
                case "hunted":
                    return hunteds.remove(hplayer);
            }
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

    public boolean swapTeams(HvHPlayer hplayer) {
        switch(hplayer.team) {
            case "hunter":
                if (needsHunted() && remove(hplayer)) {
                    hplayer.joinTeam("hunted");
                    hunteds.add(hplayer);
                    return true;
                }
                break;
            case "hunted":
                if (needsHunter() && remove(hplayer)) {
                    hplayer.joinTeam("hunter");
                    hunters.add(hplayer);
                    return true;
                }
                break; 
        }
        return false;
    }

    // respawn hunters
    public void respawn(HvHPlayer hplayer) {
        if (playing && hplayer.team.equals("hunter")) {
            hplayer.teleport(gameSpawn);
        }
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

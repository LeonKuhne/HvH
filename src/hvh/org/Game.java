/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hvh.org;

import java.util.ArrayList;
import java.util.List;
import org.bukkit.entity.Player;

/**
 * @author Leee Leee
 */
class Game {
    
    public final static int NUM_HUNTED = 1;
    public final static int NUM_HUNTER = 1;
    
    public boolean inLobby;
    public boolean waitingToStart;
    
    private List<HvHPlayer> hunters;
    private List<HvHPlayer> hunteds;
    
    public Game() {
        inLobby = true;
        waitingToStart = false;
        
        hunters = new ArrayList();
        hunteds = new ArrayList();
    }
    
    public void start() {
        inLobby = false;
        waitingToStart = false;
        
        // TODO 
        // tp to hvh world
        // set spawn
	    announce("the game is starting");
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
	    tellGroup(hunters, message);	
	    tellGroup(hunteds, message);	
    }

    public void tellGroup(List<HvHPlayer> group, String message) {
    	for (HvHPlayer hplayer : group) {
	        hplayer.player.sendMessage("[HvH Game] " + message);
	    }
    }
    
}

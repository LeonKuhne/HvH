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
    
    public boolean needsPlayer(Player player) {
        player.tryJoin(game)
    	switch(player.team) {
            case "hunter":
                return needsHunter();
            case "hunted":
                return needsHunted();
            default:
                return false;
        }
    }
    
    public boolean addPlayer(HvHPlayer player) {
        if (needsPlayer()) {
            hunters.add(player);
	        checkReady();
            return true;
        }
        return false;
    }


    public void announce(String message) {
	    tellGroup(hunters, message);	
	    tellGroup(hunteds, message);	
    }

    public void tellGroup(List<Player> group, String message) {
    	for (Player player : group) {
	        player.sendMessage("HvH: " + message);
	    }
    }
    
}

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
    public final static int NUM_HUNTER = 2;
    
    public boolean inLobby;
    public boolean waitingToStart;
    
    private List<Player> hunters;
    private List<Player> hunteds;
    
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
    
    private boolean needsHunted() {
        return hunteds.size() < NUM_HUNTED;
    }
    
    private boolean needsHunter() {
        return hunters.size() < NUM_HUNTER;
    }
    
    public boolean addHunted(Player player) {
	player.sendMessage("you are now the hunted");
        
	if (needsHunted()) {
            hunteds.add(player);
	    checkReady();
            return true;
        }
        return false;
    }
    
    public boolean addHunter(Player player) {
	player.sendMessage("you are now a hunter");

        if (needsHunter()) {
            hunters.add(player);
	    checkReady();
            return true;
        }
        return false;
    }

    public void announce(String string) {
    	for (Player player : players) {
	    player.sendMessage("HvH: " + string);
	}
    }
    
}

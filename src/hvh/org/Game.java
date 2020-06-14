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
    
    private boolean inLobby;
    private List<Player> hunters;
    private List<Player> hunteds;
    
    public Game() {
        inLobby = true;
        
        hunters = new ArrayList();
        hunteds = new ArrayList();
    }
    
    // call this function repeatedly
    public void checkPlayers() {
        if (inLobby) {
            start();
        }
    }
    
    public void start() {
        inLobby = false;
        
        // TODO 
        // tp to hvh world
        // set spawn
    }
    
    private boolean needsHunted() {
        return hunteds.size() < NUM_HUNTED;
    }
    
    private boolean needsHunter() {
        return hunters.size() < NUM_HUNTER;
    }
    
    public boolean addHunted(Player player) {
        if (needsHunted()) {
            hunteds.add(player);
            return true;
        }
        return false;
    }
    
    public boolean addHunter(Player player) {
        if (needsHunter()) {
            hunters.add(player);
            return true;
        }
        return false;
    }
    
}

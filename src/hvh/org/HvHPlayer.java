package hvh.org;

import java.util.List;
import org.bukkit.entity.Player;
import org.bukkit.Location;
import org.bukkit.ChatColor;

public class HvHPlayer {

    protected Game currGame;
    private Hub hub;
    public Player player;
    public String team;

    public HvHPlayer(Player player, Hub hub) {
        this.player = player;
        this.hub = hub;
        this.team = "";
        
        loadFromGame();
    }

    public boolean inGame() {
        return currGame != null;
    }

    public boolean isPlaying() {
        return inGame() && currGame.playing;
    }


    // Message Player
    //
    
    public void help(String message, ChatColor color) {
        String prompt = team != "" ? "[HvH " + team + "] " : "[HvH player] ";
        player.sendMessage(ChatColor.GOLD + prompt + color + message);
    }
    
    public void help(String message) {
        help(message, ChatColor.AQUA);
    }
    
    public void notify(String message) {
        help(message, ChatColor.GREEN);
    }

    // SETUP/HELPER ACTIONS
    //

    public void respawn() {
        if (inGame()) {
            currGame.respawn(this);
        }
    }

    public void teleport(Location loc) {
        player.teleport(loc);
    }

    public boolean joinTeam(String team) {
        if (team.equals("hunter") || team.equals("hunted")) {
            help("Joined team: " + ChatColor.GREEN + team);
            this.team = team;
            return true;
        } else {
            help("Team must be either hunter or hunted");
            return false;
        }
    }


    // GAME ACTIONS
    // 
    
    public void leaveGame() {
    	for (Game game : hub.games) {
	        if (game.remove(player)) {
                notify("You left the game");
                currGame = null;
                return;
	        }
	    }
        help("You're not in a game");
    }

    public void joinGame() {
        if (!inGame()) {
            Game game = findOrCreateGame();
            game.addPlayer(this);
            currGame = game;
            notify("You joined a game");
        } else {
            help("You're already in a game");
        }
    }

    public void startGame() {
        if (inGame()) {
            notify("forcing game to start");
            currGame.start();
        } else {
            help("you aren't in a game yet");
        }
    }

    public void endGame() {
        if (inGame()) {
            currGame.end();
        }
    }

    public void switchTo(String team) {
        if (this.team.equals(team)) {
            help("You're already on that team");
        } else {
            if (inGame() && currGame.inLobby && currGame.swapTeams(this)) {
                notify("You swapped teams");
            } else {
                help("The other team is full");
            }
        }
    }


    // UTIL
    //

    private void loadFromGame() {
        for (Game game : hub.games) {
            HvHPlayer hplayer = game.getHvHPlayer(player);
            if (hplayer != null) { 
                this.team = hplayer.team;
                this.currGame = game;
            }
        }
    }

    private Game findGame() {
        switch (team) {
            case "hunter":
                for (Game game : hub.games) {
                    if (game.needsHunter()) {
                        help("found game " + game);
                        return game;
                    }
                }
                break;
            case "hunted":
                for (Game game : hub.games) {
                    if (game.needsHunted()) {
                        help("found game " + game);
                        return game;
                    }
                }
                break;
        }

        return null;
    }

    private Game findOrCreateGame() {
        // find
        Game game = findGame();
        if (game != null) {
            return game;
        }
   
	    // create
	    game = new Game(player.getWorld(), hub.getSpawn());
	    hub.games.add(game);
        help("Created new game " + ChatColor.GREEN + game);
        return game;
    }
}

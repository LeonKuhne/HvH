package hvh.org;

import java.util.List;
import org.bukkit.entity.Player;
import org.bukkit.Location;

public class HvHPlayer {

    private List<Game> games;
    protected Game currGame;
    public Player player;
    public String team;

    public HvHPlayer(Player player, List<Game> games) {
        this.games = games;

        currGame = findGame();
        if (currGame == null) {
            this.player = player;
        } else {
            this.team = game.getTeam(player);
        }
    }

    public boolean inGame() {
        return currGame == null;
    }

    public void msg(String message) {
        player.sendMessage("[HvH] " + message);
    }

    public void teleport(Location loc) {
        player.teleport(loc);
    }

    public void leaveGame() {
    	for (Game game : games) {
	        if (game.hasPlayer(player)) {
	        	game.remove(player);
	        }
	    }
        msg("you left the game");
    }

    public void joinTeam(String team) {
        this.team = team;
        msg("you are a " + team);
    }

    private void joinGame() {
       Game game = findOrCreateGame();
       game.addPlayer(this);
       currGame = game;
       msg("you joined the game as a " + team);
    }
    
    private Game findOrCreateGame() {
        // find
        for (Game game : games) {
            if (game.needsPlayer(this)) {
                msg("found game " + game);
                return game;
            }
        }
    
	    // create
	    game = new Game();
	    games.add(game);
        msg("created new game " + game);
        return game;
    }
}

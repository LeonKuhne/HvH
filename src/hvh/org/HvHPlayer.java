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
        this.team = "";

        currGame = findGame();
        if (currGame == null) {
            this.player = player;
        } else {
            this.team = currGame.getTeam(player);
        }
    }

    public boolean inGame() {
        return currGame == null;
    }

    public void help(String message) {
        player.sendMessage("[HvH] " + message);
    }

    public void teleport(Location loc) {
        player.teleport(loc);
    }

    public void leaveGame() {
    	for (Game game : games) {
	        if (game.remove(player)) {
                msg("you left the game");
                return;
	        }
	    }
        msg("you're not in a game");
    }

    public void joinTeam(String team) {
        this.team = team;
        msg("you are a " + team);
    }

    public void joinGame() {
       Game game = findOrCreateGame();
       game.addPlayer(this);
       currGame = game;
       msg("you joined the game as a " + team);
    }
    
    private Game findGame() {
        switch (team) {
            case "hunter":
                for (Game game : games) {
                    if (game.needsHunter()) {
                        msg("found game " + game);
                        return game;
                    }
                }
                break;
            case "hunted":
                for (Game game : games) {
                    if (game.needsHunted()) {
                        msg("found game " + game);
                        return game;
                    }
                }
                break;
            default:
                msg("you need to choose a team first");
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
	    game = new Game();
	    games.add(game);
        msg("created new game " + game);
        return game;
    }
}

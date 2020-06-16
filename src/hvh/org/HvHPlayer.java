package hvh.org;

import java.util.List;
import org.bukkit.entity.Player;
import org.bukkit.Location;
import org.bukkit.ChatColor;

public class HvHPlayer {

    private List<Game> games;
    protected Game currGame;
    public Player player;
    public String team;

    public HvHPlayer(Player player, List<Game> games) {
        this.games = games;
        this.team = "";
        this.player = player;

        currGame = findGame();
        if (currGame != null) {
            this.team = currGame.getTeam(player);
        }
    }

    public boolean inGame() {
        return currGame != null;
    }

    public void help(String message) {
        String prompt = team != null ? "[HvH " + team + "]" : "[HvH -----]";
        player.sendMessage(ChatColor.GOLD + prompt + ChatColor.AQUA + message);
    }

    public void teleport(Location loc) {
        player.teleport(loc);
    }

    public void leaveGame() {
    	for (Game game : games) {
	        if (game.remove(player)) {
                help("You left the game");
                return;
	        }
	    }
        help("You're not in a game");
    }

    public void joinTeam(String team) {
        this.team = team;
        help("On team: " + ChatColor.GREEN + team);
    }

    public void joinGame() {
       Game game = findOrCreateGame();
       game.addPlayer(this);
       currGame = game;
       help("You joined the game");
    }
    
    private Game findGame() {
        switch (team) {
            case "hunter":
                for (Game game : games) {
                    if (game.needsHunter()) {
                        help("found game " + game);
                        return game;
                    }
                }
                break;
            case "hunted":
                for (Game game : games) {
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
	    game = new Game();
	    games.add(game);
        help("Created new game " + ChatColor.GREEN + game);
        return game;
    }
}

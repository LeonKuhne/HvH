import org.bukkit.entity.Player;

public class HvHPlayer extends Player {

    private List<Game> games;
    protected currGame;
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

    public void msg(String message) {
        player.sendMessage("[HvH] " + message);
    }

    public void leaveGame() {
    	for (Game game : games) {
	        if (game.hasPlayer(player)) {
	        	game.remove(player);
	        }
	    }

        msg("you left the game");
    }

    private void joinGame() {
       Game game = findOrCreateGame();
       game.addPlayer(this);
       currGame = game;
       
       msg("you joined as a " + team);
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

package hvh.org;

import java.util.List;
import java.util.ArrayList;
import org.bukkit.plugin.Plugin;
import org.bukkit.Location;
import org.bukkit.entity.Player;


/**
 * @author Leee Leee
 * Assigns players to hunter/hunted classes and manages players and games
 */
public class Hub {
    
    List<Game> games;
    List<Player> admins;
    Location spawn;
    
    public Hub(Plugin plugin, Player player) {
	    games = new ArrayList();
	    admins = new ArrayList();
        setSpawn(player);
        
        plugin.getServer().getScheduler().runTaskTimer(plugin, () -> tryStartGames(), 50l, 100l);
    }

    private void tryStartGames() {
    	for (Game game : games) {
	        if (game.waitingToStart) {
	        	game.start();
	        }
    	}
    }

    /**
     * Print a help message to a user
     */
    private void help(Player player, String message) {
        player.sendMessage("$5[HvH Hub]$f " + message);
    }

    private void tell(String message) {
        for (Player admin : admins) {
            help(admin, message);
        }
    }

    public void parseAdminCommand(Player admin, List<String> args) {
        if (args.size() >= 1) {
            String cmd = args.remove(0);

            switch (cmd) {
                case "tp":
	                player.teleport(spawn);
		            help("teleported to hub");
                    return;
                case "setspawn":
                    setSpawn(player);
                    return;
                case "subscribe":
                    if (!subscribers.contains(player)) {
                        subscribers.add(player);
                        tell("spawn set");
                    }
                    return;
               default:
                    help("unknown command \"" + cmd + "\"" );
            }
        }

        help("admin commands: spawn/tp, setspawn, subscribe");
    }
    
    public void parseCommand(Player normie, List<String> args) {
        HvHPlayer player = new HvHPlayer(normie, games);
        String cmd = args.remove(0);
        
        switch (cmd) {
            case "join":
                // check if player already in game
                if (player.inGame()) {
                    player.sendMessage("sir. you are already in a game");
                } else if (args.size() > 0) {
                    // add player to a game
                    String team = args[0];                                      // team the player wants to be on
                    player.setTeam(team);                                       // set the players team
                    hplayer.joinGame();                                         // add player to game lobby
		            return true;
		        } else {
                    help("you must specify a team: /hvh join [hunter/hunted]");
		        }
		        return;
	        case "leave":
                player.leaveGame();
                return;
            case "admin":
                parseAdminCommand(player, args);
                return;
            default:
                help("unknown command " + cmd);
        }
        
		help("available commands: join, spawn, setspawn");
    }
    
    public void setSpawn(Player player) {
        spawn = player.getLocation();
        
    }
}

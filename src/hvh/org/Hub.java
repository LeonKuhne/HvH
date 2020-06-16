package hvh.org;

import java.util.List;
import java.util.ArrayList;
import org.bukkit.plugin.Plugin;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.ChatColor;

/**
 * @author Leee Leee
 * Assigns players to hunter/hunted classes and manages players and games
 */
public class Hub {
    
    List<Game> games;
    List<HvHPlayer> admins;
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
    private void help(HvHPlayer hplayer, String message) {
        help(hplayer.player, message);
    }
    private void help(Player player, String message) {
        player.sendMessage(ChatColor.GOLD + "[HvH Hub] " + ChatColor.RESET + message);
    }

    private void tell(String message) {
        for (HvHPlayer admin : admins) {
            help(admin, message);
        }
    }

    public void parseAdminCommand(HvHPlayer admin, List<String> args) {
        if (args.size() >= 1) {
            String cmd = args.remove(0);

            switch (cmd) {
                case "tp":
	                admin.teleport(spawn);
		            help(admin, "teleported to hub");
                    return;
                case "setspawn":
                    setSpawn(admin);
                    return;
                case "subscribe":
                    if (!admins.contains(admin)) {
                        admins.add(admin);
                        tell("spawn set");
                    }
                    return;
               default:
                    help(admin, "unknown command: " + cmd);
            }
        }

        help(admin, "admin commands: spawn/tp, setspawn, subscribe");
    }
    
    public void parseCommand(Player normie, List<String> args) {
        HvHPlayer player = new HvHPlayer(normie, games);
        
        if (args.size() >= 1) {
            String cmd = args.remove(0);
        
            switch (cmd) {
                case "join":
                    // check if player already in game
                    if (player.inGame()) {
                        help(player, "sir. you are already in a game");
                    } else if (args.size() > 0) {
                        // add player to a game
                        String team = args.get(0);                              // team the player wants to be on
                        player.joinTeam(team);                                  // set the players team
                        player.joinGame();                                      // add player to game lobby
		            } else {
                        help(player, "you must specify a team: /hvh join [hunter/hunted]");
	    	        }
		            return;
	            case "leave":
                    player.leaveGame();
                    return;
                case "admin":
                    parseAdminCommand(player, args);
                    return;
                default:
                    help(player, "unknown command " + cmd);
            }
    }
   
    String helpMsg = "user commands: join, leave"; 
    if (normie.isOp()) {
        helpMsg += ", admin";
    }
    help(player, helpMsg);
}
    
    public void setSpawn(HvHPlayer hplayer) {
        setSpawn(hplayer.player);
    }
    public void setSpawn(Player player) {
        spawn = player.getLocation();
    }
}

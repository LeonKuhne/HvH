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
    
    public List<Game> games;
    private List<Player> admins;
    private Location spawn;
    
    public Hub(Plugin plugin, Player player) {
	    games = new ArrayList();
	    admins = new ArrayList();
        
        admins.add(player);
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
        for (Player admin : admins) {
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
                    tell("spawn set");
                    return;
                case "subscribe":
                    if (!admins.contains(admin.player)) {
                        admins.add(admin.player);
                    }
                    tell(admin.player.getDisplayName() + " subscribed!");
                    return;
               default:
                    help(admin, "Unknown command: " + ChatColor.RED + cmd);
            }
        }

        help(admin, "admin commands: " + ChatColor.GREEN + "tp, setspawn, subscribe");
    }
    
    public void parseCommand(Player normie, List<String> args) {
        System.out.println(normie);
        HvHPlayer player = createGamer(normie);
        
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
                        help(player, "you must specify a team: " + ChatColor.GREEN + "/hvh join [hunter/hunted]");
	    	        }
		            return;
	            case "leave":
                    player.leaveGame();
                    return;
                case "admin":
                    parseAdminCommand(player, args);
                    return;
                default:
                    help(player, "Unknown command: " + ChatColor.RED + cmd);
            }
        }

        String helpMsg = "User commands: " + ChatColor.GREEN +  "join, leave"; 
        if (normie.isOp()) {
            helpMsg += ", admin";
        }
        help(player, helpMsg);
    }

    public HvHPlayer createGamer(Player player) {
        return new HvHPlayer(player, this);
    }
    
    public void setSpawn(HvHPlayer hplayer) {
        setSpawn(hplayer.player);
    }
    public void setSpawn(Player player) {
        spawn = player.getLocation();
    }

    public Location getSpawn() {
        return spawn.clone();
    }
}

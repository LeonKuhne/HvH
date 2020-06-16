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
        
        plugin.getServer().getScheduler().runTaskTimer(plugin, () -> tryUpdateGames(), 50l, 100l);
    }

    private void tryUpdateGames() {
    	for (Game game : games) {
            // end done games
            if (game.done) {
                game.close();
                games.remove(game);
            }

            // start ready games
            else if (game.waitingToStart) {
	        	game.start();
            }
        }
    }

    /**
     * Print a help message to a user
     */
    private void failed(HvHPlayer hplayer, String message) {
        failed(hplayer.player, message);
    }
    private void failed(Player player, String message) {
        message += ChatColor.RED + "Failed: " + message;
        help(player, message);
    }
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
                case "start":
                    admin.startGame();
                    return;
                default:
                    help(admin, "Unknown command: " + ChatColor.RED + cmd);
            }
        }

        help(admin, "admin commands: " + ChatColor.GREEN + "tp, setspawn, subscribe, start");
    }
    
    public void parseCommand(Player normie, List<String> args) {
        HvHPlayer player = new HvHPlayer(normie, this);
        
        if (args.size() >= 1) {
            String cmd = args.remove(0);
        
            switch (cmd) {
                case "join":
                    if (args.size() > 0) {
                        String team = args.get(0);

                        if (player.inGame()) {
                            // switch teams
                            player.switchTo(team);

                        } else {
                            // add player to a game
                            if (player.joinTeam(team)) {
                                player.joinGame();
                            }
                        }
		            } else {
                        help(player, "You must specify a team: " + ChatColor.GREEN + "/hvh join [hunter/hunted]");
	    	        }
		            return;
	            case "leave":
                    player.leaveGame();
                    return;
                case "admin":
                    parseAdminCommand(player, args);
                    return;
                case "info":
                    help(player, "Your info: " + player);
                    return;
                case "help":
                    help(player, "Type a command to see its arguments");
                    break;
                default:
                    help(player, "Unknown command: " + ChatColor.RED + cmd);
            }
        }

        String helpMsg = "User commands: " + ChatColor.GREEN +  "join, leave, info, help"; 
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

    public Location getSpawn() {
        return spawn.clone();
    }

    public String toString() {
        return "Games: " + games.size();
    }
}

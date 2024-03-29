package hvh.org;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.Location;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerRespawnEvent;

/**
 * @author Leee Leee
 */
public class GameEndEvent implements Listener {
    
    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        Player player = event.getEntity();
        HvHPlayer hplayer = new HvHPlayer(player, Main.hub);
        
        // if player is hunter, end game, otherwise we don't care
        if (hplayer.isPlaying()) {
            if (hplayer.team.equals("hunted")) {
                //player.spigot().respawn();
                hplayer.endGame();
            }
        }
    }

    @EventHandler
    public void onPlayerRespawn(PlayerRespawnEvent event) {
        Player player = event.getPlayer();
        HvHPlayer hplayer = new HvHPlayer(player, Main.hub);
        
        if (hplayer.isPlaying()) {
            if (hplayer.team.equals("hunter")) {
                hplayer.help("It's not too late, keep chasing!");
                Location loc = hplayer.getGame().getSpawn();
                if (loc != null) {
                    event.setRespawnLocation(loc);
                } else {
                    hplayer.help("Game not active, can't respawn you");
                }
            }
        }   
    }
}

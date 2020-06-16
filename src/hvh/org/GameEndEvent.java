package hvh.org;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerRespawnEvent;

/**
 * @author Leee Leee
 */
public class GameEndEvent implements Listener {
    
    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        System.out.println("player died");
        Player player = event.getEntity();
        HvHPlayer hplayer = new HvHPlayer(player, Main.hub);
        
        // if player is hunter, end game, otherwise we don't care
        if (hplayer.isPlaying()) {
            if (hplayer.team == "hunted") {
                player.spigot().respawn();
                hplayer.endGame();
            }
        }
    }

    @EventHandler
    public void onPlayerRespawn(PlayerRespawnEvent event) {
        System.out.println("player respawned");
        Player player = event.getPlayer();
        HvHPlayer hplayer = Main.hub.createGamer(player);
        
        // if player is hunter, say hi
        if (hplayer.isPlaying()) {
            if (hplayer.team == "hunter") {
                hplayer.help("It's not too late, keep chasing!");
            }
        }   
    }
}

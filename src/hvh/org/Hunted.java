package hvh.org;

import java.util.List;
import org.bukkit.entity.Player;

public class Hunted extends HvHPlayer {
    private List<Game> games;
    public Hunted(Player player, List<Game> games) {
        super(player, games);
    }
}

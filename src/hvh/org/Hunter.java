package hvh.org;

import java.util.List;
import org.bukkit.entity.Player;

public class Hunter extends HvHPlayer {
    private List<Game> games;
    public Hunter(Player player, List<Game> games) {
        super(player, games);
    }
}


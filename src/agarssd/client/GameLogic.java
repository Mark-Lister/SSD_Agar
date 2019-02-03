package agarssd.client;

import agarssd.model.MoveCommand;
import agarssd.model.Player;
import agarssd.model.World;

import java.util.Random;

public interface GameLogic {
    public MoveCommand getNextMoveCommand(World world, Player myPlayer);

}

package agarssd.client;

import agarssd.model.MoveCommand;
import agarssd.model.Player;
import agarssd.model.World;

import java.awt.*;

public class FollowMouseStrategy implements GameLogic {
    public MoveCommand getNextMoveCommand(World world, Player myPlayer) {
        if (world == null) {
            return null;
        }
        MoveCommand command = new MoveCommand();
        Point mouse = MouseInfo.getPointerInfo().getLocation();
        //Some random offsets to make it centered
        command.toX = mouse.x - 5;
        command.toY = mouse.y - 25;
        return command;
    }
}

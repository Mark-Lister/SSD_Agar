package agarssd.client;

import agarssd.model.Item;
import agarssd.model.MoveCommand;
import agarssd.model.Player;
import agarssd.model.World;
import static java.lang.Math.abs;

public class GreedyStrategy implements GameLogic{
    public MoveCommand getNextMoveCommand(World world, Player myPlayer) {
        if(world == null) {
            return null;
        }
        MoveCommand command = new MoveCommand();
        //System.out.println("X: " + myPlayer.positionX + "Y: " + myPlayer.positionY);
        Item item = world.items.get(0);
        float newDist;
        float distance = abs(item.positionX - myPlayer.positionX) + abs(item.positionY - myPlayer.positionY);
        float x = item.positionX;
        float y = item.positionY;
        for(int i = 1; i < world.items.size(); i++) {
            item = world.items.get(i);
            newDist = abs(item.positionX - myPlayer.positionX) + abs(item.positionY - myPlayer.positionY);
            if(newDist < distance){
                distance = newDist;
                x = item.positionX;
                y = item.positionY;
            }
        }
        command.toX = x;
        command.toY = y;
        return command;
    }
}

package agarssd.client;

import agarssd.model.Item;
import agarssd.model.MoveCommand;
import agarssd.model.Player;
import agarssd.model.World;
import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import java.util.Observable;

import java.io.IOException;

public class GameClient extends Observable {
    public static GameClient instance;
    public static boolean started = false;

    // public static final String ADDRESS = "127.0.0.1"; // For testing locally
     public static final String ADDRESS = "206.189.34.126"; // For testing on online server
    // public static final String ADDRESS = "192.168.xxx.xxx"; // For testing on lan network

    // Please do not modify these variables.
    public static final int PORT = 54555;
    public static final int TIMEOUT = 5000;
    public static final int LOGIC_DELAY = 500;

    private Client kryoClient;

    private Gui gui = new Gui();

    private World world;
    private Player myPlayer;
    private GameLogic logic;
    private boolean running;
    private GameLogic strategy;

    public static synchronized GameClient getInstance(){
        if(instance == null){
            instance = new GameClient();
            instance.addObserver(instance.gui);

        }
        return instance;
    }

    public void start() {
        if(!started) {

            started = true;
            initNetwork();
            initLogic();
            gui.setVisible(true);

        }
    }

    public void setStrategy(GameLogic strategy){
        this.strategy = strategy;

    }

    private void initLogic() {
        //logic = new GameLogic();
        running = true;
        Thread logicThread = new Thread() {
            @Override
            public void run() {
                super.run();
                while(running) {
                    refreshMyPlayer();
                    //MoveCommand command = logic.getNextMoveCommand(world, myPlayer);
                    MoveCommand command = strategy.getNextMoveCommand(world, myPlayer);
                    if (command != null) {
                        kryoClient.sendTCP(command);
                    }
                    try {
                        Thread.sleep(LOGIC_DELAY);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        };
        logicThread.start();
    }

    private void updateGui() {
        if(this.gui != null) {
           // gui.update(world);
            setChanged();
            notifyObservers(world);
            //System.out.println(world.items);
        }
    }

    private void initNetwork() {
        kryoClient = new Client();
        kryoClient.getKryo().register(World.class);
        kryoClient.getKryo().register(Player.class);
        kryoClient.getKryo().register(Item.class);
        kryoClient.getKryo().register(MoveCommand.class);
        kryoClient.getKryo().register(java.util.List.class);
        kryoClient.getKryo().register(java.util.ArrayList.class);
        kryoClient.start();
        kryoClient.addListener(new Listener() {
            public void received (Connection connection, Object object) {
                if (object instanceof Player) {
                    myPlayer = (Player) object;
                    gui.registerMyPlayer(myPlayer);
                } else if (object instanceof World) {
                    world = (World) object;
                    refreshMyPlayer();
                    updateGui();
                }
            }
        });
        try {
            kryoClient.connect( TIMEOUT, ADDRESS, PORT);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void refreshMyPlayer() {
        if(world == null) {
            return;
        }
        for(Player p : world.players) {
            if(p.id == myPlayer.id) {
                myPlayer = p;
            }
        }
    }
}

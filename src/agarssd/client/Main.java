package agarssd.client;


public class Main {
    public static void main(String[] args) {
        GameClient gameClient = GameClient.getInstance();
        gameClient.setStrategy(new GreedyStrategy());
        //gameClient.addObserver(new Gui());
        gameClient.start();
    }
}


public class Scheduler {
    private Server server;

    public Scheduler(int i){
        server = new Server(i);
        Thread th = new Thread(server);
        th.start();
    }

    public Server getServer() {
        return server;
    }

    public void setServer(Server server) {
        this.server = server;
    }

    public void dispatchTaskOnServer(Task t){
        //call the addTask method
        server.addTask(t);
    }
}
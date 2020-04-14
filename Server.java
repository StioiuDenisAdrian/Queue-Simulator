import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

public class Server implements Runnable {

    private BlockingQueue<Task> tasks;
    private AtomicInteger waitingPeriod;
    int queue;

    public Server(int i) {
        //initialize queue and waitingPeriod
        queue = i;
        tasks = new LinkedBlockingQueue<Task>();
        waitingPeriod = new AtomicInteger(0);
    }

    public void run() {
        while (true) {
            try {
                //take next task from queue
                //stop the thread for a time equal with the task's processing time
                //decrement the waitingPeriod
                Thread.sleep(1000);
                Task t = tasks.take();
                tasks.add(t);
                if ((tasks.peek().getProcessTime()) == 1) {
                    tasks.poll();
                } else {
                    tasks.peek().setProcessTime(tasks.peek().getProcessTime() - 1);
                }
                waitingPeriod.addAndGet((-1));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public boolean empty() {
        return tasks.isEmpty();
    }

    public void print() {
        if (tasks.isEmpty()) {
            System.out.println("Queue " + queue + ": closed");
        } else {
            if ((tasks.peek().getProcessTime() - 1) > 1) {
                System.out.println("Queue " + queue + ": " + tasks.peek().toString());
            } else {
                System.out.println("Queue " + queue + ": " + print(tasks));
            }
        }
    }


    public String print(BlockingQueue<Task> t) {
        String s = "";
        for (Task i : t) {
            s += i.toString() + " ";
        }
        return s;
    }

    public void addTask(Task t) {
        //add task to queue
        //increment the waitingPeriod
        tasks.add(t);
        waitingPeriod.addAndGet(t.getProcessTime());
    }

    public Task[] getTasks() {
        Task[] t = new Task[tasks.size()];
        tasks.toArray(t);
        return t;
    }

    public int totalTime() {
        return tasks.size();
    }
}

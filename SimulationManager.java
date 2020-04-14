
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

public class SimulationManager implements Runnable {
    //data read from file
    public static int timeLimit; //maximum processing time
    public static int maxProcessingTime;
    public static int minProcessingTime;
    public static int maxArrivalTime;
    public static int minArrivalTime;
    public static int numberOfServers;
    public static int numberOfClients;
    //entity responsible with queue management and client distribution
    private Scheduler[] scheduler = new Scheduler[numberOfServers];
    //pool of tasks(clients shopping in the store
    private List<Task> generatedTasks = new ArrayList<>();

    public SimulationManager() {
        //initialize the scheduler
        // =>create and start numberOfServers threads
        //generate numberOfClient clients using generateNRandomTasks()
        //store them to generatedTasks

        for (int i = 0; i < numberOfServers; i++) {
            scheduler[i] = new Scheduler(i);
        }
        generateNRandomTasks();
    }

    private void generateNRandomTasks() {
        //generate N random tasks
        //-random processing time
        //minProcessingTime<processingTime<maxProcessingTime
        //-random arrivalTime
        //sort list with respect to arrivalTime
        int processingTime;
        int arrivalTime;
        for (int i = 0; i < numberOfClients; i++) {
            Task t = new Task();
            t.setId(i + 1);
            processingTime = minProcessingTime + (int) (Math.random() * (maxProcessingTime - minProcessingTime) + 1);
            t.setProcessTime(processingTime);
            arrivalTime = minArrivalTime + (int) (Math.random() * (maxArrivalTime - minArrivalTime) + 1);
            t.setArrivalTime(arrivalTime);
            generatedTasks.add(t);
        }
        Collections.sort(generatedTasks);
    }

    private String toString(List<Task> t) {
        String s = "Waiting clients: ";
        for (Task i : t)
            s += i.toString() + " ";
        return s;
    }

    @Override
    public void run() {
        int currentTime = 0;
        double averageWaiting = 0;
        List<Task> aux = new ArrayList<>();
        while (currentTime < timeLimit) {
            //iterate generated tasks list and pick tasks that have the
            //arrivalTime equal with the currentTime
            // -send tasks to queue by calling the dispatchTask method
            //from Scheduler
            // delete client from list

            System.out.println("Time: " + currentTime);

            for (Task i : generatedTasks) {
                i.setWaitingTime(i.getWaitingTime() + 1);
                if (i.getArrivalTime() == currentTime) {
                    scheduler[getMin()].dispatchTaskOnServer(i);
                    averageWaiting += i.getWaitingTime();
                    aux.add(i);
                }
            }
            generatedTasks.removeAll(aux);
            System.out.println(toString(generatedTasks));
            for (Scheduler x : scheduler) {
                x.getServer().print();
            }
            if (generatedTasks.isEmpty()) {
                int x = 0;
                for (Scheduler s : scheduler) {
                    if (s.getServer().empty()) {
                        x++;
                    }
                }
                if (x == numberOfServers) {
                    averageWaiting /= numberOfClients;
                    System.out.println("Average waiting time: \n" + averageWaiting);
                    break;
                }
            }
            if(currentTime+1==timeLimit){
                averageWaiting /= numberOfClients;
                System.out.println("Average waiting time: \n" + averageWaiting);
            }
            currentTime++;
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            //wait an interval of 1 second
        }
    }

    public int getMin() {
        int min = 0;
        for (int i = 0; i < numberOfServers - 1; i++) {
            if (scheduler[i].getServer().totalTime() > scheduler[i + 1].getServer().totalTime()) {
                min = i + 1;
            }
        }
        return min;
    }


    public static void main(String[] args) throws Exception {
        String s1 = "", s2 = "";
        String[] arrTime;
        String[] procTime;
        try {
            Scanner sc = new Scanner(new File(args[0]));
            while (sc.hasNextLine()) {
                numberOfClients = Integer.parseInt(sc.nextLine());
                numberOfServers = Integer.parseInt(sc.nextLine());
                timeLimit = Integer.parseInt(sc.nextLine());
                s1 = sc.nextLine();
                s2 = sc.nextLine();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        arrTime = s1.split(",");
        procTime = s2.split(",");
        minArrivalTime = Integer.parseInt(arrTime[0]);
        maxArrivalTime = Integer.parseInt(arrTime[1]);
        minProcessingTime = Integer.parseInt(procTime[0]);
        maxProcessingTime = Integer.parseInt(procTime[1]);

        PrintStream fileStream = new PrintStream(args[1]);
        System.setOut(fileStream);

        SimulationManager gen = new SimulationManager();
        Runnable target;
        Thread t = new Thread(gen);
        t.start();

    }

}
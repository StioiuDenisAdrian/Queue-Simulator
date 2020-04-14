
public class Task implements Comparable<Task> {
    private int arrivalTime;
    private int processTime;
    private int id;
    private int waitingTime=0;

    public int getWaitingTime() {
        return waitingTime;
    }

    public void setWaitingTime(int waitingTime) {
        this.waitingTime = waitingTime;
    }

    public Task(int id, int arrivalTime, int processTime) {
        this.id = id;
        this.arrivalTime = arrivalTime;
        this.processTime = processTime;
    }
    public Task(){}
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getArrivalTime() {
        return arrivalTime;
    }

    public void setArrivalTime(int arrivalTime) {
        this.arrivalTime = arrivalTime;
    }

    public int getProcessTime() {
        return processTime;
    }

    public void setProcessTime(int processTime) {
        this.processTime = processTime;
    }

    public String toString() {
        return " (" +id+" , "+ arrivalTime + " , " + processTime + ") ";
    }

    @Override
    public int compareTo(Task o) {
        return this.arrivalTime-o.arrivalTime;
    }
}
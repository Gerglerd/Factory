package storage;
import java.util.ArrayDeque;
import java.util.Queue;

public class Storage<T> {
    private Queue<T> queue;
    private int queue_max;

    public Storage(int queue_max){
        queue = new ArrayDeque<>();
        this.queue_max = queue_max;
    }

    public synchronized void storageAdd(T item) throws InterruptedException {
        while (queue.size() >= queue_max)
            wait();
        queue.add(item);
        notifyAll();
    }

    public synchronized T storageGet() throws InterruptedException {
        while (queue.isEmpty())
            wait();
        T obj = queue.poll(); //return with delete from begin queue
        notifyAll();
        return obj;
    }

    public int storageSize(){
        return queue.size();
    }
}

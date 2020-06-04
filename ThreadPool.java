package Thread;

import details.Accessories;
import details.Carcass;
import details.Engine;
import details.Request;
import storage.CarStorage;
import storage.Storage;

import java.util.ArrayDeque;
import java.util.Queue;

public class ThreadPool {
    Thread threads[];
    Queue<Request> queue;
    Storage<Accessories> accessoriesStorage;
    Storage<Engine> engineStorage;
    Storage<Carcass> carcassStorage;
    CarStorage carStorage;
    int carId;

    public ThreadPool(int storage) {
        queue = new ArrayDeque<>();
        threads = new Thread[storage];
        for (int i = 0; i < storage; i++){
            threads[i] = new Thread(new Worker(this));
            threads[i].start();
        }
    }

    public synchronized void addRequest(Request request){
        queue.add(request);
        notifyAll();
    }

    public synchronized Request getRequest() throws InterruptedException {
        while (queue.isEmpty())
            wait();
        Request object = queue.poll();
        return object;
    }

    public synchronized int getId(){
        return carId;
    }

    public void terminate() {
        for (int i = 0; i < threads.length; i++)
            threads[i].interrupt();
    }
}

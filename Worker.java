package Thread;

import details.*;

public class Worker implements Runnable {
    private ThreadPool threadPool;

    Worker(ThreadPool threadPool){
        this.threadPool = threadPool;
    }

    @Override
    public void run() {
        while (!Thread.interrupted()){
            try{
                Request request = threadPool.getRequest();
                Accessories accessories = threadPool.accessoriesStorage.storageGet();
                Carcass carcass = threadPool.carcassStorage.storageGet();
                Engine engine = threadPool.engineStorage.storageGet();
                Car car = new Car(threadPool.getId(), accessories,engine,carcass);
                threadPool.carStorage.storageAdd(car);
            } catch (InterruptedException e) {
                break;
            }
        }
    }
}

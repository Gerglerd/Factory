package storage;
import Window.Window;
import details.Car;

public class CarStorage extends Storage<Car>{

    public CarStorage(int queue_max){
        super(queue_max);
    }

    public Car getStorage() throws InterruptedException {
        synchronized (Window.controller){
            Window.controller.requestActive++;
            Window.controller.notify();
        }
        return super.storageGet();
    }
}

package storage;

import Window.Window;
import details.Car;

import java.util.Date;

public class Trader {
    private Thread thread;
    private int delay;
    public Trader(int time, int traderId, CarStorage carStorage){
        delay = time;
        thread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (!Thread.interrupted()){
                    try{
                        Car car = carStorage.getStorage();
                        if (Window.logSale){
                            System.out.printf("%s: Trader #%d: Car #%d (Accessories #%d, Carcass #%d, Engine #%d)\n" +
                                    new Date().toString(), traderId, car.getDetailId(), car.getAccessories().getDetailId(),
                                    car.getCarcass().getDetailId(), car.getEngine().getDetailId());
                        }
                        Thread.sleep(delay);
                    } catch (InterruptedException e) {
                        break;
                    }
                }
            }
        });
        thread.start();
    }

    public void terminate(){
        thread.interrupt();
    }

    public void setDelay(int time){
        delay = time;
    }
}

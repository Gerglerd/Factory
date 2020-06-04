package delivery;

import details.Accessories;
import storage.Storage;

public class AccessoriesDelivery {
    private Thread thread;
    private int delay;
    private int count;
    public AccessoriesDelivery(Storage<Accessories> storage, int time, int id){
        delay = time;
        thread = new Thread(new Runnable() {
            @Override
            public void run() {
                int current_id = id;
                while (!Thread.interrupted()){
                    try {
                        Accessories thing = new Accessories(current_id);
                        storage.storageAdd(thing);
                        current_id++;
                        count++;
                        Thread.sleep(delay);
                    }catch(InterruptedException e) {
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

    public int getCountDelivery(){
        return count;
    }

    public void setDelay(int time){
        delay = time;
    }
}

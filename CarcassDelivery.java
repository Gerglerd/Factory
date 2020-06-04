package delivery;

import details.Carcass;
import storage.Storage;
public class CarcassDelivery {
    private Thread thread;
    private int count;
    private int delay;
    public CarcassDelivery(Storage<Carcass> storage, int time){
        delay = time;
        thread = new Thread(new Runnable() {
            @Override
            public void run() {
                int current_id = 1;
                while (!Thread.interrupted()){
                    try {
                        Carcass thing = new Carcass(current_id);
                        storage.storageAdd(thing);
                        current_id++;
                        count++;
                        Thread.sleep(delay);
                    }catch (InterruptedException e){
                        break;
                    }
                }
            }
        });
        thread.start();
    }

    public void terminate(){thread.interrupt();}

    public int getCount(){return count;}

    public void setDelay(int time){delay = time;}
}

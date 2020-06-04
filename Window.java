package Window;

import delivery.AccessoriesDelivery;
import delivery.CarcassDelivery;
import delivery.EngineDelivery;
import details.Accessories;
import details.Car;
import details.Carcass;
import details.Engine;
import storage.CarStorage;
import storage.*;
import Thread.ThreadPool;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.WindowEvent;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.Hashtable;

public class Window {
    private JFrame frame;
    private JLabel label;
    private Thread thread;

    public static Kit kit = new Kit("kit.txt");
    public static Storage<Accessories> accessoriesStorage = new Storage<Accessories>(kit.getStatus("StorageAccessoriesSize"));
    public static Storage<Engine> engineStorage = new Storage<>(kit.getStatus("StorageEngineSize"));
    public static Storage<Carcass> carcassStorage = new Storage<>(kit.getStatus("StorageCarcassSize"));
    public static CarStorage carStorage = new CarStorage(kit.getStatus("StorageCarSize"));
    public static ThreadPool threadPool = new ThreadPool(kit.getStatus("Workers"));
    public static Controller controller = new Controller();

    public static boolean logSale = (kit.getStatus("LogSale") == 1);
    private static AccessoriesDelivery accessoriesDelivery[];
    private static EngineDelivery engineDelivery;
    private static CarcassDelivery carcassDelivery;
    private static Trader trader[];

    public static void main(String[] args){
        int accessoriesDeliveryCount = kit.getStatus("AccessoriesDelivery");
        accessoriesDelivery = new AccessoriesDelivery[accessoriesDeliveryCount];
        for (int i = 0; i < accessoriesDeliveryCount; i++)
            accessoriesDelivery[i] = new AccessoriesDelivery(accessoriesStorage, 5000, i*15000);
        carcassDelivery = new CarcassDelivery(carcassStorage, 2500);
        engineDelivery = new EngineDelivery(engineStorage, 1250);
        int traderCount = kit.getStatus("Trader");
        trader = new Trader[traderCount];
        for (int i = 0; i < traderCount; i++)
            trader[i] = new Trader(3000, i, carStorage);
        Window window = new Window();
        window.frame.setVisible(true);
    }

    private void update(){
        int accessoriesProduced = 0;
        for (int i = 0; i < accessoriesDelivery.length; i++)
            accessoriesProduced += accessoriesDelivery[i].getCountDelivery();
        String message = String.format("<html>Accessories: Details in storage: %d, Total: %d<br>Carcass: Details in storage: %d, Total: %d<br>Engine: Details in storage: %d, Total: %d</html>",
                accessoriesStorage.storageSize(), accessoriesProduced,
                carcassStorage.storageSize(),carcassDelivery.getCount(),
                engineStorage.storageSize(),engineDelivery.getCount());
        label.setText(message);
        label.revalidate();
    }

    private void initialize(){
        Hashtable<Integer, JLabel> hashtable = new Hashtable<>();
        Hashtable<Integer, JLabel> hashtable1 = new Hashtable<>();
        Hashtable<Integer, JLabel> hashtable2 = new Hashtable<>();
        Hashtable<Integer, JLabel> hashtable3 = new Hashtable<>();
        frame = new JFrame();
        frame.setBounds(100,100,400,260);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JPanel jPanel = new JPanel();
        frame.getContentPane().add(jPanel, BorderLayout.NORTH);
        jPanel.setLayout(new GridLayout(0,1,0,0));
        JSlider slider = new JSlider(JSlider.HORIZONTAL, 100, 15000, 2000);
        hashtable.put( 100 , new JLabel("   accessories") );
        slider.setLabelTable(hashtable);
        slider.setPaintLabels(true);
        jPanel.add(slider);
        slider.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                int value = slider.getValue();
                for (int i = 0; i < accessoriesDelivery.length; i++)
                    accessoriesDelivery[i].setDelay(value);
            }
        });
        JSlider jSlider = new JSlider(JSlider.HORIZONTAL, 100,15000,1000);
        hashtable1.put( 100 , new JLabel("   carcass") );
        jSlider.setLabelTable(hashtable1);
        jSlider.setPaintLabels(true);
        jPanel.add(jSlider);
        jSlider.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                carcassDelivery.setDelay(jSlider.getValue());
            }
        });
        JSlider jSlider1 = new JSlider(JSlider.HORIZONTAL, 100, 15000,500);
        hashtable2.put( 100 , new JLabel("   engine") );
        jSlider1.setLabelTable(hashtable2);
        jSlider1.setPaintLabels(true);
        jPanel.add(jSlider1);
        jSlider1.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                engineDelivery.setDelay(jSlider1.getValue());
            }
        });
        JSlider jSlider2 = new JSlider(JSlider.HORIZONTAL, 100, 15000,500);
        hashtable3.put( 100 , new JLabel("   trader") );
        jSlider2.setLabelTable(hashtable3);
        jSlider2.setPaintLabels(true);
        jPanel.add(jSlider2);
        jSlider2.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                int value = jSlider2.getValue();
                for (int i = 0; i < trader.length; i++)
                    trader[i].setDelay(value);
            }
        });
        JPanel panel = new JPanel();
        frame.getContentPane().add(panel, BorderLayout.CENTER);
        panel.setLayout(new GridLayout(0, 1, 0, 0));
        label = new JLabel("54321");
        label.setHorizontalAlignment(SwingConstants.CENTER);
        panel.add(label);
        thread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (!Thread.interrupted()) {
                    try {
                        Thread.sleep(500);
                        EventQueue.invokeLater(new Runnable() {
                            @Override
                            public void run() {
                                Window.this.update();
                            }
                        });
                    } catch (InterruptedException e) {
                        break;
                    }
                }
            }
        });
        thread.start();
        frame.addWindowListener(new java.awt.event.WindowAdapter(){
            public void windowClose(WindowEvent windowEvent){
                thread.interrupt();
                threadPool.terminate();
                controller.terminate();
                for (int i = 0; i < accessoriesDelivery.length; i++)
                    accessoriesDelivery[i].terminate();
                carcassDelivery.terminate();
                engineDelivery.terminate();
                for (int i = 0; i < trader.length; i++)
                    trader[i].terminate();
                System.exit(0);
            }
        });
    }

    public Window(){
        initialize();
    }
}

package details;

public class Car extends Detail{
    private Accessories accessories;
    private Engine engine;
    private Carcass carcass;

    public Car(int current_id, Accessories accessories, Engine engine, Carcass carcass) {
        super(current_id);
        this.accessories = accessories;
        this.carcass = carcass;
        this.engine = engine;
    }

    public Accessories getAccessories(){
        return accessories;
    }

    public Carcass getCarcass(){
        return carcass;
    }

    public Engine getEngine(){
        return engine;
    }
}

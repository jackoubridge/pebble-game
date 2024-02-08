import java.util.List;

public class Bag {
    // Initialise variable
    public List<Integer> pebblesInBag;

    // Create constructor
    public Bag(List<Integer> pebblesInBag) {
        this.pebblesInBag = pebblesInBag;
    }

    // Setter and getter
    public List<Integer> getPebblesInBag() {return pebblesInBag;}

    public void setPebblesInBag(List<Integer> pebblesInBag) {
        this.pebblesInBag = pebblesInBag;
    }
}



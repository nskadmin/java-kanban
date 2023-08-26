import java.util.ArrayList;

public class Epic extends Task {
    public ArrayList<Integer> epicSubTasks = new ArrayList<>();

    public Epic(String title, String description) {
        super(title, description);
    }
}

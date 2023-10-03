package tasks;

import java.util.ArrayList;

public class Epic extends Task {
    private final ArrayList<Integer> epicSubTasks = new ArrayList<>();

    public Epic(String title, String description) {
        super(title, description);
    }

    public ArrayList<Integer> getEpicSubTasks() {
        return epicSubTasks;
    }
}

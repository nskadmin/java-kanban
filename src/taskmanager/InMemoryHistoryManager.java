package taskmanager;

import tasks.Task;

import java.util.ArrayList;

public final class InMemoryHistoryManager implements HistoryManager {
    private final static ArrayList<Task> history = new ArrayList<>();
    private static int historyPosition = 0;

    @Override
    public void add(Task task) {
        if (historyPosition < 10 && task != null) {
            history.add(task);
            historyPosition++;
        } else if (historyPosition == 10 && task != null) {
            history.remove(0);
            history.add(task);
        }
    }

    @Override
    public ArrayList<Task> getHistory() {
        return history;
    }
}

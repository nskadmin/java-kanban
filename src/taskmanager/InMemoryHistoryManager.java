package taskmanager;

import tasks.Task;

import java.util.ArrayList;
import java.util.LinkedList;

public final class InMemoryHistoryManager implements HistoryManager {
    private static final int MAX_TASKS_IN_HISTORY = 10;
    private final LinkedList<Task> history = new LinkedList<>();

    @Override
    public void add(Task task) {
        if (task != null) {
            if (history.size() == MAX_TASKS_IN_HISTORY) {
                history.removeFirst();
            }
            history.add(task);
        }
    }

    @Override
    public LinkedList<Task> getHistory() {
        return history;
    }
}

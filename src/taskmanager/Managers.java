package taskmanager;

public final class Managers extends InMemoryTaskManager {
    private Managers() {

        super(getDefaultHistory());
    }

    public static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }

    public static TaskManager getDefault() {
        return new InMemoryTaskManager(getDefaultHistory());
    }

}

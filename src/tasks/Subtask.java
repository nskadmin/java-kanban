package tasks;

public class Subtask extends Task {
    private int epicTitle;//tasks.Epic id за которым закреплен subtask

    public Subtask(String title, String description, int epicTitle) {
        super(title, description);
        this.epicTitle = epicTitle;
    }

    public int getEpicTitle() {
        return epicTitle;
    }
}

package tasks;

public class Subtask extends Task {
    public int getEpicTitle() {
        return epicTitle;
    }

    public void setEpicTitle(int epicTitle) {
        this.epicTitle = epicTitle;
    }

    private int epicTitle;//tasks.Epic id за которым закреплен subtask

    public Subtask(String title, String description) {
        super(title, description);
    }
}

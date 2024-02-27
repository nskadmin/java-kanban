package tasks;

public class Task {
    private String description;
    private int id;
    private TaskStatus status;
    private String title;

    public Task() {
    }

    public Task(String title, String description) {
        this.title = title;
        this.description = description;
        this.status = TaskStatus.NEW;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public TaskStatus getStatus() {
        return status;
    }

    public void setStatus(TaskStatus status) {
        this.status = status;
    }

    @Override
    public String toString() {
        String tasktype = "";
        String epicName = "";
        if (this instanceof Epic) tasktype = String.valueOf(TaskType.EPIC);
        else if (this instanceof Subtask) {
            tasktype = String.valueOf(TaskType.SUBTASK);
            epicName = String.valueOf(((Subtask) this).getEpicTitle());
        } else tasktype = String.valueOf(TaskType.TASK);
        return String.format("%d,%s,%s,%s,%s,%s", this.getId(), tasktype, this.getTitle(), this.getStatus(),
                this.getDescription(), epicName);
    }

}

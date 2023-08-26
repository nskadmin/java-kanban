import java.util.HashMap;

public class Manager {
    public HashMap<Integer, Task> taskCollection = new HashMap<>();
    public HashMap<Integer, Subtask> subtaskCollection = new HashMap<>();
    public HashMap<Integer, Epic> epicCollection = new HashMap<>();
    public int nextId = 0;

    public void addTask(Task task) {//добавление задач
        task.id = nextId++;
        taskCollection.put(task.id, task);
    }

    public void addTask(Subtask subtask) {
        subtask.id = nextId++;
        subtaskCollection.put(subtask.id, subtask);
    }

    public void addTask(Epic epic) {
        epic.id = nextId++;
        epicCollection.put(epic.id, epic);
        subtaskCollection.forEach((key, value) -> {
            if (value.epicTitle == -1) {
                epic.epicSubTasks.add(value.id);
                value.epicTitle = epic.id;
            }
        });
//        for (Integer epicSubTask : epic.epicSubTasks) {
//            Subtask subtask = subtaskCollection.get(epicSubTask);
//            subtask.epicTitle = epic.id;//указываем в рамках какого эпика подзадача
//        }
    }

    public void updateTask(Task task) {
        taskCollection.put(task.id, task);
    }

    public void updateTask(Subtask subtask) {
        subtaskCollection.put(subtask.id, subtask);
        Epic epic = epicCollection.get(subtask.epicTitle);
        updateEpicStatus(epic);
    }

    public void updateEpicStatus(Epic epic) {
        String status = "NEW";
        String prevStatus = "NEW";
        int count = 0;
        for (Integer epicSubTask : epic.epicSubTasks) {
            if (subtaskCollection.get(epicSubTask).status.equals("NEW")) {
                epic.status = "NEW";
                return;
            } else if (subtaskCollection.get(epicSubTask).status.equals("DONE") && count == 0) {
                status = "DONE";
            } else if (subtaskCollection.get(epicSubTask).status.equals("DONE") && count != 0 &&
                    prevStatus.equals("DONE")) {
                status = "DONE";
            } else {
                status = "IN_PROGRESS";
            }
            prevStatus = status;
            count++;
        }
        epic.status = status;

    }

    public void updateTask(Epic epic) {
        epicCollection.put(epic.id, epic);
        updateEpicStatus(epic);//обновляем статус epic
    }

    public void printTask() {
        taskCollection.forEach((key, value) -> {
            System.out.println("Id задачи: " + key + "; Имя задачи: " + value.title +
                    "; Описание задачи: " + value.description + "; Статус задачи: " + value.status);
        });
    }

    public void printSubTask() {
        subtaskCollection.forEach((key, value) -> {
            System.out.println("Id подзадачи: " + key + "; Имя подзадачи: " + value.title +
                    "; Описание подзадачи: " + value.description + "; Статус подзадачи: " + value.status
                    + "; подзадача большой задачи: " + epicCollection.get(value.epicTitle).title);
        });
    }

    public void printEpic() {
        epicCollection.forEach((key, value) -> {
            if (value.epicSubTasks.size() == 0) value.status = "NEW";
            System.out.println("Id epic задачи: " + key + "; Имя epic задачи: " + value.title +
                    "; Описание epic задачи: " + value.description + "; Статус epic задачи: " + value.status);
        });
    }

    public void deleteTask() {
        taskCollection.clear();
    }

    public void deleteSubTask() {
        subtaskCollection.clear();
    }

    public void deleteEpic() {
        epicCollection.clear();
    }

    public Task getTaskById(int id) {
        return taskCollection.get(id);
    }

    public Subtask getSubTaskById(int id) {
        return subtaskCollection.get(id);
    }

    public Epic getEpicById(int id) {
        return epicCollection.get(id);
    }

    public void deleteTaskById(Integer id) {
        taskCollection.remove(id);
    }

    public void deleteSubTaskById(Integer id) {
        epicCollection.forEach((key, value) -> {
            if (value.epicSubTasks.contains(id)) {
                value.epicSubTasks.remove(id);
                subtaskCollection.remove(id);
            }
        });

    }

    public void deleteEpicById(Integer id) {
        epicCollection.forEach((key, value) -> {
            if (key.equals(id)) {
                //сначала удаляем все подзадачи затем сам epic
                for (Integer epicSubTask : value.epicSubTasks) {
                    subtaskCollection.remove(epicSubTask);
                }
                epicCollection.remove(id);
                return;
            }
        });
    }

    public void printEpicSubTasks(Integer id) {
        epicCollection.forEach((key, value) -> {
            if (key.equals(id)) {
                for (Integer epicSubTask : value.epicSubTasks) {
                    System.out.println("Id подзадачи: " + subtaskCollection.get(epicSubTask).id
                            + "; Имя подзадачи: " + subtaskCollection.get(epicSubTask).title
                            + "; Описание подзадачи: " + subtaskCollection.get(epicSubTask).description
                            + "; Статус подзадачи: " + subtaskCollection.get(epicSubTask).status);
                }
                return;
            }
        });
    }
}

package taskmanager;

import tasks.Epic;
import tasks.Subtask;
import tasks.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Manager {
    private final HashMap<Integer, Task> taskCollection = new HashMap<>();
    private final HashMap<Integer, Subtask> subtaskCollection = new HashMap<>();
    private final HashMap<Integer, Epic> epicCollection = new HashMap<>();
    private int nextId = 0;

    public void addTask(Task task) {//добавление задач
        task.setId(nextId);
        taskCollection.put(task.getId(), task);
        nextId++;
    }

    public void addTask(Subtask subtask) {
        subtask.setId(nextId);
        subtaskCollection.put(subtask.getId(), subtask);
        List<Integer> lKeys = new ArrayList<Integer>(epicCollection.keySet());
        if (lKeys.size() > 0) {
            int keyId = lKeys.size() - 1;
            subtask.setEpicTitle(lKeys.get(keyId));
            Epic epic = epicCollection.get(lKeys.get(keyId));
            epic.epicSubTasks.add(subtask.getId());
            updateEpicStatus(epic);
            nextId++;
        } else {
            System.out.println("Нельзя создать подзадачу без epica");
        }
    }

    public void addTask(Epic epic) {
        epic.setId(nextId);
        epicCollection.put(epic.getId(), epic);
        nextId++;
    }

    public void updateTask(Task task) {
        if (taskCollection.containsKey(task.getId())) taskCollection.put(task.getId(), task);
    }

    public void updateTask(Subtask subtask) {
        subtaskCollection.put(subtask.getId(), subtask);
        Epic epic = epicCollection.get(subtask.getEpicTitle());
        updateEpicStatus(epic);
    }

    public void updateEpicStatus(Epic epic) {
        if (epic.epicSubTasks.size() == 0) return;
        String status = subtaskCollection.get(epic.epicSubTasks.get(0)).getStatus();
        String prevStatus = status;
        int count = 0;
        for (Integer epicSubTask : epic.epicSubTasks) {
            if (subtaskCollection.get(epicSubTask).getStatus().equals("IN_PROGRESS")) {
                epic.setStatus("IN_PROGRESS");
                return;
            } else if (subtaskCollection.get(epicSubTask).getStatus().equals("NEW") && prevStatus.equals("NEW")) {
                status = "NEW";
            } else if (subtaskCollection.get(epicSubTask).getStatus().equals("DONE") && prevStatus.equals("DONE")) {
                status = "DONE";
            } else {
                status = "IN_PROGRESS";
                return;
            }
            prevStatus = status;
            count++;
        }
        epic.setStatus(status);

    }

    public void updateTask(Epic epic) {
        epicCollection.put(epic.getId(), epic);
        //updateEpicStatus(epic);//обновляем статус epic
    }

    public void printTask() {
        taskCollection.forEach((key, value) -> {
            System.out.println("Id задачи: " + key + "; Имя задачи: " + value.getTitle() +
                    "; Описание задачи: " + value.getDescription() + "; Статус задачи: " + value.getStatus());
        });
    }

    public void printSubTask() {
        subtaskCollection.forEach((key, value) -> {
            System.out.println("Id подзадачи: " + key + "; Имя подзадачи: " + value.getTitle() +
                    "; Описание подзадачи: " + value.getDescription() + "; Статус подзадачи: " + value.getStatus()
                    + "; подзадача большой задачи: " + epicCollection.get(value.getEpicTitle()).getTitle());
        });
    }

    public void printEpic() {
        epicCollection.forEach((key, value) -> {
            if (value.epicSubTasks.size() == 0) value.setStatus("NEW");
            System.out.println("Id epic задачи: " + key + "; Имя epic задачи: " + value.getTitle() +
                    "; Описание epic задачи: " + value.getDescription() + "; Статус epic задачи: " + value.getStatus());
        });
    }

    public void deleteTask() {
        taskCollection.clear();
    }

    public void deleteSubTask() {
        subtaskCollection.clear();
        epicCollection.clear();
    }

    public void deleteEpic() {
        epicCollection.clear();
        subtaskCollection.clear();
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
        Epic epic = epicCollection.get(subtaskCollection.get(id).getEpicTitle());
        epic.epicSubTasks.remove(id);
        subtaskCollection.remove(id);
        updateEpicStatus(epic);
    }

    public void deleteEpicById(Integer id) {
        Epic epic = epicCollection.get(id);
        for (Integer epicSubTask : epic.epicSubTasks) {
            subtaskCollection.remove(epicSubTask);
        }
        epicCollection.remove(id);
    }

    public ArrayList<Task> getAllTasksList() {
        ArrayList<Task> task = new ArrayList<>();
        taskCollection.forEach((key, value) -> {
            task.add(value);
        });
        return task;
    }

    public ArrayList<Subtask> getSubTasksOfEpic(Epic epic) {
        ArrayList<Subtask> subtask = new ArrayList<>();
        for (Integer epicSubTask : epic.epicSubTasks) {
            subtask.add(subtaskCollection.get(epicSubTask));
        }
        return subtask;
    }

    public void printEpicSubTasks(Integer id) {
        epicCollection.forEach((key, value) -> {
            if (key.equals(id)) {
                for (Integer epicSubTask : value.epicSubTasks) {
                    System.out.println("Id подзадачи: " + subtaskCollection.get(epicSubTask).getId()
                            + "; Имя подзадачи: " + subtaskCollection.get(epicSubTask).getTitle()
                            + "; Описание подзадачи: " + subtaskCollection.get(epicSubTask).getDescription()
                            + "; Статус подзадачи: " + subtaskCollection.get(epicSubTask).getStatus());
                }
                return;
            }
        });
    }
}

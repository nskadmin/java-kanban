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
        subtaskCollection.put(subtask.getId(), subtask);// добавляем в коллекцию подзадач
        Epic epic = epicCollection.get(subtask.getEpicTitle());
        epic.getEpicSubTasks().add(subtask.getId());//добавляем в коллекцию подзадач эпика
        updateEpicStatus(epic);//обновляем статус эпика
        nextId++;
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
        if (subtaskCollection.containsKey(subtask.getId())) {
            subtaskCollection.put(subtask.getId(), subtask);
            Epic epic = epicCollection.get(subtask.getEpicTitle());
            updateEpicStatus(epic);
        }
    }

    public void updateEpicStatus(Epic epic) {
        if (epic.getEpicSubTasks().size() == 0) {
            epic.setStatus("NEW");
            return;
        }
        int epicSize = epic.getEpicSubTasks().size();
        int countNew = 0;
        int countDone = 0;
        int countInProgress = 0;
        int count = 0;
        for (Integer epicSubTask : epic.getEpicSubTasks()) {
            if (subtaskCollection.get(epicSubTask).getStatus().equals("IN_PROGRESS")) {
                countInProgress++;
            } else if (subtaskCollection.get(epicSubTask).getStatus().equals("NEW")) {
                countNew++;
            } else if (subtaskCollection.get(epicSubTask).getStatus().equals("DONE")) {
                countDone++;
            }
        }
        if (countDone == epicSize) {
            epic.setStatus("DONE");
        } else if (countNew == epicSize) {
            epic.setStatus("NEW");
        } else if (countInProgress > 0) {
            epic.setStatus("IN_PROGRESS");
        }
    }

    public void updateTask(Epic epic) {
        if (epicCollection.containsKey(epic.getId())) epicCollection.put(epic.getId(), epic);
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
            if (value.getEpicSubTasks().size() == 0) value.setStatus("NEW");
            System.out.println("Id epic задачи: " + key + "; Имя epic задачи: " + value.getTitle() +
                    "; Описание epic задачи: " + value.getDescription() + "; Статус epic задачи: " + value.getStatus());
        });
    }

    public void deleteTask() {
        taskCollection.clear();
    }

    public void deleteSubTask() {
        subtaskCollection.clear();
        epicCollection.forEach((key, value) -> {
            value.getEpicSubTasks().clear();
            value.setStatus("New");
        });
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
        epic.getEpicSubTasks().remove(id);
        subtaskCollection.remove(id);
        updateEpicStatus(epic);
    }

    public void deleteEpicById(Integer id) {
        Epic epic = epicCollection.get(id);
        for (Integer epicSubTask : epic.getEpicSubTasks()) {
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

    public ArrayList<Subtask> getAllSubtasksList() {
        ArrayList<Subtask> subtasks = new ArrayList<>();
        subtaskCollection.forEach((key, value) -> {
            subtasks.add(value);
        });
        return subtasks;
    }
    public ArrayList<Epic> getAllEpicsList(){
        ArrayList<Epic> epics=new ArrayList<>();
        epicCollection.forEach((key,value)->{
            epics.add(value);
        });
        return epics;
    }

    public ArrayList<Subtask> getSubTasksOfEpic(Epic epic) {
        ArrayList<Subtask> subtask = new ArrayList<>();
        for (Integer epicSubTask : epic.getEpicSubTasks()) {
            subtask.add(subtaskCollection.get(epicSubTask));
        }
        return subtask;
    }

    public void printEpicSubTasks(Integer id) {
        epicCollection.forEach((key, value) -> {
            if (key.equals(id)) {
                for (Integer epicSubTask : value.getEpicSubTasks()) {
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

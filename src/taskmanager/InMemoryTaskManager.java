package taskmanager;

import tasks.Epic;
import tasks.Subtask;
import tasks.Task;
import tasks.TaskStatus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class InMemoryTaskManager implements TaskManager {
    protected final Map<Integer, Task> taskCollection = new HashMap<>();
    protected final Map<Integer, Subtask> subtaskCollection = new HashMap<>();
    protected final Map<Integer, Epic> epicCollection = new HashMap<>();
    protected int nextId = 0;

    public HistoryManager getHistoryManager() {return historyManager;}

    private final HistoryManager historyManager = Managers.getDefaultHistory();

    private void updateEpicStatus(Epic epic) {
        if (epic.getEpicSubTasks().size() == 0) {
            epic.setStatus(TaskStatus.NEW);
            return;
        }
        int epicSize = epic.getEpicSubTasks().size();
        int countNew = 0;
        int countDone = 0;

        for (Integer epicSubTask : epic.getEpicSubTasks()) {
            
            if (subtaskCollection.get(epicSubTask).getStatus().equals(TaskStatus.NEW)) countNew++;
            else if (subtaskCollection.get(epicSubTask).getStatus().equals(TaskStatus.DONE)) countDone++;
        }
        if (countDone == epicSize) {
            epic.setStatus(TaskStatus.DONE);
        } else if (countNew == epicSize) {
            epic.setStatus(TaskStatus.NEW);
        } else {
            epic.setStatus(TaskStatus.IN_PROGRESS);
        }
    }

    public void addTaskById(Task task, int id) {
        if (task instanceof Epic) {
            epicCollection.put(id, (Epic) task);
        } else if (task instanceof Subtask) {
            subtaskCollection.put(id, (Subtask) task);
        } else {
            taskCollection.put(id, task);
        }
        if (nextId <= id) nextId = ++id;
        /*считаю, что nextId не может быть меньше id добавляемой задачи, т.к.
        при добавлении новых задач id в будущем можем перезаписать id
        добавленный из файла
        */
    }

    @Override
    public void addTask(Task task) {
        task.setId(nextId);
        taskCollection.put(task.getId(), task);
        nextId++;
    }

    @Override
    public ArrayList<Task> getHistory() {
        return historyManager.getHistory();
    }

    @Override
    public void addTask(Subtask subtask) {
        subtask.setId(nextId);
        subtaskCollection.put(subtask.getId(), subtask);// добавляем в коллекцию подзадач
        Epic epic = epicCollection.get(subtask.getEpicTitle());
        epic.getEpicSubTasks().add(subtask.getId());//добавляем в коллекцию подзадач эпика
        updateEpicStatus(epic);//обновляем статус эпика
        nextId++;
    }

    @Override
    public void addTask(Epic epic) {
        epic.setId(nextId);
        epicCollection.put(epic.getId(), epic);
        nextId++;
    }

    @Override
    public void updateTask(Task task) {
        if (taskCollection.containsKey(task.getId())) {
            taskCollection.put(task.getId(), task);
        }
    }

    @Override
    public void updateTask(Subtask subtask) {
        if (subtaskCollection.containsKey(subtask.getId())) {
            subtaskCollection.put(subtask.getId(), subtask);
            Epic epic = epicCollection.get(subtask.getEpicTitle());
            epic.getEpicSubTasks().add(subtask.getId());
            updateEpicStatus(epic);
        }
    }

    @Override
    public void updateTask(Epic epic) {
        if (epicCollection.containsKey(epic.getId())) {
            epicCollection.put(epic.getId(), epic);
        }
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
            if (value.getEpicSubTasks().size() == 0) value.setStatus(TaskStatus.NEW);
            System.out.println("Id epic задачи: " + key + "; Имя epic задачи: " + value.getTitle() +
                    "; Описание epic задачи: " + value.getDescription() + "; Статус epic задачи: " + value.getStatus());
        });
    }

    @Override
    public void deleteTask() {
        taskCollection.forEach((key, value) -> {
            historyManager.remove(key);
        });
        taskCollection.clear();
    }

    @Override
    public void deleteSubTask() {
        subtaskCollection.forEach((key, value) -> {
            historyManager.remove(key);
        });
        subtaskCollection.clear();
        for (Epic epic : epicCollection.values()) {
            epic.getEpicSubTasks().clear();
            epic.setStatus(TaskStatus.NEW);
        }
    }

    @Override
    public void deleteEpic() {
        subtaskCollection.forEach((key, value) -> {
            historyManager.remove(key);
        });
        epicCollection.forEach((key, value) -> {
            historyManager.remove(key);
        });
        epicCollection.clear();
        subtaskCollection.clear();
    }

    @Override
    public Task getTaskById(int id) {
        historyManager.add(taskCollection.get(id));
        return taskCollection.get(id);
    }

    @Override
    public Subtask getSubTaskById(int id) {
        historyManager.add(subtaskCollection.get(id));
        return subtaskCollection.get(id);
    }

    @Override
    public Epic getEpicById(int id) {
        historyManager.add(epicCollection.get(id));
        return epicCollection.get(id);
    }

    @Override
    public void deleteTaskById(Integer id) {
        taskCollection.remove(id);
        historyManager.remove(id);
    }

    @Override
    public void deleteSubTaskById(Integer id) {
        Epic epic = epicCollection.get(subtaskCollection.get(id).getEpicTitle());
        epic.getEpicSubTasks().remove(id);
        subtaskCollection.remove(id);
        updateEpicStatus(epic);
        historyManager.remove(id);
    }

    @Override
    public void deleteEpicById(Integer id) {
        Epic epic = epicCollection.get(id);
        for (Integer epicSubTask : epic.getEpicSubTasks()) {
            subtaskCollection.remove(epicSubTask);
            historyManager.remove(epicSubTask);
        }
        epicCollection.remove(id);
        historyManager.remove(id);
    }

    @Override
    public ArrayList<Task> getAllTasksList() {
        return new ArrayList<>(taskCollection.values());
    }

    @Override
    public ArrayList<Subtask> getAllSubtasksList() {
        return new ArrayList<>(subtaskCollection.values());
    }

    @Override
    public ArrayList<Epic> getAllEpicsList() {
        return new ArrayList<>(epicCollection.values());
    }

    @Override
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

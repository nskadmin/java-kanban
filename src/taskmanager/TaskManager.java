package taskmanager;

import tasks.Epic;
import tasks.Subtask;
import tasks.Task;

import java.util.ArrayList;
import java.util.LinkedList;

public interface TaskManager {
    public ArrayList<Subtask> getSubTasksOfEpic(Epic epic);

    public ArrayList<Epic> getAllEpicsList();

    public ArrayList<Subtask> getAllSubtasksList();

    public ArrayList<Task> getAllTasksList();

    public void deleteEpicById(Integer id);

    public void deleteSubTaskById(Integer id);

    public void deleteTaskById(Integer id);

    public Epic getEpicById(int id);

    public Subtask getSubTaskById(int id);

    public Task getTaskById(int id);

    public void deleteEpic();

    public void deleteSubTask();

    public void deleteTask();

    public void updateTask(Epic epic);

    public void updateTask(Subtask subtask);

    public void updateTask(Task task);

    public void addTask(Epic epic);

    public void addTask(Subtask subtask);

    public void addTask(Task task);

    public ArrayList<Task> getHistory();
}

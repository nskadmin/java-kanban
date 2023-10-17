import taskmanager.*;
import taskmanager.Managers;
import tasks.Epic;
import tasks.Subtask;
import tasks.Task;
import tasks.TaskStatus;

import java.util.List;

public class Main {

    public static void main(String[] args) {
        System.out.println("Поехали!");
        TaskManager inMemoryTaskManager = Managers.getDefault();
        HistoryManager inMemoryHistoryManager = Managers.getDefaultHistory();
        Epic epicOne = new Epic("Epic1", "Description of epic1");
        inMemoryTaskManager.addTask(epicOne);
        Subtask subtaskOne = new Subtask("Subtask1", "Description of subtask1", epicOne.getId());
        Subtask subtaskTwo = new Subtask("Subtask2", "Description of subtask2", epicOne.getId());
        Subtask subtaskThree = new Subtask("Subtask3", "Description of subtask3", epicOne.getId());
        inMemoryTaskManager.addTask(subtaskOne);
        inMemoryTaskManager.addTask(subtaskTwo);
        inMemoryTaskManager.addTask(subtaskThree);

        Epic epicTwo = new Epic("Epic2", "Description of epic2");
        inMemoryTaskManager.addTask(epicTwo);
        printAll(inMemoryTaskManager);

        System.out.println("Удаляем задачу");
        inMemoryTaskManager.deleteSubTaskById(1);
        printAll(inMemoryTaskManager);
        System.out.println("Удаляем эпик с 3 подзадачами");
        inMemoryTaskManager.deleteEpicById(0);
        printAll(inMemoryTaskManager);
        System.out.println("Удаляем оставшийся эпик");
        inMemoryTaskManager.deleteEpicById(4);
        printAll(inMemoryTaskManager);
    }

    public static void printAll(TaskManager inMemoryTaskManager) {
        for (Task task : inMemoryTaskManager.getHistory()) {
            System.out.println("Id: " + task.getId()
                    + "; Имя: " + task.getTitle()
                    + "; Описание: " + task.getDescription()
                    + "; Статус: " + task.getStatus());
        }
    }
}
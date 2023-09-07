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
        //Создайте 2 задачи
        Task taskOne = new Task("Task1", "Description of task1");
        Task taskTwo = new Task("Task2", "Description of task2");
        inMemoryTaskManager.addTask(taskOne);
        inMemoryTaskManager.addTask(taskTwo);
        inMemoryTaskManager.getTaskById(0);
        //один эпик с 2 подзадачами

        Epic epicOne = new Epic("Epic1", "Description of epic1");
        inMemoryTaskManager.addTask(epicOne);
        inMemoryTaskManager.getEpicById(2);
        Subtask subtaskOne = new Subtask("Subtask1", "Description of subtask1", epicOne.getId());
        Subtask subtaskTwo = new Subtask("Subtask2", "Description of subtask2", epicOne.getId());
        inMemoryTaskManager.addTask(subtaskOne);
        inMemoryTaskManager.addTask(subtaskTwo);

        inMemoryTaskManager.getSubTaskById(3);
        inMemoryTaskManager.getSubTaskById(4);

        //эпик с 1 подзадачей
        Epic epicTwo = new Epic("Epic2", "Description of epic2");
        inMemoryTaskManager.addTask(epicTwo);
        inMemoryTaskManager.getEpicById(5);
        Subtask subtaskThree = new Subtask("Subtask3", "Description of subtask3", epicTwo.getId());
        inMemoryTaskManager.addTask(subtaskThree);
        inMemoryTaskManager.getSubTaskById(6);
        inMemoryTaskManager.getSubTaskById(3);
        inMemoryTaskManager.getSubTaskById(4);
        inMemoryTaskManager.getSubTaskById(4);

        //меняем статус задачи1
        System.out.println("меняем статус задачи1");
        taskOne.setTitle("Task1-updated");
        taskOne.setStatus(TaskStatus.DONE);
        taskOne.setDescription("New description of task1");
        inMemoryTaskManager.updateTask(taskOne);
        //меняем подзадачи Epic1
        System.out.println("меняем подзадачи Epic1");
        subtaskOne.setStatus(TaskStatus.IN_PROGRESS);
        subtaskTwo.setStatus(TaskStatus.DONE);
        inMemoryTaskManager.updateTask(subtaskOne);
        inMemoryTaskManager.updateTask(subtaskTwo);
        inMemoryTaskManager.updateTask(epicOne);
        //меняем epic2
        System.out.println("меняем epic2");
        subtaskThree.setStatus(TaskStatus.DONE);
        inMemoryTaskManager.updateTask(subtaskThree);
        inMemoryTaskManager.updateTask(epicTwo);
        //удаляем task1
        System.out.println("удаляем task1");
        inMemoryTaskManager.deleteTaskById(1);
        //удаляем подзадачу2 в epic1
        System.out.println("удаляем подзадачу2 в epic1");
        inMemoryTaskManager.deleteSubTaskById(6);
        //печатаем полученную историю
        for (Task task : inMemoryTaskManager.getHistory()) {
            System.out.println("Id: " + task.getId()
                    + "; Имя: " + task.getTitle()
                    + "; Описание: " + task.getDescription()
                    + "; Статус: " + task.getStatus());
        }
    }
}

import taskmanager.*;
import taskmanager.Managers;
import tasks.Epic;
import tasks.Subtask;
import tasks.Task;

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
        System.out.println("Начало(пустая история): " + inMemoryTaskManager.getHistory());
        inMemoryTaskManager.getSubTaskById(2);
        System.out.println("История после запроса subtask2: " + inMemoryTaskManager.getHistory());
        inMemoryTaskManager.getSubTaskById(3);
        System.out.println("История после запроса subtask3: " + inMemoryTaskManager.getHistory());
        inMemoryTaskManager.getSubTaskById(2);
        System.out.println("История после запроса subtask2: " + inMemoryTaskManager.getHistory());
        inMemoryTaskManager.getSubTaskById(3);
        System.out.println("История после запроса subtask3: " + inMemoryTaskManager.getHistory());
        inMemoryTaskManager.getSubTaskById(2);
        System.out.println("История после запроса subtask2: " + inMemoryTaskManager.getHistory());

        inMemoryTaskManager.deleteSubTaskById(2);
        System.out.println("История после удаления subtask2: " + inMemoryTaskManager.getHistory());
        inMemoryTaskManager.deleteSubTaskById(3);
        System.out.println("История после удаления subtask3: " + inMemoryTaskManager.getHistory());
        inMemoryTaskManager.getSubTaskById(1);
        inMemoryTaskManager.getEpicById(0);
        System.out.println("История после запроса epic1 и subtask1: " + inMemoryTaskManager.getHistory());
        inMemoryTaskManager.deleteEpicById(0);
        System.out.println("История после удаления epic1: " + inMemoryTaskManager.getHistory());

        inMemoryTaskManager.getEpicById(4);
        System.out.println("История после запроса epic2: " + inMemoryTaskManager.getHistory());




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

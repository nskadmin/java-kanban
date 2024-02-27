package taskmanager;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import tasks.Epic;
import tasks.Subtask;
import tasks.Task;

import java.util.ArrayList;

public class InMemoryTaskManagerTest {

    @Test
    public void addEpic() {
        TaskManager inMemoryTaskManager = Managers.getDefault();
        Epic epicTwo = new Epic("Epic2", "Description of epic2");
        inMemoryTaskManager.addTask(epicTwo);
        Assertions.assertEquals(0, (Integer) inMemoryTaskManager.getEpicById(0).getId());
    }

    @Test
    public void getByEpicOrSubtaskObject() {
        TaskManager inMemoryTaskManager = Managers.getDefault();
        Epic epicOne = new Epic("Epic1", "Description of epic1");
        inMemoryTaskManager.addTask(epicOne);
        Subtask subtaskOne = new Subtask("Subtask1", "Description of subtask1", epicOne.getId());
        Subtask subtaskTwo = new Subtask("Subtask2", "Description of subtask2", epicOne.getId());
        Subtask subtaskThree = new Subtask("Subtask3", "Description of subtask3", epicOne.getId());
        inMemoryTaskManager.addTask(subtaskOne);
        inMemoryTaskManager.addTask(subtaskTwo);
        inMemoryTaskManager.addTask(subtaskThree);
        inMemoryTaskManager.deleteSubTaskById(1);
        Assertions.assertSame(epicOne, inMemoryTaskManager.getEpicById(0));
        Assertions.assertSame(subtaskThree, inMemoryTaskManager.getSubTaskById(3));
    }

    @Test
    public void getByNonExistId() {
        TaskManager inMemoryTaskManager = Managers.getDefault();
        Epic epicOne = new Epic("Epic1", "Description of epic1");
        inMemoryTaskManager.addTask(epicOne);
        Subtask subtaskOne = new Subtask("Subtask1", "Description of subtask1", epicOne.getId());
        Subtask subtaskTwo = new Subtask("Subtask2", "Description of subtask2", epicOne.getId());
        Subtask subtaskThree = new Subtask("Subtask3", "Description of subtask3", epicOne.getId());
        inMemoryTaskManager.addTask(subtaskOne);
        inMemoryTaskManager.addTask(subtaskTwo);
        inMemoryTaskManager.addTask(subtaskThree);
        inMemoryTaskManager.deleteSubTaskById(1);
        Assertions.assertThrows(NullPointerException.class, () -> {
            inMemoryTaskManager.deleteEpicById(1);
        });
    }

    @Test
    public void historyListIsCorrectAfterOperations() {
        TaskManager inMemoryTaskManager = Managers.getDefault();
        Epic epicOne = new Epic("Epic1", "Description of epic1");
        inMemoryTaskManager.addTask(epicOne);
        Subtask subtaskOne = new Subtask("Subtask1", "Description of subtask1", epicOne.getId());
        Subtask subtaskTwo = new Subtask("Subtask2", "Description of subtask2", epicOne.getId());
        Subtask subtaskThree = new Subtask("Subtask3", "Description of subtask3", epicOne.getId());
        inMemoryTaskManager.addTask(subtaskOne);
        inMemoryTaskManager.addTask(subtaskTwo);
        ArrayList<Task> tasks = inMemoryTaskManager.getHistory();
        inMemoryTaskManager.addTask(subtaskThree);
        inMemoryTaskManager.deleteSubTaskById(3);
        Assertions.assertEquals(tasks, inMemoryTaskManager.getHistory());
    }
}

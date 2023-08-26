public class Main {

    public static void main(String[] args) {
        System.out.println("Поехали!");
        Manager manager = new Manager();
        //Создайте 2 задачи
        Task taskOne = new Task("Task1", "Description of task1");
        Task taskTwo = new Task("Task2", "Description of task2");
        manager.addTask(taskOne);
        manager.addTask(taskTwo);
        //один эпик с 2 подзадачами
        Subtask subtaskOne = new Subtask("Subtask1", "Description of subtask1");
        Subtask subtaskTwo = new Subtask("Subtask2", "Description of subtask2");
        Epic epicOne = new Epic("Epic1", "Description of epic1");
        manager.addTask(subtaskOne);
        manager.addTask(subtaskTwo);
        manager.addTask(epicOne);
        //эпик с 1 подзадачей
        Subtask subtaskThree = new Subtask("Subtask3", "Description of subtask3");
        Epic epicTwo = new Epic("Epic2", "Description of epic2");
        manager.addTask(subtaskThree);
        manager.addTask(epicTwo);
        //печать всех задач
        System.out.println("печать всех задач");
        manager.printTask();
        //печать всех подзадач
        System.out.println("печать всех подзадач");
        manager.printSubTask();
        //печать всех epicов
        System.out.println("печать всех epicов");
        manager.printEpic();
        //меняем статус задачи1
        System.out.println("меняем статус задачи1");
        taskOne.status = "DONE";
        taskOne.description = "New description of task1";
        manager.updateTask(taskOne);
        manager.printTask();
        //меняем подзадачи Epic1
        System.out.println("меняем подзадачи Epic1");
        subtaskOne.status = "IN_PROGRESS";
        subtaskTwo.status = "DONE";
        manager.updateTask(subtaskOne);
        manager.updateTask(subtaskTwo);
        manager.printSubTask();
        //manager.updateTask(epicOne);
        manager.printEpic();
        //меняем epic2
        System.out.println("меняем epic2");
        subtaskThree.status = "DONE";
        manager.updateTask(epicTwo);
        manager.printEpic();
        //удаляем task1
        System.out.println("удаляем task1");
        manager.deleteTaskById(1);
        manager.printTask();
        //удаляем подзадачу2 в epic1
        System.out.println("удаляем подзадачу2 в epic1");
        manager.deleteSubTaskById(3);
        manager.printSubTask();
    }

}

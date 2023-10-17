package taskmanager;

import java.util.InputMismatchException;

import tasks.*;

import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ForkJoinPool;

class ManagerSaveException extends Exception {
    public ManagerSaveException() {
        super("Ошибка при работе с файлом!");
    }

    public String getDetailMessage() {
        return getMessage();
    }
}

public class FileBackedTasksManager extends InMemoryTaskManager implements TaskManager {


    public static void main(String[] args) {
        //save to filecd ~~~

        FileBackedTasksManager fbtToFile = new FileBackedTasksManager();
        Epic epicTwo = new Epic("Epic2", "Description of epic2");
        Subtask subTaskTwo = new Subtask("SubTask2", "Description of Subtask2", epicTwo.getId());
        Subtask subTaskThree = new Subtask("SubTask3", "Description of Subtask3", epicTwo.getId());
        fbtToFile.updateTask(epicTwo);
        fbtToFile.addTask(epicTwo);
        fbtToFile.addTask(subTaskTwo);
        fbtToFile.addTask(subTaskThree);
        subTaskTwo.setStatus(TaskStatus.DONE);
        subTaskThree.setStatus(TaskStatus.DONE);
        fbtToFile.updateTask(subTaskThree);
        fbtToFile.updateTask(subTaskTwo);
        Epic epicThree = new Epic("Epic3", "Description of epic3");
        Subtask subTaskFour = new Subtask("SubTask4", "Description of Subtask4", epicThree.getId());
        fbtToFile.addTask(epicThree);
        fbtToFile.addTask(subTaskFour);
        Task taskOne = new Task("task1", "Description of task1");
        fbtToFile.addTask(taskOne);
        //read from file
        FileBackedTasksManager fbtFromFile = new FileBackedTasksManager();
        File file = new File(System.getProperty("user.dir") + "\\historyManager.csv");
        fbtFromFile = loadFromFile(file);
        System.out.println(fbtFromFile.getHistory());
    }

    static FileBackedTasksManager loadFromFile(File file) {
        FileBackedTasksManager fileBackedTasksManager = new FileBackedTasksManager();
        try (FileReader fileReader = new FileReader(file)) {
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            String line = bufferedReader.readLine();
            while (bufferedReader.ready() && !line.isEmpty()) {
                if (line.contains("EPIC"))
                    fileBackedTasksManager.addTask((Epic) fileBackedTasksManager.fromString(line));
                else if (line.contains("SUBTASK"))
                    fileBackedTasksManager.addTask((Subtask) fileBackedTasksManager.fromString(line));
                else fileBackedTasksManager.addTask(fileBackedTasksManager.fromString(line));
                line = bufferedReader.readLine();
            }
            line = bufferedReader.readLine();
            List<Integer> arrayList = historyFromString(line);
            for (Task task : fileBackedTasksManager.getHistory()) {
                if (arrayList.contains(task.getId())) {
                    task.setStatus(TaskStatus.DONE);
                    fileBackedTasksManager.updateTask(task);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return fileBackedTasksManager;
    }

    private void save() {
        String filePath = System.getProperty("user.dir") + "\\historyManager.csv";
        try (FileWriter fileWriter = new FileWriter(filePath)) {
            for (Task task : this.getHistory()) {
                fileWriter.write(task.toString() + "\n");
            }
            fileWriter.write("\n" + historyToString(this.getHistoryManager()));
        } catch (IOException e) {
            try {
                throw new ManagerSaveException();
            } catch (ManagerSaveException ex) {
                throw new RuntimeException(ex);
            }
        }
    }

    static String historyToString(HistoryManager manager) {
        StringBuilder stringBuilder = new StringBuilder();
        for (Task task : manager.getHistory()) {
            if (!task.getStatus().equals(TaskStatus.NEW)) stringBuilder.append(task.getId()).append(",");
        }
        if (stringBuilder.length() != 0) stringBuilder.delete(stringBuilder.lastIndexOf(","), stringBuilder.length());
        return stringBuilder.toString();
    }

    static List<Integer> historyFromString(String value) {
        String[] split = value.split(",");
        List<Integer> list = new ArrayList<>();
        for (String val : split) {
            list.add(Integer.parseInt(val));
        }
        return list;
    }

    public Task fromString(String value) {
        String[] split = value.split(",");
        int id = Integer.parseInt(split[0]);
        TaskType taskType = TaskType.valueOf(split[1]);
        String taskName = split[2];
        String taskDescription = split[4];
        Task task = null;
        switch (taskType) {
            case TASK:
                task = new Task(taskName, taskDescription);
                break;
            case SUBTASK:
                int epicNumber = Integer.valueOf(split[5]);
                task = new Subtask(taskName, taskDescription, epicNumber);
                break;
            case EPIC:
                task = new Epic(taskName, taskDescription);
                break;
        }
        task.setId(id);
        return task;
    }

    @Override
    public ArrayList<Subtask> getSubTasksOfEpic(Epic epic) {
        return super.getSubTasksOfEpic(epic);
    }

    @Override
    public ArrayList<Epic> getAllEpicsList() {
        return super.getAllEpicsList();
    }

    @Override
    public ArrayList<Subtask> getAllSubtasksList() {
        return super.getAllSubtasksList();
    }

    @Override
    public ArrayList<Task> getAllTasksList() {
        return super.getAllTasksList();
    }

    @Override
    public void deleteEpicById(Integer id) {
        super.deleteEpicById(id);
        save();
    }

    @Override
    public void deleteSubTaskById(Integer id) {
        super.deleteSubTaskById(id);
        save();
    }

    @Override
    public void deleteTaskById(Integer id) {
        super.deleteTaskById(id);
        save();
    }

    @Override
    public Epic getEpicById(int id) {
        return super.getEpicById(id);
    }

    @Override
    public Subtask getSubTaskById(int id) {
        return super.getSubTaskById(id);
    }

    @Override
    public Task getTaskById(int id) {
        return super.getTaskById(id);
    }

    @Override
    public void deleteEpic() {
        super.deleteEpic();
    }

    @Override
    public void deleteSubTask() {
        super.deleteSubTask();
    }

    @Override
    public void deleteTask() {
        super.deleteTask();
    }

    @Override
    public void updateTask(Epic epic) {
        super.updateTask(epic);
        save();
    }

    @Override
    public void updateTask(Subtask subtask) {
        super.updateTask(subtask);
        save();
    }

    @Override
    public void updateTask(Task task) {
        super.updateTask(task);
        save();
    }

    @Override
    public void addTask(Epic epic) {
        super.addTask(epic);
        save();
    }

    @Override
    public void addTask(Subtask subtask) {
        super.addTask(subtask);
        save();
    }

    @Override
    public void addTask(Task task) {
        super.addTask(task);
        save();
    }

    @Override
    public ArrayList<Task> getHistory() {
        return super.getHistory();
    }
}
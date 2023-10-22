package taskmanager;

import java.util.*;

import tasks.*;

import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.ForkJoinPool;

class ManagerSaveException extends RuntimeException {
    public ManagerSaveException() {
        super("Ошибка при работе с файлом!");
    }

    public String getDetailMessage() {
        return getMessage();
    }
}

public class FileBackedTasksManager extends InMemoryTaskManager {

    private final String title = "id,type,name,status,description,epic" + System.lineSeparator();

    public static void main(String[] args) {
        FileBackedTasksManager fbtToFile = new FileBackedTasksManager();
        Epic epicTwo = new Epic("Epic2", "Description of epic2");
        Subtask subTaskTwo = new Subtask("SubTask2", "Description of Subtask2", epicTwo.getId());
        Subtask subTaskThree = new Subtask("SubTask3", "Description of Subtask3", epicTwo.getId());
        fbtToFile.addTask(epicTwo);
        fbtToFile.addTask(subTaskThree);
        fbtToFile.addTask(subTaskTwo);
        Epic epicThree = new Epic("Epic3", "Description of epic3");
        fbtToFile.addTask(epicThree);
        Subtask subTaskFour = new Subtask("SubTask4", "Description of Subtask4", epicThree.getId());
        fbtToFile.addTask(subTaskFour);
        Task taskOne = new Task("task1", "Description of task1");
        fbtToFile.addTask(taskOne);

        fbtToFile.getTaskById(taskOne.getId());
        fbtToFile.getEpicById(epicTwo.getId());
        fbtToFile.getSubTaskById(subTaskThree.getId());
        fbtToFile.getSubTaskById(subTaskFour.getId());
        //read from file
        File filePath = new File("resources\\historyManager.csv");
        FileBackedTasksManager fbtFromFile = loadFromFile(filePath);
        System.out.println(fbtFromFile.getHistory());
    }

    private static void updateAddedEpictasks(FileBackedTasksManager fileBackedTasksManager) {
        for (Subtask subtask : fileBackedTasksManager.getAllSubtasksList()) {
            fileBackedTasksManager.updateTask(subtask);
        }
    }

    public static FileBackedTasksManager loadFromFile(File file) {
        FileBackedTasksManager fileBackedTasksManager = new FileBackedTasksManager();
        try (FileReader fileReader = new FileReader(file)) {
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            bufferedReader.readLine();//1-я строка заголовок
            String line = bufferedReader.readLine();
            ArrayList<Task> readLinesFromFile = new ArrayList<>();
            while (bufferedReader.ready() && !line.isEmpty()) {//формируем список задач
                readLinesFromFile.add(fileBackedTasksManager.fromString(line));
                line = bufferedReader.readLine();
            }
            line = bufferedReader.readLine();//история
            for (Task val : readLinesFromFile) {//заведение задач
                if (val instanceof Epic) {
                    Epic epic = (Epic) val;
                    fileBackedTasksManager.addTaskById(epic, epic.getId());
                } else if (val instanceof Subtask) {
                    Subtask subtask = (Subtask) val;
                    fileBackedTasksManager.addTaskById(subtask, subtask.getId());
                } else {
                    fileBackedTasksManager.addTaskById(val, val.getId());
                }
            }

            updateAddedEpictasks(fileBackedTasksManager);
            List<Integer> arrayList = historyFromString(line);//список для загрузки истории
            for (Integer intVal : arrayList) {//добавление истории
                for (Task task : readLinesFromFile) {
                    if ((task.getId() == intVal)) {
                        if (task instanceof Epic) {
                            fileBackedTasksManager.getEpicById(intVal);
                        } else if (task instanceof Subtask) {
                            fileBackedTasksManager.getSubTaskById(intVal);
                        } else {
                            fileBackedTasksManager.getTaskById(intVal);
                        }
                        break;
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return fileBackedTasksManager;
    }

    private void save() {
        File filePath = new File("resources\\historyManager.csv");
        try (FileWriter fileWriter = new FileWriter(filePath)) {
            fileWriter.write(title);
            for (Task task : super.getAllTasksList()) {
                fileWriter.write(task.toString() + System.lineSeparator());
            }
            for (Subtask subtask : super.getAllSubtasksList()) {
                fileWriter.write(subtask.toString() + System.lineSeparator());
            }
            for (Epic epic : super.getAllEpicsList()) {
                fileWriter.write(epic.toString() + System.lineSeparator());
            }
            fileWriter.write("\n" + historyToString(this.getHistoryManager()));
        } catch (IOException e) {
            throw new ManagerSaveException();
        }
    }

    public static String historyToString(HistoryManager manager) {
        StringBuilder stringBuilder = new StringBuilder();
        for (Task task : manager.getHistory()) {
            stringBuilder.append(task.getId()).append(",");
        }
        if (stringBuilder.length() != 0) stringBuilder.delete(stringBuilder.lastIndexOf(","), stringBuilder.length());
        return stringBuilder.toString();
    }

    public static List<Integer> historyFromString(String value) {
        List<Integer> list = new ArrayList<>();
        if (value == null) return list;
        String[] split = value.trim().split(",");
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
        TaskStatus taskStatus = TaskStatus.valueOf(split[3]);
        String taskDescription = split[4];
        Task task = null;
        switch (taskType) {
            case TASK:
                task = new Task(taskName, taskDescription);
                break;
            case SUBTASK:
                int epicNumber = Integer.parseInt(split[5]);
                task = new Subtask(taskName, taskDescription, epicNumber);
                break;
            case EPIC:
                task = new Epic(taskName, taskDescription);
                break;
        }
        task.setId(id);
        task.setStatus(taskStatus);
        return task;
    }

    @Override
    public void addTaskById(Task task, int id) {
        super.addTaskById(task, id);
        save();
    }

    @Override
    public ArrayList<Subtask> getSubTasksOfEpic(Epic epic) {
        ArrayList<Subtask> subtasks = super.getSubTasksOfEpic(epic);
        save();
        return subtasks;
    }

    @Override
    public ArrayList<Epic> getAllEpicsList() {
        ArrayList<Epic> epics = super.getAllEpicsList();
        save();
        return epics;
    }

    @Override
    public ArrayList<Subtask> getAllSubtasksList() {
        ArrayList<Subtask> subtasks = super.getAllSubtasksList();
        save();
        return subtasks;
    }

    @Override
    public ArrayList<Task> getAllTasksList() {
        ArrayList<Task> tasks = super.getAllTasksList();
        save();
        return tasks;
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
        Epic epic = super.getEpicById(id);
        save();
        return epic;
    }

    @Override
    public Subtask getSubTaskById(int id) {
        Subtask subtask = super.getSubTaskById(id);
        save();
        return subtask;
    }

    @Override
    public Task getTaskById(int id) {
        Task task = super.getTaskById(id);
        save();
        return task;
    }

    @Override
    public void deleteEpic() {
        super.deleteEpic();
        save();
    }

    @Override
    public void deleteSubTask() {
        super.deleteSubTask();
        save();
    }

    @Override
    public void deleteTask() {
        super.deleteTask();
        save();
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
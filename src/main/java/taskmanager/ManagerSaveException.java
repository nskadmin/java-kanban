package taskmanager;

class ManagerSaveException extends RuntimeException {
    public ManagerSaveException() {
        super("Ошибка при работе с файлом!");
    }

    public String getDetailMessage() {
        return getMessage();
    }
}
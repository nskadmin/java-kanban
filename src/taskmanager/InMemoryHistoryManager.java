package taskmanager;

import tasks.Task;

import java.util.ArrayList;
import java.util.HashMap;

public final class InMemoryHistoryManager implements HistoryManager {
    private Node<Task> head;
    private Node<Task> tail;
    private int size = 0;
    private final HashMap<Integer, Node<Task>> historyAdded = new HashMap<>();

    public void removeNode(Node node) {
        if (head == null) {
            return;
        }

        if (head.task.equals(node.task)) {

            head = head.next;
            if (head != null) {
                head.prev = null;
            }
            size--;
            return;
        }

        Node<Task> current = head;
        Node<Task> prev = null;

        while (current != null && !current.task.equals(node.task)) {
            prev = current;
            current = current.next;
        }

        if (current != null) {
            prev.next = current.next;
            if (current.next != null) {
                current.next.prev = current.prev;
            }
            size--;
        }
    }

    public ArrayList<Task> getTasks() {
        ArrayList<Task> tasks = new ArrayList<>();
        Node<Task> current = head;
        while (current != null) {
            tasks.add(current.task);
            current = current.next;

        }
        return tasks;
    }

    public void linkLast(Task task) {
        final Node<Task> oldTail = tail;
        final Node<Task> newNode = new Node<>(tail, task, null);
        tail = newNode;
        if (oldTail == null) {
            head = newNode;
        } else {
            oldTail.next = newNode;
        }
        historyAdded.put(task.getId(), newNode);
    }

    @Override
    public void add(Task task) {
        if (task != null) {
            Node<Task> newNode = new Node<>(task);
            if (historyAdded.containsKey(task.getId())) {
                removeNode(newNode);
            }
            if (head == null) {
                head = newNode;
            } else {
                Node<Task> current = head;
                while (current.next != null) {
                    current = current.next;
                }
                current.next = newNode;
            }
            size++;
            linkLast(task);
        }
    }

    @Override
    public void remove(int id) {
        if (historyAdded.get(id) != null) {
            Node node = historyAdded.remove(id);
            if (node == null) {// если не было
                return;
            }
            removeNode(node);
        }
    }

    @Override
    public ArrayList<Task> getHistory() {
        return getTasks();
    }

    private static class Node<T> {
        public T task;
        public Node<T> next;
        public Node<T> prev;

        public Node(T task) {
            this.task = task;
            this.next = null;
            this.prev = null;
        }

        public Node(Node<T> prev, T task, Node<T> next) {
            this.task = task;
            this.next = next;
            this.prev = prev;
        }
    }
}

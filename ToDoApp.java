import java.awt.*;
import javax.swing.*;

public class ToDoApp {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(ToDoFrame::new);
    }
}

class ToDoFrame extends JFrame {
    private final TaskManager taskManager;
    private JTextField taskInput;
    private JList<String> taskList;

    public ToDoFrame() {
        super(" To-Do List Manager");
        this.taskManager = new TaskManager();
        configureWindow();
        initializeComponents();
        setVisible(true);
    }

    private void configureWindow() {
        setSize(500, 450);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));
        getRootPane().setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));
    }

    private void initializeComponents() {
        
        JPanel inputPanel = createInputPanel();
           
        JScrollPane listScrollPane = createTaskList();
              
        JPanel buttonPanel = createButtonPanel();

    
        add(inputPanel, BorderLayout.NORTH);
        add(listScrollPane, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    private JPanel createInputPanel() {
        JPanel panel = new JPanel(new BorderLayout(5, 0));
        taskInput = new JTextField();
        JButton addButton = new JButton("Add Task");
        
        addButton.addActionListener(e -> addNewTask());
        taskInput.addActionListener(e -> addNewTask());
        
        panel.add(taskInput, BorderLayout.CENTER);
        panel.add(addButton, BorderLayout.EAST);
        return panel;
    }

    private JScrollPane createTaskList() {
        taskList = new JList<>(taskManager.getTaskModel());
        taskList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        taskList.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        taskList.setVisibleRowCount(10);
        
        return new JScrollPane(taskList);
    }

    private JPanel createButtonPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        JButton deleteButton = new JButton("Delete Selected");
        JButton clearButton = new JButton("Clear All");
        
        deleteButton.addActionListener(e -> removeSelectedTask());
        clearButton.addActionListener(e -> clearAllTasks());
        
        panel.add(deleteButton);
        panel.add(clearButton);
        return panel;
    }

    private void addNewTask() {
        String taskDescription = taskInput.getText().trim();
        if (!taskDescription.isEmpty()) {
            taskManager.addTask(taskDescription);
            taskInput.setText("");
        } else {
            showMessage("Please enter a task description", "Empty Input");
        }
        taskInput.requestFocus();
    }

    private void removeSelectedTask() {
        try {
            taskManager.removeTask(taskList.getSelectedIndex());
        } catch (IllegalArgumentException ex) {
            showMessage(ex.getMessage(), "Selection Error");
        }
    }

    private void clearAllTasks() {
        if (taskManager.hasTasks()) {
            int response = JOptionPane.showConfirmDialog(this, 
                    "Clear all tasks? ", 
                    "Confirm Clear", 
                    JOptionPane.YES_NO_OPTION);
            
            if (response == JOptionPane.YES_OPTION) {
                taskManager.clearTasks();
            }
        } else {
            showMessage("There are no tasks to clear", "Information");
        }
    }

    private void showMessage(String message, String title) {
        JOptionPane.showMessageDialog(this, message, title, JOptionPane.INFORMATION_MESSAGE);
    }
}

class TaskManager {
    private final DefaultListModel<String> taskModel;

    public TaskManager() {
        taskModel = new DefaultListModel<>();
    }

    public DefaultListModel<String> getTaskModel() {
        return taskModel;
    }

    public void addTask(String description) {
        taskModel.addElement(description);
    }

    public void removeTask(int index) {
        if (index >= 0 && index < taskModel.size()) {
            taskModel.remove(index);
        } else {
            throw new IllegalArgumentException("Please select a task to delete");
        }
    }

    public void clearTasks() {
        taskModel.clear();
    }

    public boolean hasTasks() {
        return !taskModel.isEmpty();
    }
}
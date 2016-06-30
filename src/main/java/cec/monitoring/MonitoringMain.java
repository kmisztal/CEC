package cec.monitoring;

/**
 * The main class for the monitoring. It is used to communicate
 * with GUI. To use it, first use getInstance method to
 */

public class MonitoringMain {

    /**
     * All of the possible states subtasks can have.
     * Colors are assigned to states in Monitoring.GUI.getColor()
     * To add more states and colors, update this enum and Monitoring.GUI.getColor() method.
     */
    public enum Status {
        PROGRESS, DONE, FAILED
    }

    private static MonitoringMain instance = null;

    private MonitoringMain() {
    }

    public static MonitoringMain getInstance() throws InterruptedException {
        if (instance == null) {
            instance = new MonitoringMain();
        }
        init();
        return instance;
    }


    /**
     * Sets the number of subtasks for a particular task.
     * By default, all the subtasks are marked as "in progress".
     *
     * @param task     Number of the task to be updated. Starts from 0.
     * @param subTasks Number of subtasks in this task.
     */

    public static void setSubtasks(int task, int subTasks) {
        MonitoringGUI.setSubtasks(task, subTasks);
    }

    /**
     * Returns number of subtasks in this task.
     *
     * @param task Number of the task to be checked, starts from 0.
     */

    public int getSubtasks(int task) {
        return MonitoringGUI.getSubtasks(task);
    }

    /**
     * Returns number of subtasks in a particular task.
     */

    public int getTasks() {
        return MonitoringGUI.getTasks();
    }

    /**
     * Marks next subtask in a particular task with given status.
     *
     * @param task   Number of the task to be updated. Starts from 0.
     * @param status Status of the subtask.
     */

    public static void updateNext(int task, Status status) {
        MonitoringGUI.updateNext(task, status);
    }

    /**
     * Marks particular subtask in a given task.
     *
     * @param task    Number of the task to be updated. Starts from 0.
     * @param subtask Number of the subtask to be updated. Starts from 0.
     * @param status  Status of the subtask.
     */

    public static void update(int task, int subtask, Status status) {
        MonitoringGUI.update(task, subtask, status);
    }

    /**
     * Starts a thread with GUI.
     *
     * @throws InterruptedException
     */

    public static void init() throws InterruptedException {
        new Thread() {
            @Override
            public void run() {
                javafx.application.Application.launch(MonitoringGUI.class);
            }
        }.start();
    }

}

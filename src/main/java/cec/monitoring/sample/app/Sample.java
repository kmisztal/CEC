package cec.monitoring.sample.app;

import cec.monitoring.MonitoringMain;

public class Sample {
    public static void main(String[] args) throws InterruptedException {
        try {
            MonitoringMain.getInstance();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        MonitoringMain.setSubtasks(0, 1);
        MonitoringMain.setSubtasks(1, 4);
        MonitoringMain.setSubtasks(2, 1);
        MonitoringMain.setSubtasks(3, 1);
        MonitoringMain.setSubtasks(4, 1);
        MonitoringMain.setSubtasks(5, 1);
        MonitoringMain.setSubtasks(6, 1);
        MonitoringMain.setSubtasks(7, 1);
        MonitoringMain.setSubtasks(8, 1);
        MonitoringMain.setSubtasks(9, 1);
        MonitoringMain.setSubtasks(10, 1);
        MonitoringMain.setSubtasks(11, 1);
        MonitoringMain.setSubtasks(12, 1);
        MonitoringMain.setSubtasks(13, 1);
        MonitoringMain.setSubtasks(14, 1);
        MonitoringMain.setSubtasks(15, 1);
        MonitoringMain.setSubtasks(16, 1);
        MonitoringMain.setSubtasks(17, 1);
        MonitoringMain.setSubtasks(18, 1);
        MonitoringMain.setSubtasks(19, 1);
        MonitoringMain.setSubtasks(20, 1);
        MonitoringMain.setSubtasks(21, 1);
        MonitoringMain.setSubtasks(22, 1);
        MonitoringMain.setSubtasks(23, 1);
        MonitoringMain.setSubtasks(24, 1);
        MonitoringMain.setSubtasks(25, 1);
        MonitoringMain.setSubtasks(26, 1);
        MonitoringMain.setSubtasks(27, 1);
        MonitoringMain.setSubtasks(28, 1);
        MonitoringMain.setSubtasks(29, 1);
        MonitoringMain.setSubtasks(30, 1);
        MonitoringMain.setSubtasks(31, 1);
        MonitoringMain.setSubtasks(32, 1);
        MonitoringMain.setSubtasks(33, 1);
        MonitoringMain.updateNext(0, MonitoringMain.Status.DONE);
        MonitoringMain.updateNext(0, MonitoringMain.Status.DONE);
        Thread.sleep(3000);
        MonitoringMain.update(0, 0, MonitoringMain.Status.FAILED);
        MonitoringMain.update(0, 10, MonitoringMain.Status.DONE);
        MonitoringMain.update(2, 10, MonitoringMain.Status.DONE);
    }
}

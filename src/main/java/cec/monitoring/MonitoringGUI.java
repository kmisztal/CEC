package cec.monitoring;

import javafx.animation.AnimationTimer;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.ScrollPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.List;

public class MonitoringGUI extends Application {
    public static final int MAX_WIDTH = 800;
    public static final int MAX_HEIGHT = 1500;
    public static final int ACTUAL_HEIGHT = 700;

    private static List<ArrayList<MonitoringMain.Status>> subs;

    private static Canvas canvas;

    public static void setSubtasks(int task, int subtasks) {
        if (subs == null) {
            subs = new ArrayList<>();
        }
        ArrayList<MonitoringMain.Status> list = new ArrayList<>();
        for (int i = 0; i < subtasks; i++) {
            list.add(i, MonitoringMain.Status.PROGRESS);
        }
        subs.add(task, list);
    }

    public static int getTasks() {
        return subs.size();
    }

    public static int getSubtasks(int task) {
        if (subs.size() > task) {
            return subs.get(task).size();
        }
        System.out.println("Wrong parameter: task: " + task);
        return 0;
    }

    public static void updateNext(int task, MonitoringMain.Status status) {
        int mark = getNextTask(task);
        if (mark != subs.get(task).size()) {
            subs.get(task).set(mark, status);
        } else {
            System.out.println("No more subtasks to mark in this task: " + task);
        }
    }

    public static void update(int task, int subtask, MonitoringMain.Status status) {
        if (subs.size() > task && subs.get(task).size() > subtask) {
            subs.get(task).set(subtask, status);
        } else {
            System.out.println("Wrong parameters: task: " + task + " subtask: " + subtask);
        }
    }


    public static Color getColor(MonitoringMain.Status status) {
        Color color = Color.YELLOW;
        switch (status) {
            case PROGRESS:
                color = Color.YELLOW;
                break;
            case FAILED:
                color = Color.RED;
                break;
            case DONE:
                color = Color.GREENYELLOW;
                break;
        }
        return color;
    }

    public static int getNextTask(int task) {
        int i;
        for (i = 0; i < subs.get(task).size(); i++) {
            if (subs.get(task).get(i).equals(MonitoringMain.Status.PROGRESS)) {
                break;
            }
        }
        return i;
    }

    public static void setCanvas() {
        canvas = new Canvas(MAX_WIDTH, MAX_HEIGHT);
    }

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        DoubleProperty x = new SimpleDoubleProperty();
        DoubleProperty y = new SimpleDoubleProperty();
        primaryStage.setTitle("Monitoring GUI");
        Group root = new Group();
        Timeline timeline = new Timeline(
                new KeyFrame(Duration.seconds(0),
                        new KeyValue(x, 0),
                        new KeyValue(y, 0)
                ),
                new KeyFrame(Duration.seconds(3),
                        new KeyValue(x, 200),
                        new KeyValue(y, 200)
                )
        );
        timeline.setAutoReverse(true);
        timeline.setCycleCount(Timeline.INDEFINITE);
        setCanvas();
        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);
        scrollPane.setPrefSize(MAX_WIDTH, ACTUAL_HEIGHT);
        scrollPane.setContent(canvas);
        AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                GraphicsContext gc = canvas.getGraphicsContext2D();
                drawScene(gc);
            }
        };
        root.getChildren().add(scrollPane);
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
        timer.start();
        timeline.play();
    }


    private static void drawScene(GraphicsContext gc) {
        gc.setFill(Color.BLACK);
        gc.setStroke(Color.BLACK);
        gc.setLineWidth(1);
        int spaceY = 15;
        int y = spaceY;
        for (int i = 0; i < subs.size(); i++) {
            gc.fillText(String.valueOf(i), 10, y);
            gc.strokeLine(30, y, MAX_WIDTH, y);
            y += spaceY;
        }

        y = 0;
        if (subs != null) {
            for (ArrayList<MonitoringMain.Status> sub : subs) {
                int x = 30;
                for (int j = 0; j <= sub.size(); j++) {
                    gc.strokeLine(x, y, x, y + spaceY);
                    x += 7.7;
                }
                y += spaceY;
            }
        }
        gc.setLineWidth(1);
        if (subs != null) {
            for (int k = 0; k < subs.size(); k++) {
                y = 0;
                int x = 30;
                for (int i = 0; i <= k; i++) {
                    y += spaceY;
                }
                for (MonitoringMain.Status status : subs.get(k)) {
                    gc.setFill(getColor(status));
                    gc.setStroke(getColor(status));
                    gc.fillRect(x + 1, y - spaceY, 5.3, spaceY - 1.2);
                    x += 7.7;
                }
            }
        }

    }

}

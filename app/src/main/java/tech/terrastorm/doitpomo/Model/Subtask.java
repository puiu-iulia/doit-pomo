package tech.terrastorm.doitpomo.Model;

public class Subtask {

    private String name;
    private int id;
    private int taskId;
    private int done;

    public Subtask() {
    }

    public int getDone() {
        return done;
    }

    public void setDone(int done) {
        this.done = done;
    }


    public int getTaskId() {
        return taskId;
    }

    public void setTaskId(int taskId) {
        this.taskId = taskId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }



}

package com.edvalld.task_service.task;

import jakarta.persistence.*;

import java.util.UUID;

@Table(name = "tasks")
@Entity
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(updatable = false, nullable = false)
    private UUID id;

    private String title;

    private String task;

    private String user;

    public Task(){}
    public Task(UUID id, String title, String task, String user) {
        this.id = id;
        this.title = title;
        this.task = task;
        this.user = user;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public UUID getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTask() {
        return task;
    }

    public void setTask(String task) {
        this.task = task;
    }
}

package org.example.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.example.DTO.Task;
import java.time.LocalDateTime;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class TaskService {

    private final File file = new File("src/main/resources/Task.json");
    private final ObjectMapper mapper;

    public TaskService() {
        this.mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    }


    public List<Task> listAll() {
        if (!file.exists()) {
            return new ArrayList<>();
        }

        try {
            return mapper.readValue(file, new TypeReference<List<Task>>() {});
        } catch (IOException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    public List<Task> listWithStatus(String status) {
        if (!file.exists()) {
            return new ArrayList<>();
        }

        try {
            List<Task> todo =  mapper.readValue(file, new TypeReference<List<Task>>() {});
            return todo.stream()
                    .filter( t -> t.getStatus().equals(status))
                    .collect(Collectors.toList());
        } catch (IOException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    public void createTasks(List<Task> tasks) {
        try {
            mapper.writerWithDefaultPrettyPrinter().writeValue(file, tasks);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void createTask(String decription) {
        List<Task> tasks = listAll();

        int newId = tasks.stream().mapToInt(Task::getId).max().orElse(0) + 1;

        LocalDateTime now = LocalDateTime.now();

        Task newTask= Task.builder()
                .id(newId)
                .description(decription)
                .status("todo")
                .createdAt(now)
                .updatedAt(now)
                .build();

        tasks.add(newTask);
        createTasks(tasks);
    }

    public void updateTask(Integer id, String description){
        List<Task> tasks = listAll();

        for (Task t : tasks) {
            if (t.getId() == id) {
                t.setDescription(description);
                break;
            }
        }

        createTasks(tasks);
    }

    public void deleteTask(Integer id){
        List<Task> tasks = listAll();
        List<Task> newTask;
        newTask=tasks.stream()
                .filter( t -> t.getId()!=id)
                .collect(Collectors.toList());
        createTasks(newTask);
    }

    public void progressTask(Integer id){
        List<Task> tasks = listAll();

        for (Task t : tasks) {
            if (t.getId() == id) {
                t.setStatus("in_progress");
                break;
            }
        }

        createTasks(tasks);
    }

    public void doneTask(Integer id){
        List<Task> tasks = listAll();

        for (Task t : tasks) {
            if (t.getId() == id) {
                t.setStatus("done");
                break;
            }
        }

        createTasks(tasks);
    }
}

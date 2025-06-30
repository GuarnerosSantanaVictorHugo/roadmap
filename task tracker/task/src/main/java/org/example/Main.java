package org.example;

import org.example.CLI.Console;
import org.example.DTO.Task;
import org.example.service.TaskService;

import java.time.LocalDateTime;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        TaskService service = new TaskService();

        Console console= new Console(service);
        console.start();
    }
}
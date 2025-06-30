package org.example.CLI;

import org.example.DTO.Task;
import org.example.service.TaskService;

import java.util.Scanner;

public class Console {
    private final TaskService service;
    private final Scanner scanner = new Scanner(System.in);

    public Console(TaskService service) {
        this.service = service;
    }

    public void start(){
        System.out.println("Bienvenido al gestor de tareas");
        while (true) {
            System.out.print("> ");
            String input = scanner.nextLine().trim();
            if (input.equalsIgnoreCase("exit")) break;
            if (!input.isBlank()) {
                executeCmd(input);
            }
        }

        System.out.println("Adiós.");
    }

    public void executeCmd(String input) {
        //es list solo
        if(input.equals("list")){
            for (Task task : service.listAll()) {
                System.out.println(task);
            }
        }

        //list con estado
        if(input.startsWith("list ")){
            String status = input.substring(5).trim();
            try{
                for (Task task : service.listWithStatus(status)) {
                    System.out.println(task);
                }
            } catch (IllegalArgumentException e) {
                System.out.println("Estatus invalido. Usa: pending, in_progress, done.");
            }
        }

        //Agregar tarea
        if(input.startsWith("add ")){
            String description = getDsc(input);
            service.createTask(description);
        }

        //elimina tarea
        if(input.startsWith("delete ")){
            Integer id = Integer.valueOf(input.substring(7).trim());
            try{
                service.deleteTask(id);
            } catch (IllegalArgumentException e) {
                System.out.println("Id incorrecto");
            }
        }

        //marca en progreso
        if(input.startsWith("mark-in-progress ")){
            Integer id = Integer.valueOf(input.substring(17).trim());
            try{
                service.progressTask(id);
            } catch (IllegalArgumentException e) {
                System.out.println("Id incorrecto");
            }
        }

        //marca completo
        if(input.startsWith("mark-done ")){
            Integer id = Integer.valueOf(input.substring(10).trim());
            try{
                service.doneTask(id);
            } catch (IllegalArgumentException e) {
                System.out.println("Id incorrecto");
            }
        }

        //actualiza
        if(input.startsWith("update ")) {
            Integer id = Integer.valueOf(input.substring(7,8).trim());
            String description = getDsc(input);
            try{
                service.updateTask(id,description);
            } catch (IllegalArgumentException e) {
                System.out.println("Id incorrecto");
            }
        }
    }

    public String getDsc(String input){
        int start= input.indexOf("\"");
        int end=input.lastIndexOf("\"");
        return input.substring(start+1,end);
    }

}

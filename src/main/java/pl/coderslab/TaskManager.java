package pl.coderslab;

import org.apache.commons.lang3.math.NumberUtils;
import org.apache.commons.validator.GenericValidator;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;

public class TaskManager {

    public static void main(String[] args) {
        runTaskManager();
    }

    public static void runTaskManager() {
        System.out.println(ConsoleColors.GREEN + "Task Manager has been started");
        Scanner scanner = new Scanner(System.in);
        String[] allowedCommands = new String[]{"add", "remove", "list", "exit"};

        while (true) {
            String[][] taskArr = readFile("tasks.csv");
            System.out.println(ConsoleColors.BLUE + "Please select an option: ");
            for (String command : allowedCommands) {
                System.out.println(ConsoleColors.RESET + command);
            }
            String userCommand = scanner.nextLine().toLowerCase();
            System.out.print("You chose ");
            System.out.println(ConsoleColors.BLUE_BOLD + userCommand);

            runCommand(userCommand, taskArr);

            if (userCommand.equals("exit")) {
                break;
            }
        }
        System.out.println(ConsoleColors.RED + "Task Manager has been closed");
    }

    public static void runCommand(String userCommand, String[][] taskArr) {
        switch (userCommand) {
            case "add":
                writeFile("tasks.csv", add(taskArr));
                System.out.println(ConsoleColors.GREEN + "Task was successfully added");
                break;
            case "remove":
                writeFile("tasks.csv", remove(taskArr));
                System.out.println(ConsoleColors.GREEN + "Task was successfully removed");
                break;
            case "list":
                System.out.println(ConsoleColors.GREEN + "Task list");
                list(taskArr);
                break;
            case "exit":
                break;
            default:
                System.out.println(ConsoleColors.RED + "This option is not recognized");
        }
    }

    public static String[][] readFile(String fileName) {
        Path path = Paths.get(fileName);
        StringBuilder tasks = new StringBuilder();
        try {
            for (String line : Files.readAllLines(path)) {
                tasks.append(line).append(";");
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        // create 2D array from StringBuilder (now size is known)
        String[] tempArr = tasks.toString().split(";");
        String[][] taskArr = new String[tempArr.length][3];
        for (int i = 0; i < tempArr.length; i++) {
            String[] oneTask = tempArr[i].split(",");
            for (int j = 0; j < 3; j++) {
                taskArr[i][j] = oneTask[j];
            }
        }
        return taskArr;
    }


    public static void writeFile(String fileName, String[][] taskArr) {
        Path path = Paths.get(fileName);
        try (PrintWriter pw = new PrintWriter(String.valueOf(path))) {
            for (String[] task : taskArr) {
                for (int i = 0; i < task.length; i++) {
                    if (i != (task.length - 1)) {
                        pw.print(task[i] + ",");
                    } else {
                        pw.print(task[i] + "\n");
                    }
                }
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }


    public static String[][] add(String[][] taskArr) {
        Scanner scanner = new Scanner(System.in);
        StringBuilder newTask = new StringBuilder();

        System.out.println(ConsoleColors.BLUE + "Please add task description:");
        newTask.append(scanner.nextLine()).append("; ");

        while (true) {
            System.out.println(ConsoleColors.BLUE + "Please add task due date in a format: yyyy-MM-dd");
            String input = scanner.nextLine();
            if (GenericValidator.isDate(input, "yyyy-MM-dd", true)) {
                newTask.append(input).append("; ");
                break;
            }
            System.out.println(ConsoleColors.RED + "This value is not a valid date");
        }

        while (true) {
            System.out.println(ConsoleColors.BLUE + "Is your task important: true/false");
            String input = scanner.nextLine();
            if (input.equalsIgnoreCase("true") || input.equalsIgnoreCase("false")) {
                newTask.append(input).append("; ");
                break;
            }
            System.out.println(ConsoleColors.RED + "This value is not boolean");
        }

        String[][] newTaskArr = new String[taskArr.length + 1][taskArr[0].length];
        // copy to a bigger array
        for (int i = 0; i < Math.min(taskArr.length, taskArr.length + 1); i++) {
            for (int j = 0; j < Math.min(taskArr[0].length, taskArr[0].length); j++) {
                newTaskArr[i][j] = taskArr[i][j];
            }
        }
        // add the new task at the end
        String[] tempArr = newTask.toString().split(";");
        for (int j = 0; j < newTaskArr[0].length; j++) {
            newTaskArr[newTaskArr.length - 1][j] = tempArr[j];
        }
        return newTaskArr;
    }


    public static String[][] remove(String[][] taskArr) {
        Scanner scanner = new Scanner(System.in);
        int index = -1;
        while (true) {
            System.out.println(ConsoleColors.BLUE + "Please select a number to remove: ");
            String strIndex = scanner.nextLine();

            if (!NumberUtils.isParsable(strIndex)) {
                System.out.println(ConsoleColors.RED + "Please provide a number");
                continue;
            }

            if (Integer.parseInt(strIndex) < 0 || Integer.parseInt(strIndex) >= taskArr.length) {
                System.out.println(ConsoleColors.RED + "Task does not exist");
                continue;
            }
            index = Integer.parseInt(strIndex);
            break;
        }
        String[][] newTaskArr = new String[taskArr.length - 1][taskArr[0].length];
        int newIndex = 0;
        for (int i = 0; i < taskArr.length; i++) {
            if (i != index) {
                for (int j = 0; j < taskArr[0].length; j++) {
                    newTaskArr[newIndex][j] = taskArr[i][j];
                }
                newIndex++;
            }
        }
        return newTaskArr;
    }


    public static void list(String[][] taskArr) {
        for (int i = 0; i < taskArr.length; i++) {
            System.out.print(ConsoleColors.RESET + i + " : ");
            for (int j = 0; j < taskArr[i].length; j++) {
                if (j != (taskArr[i].length - 1)) {
                    System.out.print(taskArr[i][j] + " ");
                } else {
                    System.out.print(taskArr[i][j] + "\n");
                }
            }
        }
    }


}
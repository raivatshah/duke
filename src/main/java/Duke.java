import javax.swing.*;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.Scanner;
import java.util.ArrayList;
import java.io.FileWriter;
import java.io.File;

public class Duke {

    private Storage storage;
    private TaskList tasks;
    private Ui ui;
    private Parser parser;

    private ArrayList<Task> list;
    public Duke() {
        this.list = new ArrayList<>();
    }

    public Duke(String filePath) {
        this.ui = new Ui();
        this.storage = new Storage(filePath);
        this.parser = new Parser();
        try {
            tasks = new TaskList(storage.getPreviousTasks(filePath));
        } catch (FileNotFoundException e) {
            tasks = new TaskList();
        }
    }

    public void run() {
        ui.welcomeMessage();
        while(true) {
            String input = ui.getInput();
            if(input.equalsIgnoreCase("bye")) {
                ui.byeMessage();
                try {
                    storage.fillFileWithTasks(tasks.getTaskList());
                } catch (IOException e) {
                    ui.promptUser("Error saving to file. Please if 'data.txt' is present in '/data/");
                }
            } else if (input.startsWith("done")) {
                int index = Integer.parseInt(parser.parse(input, 2)[1]); //accept second argument from command line
                tasks.markDone(index);
            } else if (input.startsWith("delete")) {
                int index = Integer.parseInt(parser.parse(input, 2)[1]); //accept second argument from command line
                tasks.deleteTaskByIndex(index);
            } else if (input.equalsIgnoreCase("list")) {
                tasks.printList();
            } else {
                String[] parsedInput = parser.parse(input, 2);
                if(input.startsWith("todo") || input.startsWith("deadline") || input.startsWith("event")) {
                    try {
                        tasks.addTask(parsedInput[0], parsedInput[1]);
                    } catch (ArrayIndexOutOfBoundsException e) {
                        Ui.promptUser("OOPS, task description cannot be empty");
                    }
                } else {
                    Ui.promptUser("OOPS, I don't understand this input. Please use a known command and try again");
                }
            }
        }
    }

    public static void main(String[] args) {
        Duke duke = new Duke("data/data.txt");
        duke.run();
    }
}

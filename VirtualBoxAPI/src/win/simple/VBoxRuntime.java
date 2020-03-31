package win.simple;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;

@SuppressWarnings("all")
public class VBoxRuntime {

    private String command;
    private Process process;

    public VBoxRuntime(String command) {
        this.command = command;
    }

    public void exec() {
        Runtime runtime = Runtime.getRuntime();
        try {
            process = runtime.exec(new String[]{"cmd", "/c", command});
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Process getProcess() {
        return process;
    }
}

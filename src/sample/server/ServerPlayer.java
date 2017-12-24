package sample.server;

import javafx.util.Pair;
import sample.Helper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.SocketException;

public class ServerPlayer {
    private Socket socket;
    private BufferedReader in;
    private PrintWriter out;
    private String name;

    public ServerPlayer(Socket socket) {
        this.socket = socket;
        try {
            in = new BufferedReader(
                    new InputStreamReader(
                            socket.getInputStream()
                    )
            );

            out = new PrintWriter(socket.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getName() throws IOException, SocketException {
        if (name == null)
            name = in.readLine();
        return name;
    }

    public void sendName(String name) throws SocketException {
        out.println(name);
        out.flush();
    }

    public void sendQuestion(int order, CustomPair pair) throws IOException, NumberFormatException {
        System.out.println("sending to " + name + " order = " + order);
        out.println(order);
        out.flush();
        //return new Pair<>(Integer.parseInt(in.readLine()), Long.parseLong(in.readLine()));
        pair.setFirst(Integer.parseInt(in.readLine()));
        pair.setSecond(Long.parseLong(in.readLine()));

        System.out.println(name + " recieved answer");
    }

    public void sendAnswer(boolean win, int oppenentAnswer) throws SocketException {
        System.out.println("sending to " + name);
        out.println(win ? 1 : 0);
        out.println(oppenentAnswer);
        out.flush();
        System.out.println("send to " + name);
    }

    public Socket getSocket() {
        return socket;
    }

    public void setIn(BufferedReader in) {
        this.in = in;
    }

    public void setOut(PrintWriter out) {
        this.out = out;
    }

    public void sendCloseMessage() {
        out.println(Helper.CLOSING_MESSAGE);
        out.flush();
    }
}

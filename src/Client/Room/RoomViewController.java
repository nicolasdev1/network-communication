package Client.Room;

import Client.Model.Data;
import Client.Model.Message;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class RoomViewController {
    public static Thread thread;

    Socket socket;

    DataOutputStream dataOutputStream;

    DataInputStream dataInputStream;

    @FXML
    public TextField myMessage;

    @FXML
    public TextArea chatLog;

    public RoomViewController() {
        try {
            socket = new Socket(Data.ip, Data.port);
            dataOutputStream = new DataOutputStream(socket.getOutputStream());
            dataInputStream = new DataInputStream(socket.getInputStream());

            dataOutputStream.writeUTF(Data.name);
            /*
             * This Thread let the client recieve the message from the server for any time;
             */
            thread = new Thread(() -> {
                try {

                    JSONParser parser = new JSONParser();

                    while(true) {
                        String newMsgJson = dataInputStream.readUTF();

                        System.out.println("RE : " + newMsgJson);
                        Message newMsg = new Message();

                        Object obj = parser.parse(newMsgJson);
                        JSONObject msg = (JSONObject) obj;

                        newMsg.setName((String) msg.get("name"));
                        newMsg.setMessage((String) msg.get("message"));

                        chatLog.appendText(newMsg.getName() + " : " + newMsg.getMessage() + "\n");
                    }
                } catch(Exception E) {
                    E.printStackTrace();
                }

            });

            thread.start();

        } catch(IOException E) {
            E.printStackTrace();
        }

    }

    public void onClickSend() {
        try {
            String msg = myMessage.getText();

            //String json = "{" + " 'name' : '" + data.name + "', 'message' : '" + msg + "'" + "}";

            JSONObject js = new JSONObject();
            js.put("name", Data.name);
            js.put("message", msg);

            String json = js.toJSONString();


            System.out.println(json);

            dataOutputStream.writeUTF(json);
            myMessage.setText("");
            myMessage.requestFocus();

        } catch(IOException E) {
            E.printStackTrace();
        }

    }

    public void buttonPressed(KeyEvent e) {
        if(e.getCode().toString().equals("ENTER"))
        {
            onClickSend();
        }
    }
}

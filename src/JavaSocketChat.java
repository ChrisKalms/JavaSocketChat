import java.net.Socket;
import java.util.ArrayList;
import java.io.IOException;
import java.io.DataInputStream;
import java.io.DataOutputStream;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.ListView;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.layout.Priority;

import javafx.concurrent.Task;

public class JavaSocketChat extends Application{		
	Socket socket;
	DataInputStream dis;
	DataOutputStream dos;	

	@Override
	public void start(Stage stage) throws Exception{
		socket = new Socket("localhost", 1234);
		dis = new DataInputStream(socket.getInputStream());
		dos = new DataOutputStream(socket.getOutputStream());
		
		VBox vb = new VBox();
		Button b = new Button("Send");
		TextField tf = new TextField("Type Here to send message and click button to send message");
		Label lvHeader = new Label("Replies");
		final ListView<String> lv = new ListView<String>();
		
		VBox.setVgrow(lv, Priority.ALWAYS);
		
		b.setOnAction(event -> {
			String tfString = tf.getText();
			lv.getItems().add("Sent: " + tfString);
			try{
				dos.writeBytes(tfString); //content
				dos.write(13);//carriage return
				dos.write(10); //line feed
				dos.flush(); //flush the output stream
			} catch(IOException e) {
				
			}
		});
		
		
		new Thread(){
			@Override
			public void run(){
				while(true){					
					try{
						final int MAX = 250;
						Byte inByte;
						byte servBytes[] = new byte[MAX];						
						if(dis.available() > 0){
							int counter = 0;
							while((inByte = dis.readByte()) != '\n' && counter < MAX){
								servBytes[counter] = inByte;
								counter++;
							}
							String bytesToString = new String(servBytes);
							Platform.runLater(() -> {
								lv.getItems().add("Recieved: " + bytesToString.trim());
							});
						}
					} catch(IOException e){

					}
					
				}
			}
		}.start();
		
		
		vb.getChildren().addAll(b, tf, lvHeader,lv);
		Scene scene = new Scene(vb, 400,400);
		stage.setScene(scene);
		stage.show();
	}
	
	public static void main(String[] args){		
		launch(args);
	}

}

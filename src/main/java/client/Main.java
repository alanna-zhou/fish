package client;

import java.net.URL;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * The Main class is where the critter world simulation can be launched and run.
 *
 */
public class Main extends Application {
	public static void main (String [] args) {
		launch(args); 
	}

	/**
	 * Starts the application.
	 */
	@Override
	public void start(Stage stage) throws Exception {
		try {
			URL r = getClass().getResource("fish.fxml");
			if (r == null)
				throw new Exception("No FXML resource found.");
			Scene scene = new Scene(FXMLLoader.load(r));
			stage.setTitle("Critter World");
			stage.setScene(scene);
			stage.sizeToScene();
			stage.show();

		} catch (Exception e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
	}
}

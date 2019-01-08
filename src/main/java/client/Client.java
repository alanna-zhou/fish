package client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Optional;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.Pane;
import javafx.util.Duration;

import com.google.gson.Gson;

import bundles.Bundle;
import bundles.LoginBundle;
import bundles.SessionIDBundle;

public class Client {
	private URL url;
	private final Gson gson = new Gson();
	int sessionID;

	@FXML
	private Button teammate2Button;

	@FXML
	private Button createRoomButton;

	@FXML
	private Button serverURLButton;

	@FXML
	private Label playerNameLabel;

	@FXML
	private Button opponent1Button;

	@FXML
	private Button opponent3Button;

	@FXML
	private Label scoreLabel;

	@FXML
	private Button opponent2Button;

	@FXML
	private Button teammate1Button;

	@FXML
	private Pane pane;

	@FXML
	private TextField serverURLTextField;

	@FXML
	private Button joinRoomButton;

	@FXML
	private Label playerHandLabel;

	@FXML
	private Button declareButton;

	@FXML
	private Button completedSetsButton;

	@FXML
	void handleKey(ActionEvent event) {

	}

	/**
	 * Returns a BufferedReader from the specified URL through GET request.
	 */
	private BufferedReader getReaderFromGET(URL url) {
		try {
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.connect();
			handleErrorResponseCodes(connection);
			BufferedReader r = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			return r;
		} catch (IOException e) {
			System.err.println("IO exception: " + e.getMessage());
		}
		return null;
	}

	/**
	 * Returns a BufferedReader from the specified URL through POST request.
	 */
	private BufferedReader getReaderFromPOST(URL url, Bundle request) {
		try {
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.setDoOutput(true); // send a POST
			connection.setRequestMethod("POST");
			connection.setRequestProperty("Content-Type", "application/json");
			PrintWriter w = new PrintWriter(connection.getOutputStream());
			w.println(gson.toJson(request));
			w.flush();
			handleErrorResponseCodes(connection);
			BufferedReader r = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			return r;
		} catch (IOException e) {
			System.err.println("IO exception: " + e.getMessage());
		}
		return null;
	}

	/**
	 * Returns the string sent by the server from a specified URL and bundle.
	 */
	private String getStringFromPOST(URL url, Bundle request) {
		String response = "";
		try {
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.setDoOutput(true); // send a POST
			connection.setRequestMethod("POST");
			connection.setRequestProperty("Content-Type", "application/json");
			PrintWriter w = new PrintWriter(connection.getOutputStream());
			w.println(gson.toJson(request));
			w.flush();
			handleErrorResponseCodes(connection);
			BufferedReader r = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			String json_response = "";
			String text = "";
			while ((text = r.readLine()) != null) {
				json_response += text;
			}
			for (String l = r.readLine(); l != null; l = r.readLine()) {
				response += l;
			}
			return response;
		} catch (IOException e) {
			System.err.println("IO exception: " + e.getMessage());
		}
		return response;

	}

	/**
	 * Handles errors based on response code.
	 */
	private void handleErrorResponseCodes(HttpURLConnection connection) {
		try {
			if (connection.getResponseCode() == 200 || connection.getResponseCode() == 201) {
				return;
			}
			/**
			 * BufferedReader in = new BufferedReader(new
			 * InputStreamReader(urlc.getInputStream())); String line = null; while((line =
			 * in.readLine()) != null) { System.out.println(line); }
			 */
			System.out.println("printing out error stream: ");
			BufferedReader in = new BufferedReader(new InputStreamReader(connection.getErrorStream()));
			String line = null;
			while ((line = in.readLine()) != null) {
				System.out.println(line);
			}
//			InputStream e = connection.getErrorStream();

			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Error alert");
			alert.setHeaderText(connection.getResponseCode() + " " + connection.getResponseMessage());
			alert.show();

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	/**
	 * When user enters a server base url
	 */
	@SuppressWarnings("unused")
	@FXML
	private void handleURLButtonClick() {
		boolean needToRestart = false;
		if (this.url != null) {
			needToRestart = true;
		}
		try {
			String strippedURL = "";
			String urlText = serverURLTextField.getText();
			if (urlText.substring(urlText.length() - 1, urlText.length()).equals("/")) {
				strippedURL = urlText.substring(0, urlText.length() - 1);
				urlText = strippedURL;
			}
			url = new URL(urlText);
			URL urlWithParams = null;
			try {
				urlWithParams = new URL(url + "/login");
			} catch (MalformedURLException e1) {
				e1.printStackTrace();
			}
			String[] loginInfo = openLogin();
			if (loginInfo[0] == null || loginInfo[1] == null) {
				Alert alert = new Alert(AlertType.ERROR);
				alert.setTitle("Error alert");
				alert.setHeaderText("Could not authenticate");
				alert.showAndWait();
				return;
			}
			LoginBundle loginBundle = new LoginBundle(loginInfo[0], loginInfo[1]);

			SessionIDBundle sessionIDBundle = new SessionIDBundle();
			BufferedReader br = getReaderFromPOST(urlWithParams, loginBundle);
			if (br == null) {
				return;
			}
			try {
				sessionIDBundle = gson.fromJson(br, SessionIDBundle.class);

			} catch (Exception e) {
				Alert alert = new Alert(AlertType.ERROR);
				alert.setTitle("Error alert");
				alert.setHeaderText("Could not get a valid server response");
				alert.showAndWait();
				return;
			}

			if (sessionIDBundle != null) {
				sessionID = sessionIDBundle.session_id;
			} else {
				Alert alert = new Alert(AlertType.ERROR);
				alert.setTitle("Error alert");
				alert.setHeaderText("Could not get a valid server response");
				alert.showAndWait();
				return;

			}
		} catch (MalformedURLException e) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Error alert");
			alert.setHeaderText("Could not set server base URL");
			alert.setContentText("Please enter a valid server base URL");
			alert.showAndWait();
			return;
		}
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("Success");
		alert.setHeaderText("Server base URL was set");
		alert.setContentText(serverURLTextField.getText() + " was set as the server base URL.");
		alert.showAndWait();

		init();

	}

	/**
	 * Starts the game.
	 */
	private void init() {

	}

	/**
	 * Returns a string array with the 0th index as the level and the 1st index as
	 * the password.
	 */
	private String[] openLogin() {
		TextInputDialog levelDialog = new TextInputDialog("admin");
		levelDialog.setTitle("Authentication:");
		levelDialog.setHeaderText("Enter level:");
		String[] loginInfo = new String[2];
		Optional<String> result = levelDialog.showAndWait();
		if (result != null) {
			try {
				loginInfo[0] = result.get();

			} catch (Exception e) {
				loginInfo[0] = "";
			}

		}
		TextInputDialog passwordDialog = new TextInputDialog("gandalf");
		passwordDialog.setTitle("Authentication:");
		passwordDialog.setHeaderText("Enter password:");
		result = passwordDialog.showAndWait();
		if (result != null) {
			try {
				loginInfo[1] = result.get();

			} catch (Exception e) {
				loginInfo[1] = "";
			}
		}
		return loginInfo;
	}

}

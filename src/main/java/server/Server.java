package server;

import static spark.Spark.port;
import static spark.Spark.post;

import java.util.HashMap;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import com.google.gson.Gson;

import bundles.LoginBundle;
import bundles.SessionIDBundle;

public class Server {
	int session_id;
	ReentrantReadWriteLock lock = new ReentrantReadWriteLock();
	String pass = "alanna";

	public static void main(String[] args) {
		new Server().run(8080, "alanna");
	}

	/**
	 * Runs the server
	 * 
	 * @param portNum
	 * @param read
	 * @param write
	 * @param admin
	 */
	public void run(int portNum, String p) {
		// Make the server run on specified port
		port(portNum);

		// Create a new GSON converter
		Gson gson = new Gson();
		session_id = 1;
//		sim = new WorldSimulation(lock);
		/**
		 * Post request for logging in which returns the session id
		 */
		post("/login", (request, response) -> {
			lock.writeLock().lock();
			try {
				String json = request.body();
				LoginBundle login = gson.fromJson(json, LoginBundle.class);
				String password = login.password;
				if (pass.equals(password)) {
					response.status(200);
					SessionIDBundle sessionIDBundle = new SessionIDBundle(session_id++);
					return sessionIDBundle;
				} else {
					response.status(401);
					return "Password is incorrect";
				}
			} finally {
				lock.writeLock().unlock();
			}
		}, gson::toJson);
	}
}
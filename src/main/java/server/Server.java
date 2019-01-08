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
	HashMap<String, String> passHash;
	HashMap<Integer, String> levelHash;
	ReentrantReadWriteLock lock = new ReentrantReadWriteLock();

	public static void main(String[] args) {
		new Server().run(8080, "bilbo", "frodo", "gandalf");
	}

	/**
	 * Runs the server
	 * 
	 * @param portNum
	 * @param read
	 * @param write
	 * @param admin
	 */
	public void run(int portNum, String read, String write, String admin) {
		// Make the server run on specified port
		port(portNum);

		// Create a new GSON converter
		Gson gson = new Gson();
		session_id = 1;
		passHash = new HashMap<String, String>();
		levelHash = new HashMap<Integer, String>();
		passHash.put("read", read);
		passHash.put("write", write);
		passHash.put("admin", admin);
//		sim = new WorldSimulation(lock);
		/**
		 * Post request for logging in which returns the session id
		 */
		post("/login", (request, response) -> {
			lock.writeLock().lock();
			try {
				String json = request.body();
				LoginBundle login = gson.fromJson(json, LoginBundle.class);
				String level = login.level;
				String password = login.password;
				if (passHash.get(level).equals(password)) {
					levelHash.put(session_id, login.level);
					response.status(200);
					SessionIDBundle sessionIDBundle = new SessionIDBundle(session_id++);
					return sessionIDBundle;
				} else {
					response.status(401);
					return "Username and password do not match";
				}
			} finally {
				lock.writeLock().unlock();
			}
		}, gson::toJson);
	}
}
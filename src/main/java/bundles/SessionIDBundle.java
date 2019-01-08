package bundles;

/**
 * Server response to logging into the server.
 */
public class SessionIDBundle extends Bundle {
	public Integer session_id;

	public SessionIDBundle() {

	}

	public SessionIDBundle(int s) {
		session_id = s;
	}

	public String toString() {
		return "session_id: " + session_id;
	}

}

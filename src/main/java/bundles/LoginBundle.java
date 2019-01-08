package bundles;

/**
 * Client request body to log into the server.
 *
 */
public class LoginBundle extends Bundle {
	public String level, password;

	public LoginBundle(String l, String p) {
		level = l;
		password = p;
	}

	public String toString() {
		return "level: " + level + "password: " + password;
	}

}

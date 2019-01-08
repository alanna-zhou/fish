package bundles;

/**
 * Client request body to log into the server.
 *
 */
public class LoginBundle extends Bundle {
	public String password;

	public LoginBundle(String p) {
		password = p;
	}

	public String toString() {
		return "password: " + password;
	}

}

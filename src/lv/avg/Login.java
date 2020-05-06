package lv.avg;

import java.time.*;

public final class Login {
	private final String name;
	private final String klase;
	private final LocalDateTime timestamp = LocalDateTime.now();

	private final boolean anonymous;

	private static final Login ANON = new Login();

	public static Login anonymous() {
		return ANON;
	}

	public Login(String name, String klase) {
		this.name = name;
		this.klase = klase;
		anonymous = false;
	}

	private Login() {
		name = "";
		klase = "";
		anonymous = true;
	}

	@Override
	public String toString() {
		return "Login(\"" + name + "\", \"" + klase + "\")";
	}

	public String name() {
		if(anonymous)
			throw new UnsupportedOperationException("this login has no name");
		return name;
	}

	public String klase() {
		if(anonymous)
			throw new UnsupportedOperationException("this login has no klase");
		return klase;
	}

	public LocalDateTime timestamp() {
		if(anonymous)
			throw new UnsupportedOperationException("this login has no timestamp");
		return timestamp;
	}
}

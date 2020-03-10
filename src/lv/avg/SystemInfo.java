package lv.avg;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;

class SystemInfo {
	@NotNull
	private final String javaVersion, javaFXVersion;

	SystemInfo() {
		this(System.getProperty("java.version"), System.getProperty("javafx.version"));
	}

	SystemInfo(@NotNull String javaVersion, @NotNull String javaFXVersion) {
		Objects.requireNonNull(this.javaVersion = javaVersion, "Java version");
		Objects.requireNonNull(this.javaFXVersion = javaFXVersion, "JavaFX version");
	}

	public String javaFXVersion() {
		return javaFXVersion;
	}

	public String javaVersion() {
		return javaVersion;
	}
}

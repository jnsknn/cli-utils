package fi.jnsknn.cliutils.settings;

import java.util.Random;

public final class Constants {

	// Random
	public static final Random R = new Random();

	// Arrays containing vowels and consonants
	private static final char[] VOWELS = { 'a', 'e', 'i', 'o', 'u', 'y', 'ä', 'ö', 'å' };
	private static final char[] CONSONANTS = { 'b', 'c', 'd', 'f', 'g', 'h', 'j', 'k', 'l', 'm', 'n', 'p', 'q', 'r',
			's', 't', 'v', 'w', 'x', 'z' };

	public static final String LOCAL_SENDER = "CliUtils";
	public static final String LOCAL_CHANNEL = "Local";

	private Constants() {
		throw new IllegalStateException("Utility Class");
	}

	/**
	 * @return {@link #VOWELS#clone()}
	 **/
	public static char[] getVowels() {
		return VOWELS.clone();
	}

	/**
	 * @return {@link #CONSONANTS#clone()}
	 **/
	public static char[] getConsonants() {
		return CONSONANTS.clone();
	}
}

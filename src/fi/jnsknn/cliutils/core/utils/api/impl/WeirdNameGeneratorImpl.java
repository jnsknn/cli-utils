package fi.jnsknn.cliutils.core.utils.api.impl;

import fi.jnsknn.cliutils.core.utils.api.WeirdName;
import fi.jnsknn.cliutils.core.utils.api.WeirdNameGenerator;
import fi.jnsknn.cliutils.settings.Constants;

public class WeirdNameGeneratorImpl implements WeirdNameGenerator {

	private static final char[] VOWELS = Constants.getVowels();
	private static final char[] CONSONANTS = Constants.getConsonants();

	// Newly created name will go to this variable
	WeirdName weirdName = null;

	// Constructor for creating a weird name
	public WeirdNameGeneratorImpl(String name) throws IllegalStateException {
		if (name == null || name.isEmpty()) {
			throw new IllegalStateException("Parameter name cannot be null or empty!");
		}
		this.weirdName = new WeirdName(name, name, name);
	}

	// This method tries to fix "ugly" character combinations or currently
	// unsupported characters. For future development!
	private void fixName() {

		// Convert first character of a generated name to upper case character
		String[] generatedNames = weirdName.getGeneratedName().split(" ");

		StringBuilder sbTemp = new StringBuilder();

		for (int i = 0; i < generatedNames.length; i++) {
			sbTemp.append(generatedNames[i].substring(0, 1).toUpperCase()).append(generatedNames[i].substring(1))
					.append(" ");
		}

		String fixedname = sbTemp.toString();

		fixedname = fixedname.replace("kc", "ck");
		fixedname = fixedname.replace("å", "o");
		fixedname = fixedname.replace("ä", "a");
		fixedname = fixedname.replace("ö", "o");

		weirdName.setFixedName(fixedname.trim());
	}

	/**
	 * @return newly generated {@link #weirdName}
	 **/
	@Override
	public WeirdName generate() {

		// Convert all characters in string to lower case
		// for comparing purposes
		final String originalName = weirdName.getOriginalName().toLowerCase();
		StringBuilder generatedNameBuilder = new StringBuilder("");

		// Boolean variables for checking
		boolean isRoundSkip = false;

		// Loop for exploring every character in given name string and replacing them
		// with random ones
		for (int i = 0; i < originalName.length(); i++) {

			if (isRoundSkip) {
				isRoundSkip = false;
				continue;
			}

			// If name string has two exactly same characters next to each other
			if (hasSubsequentEqualCharacters(originalName, i)) {

				// If characters are something else than recognized character
				if (!isGenerateSubsequentCharacters(originalName, generatedNameBuilder, i, VOWELS)
						&& !isGenerateSubsequentCharacters(originalName, generatedNameBuilder, i, CONSONANTS)) {
					generatedNameBuilder.append(originalName.substring(i, i + 2));
				}

				// Generated two characters in one round so need to skip one round
				isRoundSkip = true;

			} else // If character is something else than recognized character
			if (!isGenerateCharacter(originalName, generatedNameBuilder, i, VOWELS)
					&& !isGenerateCharacter(originalName, generatedNameBuilder, i, CONSONANTS)) {
				generatedNameBuilder.append(originalName.substring(i, i + 1));
			}
		}

		weirdName.setGeneratedName(generatedNameBuilder.toString().trim());

		fixName();

		return weirdName;
	}

	private boolean hasSubsequentEqualCharacters(final String originalName, int i) {
		return i < originalName.length() - 1 && (originalName.charAt(i) == originalName.charAt(i + 1));
	}

	private boolean isGenerateCharacter(final String originalName, StringBuilder generatedNameBuilder, int i,
			char[] characters) {
		for (int j = 0; j < characters.length; j++) {
			if (originalName.charAt(i) == characters[j]) {
				String randomStr = String.valueOf(characters[Constants.R.nextInt(characters.length)]);
				generatedNameBuilder.append(randomStr);
				return true;
			}
		}
		return false;
	}

	private boolean isGenerateSubsequentCharacters(final String originalName, StringBuilder generatedNameBuilder, int i,
			char[] characters) {
		for (int j = 0; j < characters.length; j++) {
			if (originalName.charAt(i) == characters[j]) {
				String randomStr = String.valueOf(characters[Constants.R.nextInt(characters.length)]);
				generatedNameBuilder.append(randomStr).append(randomStr);
				return true;
			}
		}
		return false;
	}

}

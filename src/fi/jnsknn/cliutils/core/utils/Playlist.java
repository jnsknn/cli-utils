package fi.jnsknn.cliutils.core.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * This class handles playlist of youtube videos
 **/
public class Playlist {

	private static final String PLAYLIST_URL_BASE = "https://www.youtube.com/watch_videos?video_ids=";
	private static final String YOUTUBE_ID_PATTERN = "https?:\\/\\/(?:[0-9A-Z-]+\\.)?(?:youtu\\.be\\/|youtube\\.com\\S*[^\\w\\-\\s])([\\w\\-]{11})(?=[^\\w\\-]|$)(?![?=&+%\\w]*(?:['\"][^<>]*>|<\\/a>))[?=&+%\\w]*";

	private static Playlist instance;
	private static List<String> pl = new ArrayList<>();

	private Playlist() {
	}

	public static Playlist getInstance() {
		if (instance == null) {
			instance = new Playlist();
		}
		return instance;
	}

	public List<String> getPlaylist() {
		return pl;
	}

	public String getPlaylistURL() {

		if (pl != null && !pl.isEmpty()) {
			StringBuilder sb = new StringBuilder(PLAYLIST_URL_BASE);

			for (String item : pl) {
				if (item != null) {
					sb.append(item + ",");
				}
			}

			return sb.substring(0, sb.length() - 1);
		}

		return "Add youtube videos to playlist by posting links on chat!";
	}

	public void addPlaylist(String url) {
		Pattern pattern = Pattern.compile(YOUTUBE_ID_PATTERN, Pattern.CASE_INSENSITIVE);
		Matcher matcher = pattern.matcher(url);
		if (matcher.matches()) {
			pl.add(matcher.group(1));
		}
	}

	public void clearPlaylist() {
		pl.clear();
	}
}

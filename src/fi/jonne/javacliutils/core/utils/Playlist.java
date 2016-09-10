package fi.jonne.javacliutils.core.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * This class handles playlist of youtube videos
 * **/
public class Playlist {
	
	private static final String PLAYLIST_URL_BASE = "https://www.youtube.com/watch_videos?video_ids=";
	public static final String YOUTUBE_ID_PATTERN = "https?:\\/\\/(?:[0-9A-Z-]+\\.)?(?:youtu\\.be\\/|youtube\\.com\\S*[^\\w\\-\\s])([\\w\\-]{11})(?=[^\\w\\-]|$)(?![?=&+%\\w]*(?:['\"][^<>]*>|<\\/a>))[?=&+%\\w]*";
	
	private static Playlist instance;
	private static List<String> playlist = new ArrayList<String>();
	
	public Playlist(){}
	
	public static Playlist getInstance(){
		if(instance == null){
			instance = new Playlist();
		}
		return instance;
	}
	
	public List<String> getPlaylist(){
		return playlist;
	}
	
	public String getPlaylistURL(){
		
		if(playlist.size() > 0 && playlist != null){			
			String url = PLAYLIST_URL_BASE;
			
			for(String item : playlist){
				if(item != null){
					url += item + ",";				
				}
			}
			
			url = url.substring(0, url.length()-1);
			
			return url;
		}
		
		return "Add youtube videos to playlist by posting links on chat!";
	}
	
	public void addPlaylist(String url){
	    Pattern pattern = Pattern.compile(YOUTUBE_ID_PATTERN, Pattern.CASE_INSENSITIVE);
	    Matcher matcher = pattern.matcher(url);
	    if (matcher.matches()){
	        playlist.add(matcher.group(1));
	    }
	}
	
	public void clearPlaylist(){
		playlist.clear();
	}
}

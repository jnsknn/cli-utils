package fi.jnsknn.cliutils.core.utils;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import fi.jnsknn.cliutils.core.Communicator;
import fi.jnsknn.cliutils.settings.Constants;
import fi.jnsknn.cliutils.settings.Settings;

/**
 * This class handles all TimerInfo objects
 **/
public class TimerInfoContainer {

	private static TimerInfoContainer instance;
	private Map<Integer, TimerInfo> timerInfos;
	private AtomicInteger nextId;

	private TimerInfoContainer(Map<Integer, TimerInfo> timerInfos, AtomicInteger nextId) {
		this.timerInfos = timerInfos;
		this.nextId = nextId;
	}

	public static TimerInfoContainer getInstance() {
		if (instance == null) {
			instance = new TimerInfoContainer(new HashMap<>(), new AtomicInteger(0));
		}
		return instance;
	}

	public void setTimer(TimerInfo timerInfo) {
		timerInfos.put(timerInfo.getId(), timerInfo);
		saveTimers();
	}

	public TimerInfo getTimer(int id) {
		return timerInfos.get(id);
	}

	public int getNextId() {
		return nextId.incrementAndGet();
	}

	public void setNextId(int id) {
		nextId.set(id);
	}

	public Map<Integer, TimerInfo> getTimers() {
		return timerInfos;
	}

	public void removeTimer(TimerInfo timer) {
		timerInfos.remove(timer.getId());
		timer.getTimer().cancel();
		saveTimers();
	}

	public boolean isTimerExist(int id) {
		return timerInfos.get(id) != null;
	}

	@SuppressWarnings("unchecked")
	public void saveTimers() {
		JSONObject objRoot = new JSONObject();

		JSONArray objArr = new JSONArray();

		for (Map.Entry<Integer, TimerInfo> timer : timerInfos.entrySet()) {

			JSONObject obj = new JSONObject();

			obj.put("id", String.valueOf(timer.getValue().getId()));
			obj.put("timeStampStart", String.valueOf(timer.getValue().getTimeStampStart()));
			obj.put("timeStampEnd", String.valueOf(timer.getValue().getTimeStampEnd()));
			obj.put("name", String.valueOf(timer.getValue().getName()));
			obj.put("owner", String.valueOf(timer.getValue().getOwner()));
			obj.put("channel", String.valueOf(timer.getValue().getChannel()));
			obj.put("isTimerRepeating", String.valueOf(timer.getValue().isTimerRepeating()));

			objArr.add(obj);
		}

		objRoot.put("timerInfos", objArr);

		try (FileWriter file = new FileWriter("timerinfos.json")) {
			file.write(objRoot.toJSONString());
			file.flush();
		} catch (IOException e) {
			Communicator.getInstance().handleError(Constants.LOCAL_CHANNEL, Settings.getCurrentLocalSender(),
					"saveTimers error: " + e.getMessage());
		}
	}

	@SuppressWarnings("unchecked")
	public void initializeTimers() {
		Communicator.getInstance().handleOutput(Constants.LOCAL_CHANNEL, Settings.getCurrentLocalSender(),
				"Initializing timers...");
		JSONParser parser = new JSONParser();

		try {

			Object obj = parser.parse(new FileReader("timerinfos.json"));

			JSONObject jsonObject = (JSONObject) obj;

			JSONArray timerObjects = (JSONArray) jsonObject.get("timerInfos");
			Iterator<JSONObject> iterator = timerObjects.iterator();
			while (iterator.hasNext()) {
				JSONObject objNext = iterator.next();

				Integer id = Integer.valueOf((String) objNext.get("id"));
				Long timeStampStart = Long.valueOf((String) objNext.get("timeStampStart"));
				Long timeStampEnd = Long.valueOf((String) objNext.get("timeStampEnd"));
				String name = (String) objNext.get("name");
				String owner = (String) objNext.get("owner");
				String channel = (String) objNext.get("channel");
				Boolean isTimerRepeating = Boolean.valueOf((String) objNext.get("isTimerRepeating"));

				new TimerInfo(id, timeStampStart, timeStampEnd, name, owner, channel, isTimerRepeating);

				nextId.set(id);
			}
			Communicator.getInstance().handleOutput(Constants.LOCAL_CHANNEL, Settings.getCurrentLocalSender(), "[OK]");
		} catch (FileNotFoundException e) {
			Communicator.getInstance().handleError(Constants.LOCAL_CHANNEL, Settings.getCurrentLocalSender(),
					"initializeTimers error: " + e.getMessage());
			Communicator.getInstance().handleError(Constants.LOCAL_CHANNEL, Settings.getCurrentLocalSender(),
					"Writing timerinfos.json file to root folder...");
			saveTimers();
			Communicator.getInstance().handleOutput(Constants.LOCAL_CHANNEL, Settings.getCurrentLocalSender(), "[OK]");
		} catch (IOException e) {
			Communicator.getInstance().handleError(Constants.LOCAL_CHANNEL, Settings.getCurrentLocalSender(),
					"initializeTimers IO error: " + e.getMessage());
		} catch (ParseException e) {
			Communicator.getInstance().handleError(Constants.LOCAL_CHANNEL, Settings.getCurrentLocalSender(),
					"initializeTimers parse error: " + e.getMessage());
			Communicator.getInstance().handleError(Constants.LOCAL_CHANNEL, Settings.getCurrentLocalSender(),
					"Writing new timerinfos.json file to root folder...");
			saveTimers();
			Communicator.getInstance().handleOutput(Constants.LOCAL_CHANNEL, Settings.getCurrentLocalSender(), "[OK]");
		}
	}
}

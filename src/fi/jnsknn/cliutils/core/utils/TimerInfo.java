package fi.jnsknn.cliutils.core.utils;

import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

import fi.jnsknn.cliutils.core.Communicator;
import fi.jnsknn.cliutils.settings.Constants;
import fi.jnsknn.cliutils.settings.Settings;

public class TimerInfo extends TimerTask {

	// Run timer every second
	public static final long TIMER_PERIOD = 1000L;
	private static final Map<String, Integer> TIMER_MX = new HashMap<>() {
		private static final long serialVersionUID = 1L;
		{
			put("h", 3600000);
			put("m", 60000);
			put("s", 1000);
		}
	};

	private String name = "timer";
	private String owner = Settings.getCurrentLocalSender();
	private String channel = Constants.LOCAL_CHANNEL;
	private long timeStampStart;
	private long timeStampEnd;
	private long time;
	private long delay;
	private Timer timer;
	private int id;
	private boolean isTimerRepeating = false;

	/**
	 * Use this constructor when setting timers from file
	 **/
	public TimerInfo(int id, long timerStart, long timerEnd, String timerName, String timerOwner, String timerChannel,
			boolean isRepeating) {

		this.time = timerEnd - getCurrentTime();

		// Set delay if there is any delay left
		this.delay = timerStart - getCurrentTime();

		if (this.delay <= 0L) {
			this.delay = 0L;
		}

		this.timeStampStart = timerStart;
		this.timeStampEnd = timerEnd;

		// If timer has ended and timer was defined as repeating, then shift start and
		// end time stamp and set new time
		if (this.time <= 0L && isRepeating) {

			// Offline time to determine how long time has passed since timer stamp end
			long offlineTime = -this.time;

			// Original time cycle
			long cycleTime = timerEnd - timerStart;

			// How many original time cycles can fit to offline time
			long cycleTimes = offlineTime / cycleTime;

			// Put time left to this time
			this.time = cycleTime - (offlineTime - (cycleTime * cycleTimes));

		}

		// Set timer if there is any time left
		if (this.time >= 0L) {

			this.id = id;

			this.setName(timerName);
			this.owner = timerOwner;
			this.channel = timerChannel;
			this.isTimerRepeating = isRepeating;
			this.timer = new Timer(this.getName() + "-" + this.id);

			String msg = "Timer [" + this.id + "] [" + this.getName() + "] for " + this.owner + " has "
					+ parseTimeStringFromTime(this.time, true) + " left";

			if (this.isTimerRepeating) {

				msg += " and scheduled to repeat every "
						+ parseTimeStringFromTime(this.timeStampEnd - this.timeStampStart, true);

				if (this.delay > 0L) {
					msg += " in " + parseTimeStringFromTime(this.delay, true);
				}
			}

			msg += "!";

			Communicator.getInstance().printOutput(this.channel, this.owner, msg);
			this.timer.scheduleAtFixedRate(this, this.delay, TIMER_PERIOD);
			TimerInfoContainer.getInstance().setTimer(this);
		}

	}

	/**
	 * Use this constructor for creating public online timers if
	 * IRCIRCBot.getInstance() is connected
	 **/
	public TimerInfo(String timerTime, String timerDelay, String timerName, String timerOwner, String timerChannel,
			boolean isRepeating) {

		this.time = parseTimeFromTimerString(timerTime);

		if (this.time > 0L) {

			this.id = TimerInfoContainer.getInstance().getNextId();

			if (timerName != null && !timerName.isEmpty()) {
				this.setName(timerName);
			}

			this.owner = timerOwner;
			this.channel = timerChannel;

			this.delay = parseTimeFromTimerString(timerDelay);

			this.timeStampStart = getCurrentTime() + this.delay;
			this.timeStampEnd = this.timeStampStart + this.time;

			this.isTimerRepeating = isRepeating;
			this.timer = new Timer(this.getName() + "-" + this.id);

			if (this.isTimerRepeating) {

				if (this.delay <= 0L) {
					this.setName(timerDelay + timerName);
				}

				String msg = this.owner + ", your timer [" + this.id + "] [" + this.getName()
						+ "] has been scheduled to repeat every " + parseTimeStringFromTime(this.time, true);

				if (this.delay > 0L) {
					msg += " in " + parseTimeStringFromTime(this.delay, true);
				} else {
					msg += " starting now";
				}

				msg += "!";

				Communicator.getInstance().handleOutput(this.channel, this.owner, msg);
			}

			this.timer.scheduleAtFixedRate(this, this.delay, TIMER_PERIOD);
			TimerInfoContainer.getInstance().setTimer(this);
		}
	}

	/**
	 * Use this constructor for creating offline local timers
	 **/
	public TimerInfo(String timerTime, String timerDelay, String timerName, boolean isRepeating) {

		this.time = parseTimeFromTimerString(timerTime);

		if (this.time > 0L) {

			this.id = TimerInfoContainer.getInstance().getNextId();

			if (timerName != null && !timerName.isEmpty()) {
				this.setName(timerName);
			}

			this.delay = parseTimeFromTimerString(timerDelay);

			this.timeStampStart = getCurrentTime() + this.delay;
			this.timeStampEnd = this.timeStampStart + this.time;

			this.isTimerRepeating = isRepeating;
			this.timer = new Timer(this.getName() + "-" + this.id);

			if (this.isTimerRepeating) {

				if (this.delay <= 0L) {
					this.setName(timerDelay + timerName);
				}

				String msg = this.owner + ", your timer [" + this.id + "] [" + this.getName()
						+ "] has been scheduled to repeat every " + parseTimeStringFromTime(this.time, true);

				if (this.delay > 0L) {
					msg += " in " + parseTimeStringFromTime(this.delay, true);
				} else {
					msg += " starting now";
				}

				msg += "!";

				Communicator.getInstance().handleOutput(this.channel, this.owner, msg);
			}

			this.timer.scheduleAtFixedRate(this, this.delay, TIMER_PERIOD);
			TimerInfoContainer.getInstance().setTimer(this);
		}
	}

	private long parseTimeFromTimerString(String timerString) {
		try {

			final int strLength = timerString.length();
			long hours = 0;
			long minutes = 0;
			long seconds = 0;
			char hms;
			boolean charFound = false;

			String timerTimeTemp = timerString;

			for (int i = 0; i < strLength; i++) {

				hms = timerString.charAt(i);

				switch (hms) {
				case 'h':
					String[] hourArr = timerTimeTemp.split("h");
					if (hourArr.length > 1) {
						timerTimeTemp = hourArr[1];
					}
					hours += Long.valueOf(hourArr[0]);
					charFound = true;
					break;
				case 'm':
					String[] minArr = timerTimeTemp.split("m");
					if (minArr.length > 1) {
						timerTimeTemp = minArr[1];
					}
					minutes += Long.valueOf(minArr[0]);
					charFound = true;
					break;
				case 's':
					String[] secArr = timerTimeTemp.split("s");
					if (secArr.length > 1) {
						timerTimeTemp = secArr[1];
					}
					seconds += Long.valueOf(secArr[0]);
					charFound = true;
					break;
				default:
					charFound = false;
					break;
				}
			}

			long parsedTimeLong = (hours * TIMER_MX.get("h") + minutes * TIMER_MX.get("m")
					+ seconds * TIMER_MX.get("s"));

			if (!charFound) {
				return 0L;
			}

			return parsedTimeLong;
		} catch (NumberFormatException e) {
			Communicator.getInstance().handleError(Constants.LOCAL_CHANNEL, Settings.getCurrentLocalSender(),
					"parseTimeFromTimerString error: " + e.getMessage());
			return 0L;
		}
	}

	public String parseTimeStringFromTime(long timeLong, boolean isVerboseOutput) {
		String timeString = "";

		long timeLeft = timeLong;

		try {

			long hoursLeft = TimeUnit.MILLISECONDS.toHours(timeLeft);
			long minutesLeft = TimeUnit.MILLISECONDS.toMinutes(timeLeft - (hoursLeft * TIMER_MX.get("h")));
			long secondsLeft = TimeUnit.MILLISECONDS
					.toSeconds((timeLeft - minutesLeft * TIMER_MX.get("m")) - (hoursLeft * TIMER_MX.get("h")));

			if (!isVerboseOutput) {
				return String.format("%02d:%02d:%02d", hoursLeft, minutesLeft, secondsLeft);
			}
			if (hoursLeft > 0) {

				if (hoursLeft == 1) {
					timeString += hoursLeft + " hour";
				} else {
					timeString += hoursLeft + " hours";
				}
			}

			if (minutesLeft > 0 && minutesLeft < 60) {

				if (hoursLeft > 0) {
					timeString += " and ";
				}

				if (minutesLeft == 1) {
					timeString += minutesLeft + " minute";
				} else {
					timeString += minutesLeft + " minutes";
				}
			}

			if (secondsLeft > 0 && secondsLeft < 60) {
				if (hoursLeft > 0 || minutesLeft > 0) {
					timeString += " and ";
				}

				if (secondsLeft == 1) {
					timeString += secondsLeft + " second";
				} else {
					timeString += secondsLeft + " seconds";
				}
			}

			return timeString;
		} catch (NumberFormatException e) {
			Communicator.getInstance().handleError(Constants.LOCAL_CHANNEL, Settings.getCurrentLocalSender(),
					"parseTimeStringFromTime error: " + e.getMessage());
			return "";
		}
	}

	public static long getCurrentTime() {
		return 1000 * ((System.currentTimeMillis() + 500) / 1000);
	}

	@Override
	public void run() {

		if (this.time == (this.timeStampEnd - this.timeStampStart)) {
			Communicator.getInstance().handleOutput(this.channel, this.owner, this.owner + ", your timer [" + this.id
					+ "] [" + this.getName() + "] has been set for " + parseTimeStringFromTime(this.time, true) + "!");
		} else if (this.isTimerRepeating && this.time <= 0L) {

			this.time = this.timeStampEnd - this.timeStampStart;

			this.timeStampStart = getCurrentTime();
			this.timeStampEnd = this.timeStampStart + this.time;

			Communicator.getInstance().handleOutput(this.channel, this.owner,
					this.owner + ", your timer [" + this.id + "] [" + this.getName()
							+ "] has finished and set again for " + parseTimeStringFromTime(this.time, true));

			TimerInfoContainer.getInstance().setTimer(this);
			if (Settings.IS_SOUND_ENABLED) {
				TimerInfoSound.tone(5000, 50, 0.1);
			}
		} else if (!this.isTimerRepeating && this.time <= 0L) {
			Communicator.getInstance().handleOutput(this.channel, this.owner,
					this.owner + ", your timer [" + this.id + "] [" + this.getName() + "] has finished!");
			TimerInfoContainer.getInstance().removeTimer(this);
			if (Settings.IS_SOUND_ENABLED) {
				TimerInfoSound.tone(5000, 50, 0.1);
			}
		}

		// Don't send message on first tick and send message if timer...
		if (this.time != (this.timeStampEnd - this.timeStampStart) && (this.time == 3600 * 1000 || // has 1 hour left
				this.time == 1800 * 1000 || // has 30 minutes left
				this.time == 600 * 1000 || // has 10 minutes left
				this.time == 300 * 1000 || // has 5 minutes left
				this.time == 60 * 1000 || // has 1 minute left
				this.time == 30 * 1000)) { // has 30 seconds left

			Communicator.getInstance().handleOutput(this.channel, this.owner, this.owner + ", your timer [" + this.id
					+ "] [" + this.getName() + "] has " + parseTimeStringFromTime(this.time, true) + " left");
		}
		this.time -= TIMER_PERIOD;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getOwner() {
		return owner;
	}

	public void setOwner(String owner) {
		this.owner = owner;
	}

	public String getChannel() {
		return channel;
	}

	public void setChannel(String channel) {
		this.channel = channel;
	}

	public long getTimeStampStart() {
		return timeStampStart;
	}

	public void setTimeStampStart(long timeStampStart) {
		this.timeStampStart = timeStampStart;
	}

	public long getTimeStampEnd() {
		return timeStampEnd;
	}

	public void setTimeStampEnd(long timeStampEnd) {
		this.timeStampEnd = timeStampEnd;
	}

	public long getTime() {
		return time;
	}

	public void setTime(long time) {
		this.time = time;
	}

	public long getDelay() {
		return delay;
	}

	public void setDelay(long delay) {
		this.delay = delay;
	}

	public Timer getTimer() {
		return timer;
	}

	public void setTimer(Timer timer) {
		this.timer = timer;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public boolean isTimerRepeating() {
		return isTimerRepeating;
	}

	public void setTimerRepeating(boolean isTimerRepeating) {
		this.isTimerRepeating = isTimerRepeating;
	}

}

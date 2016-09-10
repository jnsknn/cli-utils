# Java CLI Utils

Java Maven project where I try to do and learn different things with Java. The main program contains sub programs which can be executed with right commands and arguments.

# Commands

Arguments are separated with space.

- ?calculate [arg] calculates result from given string using JavaScript engine
- ?ufo [arg...arg] creates weird name based on given strings
- ?irc [arg(BotName) arg(server) arg(#Channel)] connects to irc
- ?timer [arg((int)time (char)h/m/s) arg(Timer name)...arg(Timer name)] sets timer
- ?rtimer [arg((int)time (char)h/m/s) arg(Timer name)...arg(Timer name)] sets repeating timer
- ?timer with no arguments lists all set timers
- ?rmtimer [arg((int)id)] removes timer
- ?exit disconnects irc and quits the main program
- ?help posts link to this file

# Examples

- Input: ?calculate 1+1*(123-5.5)/43
	- Output: 1+1*(123-5.5)/43 = 3.7325581395348837

- Input: ?ufo Jonne Niskanen
	- Output: Fynni Semropow

- Input: ?irc Jbot irc.elisa.fi #CHANNELNAME
	- Output: Connecting to irc.elisa.fi...
	- You can also speak to channel when bot is connected by typing: #CHANNELNAME hello

- Input: ?timer 2h20m30s go to work
	- Output: Your timer [go to work] has been set for 2 hours and 20 minutes and 30 seconds!
	- Output: Your timer [go to work] has 1 hour left
	- Output: Your timer [go to work] has 30 minutes left
	- Output: Your timer [go to work] has 5 minutes left
	- Output: Your timer [go to work] has 1 minute left
	- Output: Your timer [go to work] has 30 seconds left
	- Output: Your timer [go to work] has finished!

You can make timer repeating by using command ?rtimer instead of regular ?timer

- Input: ?timer
	- Output: >>Timer [1] [go to work] for You has 1 hour and 58 minutes and 16 seconds remaining!<<>>Timer [2] [food!] for You has 8 minutes and 34 seconds remaining!<<>>Timer [3] [check oven] for You has 59 minutes and 55 seconds remaining!<<
		
- Input: ?rmtimer 1
	- Output: You, your timer go to work has been removed!

- Input: ?help
	- Output: https://github.com/jnsknn/java-cli-utils/blob/master/README.md

# Dependencies

- PircBot http://www.jibble.org/pircbot.php

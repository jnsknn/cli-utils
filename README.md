# Java CLI Utils

Java Maven project where I try to do and learn different things with Java. The main program contains sub programs which can be executed with right commands and arguments.

# Commands

All commands except ?exit takes arguments. Arguments are separated with space.

- ?calculate [arg] calculates result from given string using JavaScript engine

- ?ufoname [arg...arg] creates weird name based on given strings

- ?irc [arg(BotName) arg(server) arg(#Channel)] connects to irc

- ?timer [arg((int)time|h|m|s) arg(Timer name)...arg(Timer name)] sets timer

- ?timer with no arguments lists all set timers

- ?rmtimer [arg((int)id)] removes timer

- ?exit disconnects irc and quits the main program

# Examples

Input: ?calculate 1+1*(123-5.5)/43
Output: 1+1*(123-5.5)/43 = 3.7325581395348837

Input: ?ufoname Jonne Niskanen
Output: Fynni Semropow

Input: ?irc Jbot irc.elisa.fi #CHANNELNAME
Output: Connecting to irc.elisa.fi...
		1472743342656 *** Connected to server.
		1472743342656 >>>NICK Jbot
		...
		...
		
- You can also speak to channel when bot is connected by typing: #CHANNELNAME hello

Input: ?timer 2h20m30s go to work
Output: Your timer [go to work] has been set for 2 hours and 20 minutes and 30 seconds!
Output: Your timer [go to work] has 1 hour left
Output: Your timer [go to work] has 30 minutes left
Output: Your timer [go to work] has 5 minutes left
Output: Your timer [go to work] has 1 minute left
Output: Your timer [go to work] has 30 seconds left
Output: Your timer [go to work] has finished!

Input: ?timer
Output: Timer id [1] name [go to work] for you has 2 hours and 16 minutes and 21 seconds remaining!
		Timer id [2] name [go to work] for you has 56 minutes and 46 seconds remaining!
		Timer id [3] name [go to work] for you has 27 minutes and 18 seconds remaining!
		Timer id [4] name [go to work] for you has 2 minutes and 43 seconds remaining!
		
Input: ?rmtimer 1
Output: you, your timer go to work has been removed!

# Dependencies

- PircBot http://www.jibble.org/pircbot.php

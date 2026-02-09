package com.firemaking;

import java.time.Duration;
import java.time.Instant;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FiremakingSession
{
	private static final int MINIMUM_FIRES_FOR_RATE = 3;

	private Instant startTime;
	private Instant lastActivity;
	private int firesLit;
	private int startXp;
	private int lastXp;
	private int cachedXpPerHour = -1;
	private Instant lastXpPerHourCalc = Instant.EPOCH;

	FiremakingSession()
	{
		startTime = Instant.now();
		lastActivity = Instant.now();
		firesLit = 0;
		startXp = -1;
		lastXp = -1;
	}

	void incrementFiresLit()
	{
		firesLit++;
		lastActivity = Instant.now();
	}

	/**
	 * Returns XP gained since session start, or 0 if XP hasn't been tracked yet.
	 */
	int getXpGained()
	{
		if (startXp == -1 || lastXp == -1)
		{
			return 0;
		}

		return lastXp - startXp;
	}

	/**
	 * Returns XP per hour, or -1 if not enough data.
	 * Cached and recalculated at most every 5 seconds to avoid jittery updates.
	 */
	int getXpPerHour()
	{
		Instant now = Instant.now();
		if (cachedXpPerHour != -1 && Duration.between(lastXpPerHourCalc, now).getSeconds() < 5)
		{
			return cachedXpPerHour;
		}

		int xpGained = getXpGained();
		if (xpGained <= 0 || firesLit < MINIMUM_FIRES_FOR_RATE)
		{
			return -1;
		}

		Duration elapsed = Duration.between(startTime, now);
		long seconds = elapsed.getSeconds();
		if (seconds == 0)
		{
			return -1;
		}

		cachedXpPerHour = (int) (xpGained * 3600.0 / seconds);
		lastXpPerHourCalc = now;
		return cachedXpPerHour;
	}

	/**
	 * Update XP from a StatChanged event. Sets startXp on first call.
	 */
	void updateXp(int currentXp)
	{
		if (startXp == -1)
		{
			startXp = currentXp;
		}
		lastXp = currentXp;
	}
}

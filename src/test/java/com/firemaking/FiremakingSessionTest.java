package com.firemaking;

import java.time.Instant;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class FiremakingSessionTest
{
	private FiremakingSession session;

	@Before
	public void setUp()
	{
		session = new FiremakingSession();
	}

	@Test
	public void testIncrementFiresLit()
	{
		Assert.assertEquals(0, session.getFiresLit());
		session.incrementFiresLit();
		Assert.assertEquals(1, session.getFiresLit());
		session.incrementFiresLit();
		Assert.assertEquals(2, session.getFiresLit());
	}

	@Test
	public void testXpGainedNoData()
	{
		Assert.assertEquals(0, session.getXpGained());
	}

	@Test
	public void testXpTracking()
	{
		session.updateXp(1000);
		Assert.assertEquals(0, session.getXpGained());

		session.updateXp(1200);
		Assert.assertEquals(200, session.getXpGained());

		session.updateXp(1500);
		Assert.assertEquals(500, session.getXpGained());
	}

	@Test
	public void testXpPerHourCalculation()
	{
		session.setStartTime(Instant.now().minusSeconds(3600));

		// Need at least 3 fires for rates
		session.incrementFiresLit();
		session.incrementFiresLit();
		session.incrementFiresLit();

		session.updateXp(10000);
		session.updateXp(10500);

		int xpPerHr = session.getXpPerHour();
		// 500 XP in 1 hour = ~500/hr
		Assert.assertTrue("Expected xp/hr around 500, got " + xpPerHr, xpPerHr >= 490 && xpPerHr <= 510);
	}

	@Test
	public void testXpPerHourNotEnoughFires()
	{
		session.setStartTime(Instant.now().minusSeconds(3600));
		session.incrementFiresLit();
		session.updateXp(10000);
		session.updateXp(10500);

		// Only 1 fire, below minimum of 3
		Assert.assertEquals(-1, session.getXpPerHour());
	}
}

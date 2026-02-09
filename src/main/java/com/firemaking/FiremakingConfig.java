package com.firemaking;

import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;
import net.runelite.client.config.Units;

@ConfigGroup("firemaking")
public interface FiremakingConfig extends Config
{
	@ConfigItem(
		keyName = "statTimeout",
		name = "Reset stats",
		description = "Duration the session tracker stays active after you stop firemaking",
		position = 1
	)
	@Units(Units.MINUTES)
	default int statTimeout()
	{
		return 5;
	}

	@ConfigItem(
		keyName = "showFiremakingStats",
		name = "Show stats overlay",
		description = "Show the firemaking session stats overlay",
		position = 2
	)
	default boolean showFiremakingStats()
	{
		return true;
	}
}

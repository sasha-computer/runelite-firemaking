package com.firemaking;

import com.google.inject.Provides;
import java.time.Duration;
import java.time.Instant;
import javax.inject.Inject;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.AnimationID;
import net.runelite.api.ChatMessageType;
import net.runelite.api.Client;
import net.runelite.api.Player;
import net.runelite.api.Skill;
import net.runelite.api.events.ChatMessage;
import net.runelite.api.events.GameTick;
import net.runelite.api.events.StatChanged;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.ui.overlay.OverlayManager;
import net.runelite.client.ui.overlay.OverlayMenuEntry;

@Slf4j
@PluginDescriptor(
	name = "Firemaking",
	description = "Tracks firemaking session stats including fires lit, XP gained, and rates",
	tags = {"firemaking", "skilling", "overlay"}
)
public class FiremakingPlugin extends Plugin
{
	private static final String FIRE_MESSAGE = "The fire catches and the logs begin to burn.";
	private static final int FIREMAKING_ANIMATION = AnimationID.FIREMAKING;

	@Inject
	private Client client;

	@Inject
	private FiremakingConfig config;

	@Inject
	private FiremakingOverlay overlay;

	@Inject
	private OverlayManager overlayManager;

	@Getter
	private FiremakingSession session;

	@Getter
	private boolean firemaking;

	@Override
	protected void startUp()
	{
		overlayManager.add(overlay);
	}

	@Override
	protected void shutDown()
	{
		overlayManager.remove(overlay);
		session = null;
		firemaking = false;
	}

	@Provides
	FiremakingConfig provideConfig(ConfigManager configManager)
	{
		return configManager.getConfig(FiremakingConfig.class);
	}

	@Subscribe
	public void onOverlayMenuClicked(net.runelite.client.events.OverlayMenuClicked event)
	{
		OverlayMenuEntry entry = event.getEntry();
		if (event.getOverlay() == overlay && entry.getOption().equals(FiremakingOverlay.RESET))
		{
			session = null;
		}
	}

	@Subscribe
	public void onChatMessage(ChatMessage event)
	{
		if (event.getType() != ChatMessageType.SPAM && event.getType() != ChatMessageType.GAMEMESSAGE)
		{
			return;
		}

		if (event.getMessage().equals(FIRE_MESSAGE))
		{
			if (session == null)
			{
				session = new FiremakingSession();
			}

			session.incrementFiresLit();
		}
	}

	@Subscribe
	public void onStatChanged(StatChanged event)
	{
		if (event.getSkill() != Skill.FIREMAKING)
		{
			return;
		}

		if (session != null)
		{
			session.updateXp(event.getXp());
		}
	}

	@Subscribe
	public void onGameTick(GameTick event)
	{
		Player local = client.getLocalPlayer();
		if (local != null)
		{
			firemaking = local.getAnimation() == FIREMAKING_ANIMATION;
		}

		if (session != null)
		{
			Duration sinceLastActivity = Duration.between(session.getLastActivity(), Instant.now());
			if (sinceLastActivity.compareTo(Duration.ofMinutes(config.statTimeout())) >= 0)
			{
				log.debug("Firemaking session timed out");
				session = null;
			}
		}
	}
}

package com.firemaking;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import javax.inject.Inject;
import static net.runelite.api.MenuAction.RUNELITE_OVERLAY;
import static net.runelite.api.MenuAction.RUNELITE_OVERLAY_CONFIG;
import net.runelite.client.ui.overlay.OverlayMenuEntry;
import net.runelite.client.ui.overlay.OverlayPanel;
import net.runelite.client.ui.overlay.OverlayPosition;
import net.runelite.client.ui.overlay.components.LineComponent;
import net.runelite.client.ui.overlay.components.TitleComponent;

class FiremakingOverlay extends OverlayPanel
{
	static final String RESET = "Reset";

	private final FiremakingPlugin plugin;
	private final FiremakingConfig config;

	@Inject
	private FiremakingOverlay(FiremakingPlugin plugin, FiremakingConfig config)
	{
		super(plugin);
		setPosition(OverlayPosition.TOP_LEFT);
		this.plugin = plugin;
		this.config = config;
		getMenuEntries().add(new OverlayMenuEntry(RUNELITE_OVERLAY_CONFIG, "Configure", "Firemaking overlay"));
		getMenuEntries().add(new OverlayMenuEntry(RUNELITE_OVERLAY, RESET, "Firemaking overlay"));
	}

	@Override
	public Dimension render(Graphics2D graphics)
	{
		FiremakingSession session = plugin.getSession();

		if (session == null || !config.showFiremakingStats())
		{
			return null;
		}

		panelComponent.getChildren().add(TitleComponent.builder()
			.text("Firemaking")
			.color(Color.CYAN)
			.build());

		panelComponent.getChildren().add(LineComponent.builder()
			.left("Fires lit:")
			.right(Integer.toString(session.getFiresLit()))
			.build());

		int xpGained = session.getXpGained();
		if (xpGained > 0)
		{
			panelComponent.getChildren().add(LineComponent.builder()
				.left("XP gained:")
				.right(Integer.toString(xpGained))
				.build());
		}

		int xpPerHr = session.getXpPerHour();
		if (xpPerHr != -1)
		{
			panelComponent.getChildren().add(LineComponent.builder()
				.left("XP/hr:")
				.right(Integer.toString(xpPerHr))
				.build());
		}

		return super.render(graphics);
	}
}

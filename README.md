# Firemaking Plugin for RuneLite

A [RuneLite](https://runelite.net/) plugin that tracks your firemaking session stats with a real-time overlay.

## Features

- **Fires lit** counter, updated each time you successfully light a fire
- **XP gained** during the current session
- **XP/hr** rate, calculated after 3+ fires and smoothed to avoid jitter
- **Configurable session timeout** (default 5 minutes of inactivity)
- **Toggle overlay** on/off from the config panel
- **Right-click reset** to clear the session from the overlay

## Configuration

| Option | Description | Default |
|--------|-------------|---------|
| Reset stats | How long the session stays active after you stop firemaking | 5 minutes |
| Show stats overlay | Show or hide the overlay panel | On |

## Installation

Search for **Firemaking** in the RuneLite Plugin Hub.

## Building from Source

```
./gradlew build
```

## License

BSD 2-Clause. See [LICENSE](LICENSE).

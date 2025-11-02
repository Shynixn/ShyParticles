# Commands Reference

This page provides a complete reference for all ShyParticles commands. All commands require the `shyparticles.command` permission unless otherwise specified.

## 🎮 Getting Started

To see all available commands in-game, use:
```
/shyparticles help 1
```

---

## 📝 Command Overview

| Command | Purpose | Permission Required |
|---------|---------|-------------------|
| `/shyparticles play` | Play a particle effect at a location | `shyparticles.play` |
| `/shyparticles follow` | Start a follow particle effect on a player | `shyparticles.follow` |
| `/shyparticles stop` | Stop a specific particle effect by session ID | `shyparticles.stop` |
| `/shyparticles stopfollow` | Stop follow particle effects on a player | `shyparticles.stopfollow` |
| `/shyparticles list` | List all available particle effects | `shyparticles.list` |
| `/shyparticles reload` | Reload all configurations | `shyparticles.reload` |

---

## 🔧 Detailed Command Reference

### `/shyparticles play`
**Purpose:** Play a particle effect at a specific location

```
/shyparticles play <effect> [x] [y] [z] [world] [player]
```

**Parameters:**

* `<effect>` - The name of the particle effect to play (required)
* `[x]` - X coordinate (optional, defaults to player location)
* `[y]` - Y coordinate (optional, requires x)
* `[z]` - Z coordinate (optional, requires x and y)
* `[world]` - World name (optional, requires x, y, z)
* `[player]` - Target player to see the effect (optional)

**Coordinate Format:**
* Absolute: `100` (exact coordinate)
* Relative: `~5` (5 blocks from current position)
* Current: `~0` (current position)

**Behavior:**

* **Creates:** Location-based particle effect
*  **Requires:** `shyparticles.effect.start.<effect_name>` permission
* **Location:** If no coordinates provided, uses player's current location
* **Visibility:** If player specified, only that player sees the effect

**Examples:**
```bash
# Play flame tornado at your location
/shyparticles play flame_tornado

# Play blue sphere 10 blocks above you
/shyparticles play blue_sphere ~ ~10 ~

# Play rainbow spiral at specific coordinates
/shyparticles play rainbow_spiral 100 65 -200

# Play effect in different world
/shyparticles play soul_vortex 0 100 0 world_nether

# Play effect visible only to specific player
/shyparticles play pulsing_heart 50 70 -100 world Steve
```

**Common Use Cases:**

* Event decorations at specific locations
* Building showcase enhancements
* Celebration effects for achievements
* Environmental storytelling elements

---

### `/shyparticles follow`
**Purpose:** Start a particle effect that follows a player

```
/shyparticles follow <effect> [player]
```

**Parameters:**

* `<effect>` - The name of the particle effect to start (required)
* `[player]` - Target player to follow (optional, defaults to command sender)

**Behavior:**

*  **Follows:** The specified player's movement
* **Self:** Requires `shyparticles.follow` permission
* **Others:** Requires `shyparticles.followother` permission for other players
*  **Updates:** Effect location updates with player movement
*  **Requires:** `shyparticles.effect.start.<effect_name>` permission

**Examples:**
```bash
# Start dancing circles following you
/shyparticles follow dancing_circles

# Make electric storm follow another player (admin only)
/shyparticles follow electric_storm Notch

# Start cherry blossom wind following you
/shyparticles follow cherry_blossom_wind
```

**Common Use Cases:**

* Player status indicators (VIP effects, special events)
* Cosmetic rewards for achievements
* Role-playing enhancement (magical auras, etc.)
* Event participation markers

---

### `/shyparticles stop`
**Purpose:** Stop a specific particle effect using its session ID

```
/shyparticles stop <sessionId>
```

**Parameters:**

* `<sessionId>` - The unique session ID of the effect to stop (required)

**Behavior:**

* **Stops:** Specific particle effect by session ID
*  **Session IDs:** Generated when effects start, shown in play command output
*  **Requires:** `shyparticles.stop` permission

**Examples:**
```bash
# Stop effect with specific session ID
/shyparticles stop abc-123-def-456

# Use tab completion to see active session IDs
/shyparticles stop <TAB>
```

**Common Use Cases:**

* Cleaning up location-based effects
* Stopping specific effects without affecting others
* Administrative control over particle effects

---

### `/shyparticles stopfollow`
**Purpose:** Stop all follow particle effects on a player

```
/shyparticles stopfollow [player]
```

**Parameters:**

* `[player]` - Target player to stop following effects (optional, defaults to command sender)

**Behavior:**

* **Stops:** All follow effects on specified player
* **Self:** Requires `shyparticles.stopfollow` permission
* **Others:** Requires `shyparticles.stopfollowother` permission for other players
*  **Clears:** All follow effects, not just specific ones

**Examples:**
```bash
# Stop all follow effects on yourself
/shyparticles stopfollow

# Stop all follow effects on another player (admin only)
/shyparticles stopfollow Steve
```

**Common Use Cases:**

* Removing cosmetic effects when leaving events
* Clearing effects when changing player status
* Administrative cleanup of player effects

---

### `/shyparticles list`
**Purpose:** Display all available particle effects

```
/shyparticles list
```

**Parameters:** None

**Behavior:**

* 📋 **Shows:** All configured particle effects
* 🔤 **Sorted:** Alphabetically by effect name
* 🔒 **Requires:** `shyparticles.list` permission

**Examples:**
```bash
# See all available effects
/shyparticles list
```

**Sample Output:**
```
Available effects: blue_sphere, box_tower, cherry_blossom_wind, dancing_circles, electric_storm, enchanting_portal, flame_tornado, orbital_rings, pulsing_heart, rainbow_spiral, soul_vortex, yellow_star
```

**Common Use Cases:**

* Discovering available effects
* Reference for command usage
* Administrative overview of configured effects

---

### `/shyparticles reload`
**Purpose:** Reload all plugin configurations and effect files

```
/shyparticles reload
```

**Parameters:** None

**Behavior:**

* 📁 **Reloads:** All `.yml` files in the effects folder
* 🔄 **Refreshes:** Plugin configuration and language files
* ⚡ **Updates:** Changes take effect immediately
* 🔒 **Requires:** `shyparticles.reload` permission

**Examples:**
```bash
# Reload after editing effect files
/shyparticles reload
```

**Common Use Cases:**

* After editing effect configuration files
* Adding new particle effects without server restart
* Modifying existing effect parameters
* Testing configuration changes

**⚠️ Important Notes:**

* Always run this command after editing effect `.yml` files
* Active effects continue running with old settings until restarted
* Invalid configurations will show error messages in console

---

## 💡 Usage Tips

### Command Integration
Many server administrators integrate these commands with other plugins:

**Event Management:**
```bash
# Start event effects
/shyparticles play enchanting_portal 100 65 -200

# Give players celebration effects
/shyparticles follow pulsing_heart %player%
```

**WorldGuard Integration:**
```bash
# Region entry effect
/shyparticles play blue_sphere %regioncenter%

# Player entering VIP area
/shyparticles follow orbital_rings %player%
```

**Minigame Plugins:**
```bash
# Arena effect on game start
/shyparticles play electric_storm

# Victory celebration
/shyparticles follow rainbow_spiral %winner%
```

### Effect Management
When managing multiple effects:

* Each effect gets a unique session ID when started
* Use `/shyparticles list` to see available effects
* Session IDs are shown when effects start
* Tab completion helps with session IDs and effect names

### Permission-Based Effects
Control which effects players can use:

* `shyparticles.effect.start.*` - Access to all effects
* `shyparticles.effect.start.flame_tornado` - Access to specific effect
* Different permissions for different player groups

### Performance Considerations
* Location effects continue until manually stopped
* Follow effects stop when player logs out
* Use `/shyparticles stop <sessionId>` to clean up location effects
* Regular cleanup prevents server performance issues
# Configuration Guide

This guide will walk you through creating and configuring particle effects in ShyParticles. By the end, you'll understand how to customize existing effects and create your own stunning particle displays.

## 📁 Understanding Effect Structure

ShyParticles uses YAML configuration files to define particle effects. Each effect is stored in the `/plugins/ShyParticles/effects/` folder and consists of multiple layers that create complex visual displays.

### 🎆 Effect Components

Every particle effect has these key components:

**Basic Properties:**

- **Name:** Unique identifier for the effect
- **Duration:** How long the effect runs (in ticks, 20 ticks = 1 second)
- **Repeat:** Whether the effect loops indefinitely

**Layers:**

- **Particle:** The Minecraft particle type to use
- **Shape:** Geometric pattern (CIRCLE, SPHERE, SPIRAL, etc.)
- **Options:** Size, density, color, and behavior settings
- **Modifiers:** Animations like rotation, movement, and scaling

### 🌟 Available Shapes

ShyParticles supports various geometric shapes, each with specific required properties:

**CIRCLE** - Flat circular patterns

- **Required:** `radius`, `particleCount`, `density`
- **Optional:** `skip` (performance)

**SPHERE** - 3D spherical displays  

- **Required:** `radius`, `particleCount`, `density`
- **Optional:** `skip` (performance)

**SPIRAL** - Twisting spiral patterns

- **Required:** `radius`, `height`, `turns`, `particleCount`, `density`
- **Optional:** `skip` (performance)

**LINE** - Straight particle lines

- **Required:** `width`, `particleCount`
- **Optional:** `skip` (performance)

**RECTANGLE** - Rectangular outlines

- **Required:** `width`, `length`, `particleCount`
- **Optional:** `skip` (performance)

**CUBE** - 3D cube structures

- **Required:** `width`, `length`, `height`, `particleCount`
- **Optional:** `skip` (performance)

**RANDOM** - Randomly distributed particles

- **Required:** `radius`, `height`, `particleCount`
- **Optional:** `skip` (performance)

**POINT** - Single particle location

- **Required:** `particleCount` (usually 1)
- **Optional:** `skip` (performance)

**HEART** - Heart-shaped patterns

- **Required:** `radius`, `particleCount`, `density`
- **Optional:** `skip` (performance)

**STAR** - Star-shaped displays

- **Required:** `radius`, `particleCount`, `density`
- **Optional:** `skip` (performance)

**💡 Property Descriptions:**

- `radius` - Size of circular/spherical shapes
- `width` - X-axis dimension for rectangular shapes
- `length` - Z-axis dimension for rectangular shapes  
- `height` - Y-axis dimension for vertical shapes
- `turns` - Number of spiral rotations
- `particleCount` - Number of particles to generate
- `density` - Particle density multiplier (0.0-1.0)
- `skip` - Ticks to skip between updates (higher = better performance)

### ⚡ Animation Modifiers

Bring your effects to life with modifiers:

- **ROTATE** - Spin effects around X, Y, or Z axes
- **MOVE** - Translate particles over time in any direction
- **PULSE** - Scale effects between min and max values
- **WAVE** - Add wave-like vertical motion with amplitude and frequency
- **OSCILLATE** - Create orbital motion around specified axes
- **RANDOM** - Add random movement with configurable strength
- **OPTIONS_SET** - Override particle options at specific times
- **OPTIONS_ADD** - Add values to existing particle options over time

## 🔧 Creating Your First Effect

### Step 1: Explore Existing Effects

1. **Navigate to the effects folder:**
   ```
   /plugins/ShyParticles/effects/
   ```

2. **Examine the pre-built effects:**
   - `blue_sphere.yml` - Simple rotating blue sphere
   - `flame_tornado.yml` - Complex multi-layer tornado
   - `rainbow_spiral.yml` - Colorful spiral animation
   - And 9 other stunning effects!

3. **Choose a base effect to customize:**
   - Copy an existing effect file
   - Rename it to match your desired effect name

### Step 2: Basic Effect Configuration

Let's create a simple custom effect by modifying `blue_sphere.yml`:

```yaml
name: "my_custom_sphere"
duration: 120  # 6 seconds (120 ticks ÷ 20 = 6 seconds)
repeat: true   # Loop indefinitely

layers:
  - particle: "DUST,REDSTONE"
    shape: "SPHERE"
    options:
      skip: 2          # Update every 2 ticks for performance
      radius: 3.0      # Make it larger than the original
      particleCount: 150 # More particles for density
      density: 1.2     # Increase particle density
      red: 255         # RGB color values
      green: 100       # Orange-ish color
      blue: 0
      scale: 1.5       # Larger particle size
```

### Step 3: Apply Your Configuration

1. **Save your effect file** in `/plugins/ShyParticles/effects/`

2. **Reload the plugin:**
   ```
   /shyparticles reload
   ```

3. **Test your effect:**
   ```
   /shyparticles play my_custom_sphere
   ```

---

## 🎮 Using Your Effects

### Setting Up Permissions

Before players can use effects, they need the appropriate permissions:

1. **Grant basic command access:**
   ```
   shyparticles.command
   ```

2. **Allow specific effects:**
   ```
   shyparticles.effect.start.my_custom_sphere
   ```
   
   Or grant access to all effects:
   ```
   shyparticles.effect.start.*
   ```

3. **Grant visibility permissions** (required to see particle effects):
   ```
   shyparticles.effect.visible.my_custom_sphere
   ```
   
   Or grant visibility to all effects:
   ```
   shyparticles.effect.visible.*
   ```

4. **Grant play permissions for different command types:**
   ```
   shyparticles.play        # Location-based effects
   shyparticles.follow      # Follow effects
   shyparticles.list        # View available effects
   ```

**⚠️ Important:** Players need **both** the `start` and `visible` permissions to use effects. The `start` permission allows them to trigger effects, while the `visible` permission allows them to see the particle displays.

### Testing and Using Effects

**Play at your location:**

```
/shyparticles play my_custom_sphere
```

**Play at specific coordinates:**

```
/shyparticles play flame_tornado 100 65 -200
```

**Start a follow effect:**

```
/shyparticles follow rainbow_spiral
```

**List all available effects:**

```
/shyparticles list
```

**Stop follow effects:**

```
/shyparticles stopfollow
```

---

## 🎨 Advanced Customization

### Multi-Layer Effects

Create complex effects by combining multiple particle layers:

```yaml
name: "epic_tornado"
duration: 300
repeat: true

layers:
  # Base fire layer
  - particle: "FLAME"
    shape: "SPIRAL"
    options:
      skip: 1
      radius: 2.0
      height: 5.0
      turns: 4
      particleCount: 200
    modifiers:
      - type: "ROTATE"
        angle: 5
        speed: 1.0
        axis: Y

  # Smoke layer
  - particle: "LARGE_SMOKE"
    shape: "SPIRAL"
    options:
      skip: 2
      radius: 2.5
      height: 6.0
      turns: 2
      particleCount: 100
    modifiers:
      - type: "ROTATE"
        angle: 3
        speed: 1.0
        axis: Y

  # Ember particles with upward movement
  - particle: "LAVA"
    shape: "RANDOM"
    options:
      skip: 4
      radius: 3.0
      height: 6.0
      particleCount: 50
    modifiers:
      - type: "MOVE"
        y: 0.05
        speed: 1.0
```

### Color Customization

For DUST particles, you can create any color. Example named presets:

```yaml
pure_red:
  red: 255
  green: 0
  blue: 0

purple:
  red: 128
  green: 0
  blue: 255

custom_orange:
  red: 255
  green: 165
  blue: 0
```

### Performance Optimization

**Skip Values:**

- Higher skip = better performance, less smooth animation
- Lower skip = smoother animation, more server load
- Start with skip: 2 and adjust based on your needs

**Particle Count:**

- More particles = better visual quality, more resource usage
- Balance visual appeal with server performance
- Test with multiple effects running simultaneously

### Animation Timing

**Duration and Repeat:** Example effect presets:

```yaml
example_one:
  duration: 100    # 5 seconds
  repeat: false    # Play once then stop

example_two:
  duration: 40     # 2 seconds
  repeat: true     # Loop forever
```

**Modifier Examples:**

```yaml
modifiers:
  # Rotation around Y axis
  - type: "ROTATE"
    angle: 5         # Degrees per tick
    speed: 1.0       # Speed multiplier
    axis: Y          # X, Y, Z, or ALL
    start: 0         # Start immediately
    end: 200         # Stop after 200 ticks

  # Movement with directional offsets
  - type: "MOVE"
    x: 0.0           # X offset per tick
    y: 0.1           # Y offset per tick
    z: 0.0           # Z offset per tick
    forward: 0.0     # Forward direction offset
    sideward: 0.0    # Sideward direction offset
    upward: 0.0      # Upward direction offset
    speed: 1.0       # Speed multiplier
    usePitch: false  # Whether to use pitch in directional calculations

  # Pulsing effect
  - type: "PULSE"
    minScale: 0.5    # Minimum scale factor
    maxScale: 1.5    # Maximum scale factor
    speed: 2.0       # Pulse speed

  # Wave motion
  - type: "WAVE"
    amplitude: 1.0   # Wave height
    frequency: 1.0   # Wave frequency
    speed: 1.0       # Wave speed

  # Oscillating orbital motion
  - type: "OSCILLATE"
    x: 2.0           # X orbit radius
    y: 1.0           # Y orbit radius
    z: 2.0           # Z orbit radius
    axis: Y          # Orbit around Y axis
    speed: 1.0       # Orbital speed

  # Options modification - Set specific values at certain times
  - type: "OPTIONS_SET"
    start: 100       # Start after 100 ticks
    end: 200         # End after 200 ticks
    options:
      radius: 5.0    # Override radius to 5.0
      red: 255       # Change color to red
      green: 0
      blue: 0
      scale: 2.0     # Make particles larger

  # Options modification - Add to existing values over time
  - type: "OPTIONS_ADD"
    start: 0         # Start immediately
    speed: 1.0       # Rate of change
    options:
      radius: 0.01   # Gradually increase radius
      scale: 0.005   # Gradually increase particle size
      particleCount: 1 # Gradually add more particles
```

### Dynamic Option Modification

**OPTIONS_SET and OPTIONS_ADD** modifiers allow you to change particle properties over time:

**OPTIONS_SET** - Replaces current option values:

```yaml
modifiers:
  - type: "OPTIONS_SET"
    start: 50        # Apply changes after 50 ticks
    end: 100         # Stop changes after 100 ticks
    options:
      red: 255       # Set color to bright red
      green: 0
      blue: 0
      scale: 3.0     # Make particles 3x larger
      speed: 0.5     # Slow down particle movement
      count: 10      # Spawn 10 particles per packet
```

**OPTIONS_ADD** - Adds to existing option values each tick:

```yaml
modifiers:
  - type: "OPTIONS_ADD"
    start: 0         # Start immediately
    speed: 1.0       # Rate multiplier
    options:
      radius: 0.02   # Grow radius by 0.02 each tick
      scale: 0.01    # Increase particle size each tick
      red: -1        # Fade red color over time
      spreadX: 0.005 # Gradually increase spread
```

**Available Options** for modification:

- **Color**: `red`, `green`, `blue`, `alpha`, `fromRed`, `toRed`, etc.
- **Size/Shape**: `radius`, `scale`, `width`, `height`, `length`
- **Behavior**: `speed`, `count`, `density`, `particleCount`
- **Effects**: `spreadX`, `spreadY`, `spreadZ`, `roll`, `delay`

---

## 🔊 Sounds

ShyParticles supports simple sound definitions that are played while an effect runs. Sounds are defined per-effect using the `sounds:` list in the effect YAML. The implementation uses a small SoundEffect class with the following fields:

- `name` (string) — Sound identifier. Can be a Bukkit Sound enum name (e.g. `ENTITY_EXPERIENCE_ORB_PICKUP`) or any custom sound string from a resource pack. Multiple candidates may be provided comma-separated; the plugin will try to resolve to a Bukkit Sound and fall back to the raw string.
- `volume` (float) — Volume passed to Bukkit's playSound (0.0 to 1.0 is typical).
- `pitch` (float) — Pitch passed to Bukkit's playSound (typical range 0.5 to 2.0).
- `start` (int) — First tick at which the sound is eligible to play (inclusive).
- `end` (int) — Last tick at which the sound is eligible to play (inclusive).

Behavior notes (implementation details):

- Sounds are evaluated every tick while the effect runs. For each configured sound, the effect checks whether the current tick is between `start` and `end` (inclusive). If it is, the sound is played. You can also use the `interval` property to define the played interval in ticks.
- Playback is location-based using the effect origin. If the effect was started for a specific player (follow/play targeting a player), the plugin will call `player.playSound(...)`, so only that player hears it. If the effect is played at a world/location (no single player target), the plugin calls `world.playSound(...)` and normal server visibility applies.
- The audible range of a played sound is determined by Bukkit/server behavior and the `volume` value — there is no separate per-sound `range` setting.

Examples

1) One-shot sound at effect start:

```yaml
sounds:
  - name: "ENTITY_EXPERIENCE_ORB_PICKUP"
    volume: 1.0
    pitch: 1.0
    start: 0
    end: 0
    interval: 1
```

2) Play a custom resource-pack sound every tick from tick 10 to 100:

```yaml
sounds:
  - name: "my.custom.sound"
    volume: 0.8
    pitch: 1.0
    start: 10
    end: 100
```

3) Provide multiple candidates for resolution (tries enum names first):

```yaml
sounds:
  - name: "ENTITY_BLAZE_SHOOT,block.note_block.pling"
    volume: 0.6
    pitch: 0.9
    start: 0
    end: 0
```

Tips

- To avoid loud sounds every tick, keep `end` close to `start` or only set both to the same tick for a single play.
- Use per-effect `range` and permissions to control which players can see and hear the effect; however sound audible radius itself is controlled by Bukkit and the `volume` parameter.

---

## ❓ Common Issues

**Q: My effect isn't showing**

* Check that you have the required permissions
* Verify the effect name matches the YAML filename (without .yml)
* Ensure you ran `/shyparticles reload` after creating/editing effects
* Test with `/shyparticles list` to confirm the effect is loaded

**Q: Effect looks different than expected**

* Check particle count and density settings
* Verify RGB color values are between 0-255
* Test skip values (lower = smoother, higher = choppier)
* Make sure scale values are appropriate (typically 0.5-2.0)

**Q: Server performance issues**

* Increase skip values to reduce particle frequency
* Lower particle counts in busy effects
* Limit the number of simultaneous effects
* Use `/shyparticles stop <sessionId>` to clean up location effects

**Q: Particles not following player**

* Ensure you're using `/shyparticles follow` instead of `/shyparticles play`
* Check that the player has the required follow permissions
* Verify the effect configuration supports follow mode

**Q: How to create custom particles**

* Start by copying and modifying existing effects
* Use different particle types (FLAME, HEART, NOTE, etc.)
* Experiment with shapes and modifiers
* Test frequently with `/shyparticles reload` and `/shyparticles play`

---

## 🚀 Quick Start Checklist

1. ✅ Install ShyParticles in `/plugins/` folder
2. ✅ Restart server to generate default effect files
3. ✅ Grant permissions: `shyparticles.command` and `shyparticles.effect.start.*`
4. ✅ Test with `/shyparticles list` and `/shyparticles play blue_sphere`
5. ✅ Customize effects by editing YAML files in `/plugins/ShyParticles/effects/`
6. ✅ Use `/shyparticles reload` after making changes
7. ✅ Experiment with different shapes, colors, and modifiers!

**💡 Pro Tip:** Start with simple modifications to existing effects before creating completely new ones. The pre-built effects provide excellent examples of different techniques and configurations.

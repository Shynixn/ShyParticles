# ShyParticles

A powerful and user-friendly Minecraft particle effects plugin that allows server owners to create stunning particle effects using simple YAML configurations.

## Features

- **Easy YAML Configuration**: Create complex particle effects without any programming knowledge
- **Multiple Particle Shapes**: Circle, spiral, sphere, line, heart, star, and more
- **Dynamic Modifiers**: Rotation, fading, pulsing, waves, and random movements
- **Layered Effects**: Combine multiple particle types and shapes into one effect
- **Performance Optimized**: Adaptive performance system that reduces particles during low TPS
- **Sound Integration**: Add sound effects that play with your particle effects
- **Conditional Playback**: Set conditions like biome, world, weather, and time restrictions
- **Player-Specific Effects**: Effects can be visible to all players or just specific ones
- **Easy Commands**: Simple commands to play, stop, and manage effects

## Quick Start

1. Install the plugin in your `plugins` folder
2. Start your server to generate the configuration files
3. Check the `plugins/ShyParticles/effects/` folder for example effects
4. Use `/shyparticles play <effect_name>` to test effects
5. Create your own effects by adding YAML files to the effects folder

## Commands

- `/shyparticles help` - Show help information
- `/shyparticles play <effect> [x] [y] [z]` - Play a particle effect
- `/shyparticles stop <effectId|all>` - Stop running effects
- `/shyparticles list` - List all available effects
- `/shyparticles reload` - Reload all effects and configuration

## Permissions

- `shyparticles.command` - Basic command access
- `shyparticles.play` - Play particle effects
- `shyparticles.stop` - Stop particle effects
- `shyparticles.reload` - Reload the plugin
- `shyparticles.create` - Create new effects (future feature)

## Creating Particle Effects

### Basic Structure

```yaml
name: "my_effect"
displayName: "&e&lMy Effect"
description: "A cool particle effect"
duration: 10  # seconds (0 = infinite)
repeat: true  # repeat when finished

layers:
  - particle: "FLAME"
    shape: "circle"
    options:
      radius: 2.0
      particleCount: 12
    modifiers:
      - type: "rotate"
        speed: 30.0
```

### Available Particle Shapes

- **circle**: Particles arranged in a circle
- **sphere**: Particles arranged in a sphere
- **spiral**: Particles arranged in a spiral pattern
- **line**: Particles arranged in a straight line
- **rectangle**: Particles arranged in a rectangle
- **cube**: Particles arranged in a cube
- **heart**: Particles arranged in a heart shape
- **star**: Particles arranged in a star shape
- **point**: Single point particle
- **random**: Randomly positioned particles

### Shape Options

```yaml
options:
  radius: 2.0          # Size of circular shapes
  height: 4.0          # Height for vertical shapes
  turns: 5             # Number of turns for spirals
  density: 1.0         # Particle density (0.0-1.0)
  particleCount: 10    # Number of particles to spawn
  width: 2.0           # Width for rectangular shapes
  length: 3.0          # Length for rectangular shapes
  offsetX: 0.0         # X offset from center
  offsetY: 1.0         # Y offset from center
  offsetZ: 0.0         # Z offset from center
  speed: 0.1           # Particle movement speed
  extra: 0.0           # Extra particle data
```

### Available Modifiers

#### Rotation
```yaml
- type: "rotate"
  speed: 45.0    # degrees per second
  axis: "Y"      # X, Y, or Z axis
```

#### Fading
```yaml
- type: "fade"
  fadeTime: 3.0      # seconds to fade
  startAlpha: 1.0    # starting opacity
  endAlpha: 0.0      # ending opacity
```

#### Pulsing
```yaml
- type: "pulse"
  speed: 2.0         # pulses per second
  minScale: 0.8      # minimum scale
  maxScale: 1.2      # maximum scale
```

#### Wave Motion
```yaml
- type: "wave"
  amplitude: 1.0     # wave height
  frequency: 2.0     # wave frequency
  axis: "Y"          # wave direction
```

#### Random Movement
```yaml
- type: "random"
  strength: 0.5      # randomness strength
```

#### Static Offset
```yaml
- type: "offset"
  x: 0.5
  y: 1.0
  z: -0.5
```

### Effect Conditions

```yaml
conditions:
  minTps: 15.0                    # minimum TPS required
  maxInstances: 3                 # max concurrent instances
  allowedBiomes: ["PLAINS"]       # allowed biomes
  allowedWorlds: ["world"]        # allowed worlds
  allowedWeather: ["CLEAR"]       # allowed weather
  allowedTimes:                   # allowed time ranges
    - start: 0      # dawn
      end: 12000    # dusk
```

### Sound Effects

```yaml
sounds:
  - sound: "BLOCK_FIRE_AMBIENT"
    volume: 0.5
    pitch: 1.0
    delay: 0          # ticks
    repeat: true
    interval: 60      # ticks between repeats
```

## Example Effects

### Fire Tornado
A spinning tornado of fire and smoke particles (as shown in your example).

### Magic Portal
A swirling portal effect with multiple particle types and counter-rotating elements.

### Growing Flower
A flower that grows from the ground with blooming petals and falling pollen.

### Explosion Burst
A quick explosive effect with smoke trails and flying sparks.

## Performance Settings

Configure in `config.yml`:

```yaml
engine:
  maxParticlesPerEffect: 1000
  maxEffectsPerPlayer: 5
  maxGlobalEffects: 50
  updateFrequency: 1        # ticks
  renderDistance: 64.0

performance:
  adaptivePerformance: true
  tpsThreshold: 18.0
  reductionFactor: 0.5      # 50% particles when TPS is low
```

## Developer API

Other plugins can interact with ShyParticles:

```java
ParticleEffectService service = Bukkit.getServicesManager()
    .getRegistration(ParticleEffectService.class).getProvider();

// Play an effect
service.playEffect("fire_tornado", player.getLocation(), player);

// Stop all effects for a player
service.stopAllEffects(player);
```

## Building

Requirements:
- Java 21+
- Gradle 8+

```bash
./gradlew shadowJar
```

The compiled plugin will be in `build/libs/ShyParticles-1.0.0.jar`.

## Support

- Minecraft 1.13+ (API version 1.13)
- Spigot/Paper servers
- Folia support included

## License

This project follows the same patterns and practices as other Shynixn plugins.
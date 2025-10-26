/**
 * ShyParticles 3D Simulator
 * JavaScript implementation of the particle effect system
 */

class ParticleSimulator {
    constructor() {
        this.scene = null;
        this.camera = null;
        this.renderer = null;
        this.controls = null;
        this.effects = new Map();
        this.effectTemplates = new Map();
        this.frameCount = 0;
        this.lastTime = 0;
        this.fps = 0;

        this.initializeScene();
        this.initializeEffectTemplates();
        this.bindControls();
        this.animate();
    }

    initializeScene() {
        // Scene setup
        this.scene = new THREE.Scene();
        this.scene.background = new THREE.Color(0x001122);

        // Camera setup
        this.camera = new THREE.PerspectiveCamera(
            75,
            window.innerWidth / window.innerHeight,
            0.1,
            1000
        );
        this.camera.position.set(5, 5, 5);
        this.camera.lookAt(0, 0, 0);

        // Renderer setup
        this.renderer = new THREE.WebGLRenderer({ antialias: true });
        this.renderer.setSize(window.innerWidth, window.innerHeight);
        this.renderer.setPixelRatio(window.devicePixelRatio);
        document.getElementById('container').appendChild(this.renderer.domElement);

        // Orbit controls
        this.setupOrbitControls();

        // Lighting
        const ambientLight = new THREE.AmbientLight(0x404040, 0.4);
        this.scene.add(ambientLight);

        const directionalLight = new THREE.DirectionalLight(0xffffff, 0.8);
        directionalLight.position.set(10, 10, 5);
        this.scene.add(directionalLight);

        // Grid helper
        const gridHelper = new THREE.GridHelper(20, 20);
        gridHelper.material.transparent = true;
        gridHelper.material.opacity = 0.3;
        this.scene.add(gridHelper);

        // Axes helper
        const axesHelper = new THREE.AxesHelper(5);
        this.scene.add(axesHelper);

        // Window resize handler
        window.addEventListener('resize', () => this.onWindowResize(), false);
    }

    setupOrbitControls() {
        // Simple orbit controls implementation
        let mouseDown = false;
        let mouseX = 0;
        let mouseY = 0;
        let targetRotationX = 0;
        let targetRotationY = 0;
        let rotationX = 0;
        let rotationY = 0;

        this.renderer.domElement.addEventListener('mousedown', (event) => {
            mouseDown = true;
            mouseX = event.clientX;
            mouseY = event.clientY;
        });

        this.renderer.domElement.addEventListener('mouseup', () => {
            mouseDown = false;
        });

        this.renderer.domElement.addEventListener('mousemove', (event) => {
            if (!mouseDown) return;

            const deltaX = event.clientX - mouseX;
            const deltaY = event.clientY - mouseY;

            targetRotationY += deltaX * 0.01;
            targetRotationX += deltaY * 0.01;

            mouseX = event.clientX;
            mouseY = event.clientY;
        });

        this.renderer.domElement.addEventListener('wheel', (event) => {
            const scale = event.deltaY > 0 ? 1.1 : 0.9;
            this.camera.position.multiplyScalar(scale);
        });

        // Update camera rotation
        const updateCamera = () => {
            rotationX += (targetRotationX - rotationX) * 0.1;
            rotationY += (targetRotationY - rotationY) * 0.1;

            const distance = this.camera.position.length();
            this.camera.position.x = distance * Math.sin(rotationY) * Math.cos(rotationX);
            this.camera.position.y = distance * Math.sin(rotationX);
            this.camera.position.z = distance * Math.cos(rotationY) * Math.cos(rotationX);
            this.camera.lookAt(0, 0, 0);

            requestAnimationFrame(updateCamera);
        };
        updateCamera();
    }

    initializeEffectTemplates() {
        // Define effect templates based on YAML configurations
        this.effectTemplates.set('blue_circle', {
            name: 'blue_circle',
            duration: 10,
            repeat: true,
            layers: [{
                particle: 'dust',
                shape: 'CIRCLE',
                options: {
                    radius: 2.0,
                    particleCount: 20,
                    density: 1.0,
                    speed: 0.0,
                    red: 0,
                    green: 100,
                    blue: 255,
                    scale: 1.5
                }
            }]
        });

        this.effectTemplates.set('blue_sphere', {
            name: 'blue_sphere',
            duration: 10,
            repeat: true,
            layers: [{
                particle: 'DUST',
                shape: 'SPHERE',
                options: {
                    radius: 2.0,
                    particleCount: 100,
                    density: 1.0,
                    speed: 0.0,
                    red: 0,
                    green: 100,
                    blue: 255
                }
            }]
        });

        this.effectTemplates.set('green_spiral', {
            name: 'green_spiral',
            duration: 15,
            repeat: true,
            layers: [{
                particle: 'HAPPY_VILLAGER',
                shape: 'SPIRAL',
                options: {
                    radius: 1.5,
                    height: 3.0,
                    particleCount: 30,
                    density: 1.0,
                    speed: 0.0,
                    red: 0,
                    green: 255,
                    blue: 0
                },
                modifiers: [{
                    type: 'ROTATE',
                    speed: 30.0,
                    axis: 'Y'
                }]
            }]
        });

        this.effectTemplates.set('celestial_dance', {
            name: 'celestial_dance',
            duration: 20,
            repeat: true,
            layers: [
                {
                    particle: 'END_ROD',
                    shape: 'CIRCLE',
                    options: {
                        radius: 3.0,
                        particleCount: 16,
                        density: 1.0,
                        red: 255,
                        green: 215,
                        blue: 0
                    }
                },
                {
                    particle: 'PORTAL',
                    shape: 'SPHERE',
                    options: {
                        radius: 2.0,
                        particleCount: 50,
                        density: 0.8,
                        red: 128,
                        green: 0,
                        blue: 255
                    }
                }
            ]
        });

        this.effectTemplates.set('electric_ring', {
            name: 'electric_ring',
            duration: 8,
            repeat: true,
            layers: [{
                particle: 'SOUL_FIRE_FLAME',
                shape: 'CIRCLE',
                options: {
                    radius: 2.5,
                    particleCount: 32,
                    density: 1.0,
                    red: 0,
                    green: 255,
                    blue: 255
                }
            }]
        });

        this.effectTemplates.set('sparkle_fountain', {
            name: 'sparkle_fountain',
            duration: 12,
            repeat: true,
            layers: [{
                particle: 'FIREWORK',
                shape: 'RANDOM',
                options: {
                    radius: 1.0,
                    height: 4.0,
                    particleCount: 80,
                    density: 1.0,
                    red: 255,
                    green: 255,
                    blue: 255
                }
            }]
        });

        this.effectTemplates.set('fire_tornado', {
            name: 'fire_tornado',
            duration: 15,
            repeat: true,
            layers: [{
                particle: 'FLAME',
                shape: 'SPIRAL',
                options: {
                    radius: 1.0,
                    height: 5.0,
                    particleCount: 60,
                    density: 1.0,
                    red: 255,
                    green: 100,
                    blue: 0
                }
            }]
        });

        this.effectTemplates.set('magic_portal', {
            name: 'magic_portal',
            duration: 0, // infinite
            repeat: true,
            layers: [
                {
                    particle: 'PORTAL',
                    shape: 'CIRCLE',
                    options: {
                        radius: 2.0,
                        particleCount: 24,
                        density: 1.0,
                        red: 128,
                        green: 0,
                        blue: 255
                    }
                },
                {
                    particle: 'END_ROD',
                    shape: 'CIRCLE',
                    options: {
                        radius: 2.2,
                        particleCount: 12,
                        density: 1.0,
                        red: 255,
                        green: 255,
                        blue: 255
                    }
                }
            ]
        });
    }

    bindControls() {
        // Effect selection
        const effectSelect = document.getElementById('effectSelect');
        effectSelect.addEventListener('change', (e) => {
            if (e.target.value !== 'custom') {
                this.loadEffectTemplate(e.target.value);
            }
        });

        // Range inputs with value display
        const rangeInputs = ['particleCount', 'radius', 'height', 'speed', 'density', 'particleSize'];
        rangeInputs.forEach(id => {
            const input = document.getElementById(id);
            const display = document.getElementById(id + 'Value');
            input.addEventListener('input', (e) => {
                display.textContent = e.target.value;
            });
        });

        // Play/Stop buttons
        document.getElementById('playBtn').addEventListener('click', () => {
            this.playCurrentEffect();
        });

        document.getElementById('stopBtn').addEventListener('click', () => {
            this.stopAllEffects();
        });

        document.getElementById('resetCamera').addEventListener('click', () => {
            this.resetCamera();
        });

        // Keyboard controls
        document.addEventListener('keydown', (e) => {
            if (e.code === 'Space') {
                e.preventDefault();
                if (this.effects.size > 0) {
                    this.stopAllEffects();
                } else {
                    this.playCurrentEffect();
                }
            }
        });
    }

    loadEffectTemplate(templateName) {
        const template = this.effectTemplates.get(templateName);
        if (!template) return;

        const layer = template.layers[0]; // Use first layer for UI
        const options = layer.options;

        // Update UI controls
        document.getElementById('shapeSelect').value = layer.shape;
        document.getElementById('particleCount').value = options.particleCount || 50;
        document.getElementById('radius').value = options.radius || 2.0;
        document.getElementById('height').value = options.height || 3.0;
        document.getElementById('density').value = options.density || 1.0;
        document.getElementById('red').value = options.red || 255;
        document.getElementById('green').value = options.green || 255;
        document.getElementById('blue').value = options.blue || 255;

        // Update value displays
        document.getElementById('particleCountValue').textContent = options.particleCount || 50;
        document.getElementById('radiusValue').textContent = options.radius || 2.0;
        document.getElementById('heightValue').textContent = options.height || 3.0;
        document.getElementById('densityValue').textContent = options.density || 1.0;
    }

    getCurrentEffectConfig() {
        return {
            name: 'custom',
            duration: 0,
            repeat: true,
            layers: [{
                particle: 'custom',
                shape: document.getElementById('shapeSelect').value,
                options: {
                    radius: parseFloat(document.getElementById('radius').value),
                    height: parseFloat(document.getElementById('height').value),
                    particleCount: parseInt(document.getElementById('particleCount').value),
                    density: parseFloat(document.getElementById('density').value),
                    speed: parseFloat(document.getElementById('speed').value),
                    red: parseInt(document.getElementById('red').value),
                    green: parseInt(document.getElementById('green').value),
                    blue: parseInt(document.getElementById('blue').value),
                    particleSize: parseFloat(document.getElementById('particleSize').value)
                }
            }]
        };
    }

    playCurrentEffect() {
        const selectedTemplate = document.getElementById('effectSelect').value;
        let effectConfig;

        if (selectedTemplate === 'custom') {
            effectConfig = this.getCurrentEffectConfig();
        } else {
            effectConfig = this.effectTemplates.get(selectedTemplate);
        }

        if (effectConfig) {
            this.startEffect(effectConfig);
        }
    }

    startEffect(effectMeta) {
        const effectId = 'effect_' + Date.now();
        const effect = new ParticleEffect(effectId, effectMeta, this.scene);
        this.effects.set(effectId, effect);
        this.updateActiveEffectsList();
        return effectId;
    }

    stopAllEffects() {
        this.effects.forEach(effect => effect.close());
        this.effects.clear();
        this.updateActiveEffectsList();
    }

    stopEffect(effectId) {
        const effect = this.effects.get(effectId);
        if (effect) {
            effect.close();
            this.effects.delete(effectId);
            this.updateActiveEffectsList();
        }
    }

    updateActiveEffectsList() {
        const container = document.getElementById('activeEffects');
        container.innerHTML = '';

        this.effects.forEach((effect, id) => {
            const div = document.createElement('div');
            div.className = 'effect-item';
            div.textContent = effect.name;
            div.addEventListener('click', () => this.stopEffect(id));
            container.appendChild(div);
        });

        if (this.effects.size === 0) {
            container.innerHTML = '<div style="opacity: 0.5;">No active effects</div>';
        }
    }

    resetCamera() {
        this.camera.position.set(5, 5, 5);
        this.camera.lookAt(0, 0, 0);
    }

    onWindowResize() {
        this.camera.aspect = window.innerWidth / window.innerHeight;
        this.camera.updateProjectionMatrix();
        this.renderer.setSize(window.innerWidth, window.innerHeight);
    }

    animate() {
        requestAnimationFrame(() => this.animate());

        const currentTime = performance.now();
        this.frameCount++;

        // Update FPS counter
        if (currentTime - this.lastTime >= 1000) {
            this.fps = Math.round((this.frameCount * 1000) / (currentTime - this.lastTime));
            document.getElementById('fps').textContent = this.fps;
            this.frameCount = 0;
            this.lastTime = currentTime;
        }

        // Update all effects
        let totalParticles = 0;
        this.effects.forEach(effect => {
            effect.update(currentTime);
            totalParticles += effect.getParticleCount();
        });

        document.getElementById('particleCount').textContent = totalParticles;

        this.renderer.render(this.scene, this.camera);
    }
}

class ParticleEffect {
    constructor(id, effectMeta, scene) {
        this.id = id;
        this.effectMeta = effectMeta;
        this.scene = scene;
        this.name = effectMeta.name;
        this.layers = [];
        this.startTime = performance.now();
        this.running = true;
        this.tickCount = 0;

        this.initializeLayers();
    }

    initializeLayers() {
        this.effectMeta.layers.forEach((layerConfig, index) => {
            const layer = new ParticleLayer(layerConfig, this.scene);
            this.layers.push(layer);
        });
    }

    update(currentTime) {
        if (!this.running) return;

        const elapsed = currentTime - this.startTime;
        const duration = this.effectMeta.duration * 1000; // Convert to milliseconds

        // Check if effect should stop
        if (duration > 0 && elapsed >= duration && !this.effectMeta.repeat) {
            this.close();
            return;
        }

        // Reset for repeat
        if (duration > 0 && elapsed >= duration && this.effectMeta.repeat) {
            this.startTime = currentTime;
            this.tickCount = 0;
        }

        // Update layers
        this.layers.forEach(layer => {
            layer.update(this.tickCount, elapsed);
        });

        this.tickCount++;
    }

    getParticleCount() {
        return this.layers.reduce((total, layer) => total + layer.getParticleCount(), 0);
    }

    close() {
        this.running = false;
        this.layers.forEach(layer => layer.dispose());
        this.layers = [];
    }
}

class ParticleLayer {
    constructor(layerConfig, scene) {
        this.config = layerConfig;
        this.scene = scene;
        this.particles = [];
        this.particleSystem = null;
        this.geometry = null;
        this.material = null;

        this.initializeParticleSystem();
    }

    initializeParticleSystem() {
        const options = this.config.options;
        const particleCount = Math.floor(options.particleCount * options.density);

        // Create geometry
        this.geometry = new THREE.BufferGeometry();
        const positions = new Float32Array(particleCount * 3);
        const colors = new Float32Array(particleCount * 3);
        const sizes = new Float32Array(particleCount);

        // Generate initial particle positions
        const points = this.generateShapePoints(this.config.shape, options, particleCount);

        for (let i = 0; i < particleCount; i++) {
            const point = points[i] || new THREE.Vector3();
            positions[i * 3] = point.x;
            positions[i * 3 + 1] = point.y;
            positions[i * 3 + 2] = point.z;

            // Set particle color
            colors[i * 3] = (options.red || 255) / 255;
            colors[i * 3 + 1] = (options.green || 255) / 255;
            colors[i * 3 + 2] = (options.blue || 255) / 255;

            // Set particle size
            sizes[i] = options.particleSize || 0.05;
        }

        this.geometry.setAttribute('position', new THREE.BufferAttribute(positions, 3));
        this.geometry.setAttribute('color', new THREE.BufferAttribute(colors, 3));
        this.geometry.setAttribute('size', new THREE.BufferAttribute(sizes, 1));

        // Create material
        this.material = new THREE.ShaderMaterial({
            uniforms: {
                time: { value: 0.0 }
            },
            vertexShader: `
                attribute float size;
                attribute vec3 color;
                varying vec3 vColor;
                uniform float time;
                
                void main() {
                    vColor = color;
                    vec4 mvPosition = modelViewMatrix * vec4(position, 1.0);
                    gl_PointSize = size * 300.0 / -mvPosition.z;
                    gl_Position = projectionMatrix * mvPosition;
                }
            `,
            fragmentShader: `
                varying vec3 vColor;
                
                void main() {
                    float distance = length(gl_PointCoord - vec2(0.5, 0.5));
                    if (distance > 0.5) discard;
                    
                    float alpha = 1.0 - (distance * 2.0);
                    gl_FragColor = vec4(vColor, alpha);
                }
            `,
            transparent: true,
            blending: THREE.AdditiveBlending,
            depthWrite: false
        });

        // Create particle system
        this.particleSystem = new THREE.Points(this.geometry, this.material);
        this.scene.add(this.particleSystem);
    }

    generateShapePoints(shapeType, options, particleCount) {
        const points = [];
        const density = options.density || 1.0;
        const actualCount = Math.floor(particleCount * density);

        switch (shapeType.toLowerCase()) {
            case 'circle':
                for (let i = 0; i < actualCount; i++) {
                    const angle = (2 * Math.PI * i) / actualCount;
                    const x = options.radius * Math.cos(angle);
                    const z = options.radius * Math.sin(angle);
                    points.push(new THREE.Vector3(x, 0, z));
                }
                break;

            case 'sphere':
                for (let i = 0; i < actualCount; i++) {
                    const phi = Math.acos(-1 + (2 * i) / actualCount);
                    const theta = Math.sqrt(actualCount * Math.PI) * phi;
                    const x = options.radius * Math.cos(theta) * Math.sin(phi);
                    const y = options.radius * Math.cos(phi);
                    const z = options.radius * Math.sin(theta) * Math.sin(phi);
                    points.push(new THREE.Vector3(x, y, z));
                }
                break;

            case 'spiral':
                const turns = options.turns || 3;
                for (let i = 0; i < actualCount; i++) {
                    const t = i / actualCount;
                    const angle = 2 * Math.PI * turns * t;
                    const height = (options.height || 3) * t;
                    const radius = options.radius * (1 - t * 0.3); // Spiral inward
                    const x = radius * Math.cos(angle);
                    const y = height;
                    const z = radius * Math.sin(angle);
                    points.push(new THREE.Vector3(x, y, z));
                }
                break;

            case 'line':
                for (let i = 0; i < actualCount; i++) {
                    const t = i / actualCount;
                    const y = (options.height || 3) * t;
                    points.push(new THREE.Vector3(0, y, 0));
                }
                break;

            case 'cube':
                const size = options.radius || 1;
                for (let i = 0; i < actualCount; i++) {
                    const x = (Math.random() - 0.5) * size * 2;
                    const y = (Math.random() - 0.5) * size * 2;
                    const z = (Math.random() - 0.5) * size * 2;
                    points.push(new THREE.Vector3(x, y, z));
                }
                break;

            case 'heart':
                for (let i = 0; i < actualCount; i++) {
                    const t = (i / actualCount) * 2 * Math.PI;
                    const scale = options.radius || 1;
                    const x = scale * 16 * Math.pow(Math.sin(t), 3) / 16;
                    const y = scale * (13 * Math.cos(t) - 5 * Math.cos(2 * t) - 2 * Math.cos(3 * t) - Math.cos(4 * t)) / 16;
                    points.push(new THREE.Vector3(x, y, 0));
                }
                break;

            case 'star':
                const starPoints = 5;
                const outerRadius = options.radius || 1;
                const innerRadius = outerRadius * 0.4;
                for (let i = 0; i < actualCount; i++) {
                    const angle = (i / actualCount) * 2 * Math.PI;
                    const pointIndex = Math.floor((angle / (2 * Math.PI)) * starPoints * 2);
                    const isOuter = pointIndex % 2 === 0;
                    const radius = isOuter ? outerRadius : innerRadius;
                    const starAngle = (pointIndex / (starPoints * 2)) * 2 * Math.PI;
                    const x = radius * Math.cos(starAngle);
                    const z = radius * Math.sin(starAngle);
                    points.push(new THREE.Vector3(x, 0, z));
                }
                break;

            case 'random':
                const spreadRadius = options.radius || 1;
                const spreadHeight = options.height || 3;
                for (let i = 0; i < actualCount; i++) {
                    const x = (Math.random() - 0.5) * spreadRadius * 2;
                    const y = Math.random() * spreadHeight;
                    const z = (Math.random() - 0.5) * spreadRadius * 2;
                    points.push(new THREE.Vector3(x, y, z));
                }
                break;

            case 'point':
            default:
                for (let i = 0; i < actualCount; i++) {
                    points.push(new THREE.Vector3(0, 0, 0));
                }
                break;
        }

        return points;
    }

    update(tickCount, elapsed) {
        if (!this.material) return;

        // Update time uniform for shader animations
        this.material.uniforms.time.value = elapsed / 1000;

        // Apply modifiers
        if (this.config.modifiers) {
            this.config.modifiers.forEach(modifier => {
                this.applyModifier(modifier, tickCount, elapsed);
            });
        }

        // Apply animation based on speed
        const speed = this.config.options.speed || 1.0;
        if (this.config.shape.toLowerCase() === 'spiral' || this.config.shape.toLowerCase() === 'circle') {
            this.particleSystem.rotation.y = (elapsed / 1000) * speed * 0.5;
        }
    }

    applyModifier(modifier, tickCount, elapsed) {
        switch (modifier.type.toLowerCase()) {
            case 'rotate':
                const rotationSpeed = modifier.speed || 30.0;
                const axis = modifier.axis || 'Y';
                const rotation = (elapsed / 1000) * (rotationSpeed / 10);

                if (axis.toUpperCase() === 'Y') {
                    this.particleSystem.rotation.y = rotation;
                } else if (axis.toUpperCase() === 'X') {
                    this.particleSystem.rotation.x = rotation;
                } else if (axis.toUpperCase() === 'Z') {
                    this.particleSystem.rotation.z = rotation;
                }
                break;

            case 'pulse':
                const pulseScale = 1 + 0.3 * Math.sin((elapsed / 1000) * 4);
                this.particleSystem.scale.setScalar(pulseScale);
                break;

            case 'wave':
                const amplitude = modifier.amplitude || 1.0;
                const frequency = modifier.frequency || 1.0;
                this.particleSystem.position.y = amplitude * Math.sin((elapsed / 1000) * frequency * 2);
                break;
        }
    }

    getParticleCount() {
        return this.geometry ? this.geometry.attributes.position.count : 0;
    }

    dispose() {
        if (this.particleSystem) {
            this.scene.remove(this.particleSystem);
        }
        if (this.geometry) {
            this.geometry.dispose();
        }
        if (this.material) {
            this.material.dispose();
        }
    }
}

// Initialize the simulator when the page loads
document.addEventListener('DOMContentLoaded', () => {
    window.particleSimulator = new ParticleSimulator();
});

// Export for external use
window.ParticleSimulator = ParticleSimulator;
window.ParticleEffect = ParticleEffect;
window.ParticleLayer = ParticleLayer;
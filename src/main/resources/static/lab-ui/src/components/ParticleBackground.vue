<template>
  <div class="particle-container">
    <div class="particle-overlay">
      <slot></slot>
    </div>
    <canvas ref="particleCanvas" class="particle-canvas"></canvas>
  </div>
</template>

<script setup>
import { ref, onMounted, onUnmounted } from 'vue';

const particleCanvas = ref(null);
let animationFrame = null;
let particles = [];
let ctx;

// Define resizeCanvas in the component scope so it can be referenced in onUnmounted
const resizeCanvas = () => {
  if (!particleCanvas.value) return;
  const canvas = particleCanvas.value;
  canvas.width = canvas.offsetWidth;
  canvas.height = canvas.offsetHeight;
  initParticles();
};

onMounted(() => {
  const canvas = particleCanvas.value;
  if (!canvas) return;

  ctx = canvas.getContext('2d');

  // Set canvas size to match container
  window.addEventListener('resize', resizeCanvas);
  resizeCanvas();

  // Start animation
  animate();
});

onUnmounted(() => {
  window.removeEventListener('resize', resizeCanvas);
  if (animationFrame) {
    cancelAnimationFrame(animationFrame);
    animationFrame = null;
  }
  // Clear particles to free memory
  particles = [];
});

function initParticles() {
  particles = [];
  const particleCount = Math.floor(particleCanvas.value.width * particleCanvas.value.height / 10000);

  // Create gate-like structure (rectangular frame)
  createGateStructure();
}

function createGateStructure() {
  const canvas = particleCanvas.value;
  const width = canvas.width;
  const height = canvas.height;

  // Gate dimensions
  const gateWidth = width * 0.6;
  const gateHeight = height * 0.8;
  const gateX = (width - gateWidth) / 2;
  const gateY = (height - gateHeight) / 2;

  // Number of particles for the gate
  const particleDensity = 0.05; // Adjust for more/less particles
  const particlesPerSide = Math.floor(Math.max(gateWidth, gateHeight) * particleDensity);

  // Create particles for the gate frame
  // Top side
  for (let i = 0; i < particlesPerSide; i++) {
    const x = gateX + (i / particlesPerSide) * gateWidth;
    const y = gateY;
    particles.push(createParticle(x, y));
  }

  // Right side
  for (let i = 0; i < particlesPerSide; i++) {
    const x = gateX + gateWidth;
    const y = gateY + (i / particlesPerSide) * gateHeight;
    particles.push(createParticle(x, y));
  }

  // Bottom side
  for (let i = 0; i < particlesPerSide; i++) {
    const x = gateX + gateWidth - (i / particlesPerSide) * gateWidth;
    const y = gateY + gateHeight;
    particles.push(createParticle(x, y));
  }

  // Left side
  for (let i = 0; i < particlesPerSide; i++) {
    const x = gateX;
    const y = gateY + gateHeight - (i / particlesPerSide) * gateHeight;
    particles.push(createParticle(x, y));
  }

  // Add some random particles inside the gate
  const innerParticleCount = particlesPerSide * 2;
  for (let i = 0; i < innerParticleCount; i++) {
    const x = gateX + Math.random() * gateWidth;
    const y = gateY + Math.random() * gateHeight;
    particles.push(createParticle(x, y));
  }
}

function createParticle(x, y) {
  return {
    x,
    y,
    size: Math.random() * 2 + 1,
    speedX: (Math.random() - 0.5) * 0.3,
    speedY: (Math.random() - 0.5) * 0.3,
    color: `rgba(255, 255, 255, ${Math.random() * 0.5 + 0.2})`,
    // Add a slight purple tint to some particles
    tint: Math.random() > 0.8 ? 'rgba(138, 43, 226, 0.6)' : null
  };
}

function animate() {
  if (!particleCanvas.value || !ctx) return;

  ctx.clearRect(0, 0, particleCanvas.value.width, particleCanvas.value.height);

  // Update and draw particles
  particles.forEach(particle => {
    // Update position with small movement
    particle.x += particle.speedX;
    particle.y += particle.speedY;

    // Bounce off edges
    if (particle.x < 0 || particle.x > particleCanvas.value.width) {
      particle.speedX *= -1;
    }
    if (particle.y < 0 || particle.y > particleCanvas.value.height) {
      particle.speedY *= -1;
    }

    // Draw particle
    ctx.fillStyle = particle.tint || particle.color;
    ctx.beginPath();
    ctx.arc(particle.x, particle.y, particle.size, 0, Math.PI * 2);
    ctx.fill();

    // Draw connections between nearby particles
    connectParticles(particle);
  });

  animationFrame = requestAnimationFrame(animate);
}

function connectParticles(particle) {
  const maxDistance = 120;

  particles.forEach(otherParticle => {
    if (particle === otherParticle) return;

    const dx = particle.x - otherParticle.x;
    const dy = particle.y - otherParticle.y;
    const distance = Math.sqrt(dx * dx + dy * dy);

    if (distance < maxDistance) {
      // Draw line with opacity based on distance
      const opacity = 1 - (distance / maxDistance);

      // Add a purple tint to some connections
      const isPurple = particle.tint || otherParticle.tint;
      const color = isPurple ?
        `rgba(138, 43, 226, ${opacity * 0.3})` :
        `rgba(255, 255, 255, ${opacity * 0.2})`;

      ctx.strokeStyle = color;
      ctx.lineWidth = 0.5;
      ctx.beginPath();
      ctx.moveTo(particle.x, particle.y);
      ctx.lineTo(otherParticle.x, otherParticle.y);
      ctx.stroke();
    }
  });
}
</script>

<style scoped>
.particle-container {
  position: relative;
  width: 100%;
  height: 100%;
  overflow: hidden;
}

.particle-canvas {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  z-index: 1;
}

.particle-overlay {
  position: relative;
  width: 100%;
  height: 100%;
  z-index: 2;
  display: flex;
  align-items: center;
  justify-content: center;
}
</style>

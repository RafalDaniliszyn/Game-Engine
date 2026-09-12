#version 460 core

in vec2 passTexture;

out vec4 FragColor;

uniform sampler2D ourTexture;
uniform float time;
uniform float wind;
uniform float seed;
uniform vec2 xy;         // Pozycja obiektu na mapie
uniform float windWave;   // Mnożnik fali

float rand(vec2 co) {
    return fract(sin(dot(co, vec2(12.9898, 78.233))) * 43758.5453);
}

void main() {
    float weight = passTexture.y;

    float speed = 1.5;

    float waveDistortion = xy.y + xy.x;
    float phase = speed + seed;

    float wave = sin((phase+windWave));
    wave += sin((phase + cos(phase)) * 0.09125);

    wave *= 1.0 + smoothstep(0, 0.5, windWave)-0.5;

    wave *= weight;
    wave *= windWave;
    wave *= wind;

    vec2 uv = passTexture;

    uv.x += length(vec2(uv.y, 0.0)) * 0.1647 * wave;

    if (texture(ourTexture, uv).a < 0.1) {
        discard;
    }
    FragColor = texture(ourTexture, uv);
}
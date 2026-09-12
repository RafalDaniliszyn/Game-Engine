#version 460 core

layout (location = 0) in vec2 position;
layout (location = 1) in vec2 textureCoord;

out vec2 passTexture;

uniform mat4 MVP;
uniform float depth;
uniform float time;
uniform float wind;

void main() {
    vec2 pos = position;

    float weight = position.y;

    float treeSeed = MVP[3].x + MVP[3].y;
    float speed = 1.3;
    float phase = time * speed + treeSeed;
    float wave = sin(phase);
    wave += sin((phase + cos(phase)) * 0.4125);
    wave *= weight;

    wave *= wind;

    gl_Position = MVP * vec4(pos, depth, 1.0);
    passTexture = textureCoord;
}
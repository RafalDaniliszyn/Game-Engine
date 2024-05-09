#version 460 core

layout (location = 0) in vec2 position;
layout (location = 1) in vec2 textureCoord;

out vec2 passTexture;

uniform mat4 MVP;
uniform float depth;

void main() {
    gl_Position = MVP * vec4(position, depth, 1.0);
    passTexture = textureCoord;
}
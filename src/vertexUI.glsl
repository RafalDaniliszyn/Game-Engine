#version 460 core

layout (location = 0) in vec2 position;
layout (location = 1) in vec2 textureCoord;

out vec2 passTexture;

uniform mat4 transformation;

void main() {
    gl_Position = transformation * vec4(position, 0.1, 1.0);
    passTexture = textureCoord;
}
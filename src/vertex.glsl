#version 460 core

layout (location = 0) in vec3 position;
layout (location = 1) in vec4 color;
layout (location = 2) in vec2 textureCoord;

out vec4 passColor;
out vec3 passTextureCoord;
out vec2 passTexture;

uniform mat4 MVP;

void main() {
    gl_Position = MVP * vec4(position, 1.0);
    passColor = color;
    passTextureCoord = position;
    passTexture = textureCoord;
}
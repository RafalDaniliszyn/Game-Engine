#version 460 core

in vec2 passTexture;

out vec4 FragColor;

uniform sampler2D ourTexture;

void main() {
    FragColor = texture(ourTexture, passTexture);
}
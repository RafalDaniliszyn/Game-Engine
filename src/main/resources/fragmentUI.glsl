#version 460 core

in vec2 passTexture;

out vec4 FragColor;

uniform sampler2D ourTexture;

void main() {
    if (texture(ourTexture, passTexture).a < 0.1) {
        discard;
    }
    FragColor = texture(ourTexture, passTexture);
}
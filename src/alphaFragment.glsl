#version 460 core

in vec4 passColor;
in vec3 passTextureCoord;
in vec2 passTexture;

out vec4 FragColor;

uniform sampler2D ourTexture;
uniform samplerCube cubemap;

void main() {
    if (texture(ourTexture, passTexture).a < 0.1) {
        discard;
    }
    FragColor = texture(ourTexture, passTexture) * passColor;
}
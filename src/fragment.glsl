#version 460 core

in vec4 passColor;
in vec3 passTextureCoord;
in vec2 passTexture;

out vec4 FragColor;

uniform int textureType; // 0 for 2D texture, 1 for cubemap
uniform sampler2D ourTexture;
uniform samplerCube cubemap;

void main() {

    if(textureType == 0) {
        FragColor = texture(ourTexture, passTexture);
    }else {
        vec2 col = passTexture;
        FragColor = texture(cubemap, passTextureCoord);
    }

}
#version 460 core

in vec4 passColor;
in vec3 passTextureCoord;
in vec2 passTexture;

in vec3 surfaceNormal;
in vec3 toLightVector;

out vec4 FragColor;

uniform sampler2D ourTexture;
uniform samplerCube cubemap;
uniform vec3 lightColor;

void main() {
    if (texture(ourTexture, passTexture).a < 0.1) {
        discard;
    }
    vec3 unitNormal = normalize(surfaceNormal);
    vec3 unitLightVector = normalize(toLightVector);
    float nDot1 = dot(unitNormal, unitLightVector);
    float brightness = max(nDot1, 0.1);
    vec3 diffuse = brightness * lightColor;
    FragColor = vec4(diffuse, 1.0) * texture(ourTexture, passTexture);
}
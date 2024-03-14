#version 460 core

layout (location = 0) in vec3 position;
layout (location = 1) in vec4 color;
layout (location = 2) in vec2 textureCoord;
layout (location = 3) in vec3 normal;

out vec4 passColor;
out vec3 passTextureCoord;
out vec2 passTexture;
out vec3 surfaceNormal;
out vec3 toLightVector;

uniform mat4 MVP;
uniform mat4 transformationMatrix;
uniform vec3 lightPosition;
uniform float time;

void main() {
    vec3 pos = vec3(position.x + (sin(position.y * time))*0.03, position.y, position.z + (cos(position.y * time)*0.03));
    gl_Position = MVP * vec4(pos, 1.0);

    vec4 worldPosition = transformationMatrix * vec4(position, 1.0);
    surfaceNormal = (transformationMatrix * vec4(normal, 0.0)).xyz;
    toLightVector = lightPosition - worldPosition.xyz;
    passColor = color;
    passTextureCoord = position;
    passTexture = textureCoord;
}
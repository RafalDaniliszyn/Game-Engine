#version 330 core

in vec2 uv;
out vec4 FragColor;

uniform vec2 lightPos;
uniform float lightRadius;
uniform vec3 lightColor;
uniform float intensity;

void main() {
    vec2 pos = uv;

    float dist = distance(pos, lightPos);
    float normalizedDist = dist / lightRadius;

    float attenuation = 1.0 / (1.0 + normalizedDist * normalizedDist * intensity);

    float edge = 1.0 - smoothstep(0.0, 1.0, normalizedDist);
    edge = pow(edge, 2.0);

    float strength = attenuation * edge;

    vec3 color = lightColor * strength * intensity;

    FragColor = vec4(color, 1.0);
}

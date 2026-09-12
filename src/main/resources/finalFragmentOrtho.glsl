#version 330 core
in vec2 TexCoords;

out vec4 FragColor;

uniform sampler2D sceneTexture;
uniform sampler2D lightTexture;

void main()
{
    vec3 scene = texture(sceneTexture, TexCoords).rgb;
    vec3 light = texture(lightTexture, TexCoords).rgb;

    // LIGHTING COMBINATION
    //vec3 ambient = vec3(0.0003, 0.00012, 0.000144);
    vec3 ambient = vec3(0.5);
    vec3 result = scene * (light + ambient);

    FragColor = vec4(result, 1.0);
}
#version 300 es
#extension GL_OES_EGL_image_external_essl3 : require
precision highp float;

in vec2 texCoo2Frag;
out vec4 outColor;
layout (location = 4) uniform samplerExternalOES sTexture;
layout (location = 5) uniform float uProgress;
const float PI = 3.14159265;
const float uD = 80.0;
const float uR = 1.0;

void main()
{
    outColor = texture(sTexture, texCoo2Frag);

}

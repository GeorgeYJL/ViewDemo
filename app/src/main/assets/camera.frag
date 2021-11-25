#version 300 es
#extension GL_OES_EGL_image_external_essl3 : require

precision mediump float;

in vec2 v_texCoord;
out vec4 outColor;
uniform samplerExternalOES s_texture;

void main(){
//    outColor = texture(s_texture, v_texCoord);

    vec2 pos = v_texCoord.xy;
    if (pos.y > 0.5) {
        pos.y = 1.0 - pos.y;
    }


    vec4 color = texture(s_texture, pos);
    outColor = vec4(color.g,color.g,color.g,1.0);//灰度图片特效




}

//    vec4 color = texture2D(vTexture,aCoordinate);
//    gl_FragColor = vec4(color.g,color.g,color.g,1.0);//灰度图片特效
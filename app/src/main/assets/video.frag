#extension GL_OES_EGL_image_external : require
precision highp float;
varying vec2 vTexCoord2Frag;
uniform samplerExternalOES uSTexture;
void main() {
    
    //////////////////////////////////////////////////////////////////////
    //// 正常播放
    /////////////////////////////////////////////////////////////////////
//    gl_FragColor = texture2D(uSTexture,vTexCoord2Frag);



    vec2 pos = vTexCoord2Frag.xy;
    ////////////////////////////////////////////////////////////////////////
    //// 左右镜像
    /////////////////////////////////////////////////////////////////////
//    if (pos.x > 0.5) {
//        pos.x = 1.0 - pos.x;
//        gl_FragColor=texture2D(uSTexture, pos);
//    }else{
//        gl_FragColor=texture2D(uSTexture, pos);
//
//    }


    //////////////////////////////////////////////////////////////////////
    //// 左黑白 右负片
    /////////////////////////////////////////////////////////////////////
//    if (pos.x > 0.5) {
//        vec3 color=texture2D(uSTexture, pos).rgb;
//        //    //负片特效
//            float r = 1.0 - color.r;
//            float g = 1.0 - color.g;
//            float b = 1.0 - color.b;
//            gl_FragColor = vec4(r,g,b,1.0);
//    }else{
//        vec3 centralColor = texture2D(uSTexture, vTexCoord2Frag).rgb;
//        gl_FragColor = vec4(0.299*centralColor.r+0.587*centralColor.g+0.114*centralColor.b);
//
//    }


    //////////////////////////////////////////////////////////////////////
    //// 四分 + 特效
    /////////////////////////////////////////////////////////////////////

        vec4 result;
        if(pos.x <= 0.5 && pos.y<= 0.5){ //左上
            pos.x = pos.x * 2.0;
            pos.y = pos.y * 2.0;
            vec4 color = texture2D(uSTexture, pos);
            result = vec4(color.g, color.g, color.g, 1.0);
        }else if  (pos.x > 0.5 && pos.y< 0.5) {//右上
            pos.x = (pos.x - 0.5) * 2.0;
            pos.y = (pos.y) * 2.0;
            vec4 color= texture2D(uSTexture, pos);
            result = color;
        }else if (pos.y> 0.5 && pos.x < 0.5) {//左下
            pos.y = (pos.y - 0.5) * 2.0;
            pos.x = pos.x * 2.0;
            vec4 color= texture2D(uSTexture, pos);

            result = color;
        }else  if (pos.y> 0.5 && pos.x > 0.5){//右下
            pos.y = (pos.y - 0.5) * 2.0;
            pos.x = (pos.x - 0.5) * 2.0;
            vec4 color= texture2D(uSTexture, pos);
            float r = color.r;
            float g = color.g;
            float b = color.b;
            b = 0.393* r + 0.769 * g + 0.189* b;
            g = 0.349 * r + 0.686 * g + 0.168 * b;
            r = 0.272 * r + 0.534 * g + 0.131 * b;
            result = vec4(r, g, b, 1.0);
        }
        gl_FragColor = result;

    
    
//        gl_FragColor = texture2D(uSTexture,pos);

}





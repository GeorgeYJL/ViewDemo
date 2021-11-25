precision mediump float;
varying vec2 aCoordinate;
uniform sampler2D vTexture;


const float threshold = 0.3;//阈值
void main() {
//    vec4 color = texture2D(vTexture,aCoordinate);
//    gl_FragColor = vec4(color.g,color.g,color.g,1.0);//灰度图片特效


//    float r = color.r;
//    float g = color.g;
//    float b = color.b;
//    g = r * 0.3 + g * 0.59 + b * 0.11;
//    g= g <= threshold ? 0.0 : 1.0;
//    gl_FragColor = vec4(color.g,color.g,color.g,1.0);


//    //负片特效
//    float r = 1.0 - color.r;
//    float g = 1.0 - color.g;
//    float b = 1.0 - color.b;
//    gl_FragColor = vec4(r,g,b,1.0);



////    x
//    vec2 pos = aCoordinate.xy;
//    pos.x = 1.0 -pos.x;
//    gl_FragColor = texture2D(vTexture,pos);

////y
//    vec2 pos = aCoordinate.xy;
//    pos.y = 1.0 -pos.y;
//    gl_FragColor = texture2D(vTexture,pos);

    vec2 pos = aCoordinate.xy;
    //    上下分屏
//    if (pos.y<= 0.5) {
//        pos.y = pos.y ;
//    }else{
//        pos.y = pos.y - 0.5;
//    }
    //    左右镜像
//    if (pos.x > 0.5) {
//        pos.x = 1.0 - pos.x;
//    }

//    // 四分
//    if(pos.x <= 0.5){
//        pos.x = pos.x * 2.0;
//    }else{
//        pos.x = (pos.x - 0.5) * 2.0;
//    }
//
//    if (pos.y<= 0.5) {
//        pos.y = pos.y * 2.0;
//    }else{
//        pos.y = (pos.y - 0.5) * 2.0;
//    }


    // 四分 + 特效
//    vec4 result;
//    if(pos.x <= 0.5 && pos.y<= 0.5){ //左上
//        pos.x = pos.x * 2.0;
//        pos.y = pos.y * 2.0;
//        vec4 color = texture2D(vTexture, pos);
//        result = vec4(color.g, color.g, color.g, 1.0);
//    }else if (pos.x >= 0.5 && pos.y<= 0.5){//右上
//        pos.x = (pos.x - 0.5) * 2.0;
//        pos.y = (pos.y - 0.5) * 2.0;
//        vec4 color= texture2D(vTexture, pos);
//        float r = color.r;
//        float g = color.g;
//        float b = color.b;
//        g = r * 0.3 + g * 0.59 + b * 0.11;
//        g= g <= 0.4 ? 0.0 : 1.0;
//        result = vec4(g, g, g, 1.0);
//    }else if (pos.y> 0.5 && pos.x < 0.5) {//左下
//        pos.y = pos.y * 2.0;
//        pos.x = pos.x * 2.0;
//        vec4 color= texture2D(vTexture, pos);
//        float r = color.r;
//        float g = color.g;
//        float b = color.b;
//        r = 0.393* r + 0.769 * g + 0.189* b;
//        g = 0.349 * r + 0.686 * g + 0.168 * b;
//        b = 0.272 * r + 0.534 * g + 0.131 * b;
//        result = vec4(r, g, b, 1.0);
//    }else  if (pos.y> 0.5 && pos.x > 0.5){//右下
//        pos.y = (pos.y - 0.5) * 2.0;
//        pos.x = (pos.x - 0.5) * 2.0;
//        vec4 color= texture2D(vTexture, pos);
//        float r = color.r;
//        float g = color.g;
//        float b = color.b;
//        b = 0.393* r + 0.769 * g + 0.189* b;
//        g = 0.349 * r + 0.686 * g + 0.168 * b;
//        r = 0.272 * r + 0.534 * g + 0.131 * b;
//        result = vec4(r, g, b, 1.0);
//    }
//    gl_FragColor = result;
//    gl_FragColor = texture2D(vTexture,pos);


    // 局部特效
//    float centerX =0.4;
//    float centerY =0.8;
//    float raduius =0.3;
//    vec4 color=  texture2D(vTexture,pos);
//
//    float r = color.r;
//    float g = color.g;
//    float b = color.b;
//    if((pos.x-centerX)*(pos.x-centerX)+(pos.y-centerY)*(pos.y-centerY)<raduius*raduius){//表示在圆的区域内
//        gl_FragColor = vec4(g, g, g, 1.0);
//    }else{
//        gl_FragColor = vec4(r, g, b, 1.0);
//    }


//    马赛克效果
//    float cellX= 1.0;
//    float cellY= 1.0;
//    float rowCount=100.0;
//    float rate= 1000.0/1369.0;
//    pos.x = pos.x*rowCount;
//    pos.y = pos.y*rowCount/rate;
//
//    pos = vec2(floor(pos.x/cellX)*cellX/rowCount, floor(pos.y/cellY)*cellY/(rowCount/rate))+ 0.5/rowCount*vec2(cellX, cellY);
//    gl_FragColor = texture2D(vTexture,pos);


    float rate= 1000.0/1369.0;
    float cellX= 1.0;
    float cellY=1.0;
    float rowCount=100.0;

    vec2 sizeFmt=vec2(rowCount, rowCount/rate);
    vec2 sizeMsk=vec2(cellX, cellY/rate);
    vec2 posFmt = vec2(aCoordinate.x*sizeFmt.x, aCoordinate.y*sizeFmt.y);
    vec2 posMsk = vec2(floor(posFmt.x/sizeMsk.x)*sizeMsk.x, floor(posFmt.y/sizeMsk.y)*sizeMsk.y)+ 0.5*sizeMsk;

    float del = length(posMsk - posFmt);

    vec2 UVMosaic = vec2(posMsk.x/sizeFmt.x, posMsk.y/sizeFmt.y);

    vec4 result;
    if (del< cellX/2.0){

        result = texture2D(vTexture,UVMosaic);
    } else{

        result = vec4(1.0,1.0,1.0,0.0);
    }

    gl_FragColor = result;



}

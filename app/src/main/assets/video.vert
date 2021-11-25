attribute vec4 aPosition;//顶点位置
attribute vec4 aTexCoord;//纹理坐标
uniform mat4 uMatrix; //顶点变换矩阵
uniform mat4 uSTMatrix; //纹理变换矩阵

varying  vec2 vTexCoord2Frag;//片元颜色

void main() {
    vTexCoord2Frag = (uSTMatrix * aTexCoord).xy;
    gl_Position = uMatrix*aPosition;
}

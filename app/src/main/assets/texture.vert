attribute vec3 vPosition;//
uniform mat4 uMVPMatrix;
attribute vec2 vCoordinate;

varying vec2 aCoordinate;

void main() {
    gl_Position = uMVPMatrix*vec4(vPosition, 1.0);
    aCoordinate = vCoordinate;
}

#version 300 es
in vec4 a_Position;
in vec2 a_texCoord;

out vec2 v_texCoord;

void main(){
    gl_Position = a_Position;
    v_texCoord = a_texCoord;
}
//
//    #version 300 es
//
//layout (location = 0) in vec4 a_Position;
//layout (location = 1) in vec2 a_texCoord;
//
//out vec2 v_texCoord;
//
//void main()
//{
//    gl_Position = a_Position;
//    v_texCoord = a_texCoord;
//}


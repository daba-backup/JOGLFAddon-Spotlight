#version 330

struct Fog{
    float start;
    float end;
    vec4 color;
};

uniform Fog fog;
uniform sampler2D texture_sampler;

in vec2 vs_out_uv;
in vec4 vs_out_color;
in float vs_out_fog_factor;
out vec4 fs_out_color;

void main(){
    fs_out_color=mix(fog.color,vs_out_color*texture(texture_sampler,vs_out_uv),vs_out_fog_factor);
}

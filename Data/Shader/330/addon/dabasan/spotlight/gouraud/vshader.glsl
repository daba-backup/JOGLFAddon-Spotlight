#version 330

struct Camera{
    vec3 position;
    vec3 target;
    mat4 projection;
    mat4 view_transformation;
    float near;
    float far;
};
struct Spotlight{
    bool enabled;

    vec3 position;
    vec3 direction;
    float attenuation;
    float phi;
    float theta;
    float falloff;
    vec4 diffuse_color;
    vec4 specular_color;
    float diffuse_power;
    float specular_power;

    float color_clamp_min;
    float color_clamp_max;
};

const int MAX_SPOTLIGHT_NUM=256;

uniform Camera camera;
uniform Spotlight lights[MAX_SPOTLIGHT_NUM];
uniform int current_spotlight_num;
uniform float spotlight_color_sum_clamp_min;
uniform float spotlight_color_sum_clamp_max;

layout(location=0) in vec3 vs_in_position;
layout(location=1) in vec2 vs_in_uv;
layout(location=2) in vec3 vs_in_normal;
out vec2 vs_out_uv;
out vec4 vs_out_color;

void main(){
    mat4 camera_matrix=camera.projection*camera.view_transformation;
    gl_Position=camera_matrix*vec4(vs_in_position,1.0);

    vs_out_uv=vs_in_uv;

    int bound=min(current_spotlight_num,MAX_SPOTLIGHT_NUM);
    vec4 spotlight_color_sum=vec4(0.0,0.0,0.0,1.0);
    for(int i=0;i<bound;i++){
        if(lights[i].enabled==false){
            continue;
        }

        vec3 r=vs_in_position-lights[i].position;
        float length_r=length(r);
        float attenuation=1.0/(lights[i].attenuation*length_r);

        vec3 normalized_r=normalize(r);

        float cos_alpha=dot(normalized_r,lights[i].direction);
        float cos_half_theta=cos(lights[i].theta/2.0);
        float cos_half_phi=cos(lights[i].phi/2.0);

        vec4 spotlight_color;
        if(cos_alpha<=cos_half_phi){
            spotlight_color=vec4(0.0,0.0,0.0,1.0);
        }
        else{
            if(cos_alpha<=cos_half_theta){
                attenuation*=pow((cos_alpha-cos_half_phi)/(cos_half_theta-cos_half_phi),lights[i].falloff);
            }

            vec3 half_le=-normalize(camera.target+lights[i].direction);
            float specular_coefficient=pow(clamp(dot(vs_in_normal,half_le),0.0,1.0),2.0);

            vec4 diffuse_color=vec4(lights[i].diffuse_color*attenuation*lights[i].diffuse_power);
            vec4 specular_color=vec4(lights[i].specular_color*specular_coefficient*lights[i].specular_power);

            spotlight_color=diffuse_color+specular_color;
            spotlight_color=clamp(spotlight_color,lights[i].color_clamp_min,lights[i].color_clamp_max);
        }

        spotlight_color_sum+=spotlight_color;
    }
    spotlight_color_sum=clamp(spotlight_color_sum,spotlight_color_sum_clamp_min,spotlight_color_sum_clamp_max);
    vs_out_color=spotlight_color_sum;
    vs_out_color.a=1.0;
}

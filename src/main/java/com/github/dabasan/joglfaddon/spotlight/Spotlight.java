package com.github.dabasan.joglfaddon.spotlight;

import static com.github.dabasan.basis.coloru8.ColorU8Functions.*;
import static com.github.dabasan.basis.vector.VectorFunctions.*;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import com.github.dabasan.basis.coloru8.ColorU8;
import com.github.dabasan.basis.vector.Vector;
import com.github.dabasan.joglf.gl.shader.ShaderProgram;
import com.github.dabasan.tool.MathFunctions;

/**
 * Spotlight
 * 
 * @author Daba
 *
 */
class Spotlight {
	private boolean enabled;

	private Vector position;
	private Vector direction;
	private float k0;
	private float k1;
	private float k2;
	private float phi;
	private float theta;
	private float falloff;
	private ColorU8 diffuse_color;
	private ColorU8 specular_color;
	private float diffuse_power;
	private float specular_power;

	private float color_clamp_min;
	private float color_clamp_max;

	private final List<ShaderProgram> programs;

	public Spotlight() {
		enabled = true;

		position = VGet(50.0f, 50.0f, 50.0f);
		direction = VNorm(VGet(-1.0f, -1.0f, -1.0f));
		k0 = 0.0f;
		k1 = 0.01f;
		k2 = 0.0f;
		phi = MathFunctions.DegToRad(50.0f);
		theta = MathFunctions.DegToRad(30.0f);
		falloff = 1.0f;
		diffuse_color = GetColorU8(1.0f, 1.0f, 1.0f, 1.0f);
		specular_color = GetColorU8(1.0f, 1.0f, 1.0f, 1.0f);
		diffuse_power = 1.0f;
		specular_power = 0.1f;

		color_clamp_min = 0.0f;
		color_clamp_max = 1.0f;

		programs = new ArrayList<>();
	}

	public boolean IsEnabled() {
		return enabled;
	}
	public Vector GetPosition() {
		return new Vector(position);
	}
	public Vector GetDirection() {
		return new Vector(direction);
	}
	public float GetK0() {
		return k0;
	}
	public float GetK1() {
		return k1;
	}
	public float GetK2() {
		return k2;
	}
	public float GetPhi() {
		return phi;
	}
	public float GetTheta() {
		return theta;
	}
	public float GetFalloff() {
		return falloff;
	}
	public ColorU8 GetDiffuseColor() {
		return new ColorU8(diffuse_color);
	}
	public ColorU8 GetSpecularColor() {
		return new ColorU8(specular_color);
	}
	public float GetDiffusePower() {
		return diffuse_power;
	}
	public float GetSpecularPower() {
		return specular_power;
	}
	public float GetColorClampMin() {
		return color_clamp_min;
	}
	public float GetColorClampMax() {
		return color_clamp_max;
	}

	public void Enable(boolean enabled) {
		this.enabled = enabled;
	}
	public void SetPosition(Vector position) {
		this.position = position;
	}
	public void SetDirection(Vector direction) {
		this.direction = direction;
	}
	public void SetK(float k0, float k1, float k2) {
		this.k0 = k0;
		this.k1 = k1;
		this.k2 = k2;
	}
	public void SetPhi(float phi) {
		this.phi = phi;
	}
	public void SetTheta(float theta) {
		this.theta = theta;
	}
	public void SetFalloff(float falloff) {
		this.falloff = falloff;
	}
	public void SetDiffuseColor(ColorU8 diffuse_color) {
		this.diffuse_color = diffuse_color;
	}
	public void SetSpecularColor(ColorU8 specular_color) {
		this.specular_color = specular_color;
	}
	public void SetDiffusePower(float diffuse_power) {
		this.diffuse_power = diffuse_power;
	}
	public void SetSpecularPower(float specular_power) {
		this.specular_power = specular_power;
	}
	public void SetColorClamp(float min, float max) {
		color_clamp_min = min;
		color_clamp_max = max;
	}

	public void AddProgram(ShaderProgram program) {
		programs.add(program);
	}
	public void RemoveProgram(ShaderProgram program) {
		programs.remove(program);
	}
	public void RemoveAllPrograms() {
		programs.clear();
	}

	public void Update(int index) {
		final String element_name = "lights" + "[" + index + "]";

		final Function<Boolean, Integer> bti = b -> {
			if (b == false) {
				return 0;
			} else {
				return 1;
			}
		};

		for (final var program : programs) {
			program.Enable();
			program.SetUniform(element_name + ".enabled", bti.apply(enabled));
			program.SetUniform(element_name + ".position", position);
			program.SetUniform(element_name + ".direction", direction);
			program.SetUniform(element_name + ".k0", k0);
			program.SetUniform(element_name + ".k1", k1);
			program.SetUniform(element_name + ".k2", k2);
			program.SetUniform(element_name + ".phi", phi);
			program.SetUniform(element_name + ".theta", theta);
			program.SetUniform(element_name + ".falloff", falloff);
			program.SetUniform(element_name + ".diffuse_color", diffuse_color);
			program.SetUniform(element_name + ".specular_color", specular_color);
			program.SetUniform(element_name + ".diffuse_power", diffuse_power);
			program.SetUniform(element_name + ".specular_power", specular_power);
			program.SetUniform(element_name + ".color_clamp_min", color_clamp_min);
			program.SetUniform(element_name + ".color_clamp_max", color_clamp_max);
			program.Disable();
		}
	}
}

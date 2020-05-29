package com.github.dabasan.joglfaddon.spotlight;

import static com.github.dabasan.basis.vector.VectorFunctions.*;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.dabasan.basis.coloru8.ColorU8;
import com.github.dabasan.basis.vector.Vector;
import com.github.dabasan.joglf.gl.front.CameraFront;
import com.github.dabasan.joglf.gl.front.FogFront;
import com.github.dabasan.joglf.gl.front.LightingFront;
import com.github.dabasan.joglf.gl.shader.ShaderProgram;

/**
 * Spotlight manager
 * 
 * @author Daba
 *
 */
public class SpotlightMgr {
	private static Logger logger = LoggerFactory.getLogger(SpotlightMgr.class);
	public static final int MAX_SPOTLIGHT_NUM = 256;

	private static int count = 0;
	private static Map<Integer, Spotlight> lights_map = new HashMap<>();

	private static ShaderProgram spotlight_program;

	public static void Initialize() {
		spotlight_program = new ShaderProgram("dabasan/spotlight",
				"./Data/Shader/330/addon/dabasan/spotlight/vshader.glsl",
				"./Data/Shader/330/addon/dabasan/spotlight/fshader.glsl");
		CameraFront.AddProgram(spotlight_program);
		LightingFront.AddProgram(spotlight_program);
		FogFront.AddProgram(spotlight_program);

		SetColorSumClamp(0.0f, 1.0f);

		logger.info("SpotlightMgr initialized.");
	}

	public static int CreateSpotlight() {
		if (lights_map.size() > MAX_SPOTLIGHT_NUM) {
			logger.warn("No more spotlights can be created.");
			return -1;
		}

		int light_handle = count;
		Spotlight light = new Spotlight();
		light.AddProgram(spotlight_program);
		lights_map.put(light_handle, light);
		count++;

		spotlight_program.Enable();
		spotlight_program.SetUniform("current_spotlight_num", lights_map.size());
		spotlight_program.Disable();

		return light_handle;
	}
	public static int DeleteSpotlight(int spotlight_handle) {
		if (lights_map.containsKey(spotlight_handle) == false) {
			logger.trace("No such spotlight. spotlight_handle={}", spotlight_handle);
			return -1;
		}

		lights_map.remove(spotlight_handle);

		spotlight_program.Enable();
		spotlight_program.SetUniform("current_spotlight_num", lights_map.size());
		spotlight_program.Disable();

		return 0;
	}
	public static void DeleteAllSpotlights() {
		lights_map.clear();
		count = 0;

		spotlight_program.Enable();
		spotlight_program.SetUniform("current_spotlight_num", 0);
		spotlight_program.Disable();
	}

	public static int AddProgram(int spotlight_handle, ShaderProgram program) {
		if (lights_map.containsKey(spotlight_handle) == false) {
			logger.trace("No such spotlight. spotlight_handle={}", spotlight_handle);
			return -1;
		}

		Spotlight light = lights_map.get(spotlight_handle);
		light.AddProgram(program);

		return 0;
	}
	public static int RemoveAllPrograms(int spotlight_handle) {
		if (lights_map.containsKey(spotlight_handle) == false) {
			logger.trace("No such spotlight. spotlight_handle={}", spotlight_handle);
			return -1;
		}

		Spotlight light = lights_map.get(spotlight_handle);
		light.RemoveAllPrograms();

		return 0;
	}

	public static int SetPosition(int spotlight_handle, Vector position) {
		if (lights_map.containsKey(spotlight_handle) == false) {
			logger.trace("No such spotlight. spotlight_handle={}", spotlight_handle);
			return -1;
		}

		Spotlight light = lights_map.get(spotlight_handle);
		light.SetPosition(position);

		return 0;
	}
	public static int SetDirection(int spotlight_handle, Vector direction) {
		if (lights_map.containsKey(spotlight_handle) == false) {
			logger.trace("No such spotlight. spotlight_handle={}", spotlight_handle);
			return -1;
		}

		Spotlight light = lights_map.get(spotlight_handle);
		light.SetDirection(direction);

		return 0;
	}
	public static int SetPositionAndTarget(int spotlight_handle, Vector position, Vector target) {
		if (lights_map.containsKey(spotlight_handle) == false) {
			logger.trace("No such spotlight. spotlight_handle={}", spotlight_handle);
			return -1;
		}

		Spotlight light = lights_map.get(spotlight_handle);
		Vector direction = VNorm(VSub(target, position));
		light.SetPosition(position);
		light.SetDirection(direction);

		return 0;
	}
	public static int SetAttenuation(int spotlight_handle, float attenuation) {
		if (lights_map.containsKey(spotlight_handle) == false) {
			logger.trace("No such spotlight. spotlight_handle={}", spotlight_handle);
			return -1;
		}

		Spotlight light = lights_map.get(spotlight_handle);
		light.SetAttenuation(attenuation);

		return 0;
	}
	public static int SetPhi(int spotlight_handle, float phi) {
		if (lights_map.containsKey(spotlight_handle) == false) {
			logger.trace("No such spotlight. spotlight_handle={}", spotlight_handle);
			return -1;
		}

		Spotlight light = lights_map.get(spotlight_handle);
		light.SetPhi(phi);

		return 0;
	}
	public static int SetTheta(int spotlight_handle, float theta) {
		if (lights_map.containsKey(spotlight_handle) == false) {
			logger.trace("No such spotlight. spotlight_handle={}", spotlight_handle);
			return -1;
		}

		Spotlight light = lights_map.get(spotlight_handle);
		light.SetTheta(theta);

		return 0;
	}
	public static int SetFalloff(int spotlight_handle, float falloff) {
		if (lights_map.containsKey(spotlight_handle) == false) {
			logger.trace("No such spotlight. spotlight_handle={}", spotlight_handle);
			return -1;
		}

		Spotlight light = lights_map.get(spotlight_handle);
		light.SetFalloff(falloff);

		return 0;
	}
	public static int SetDiffuseColor(int spotlight_handle, ColorU8 diffuse_color) {
		if (lights_map.containsKey(spotlight_handle) == false) {
			logger.trace("No such spotlight. spotlight_handle={}", spotlight_handle);
			return -1;
		}

		Spotlight light = lights_map.get(spotlight_handle);
		light.SetDiffuseColor(diffuse_color);

		return 0;
	}
	public static int SetSpecularColor(int spotlight_handle, ColorU8 specular_color) {
		if (lights_map.containsKey(spotlight_handle) == false) {
			logger.trace("No such spotlight. spotlight_handle={}", spotlight_handle);
			return -1;
		}

		Spotlight light = lights_map.get(spotlight_handle);
		light.SetSpecularColor(specular_color);

		return 0;
	}
	public static int SetDiffusePower(int spotlight_handle, float diffuse_power) {
		if (lights_map.containsKey(spotlight_handle) == false) {
			logger.trace("No such spotlight. spotlight_handle={}", spotlight_handle);
			return -1;
		}

		Spotlight light = lights_map.get(spotlight_handle);
		light.SetDiffusePower(diffuse_power);

		return 0;
	}
	public static int SetSpecularPower(int spotlight_handle, float specular_power) {
		if (lights_map.containsKey(spotlight_handle) == false) {
			logger.trace("No such spotlight. spotlight_handle={}", spotlight_handle);
			return -1;
		}

		Spotlight light = lights_map.get(spotlight_handle);
		light.SetSpecularPower(specular_power);

		return 0;
	}
	public static int SetColorClamp(int spotlight_handle, float min, float max) {
		if (lights_map.containsKey(spotlight_handle) == false) {
			logger.trace("No such spotlight. spotlight_handle={}", spotlight_handle);
			return -1;
		}

		Spotlight light = lights_map.get(spotlight_handle);
		light.SetColorClamp(min, max);

		return 0;
	}

	public static void SetColorSumClamp(float min, float max) {
		spotlight_program.Enable();
		spotlight_program.SetUniform("spotlight_color_sum_clamp_min", min);
		spotlight_program.SetUniform("spotlight_color_sum_clamp_max", max);
		spotlight_program.Disable();
	}

	public static void Update() {
		int index = 0;
		for (var light : lights_map.values()) {
			light.Update(index);
			index++;
		}
	}
}

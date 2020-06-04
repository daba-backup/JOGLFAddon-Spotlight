package com.github.dabasan.joglfaddon.spotlight;

import static com.github.dabasan.basis.vector.VectorFunctions.*;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.dabasan.basis.coloru8.ColorU8;
import com.github.dabasan.basis.vector.Vector;
import com.github.dabasan.joglf.gl.front.CameraFront;
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

	private static ShaderProgram gouraud_program;
	private static ShaderProgram phong_program;

	public static void Initialize() {
		gouraud_program = new ShaderProgram("dabasan/spotlight/gouraud",
				"./Data/Shader/330/addon/dabasan/spotlight/gouraud/vshader.glsl",
				"./Data/Shader/330/addon/dabasan/spotlight/gouraud/fshader.glsl");
		phong_program = new ShaderProgram("dabasan/spotlight/phong",
				"./Data/Shader/330/addon/dabasan/spotlight/phong/vshader.glsl",
				"./Data/Shader/330/addon/dabasan/spotlight/phong/fshader.glsl");
		CameraFront.AddProgram(gouraud_program);
		CameraFront.AddProgram(phong_program);

		SetColorSumClamp(0.0f, 1.0f);

		logger.info("SpotlightMgr initialized.");
	}

	public static int CreateSpotlight(SpotlightShadingMethod method) {
		if (lights_map.size() > MAX_SPOTLIGHT_NUM) {
			logger.warn("No more spotlights can be created.");
			return -1;
		}

		int light_handle = count;

		Spotlight light = new Spotlight();
		if (method == SpotlightShadingMethod.GOURAUD) {
			light.AddProgram(gouraud_program);
		} else {
			light.AddProgram(phong_program);
		}

		lights_map.put(light_handle, light);
		count++;

		gouraud_program.Enable();
		gouraud_program.SetUniform("current_spotlight_num", lights_map.size());
		gouraud_program.Disable();
		phong_program.Enable();
		phong_program.SetUniform("current_spotlight_num", lights_map.size());
		phong_program.Disable();

		return light_handle;
	}
	public static int DeleteSpotlight(int spotlight_handle) {
		if (lights_map.containsKey(spotlight_handle) == false) {
			logger.trace("No such spotlight. spotlight_handle={}", spotlight_handle);
			return -1;
		}

		lights_map.remove(spotlight_handle);

		gouraud_program.Enable();
		gouraud_program.SetUniform("current_spotlight_num", lights_map.size());
		gouraud_program.Disable();
		phong_program.Enable();
		phong_program.SetUniform("current_spotlight_num", lights_map.size());
		phong_program.Disable();

		return 0;
	}
	public static void DeleteAllSpotlights() {
		lights_map.clear();
		count = 0;

		gouraud_program.Enable();
		gouraud_program.SetUniform("current_spotlight_num", 0);
		gouraud_program.Disable();
		phong_program.Enable();
		phong_program.SetUniform("current_spotlight_num", 0);
		phong_program.Disable();
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
	public static int SetK(int spotlight_handle, float k0, float k1, float k2) {
		if (lights_map.containsKey(spotlight_handle) == false) {
			logger.trace("No such spotlight. spotlight_handle={}", spotlight_handle);
			return -1;
		}

		Spotlight light = lights_map.get(spotlight_handle);
		light.SetK(k0, k1, k2);

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
		gouraud_program.Enable();
		gouraud_program.SetUniform("spotlight_color_sum_clamp_min", min);
		gouraud_program.SetUniform("spotlight_color_sum_clamp_max", max);
		gouraud_program.Disable();
		phong_program.Enable();
		phong_program.SetUniform("spotlight_color_sum_clamp_min", min);
		phong_program.SetUniform("spotlight_color_sum_clamp_max", max);
		phong_program.Disable();
	}

	public static void Update() {
		int index = 0;
		for (var light : lights_map.values()) {
			light.Update(index);
			index++;
		}
	}
}

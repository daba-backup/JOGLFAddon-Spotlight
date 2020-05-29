package com.github.dabasan.joglfaddon.spotlight;

import static com.github.dabasan.basis.coloru8.ColorU8Functions.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.github.dabasan.basis.vector.Vector;
import com.github.dabasan.basis.vector.VectorFunctions;
import com.github.dabasan.joglf.gl.input.keyboard.KeyboardEnum;
import com.github.dabasan.joglf.gl.input.mouse.MouseEnum;
import com.github.dabasan.joglf.gl.model.Model3DFunctions;
import com.github.dabasan.joglf.gl.shader.ShaderProgram;
import com.github.dabasan.joglf.gl.util.camera.FreeCamera;
import com.github.dabasan.joglf.gl.window.JOGLFWindow;
import com.github.dabasan.tool.MathFunctions;

class SpotlightTestWindow2 extends JOGLFWindow {
	private List<Integer> spotlight_handles;
	private int plane_handle;

	private FreeCamera camera;

	private Random random;

	@Override
	public void Init() {
		SpotlightMgr.Initialize();
		spotlight_handles = new ArrayList<>();

		plane_handle = Model3DFunctions.LoadModel("./Data/Model/OBJ/Plane/plane.obj");
		Model3DFunctions.RemoveAllPrograms(plane_handle);
		Model3DFunctions.AddProgram(plane_handle, new ShaderProgram("dabasan/spotlight"));

		camera = new FreeCamera();

		random = new Random();
	}

	@Override
	public void Update() {
		int front = this.GetKeyboardPressingCount(KeyboardEnum.KEY_W);
		int back = this.GetKeyboardPressingCount(KeyboardEnum.KEY_S);
		int right = this.GetKeyboardPressingCount(KeyboardEnum.KEY_D);
		int left = this.GetKeyboardPressingCount(KeyboardEnum.KEY_A);

		int diff_x;
		int diff_y;
		if (this.GetMousePressingCount(MouseEnum.MOUSE_MIDDLE) > 0) {
			diff_x = this.GetCursorDiffX();
			diff_y = this.GetCursorDiffY();
		} else {
			diff_x = 0;
			diff_y = 0;
		}

		camera.Translate(front, back, right, left);
		camera.Rotate(diff_x, diff_y);
		camera.Update();

		if (this.GetKeyboardPressingCount(KeyboardEnum.KEY_ENTER) == 1) {
			int spotlight_handle = SpotlightMgr.CreateSpotlight();

			Vector position = camera.GetPosition();
			Vector direction = VectorFunctions.VGetFromAngles(camera.GetVRotate(),
					camera.GetHRotate());
			float r = random.nextFloat();
			float g = random.nextFloat();
			float b = random.nextFloat();
			SpotlightMgr.SetPosition(spotlight_handle, position);
			SpotlightMgr.SetDirection(spotlight_handle, direction);
			SpotlightMgr.SetDiffuseColor(spotlight_handle, GetColorU8(r, g, b, 1.0f));

			float phi = MathFunctions.DegToRad(30.0f);
			float theta = MathFunctions.DegToRad(10.0f);
			SpotlightMgr.SetPhi(spotlight_handle, phi);
			SpotlightMgr.SetTheta(spotlight_handle, theta);
			SpotlightMgr.SetFalloff(spotlight_handle, 1.0f);

			spotlight_handles.add(spotlight_handle);
		}
		SpotlightMgr.Update();
	}

	@Override
	public void Draw() {
		Model3DFunctions.DrawModel(plane_handle);
	}
}

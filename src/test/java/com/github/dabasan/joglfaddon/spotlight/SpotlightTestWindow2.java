package com.github.dabasan.joglfaddon.spotlight;

import static com.github.dabasan.basis.coloru8.ColorU8Functions.*;

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
	private SpotlightMgr spotlight_mgr;
	private int plane_handle;

	private FreeCamera camera;

	private Random random;

	@Override
	public void Init() {
		spotlight_mgr = new SpotlightMgr();

		plane_handle = Model3DFunctions.LoadModel("./Data/Model/OBJ/Plane/subdivided_plane.obj");
		Model3DFunctions.RemoveAllPrograms(plane_handle);
		Model3DFunctions.AddProgram(plane_handle, new ShaderProgram("dabasan/spotlight/gouraud"));

		camera = new FreeCamera();

		random = new Random();
	}

	@Override
	public void Dispose() {
		spotlight_mgr.Dispose();
	}

	@Override
	public void Update() {
		final int front = this.GetKeyboardPressingCount(KeyboardEnum.KEY_W);
		final int back = this.GetKeyboardPressingCount(KeyboardEnum.KEY_S);
		final int right = this.GetKeyboardPressingCount(KeyboardEnum.KEY_D);
		final int left = this.GetKeyboardPressingCount(KeyboardEnum.KEY_A);

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
			final int spotlight_handle = spotlight_mgr
					.CreateSpotlight(SpotlightShadingMethod.GOURAUD);

			final Vector position = camera.GetPosition();
			final Vector direction = VectorFunctions.VGetFromAngles(camera.GetVRotate(),
					camera.GetHRotate());
			final float r = random.nextFloat();
			final float g = random.nextFloat();
			final float b = random.nextFloat();
			spotlight_mgr.SetPosition(spotlight_handle, position);
			spotlight_mgr.SetDirection(spotlight_handle, direction);
			spotlight_mgr.SetDiffuseColor(spotlight_handle, GetColorU8(r, g, b, 1.0f));

			final float phi = MathFunctions.DegToRad(30.0f);
			final float theta = MathFunctions.DegToRad(10.0f);
			spotlight_mgr.SetPhi(spotlight_handle, phi);
			spotlight_mgr.SetTheta(spotlight_handle, theta);
			spotlight_mgr.SetFalloff(spotlight_handle, 1.0f);
		}
		spotlight_mgr.Update();
	}

	@Override
	public void Draw() {
		Model3DFunctions.DrawModel(plane_handle);
	}
}

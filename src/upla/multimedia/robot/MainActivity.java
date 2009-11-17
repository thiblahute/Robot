/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package upla.multimedia.robot;

import android.app.Activity;
import android.os.Bundle;

/**
 *
 * @author thibault
 */
public class MainActivity extends Activity {

	private RobotRender robotRender;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

        /* Instanciate the robot renderer */
		robotRender = new RobotRender(this, false);
		/* Set the context view with the robotRender */
		setContentView(robotRender);
	}

	@Override
	protected void onResume() {
		super.onResume();
		robotRender.onResume();
	}

	@Override
	protected void onPause() {
		super.onPause();
		robotRender.onPause();
	}
}

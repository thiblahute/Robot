package upla.multimedia.robot;

import android.app.Activity;
import android.os.Bundle;

public class robot extends Activity
{
    private RobotRender robotRender;
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        robotRender = new RobotRender(this, false);
        /* Set the context view with the robotRender */
        setContentView(robotRender);
    }
}

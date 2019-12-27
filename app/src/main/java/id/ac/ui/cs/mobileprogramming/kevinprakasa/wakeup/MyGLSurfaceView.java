package id.ac.ui.cs.mobileprogramming.kevinprakasa.wakeup;

import android.content.Context;
import android.opengl.GLSurfaceView;

public class MyGLSurfaceView extends GLSurfaceView {

    private final MyGLRenderer renderer;

    public MyGLSurfaceView(Context context){
        super(context);

        // Create an OpenGL ES 2.0 context
        setEGLContextClientVersion(2);

        renderer = new MyGLRenderer();

        // Set the Renderer for drawing on the GLSurfaceView
        setRenderer(renderer);
    }
}
package main;
import org.lwjgl.*;
import org.lwjgl.glfw.*;
import org.lwjgl.opengl.*;
import org.lwjgl.system.*;

import sdfs.Sphere;

import java.nio.*;

import static org.lwjgl.glfw.Callbacks.*;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryStack.*;
import static org.lwjgl.system.MemoryUtil.*;

public class HelloWorld {

	// The window handle
	private long window;

	public static float xx;

	public void run() {
		System.out.println("Hello LWJGL " + Version.getVersion() + "!");

		init();
		loop();

		// Free the window callbacks and destroy the window
		glfwFreeCallbacks(window);
		glfwDestroyWindow(window);

		// Terminate GLFW and free the error callback
		glfwTerminate();
		glfwSetErrorCallback(null).free();
	}

	private void init() {
		// Setup an error callback. The default implementation
		// will print the error message in System.err.
		GLFWErrorCallback.createPrint(System.err).set();

		// Initialize GLFW. Most GLFW functions will not work before doing this.
		if ( !glfwInit() )
			throw new IllegalStateException("Unable to initialize GLFW");

		// Configure GLFW
		glfwDefaultWindowHints(); // optional, the current window hints are already the default
		glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE); // the window will stay hidden after creation
		glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE); // the window will be resizable

		// Create the window
		window = glfwCreateWindow(1920, 1080, "Hello World!", NULL, NULL);

		if ( window == NULL )
			throw new RuntimeException("Failed to create the GLFW window");

		// Setup a key callback. It will be called every time a key is pressed, repeated or released.
		glfwSetKeyCallback(window, (window, key, scancode, action, mods) -> {
			if ( key == GLFW_KEY_ESCAPE && action == GLFW_RELEASE )
				glfwSetWindowShouldClose(window, true); // We will detect this in the rendering loop
		});

		// Get the thread stack and push a new frame
		try ( MemoryStack stack = stackPush() ) {
			IntBuffer pWidth = stack.mallocInt(1); // int*
			IntBuffer pHeight = stack.mallocInt(1); // int*

			// Get the window size passed to glfwCreateWindow
			glfwGetWindowSize(window, pWidth, pHeight);

			// Get the resolution of the primary monitor
			GLFWVidMode vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());
			// Center the window
			glfwSetWindowPos(
				window,
				(vidmode.width() - pWidth.get(0)) / 2,
				(vidmode.height() - pHeight.get(0)) / 2
			);

		} // the stack frame is popped automatically
		// Make the OpenGL context current
		glfwMakeContextCurrent(window);
		// Enable v-sync
		glfwSwapInterval(1);

		// Make the window visible
		glfwShowWindow(window);
	}

	private void loop() {
		// This line is critical for LWJGL's interoperation with GLFW's
		// OpenGL context, or any context that is managed externally.
		// LWJGL detects the context that is current in the current thread,
		// creates the GLCapabilities instance and makes the OpenGL
		// bindings available for use.
		GL.createCapabilities();

		// Set the clear color
		//glClearColor(1.0f, 1.0f, 0.0f, 1.0f);
	
	
		// Run the rendering loop until the user has attempted to close
		// the window or has pressed the ESCAPE key.
		while ( !glfwWindowShouldClose(window) ) {
			glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT); // clear the framebuffer

			xx += 0.1f;
			// Draw quads
			float xu, yu;
			for (float y = 0; y < Height; y++){
				for (float x = 0; x < Width ; x++){
					//normalizing pixel coordinates to be between -1 and 1 			http://mvps.org/DirectX/articles/rayproj.htm
					xu=(x/WidthDiv2-1.0f);//won't divide by aspect ratio here since screen is already normalized
					yu=1.0f-y/HeightDiv2;
					 // Find where this pixel sample hits in the scene
					Ray ray = new Ray();
					Camera cam = new Camera();
					
					 ray = cam.makeCameraRay(
						 orig,
						 xu*aspect,//multiplication by aspect here instead
						 yu);
				
					double color = ray_march(ray);
					glPointSize(0);
					glBegin(GL_POINTS);
						glColor3f((float)color, (float)color, 0);
						glVertex2f(xu, yu);
					glEnd();
				}
			}

			glfwSwapBuffers(window); // swap the color buffers

			// Poll for window events. The key callback above will only be
			// invoked during this call.
			glfwPollEvents();

		}
		
	}
	Vect orig = new Vect(0, 0, -1);
	Sphere s = new Sphere(new Vect(0, 0, 2), 2f);
	float Width = 1920, Height = 1080;
	float aspect = Width/Height;
	float WidthDiv2 = Width*0.5f;
	float HeightDiv2 = Height*0.5f;
	public static void main(String[] args) {
		new HelloWorld().run();
	}

	public double ray_march(Ray r)
	{
		float total_distance_traveled = 0.0f;
		float MINIMUM_HIT_DISTANCE = 0.01f;
		float MAXIMUM_TRACE_DISTANCE = 30;

		while(true)
		{
			Vect current_position =  Vect.addV(r.origin, Vect.Vector_Mul(r.direction, total_distance_traveled));

			float distance_to_closest = (float) s.distToPoint(current_position);

			if (distance_to_closest < MINIMUM_HIT_DISTANCE) 
			{
				Vect normal = s.calculate_normal(current_position);
				Vect light_position = new Vect(-4.0, -3.0, 6.0);
				Vect direction_to_light = Vect.Vector_Normalise(Vect.subV(current_position, light_position));

				float diffuse_intensity = (float) Math.max(0.0, Vect.Vector_DotProduct(normal, direction_to_light));

				return 0.8*diffuse_intensity;
			}
		

			if (total_distance_traveled > MAXIMUM_TRACE_DISTANCE)
			{
				break;
			}
			total_distance_traveled += distance_to_closest;
		}
		return 0;
	} 

	

}

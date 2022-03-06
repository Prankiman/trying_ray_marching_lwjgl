package main;

public class Vect{
	public double x;
	public double y;
	public double z;
	
	public Vect(double... xyz) {
		if(xyz.length == 3){
			this.x = xyz[0];
			this.y = xyz[1];
			this.z = xyz[2];
		}
	}
	
	public static Vect subV(Vect v1, Vect v2) {
		Vect v3 = new Vect();
		v3.x = v1.x-v2.x;
		v3.y = v1.y-v2.y;
		v3.z = v1.z-v2.z;
		return v3; 
	}
	public static  Vect addV(Vect v1, Vect v2) {
		Vect v3 = new Vect();
		v3.x = v1.x+v2.x;
		v3.y = v1.y+v2.y;
		v3.z = v1.z+v2.z;
		return v3; 
	}
	
	public static  Vect Vector_Mul(Vect v1, double k)
	{
		Vect v2 = new Vect();
		v2.x = v1.x*k;
		v2.y = v1.y*k;
		v2.z = v1.z*k;
		return v2;
	}

	public static  Vect Vector_Div(Vect v1, double k)
	{
		Vect v2 = new Vect();
		v2.x = v1.x/k;
		v2.y = v1.y/k;
		v2.z = v1.z/k;
		return v2;
	}
	
	public static  double Vector_DotProduct(Vect v1, Vect v2)
	{
		return v1.x*v2.x + v1.y*v2.y + v1.z * v2.z;
	}
	
	public static  double Vector_Length(Vect v)
	{
		return (double) Math.sqrt(Vector_DotProduct(v, v));
	}

	public static  Vect Vector_Normalise(Vect v)
	{
		double l = Vector_Length(v);
		Vect v2 = new Vect();
		v2.x = v.x/l;v2.y = v.y/l;v2.z = v.z/l;
		return v2;
	}
	public static  Vect cross(Vect v1, Vect v2) {
		Vect v3 = new Vect();
		v3.x = (v1.y*v2.z)-(v1.z*v2.y);
		v3.y = (v1.z*v2.x)-(v1.x*v2.z);
		v3.z = (v1.x*v2.y)-(v1.y*v2.x);
		return v3;
	}
	
	public static  Vect Matrix_MultiplyVector(double[][] m, Vect n)
	{
		Vect v = new Vect();
		
			v.x = n.x * m[0][0] + n.y * m[1][0] + n.z * m[2][0];
			v.y= n.x * m[0][1] + n.y * m[1][1] + n.z * m[2][1];
			v.z = n.x * m[0][2] + n.y * m[1][2] + n.z * m[2][2];
		
		return v;
	}
	public static  Vect rotateYP(Vect v, double yaw, double pitch) {
        //needs to be in radians
        double yawRads = yaw;
        double pitchRads = pitch;

        Vect rotateY = new Vect(), rotateX = new Vect();
        
        // Rotate around the Y axis (pitch)
        rotateY.x = v.x;
        rotateY.y = (double) (v.y*Math.cos(pitchRads) + v.z*Math.sin(pitchRads));
        rotateY.z = (double) (-v.y*Math.sin(pitchRads) + v.z*Math.cos(pitchRads));
        
        //Rotate around X axis (yaw)
        rotateX.y = rotateY.y;
        rotateX.x = (double) (rotateY.x*Math.cos(yawRads) + rotateY.z*Math.sin(yawRads));
        rotateX.z = (double) (-rotateY.x*Math.sin(yawRads) + rotateY.z*Math.cos(yawRads));

        
        return rotateX;
    }
}
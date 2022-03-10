package sdfs;

import main.HelloWorld;
import main.Vect;


public class Sphere {
    double displacement;
    Vect pos = new Vect();
    double radius;
    public Sphere(Vect pos, double radius){
        this.pos = pos;
        this.radius = radius;
    }

    public double distToPoint(Vect p){
       displacement = Math.sin(3* (HelloWorld.xx+p.x)) * Math.sin(3 * p.y) * Math.sin(3 * p.z) * 0.3;
       return Vect.Vector_Length(Vect.subV(pos, p))-radius+displacement;
    }
    public Vect calculate_normal(Vect p)
    {
        Vect smallx = new Vect(0.001, 0.0, 0.0);
        Vect smally = new Vect(0.0, 0.001, 0.0);
        Vect smallz = new Vect(0.0, 0.0, 0.001);

        float gradient_x = (float) (distToPoint(Vect.addV(p, smallx)) - distToPoint(Vect.subV(p, smallx)));
        float gradient_y =  (float) (distToPoint(Vect.addV(p, smally)) - distToPoint(Vect.subV(p, smally)));
        float gradient_z =  (float) (distToPoint(Vect.addV(p, smallz)) - distToPoint(Vect.subV(p, smallz)));

        Vect normal = new Vect(gradient_x, gradient_y, gradient_z);

        return Vect.Vector_Normalise(normal);
    }

    public Vect getNormalAt(Vect point) {
        Vect t = new Vect();
        t.x = (point.x-pos.x);
        t.y = (point.y-pos.y);
        t.z = (point.z-pos.z);
        return Vect.Vector_Normalise(t);
     }
}

package wireframe;
import java.util.ArrayList;

public class DemoObjects {
	/* DEFINING object */
	public static ArrayList<Line> packageCube(int x, int y, int z){
		ArrayList<Line> object = new ArrayList<Line>();
		Line ab = new Line(-100 + x, 100 + y, -100 + z, 100 + x, 100 + y, -100 + z);
		Line bc = new Line(100 + x, 100 + y, -100 + z, 100 + x, -100 + y, -100 + z);
		Line cd = new Line(100 + x, -100 + y, -100 + z, -100 + x, -100 + y, -100 + z);
		Line da = new Line(-100 + x, -100 + y, -100 + z, -100 + x, 100 + y, -100 + z);
		Line ef = new Line(-100 + x, 100 + y, 100 + z, 100 + x, 100 + y, 100 + z);
		Line fg = new Line(100 + x, 100 + y, 100 + z, 100 + x, -100 + y, 100 + z);
		Line gh = new Line(100 + x, -100 + y, 100 + z, -100 + x, -100 + y, 100 + z);
		Line he = new Line(-100 + x, -100 + y, 100 + z, -100 + x, 100 + y, 100 + z);
		Line ae = new Line(-100 + x, 100 + y, -100 + z, -100 + x, 100 + y, 100 + z);
		Line bf = new Line(100 + x, 100 + y, -100 + z, 100 + x, 100 + y, 100 + z);
		Line cg = new Line(100 + x, -100 + y, -100 + z, 100 + x, -100 + y, 100 + z);
		Line dh = new Line(-100 + x, -100 + y, -100 + z, -100 + x, -100 + y, 100 + z);
		object.add(ab);
		object.add(bc);
		object.add(cd);
		object.add(da);
		object.add(ef);
		object.add(fg);
		object.add(gh);
		object.add(he);
		object.add(ae);
		object.add(bf);
		object.add(cg);
		object.add(dh);		
		return object;
	}
	
	public static ArrayList<Line> packagePyramid(){
		ArrayList<Line> object = new ArrayList<Line>();
		Line bc = new Line(0, 100, 0, 100, -100, -100);
		Line cd = new Line(100, -100, -100, -100, -100, -100);
		Line da = new Line(-100, -100, -100, 0, 100, 0);
		Line fg = new Line(0, 100, 0, 100, -100, 100);
		Line gh = new Line(100, -100, 100, -100, -100, 100);
		Line he = new Line(-100, -100, 100, 0, 100, 0);
		Line cg = new Line(100, -100, -100, 100, -100, 100);
		Line dh = new Line(-100, -100, -100, -100, -100, 100);
		object.add(bc);
		object.add(cd);
		object.add(da);
		object.add(fg);
		object.add(gh);
		object.add(he);
		object.add(cg);
		object.add(dh);		
		return object;
	}
	
	public static ArrayList<Line> packageAFrame(){
		ArrayList<Line> object = new ArrayList<Line>();
		Line ab = new Line(-100, 100, 0, 100, 100, 0);
		Line bc = new Line(100, 100, 0, 100, -100, -100);
		Line cd = new Line(100, -100, -100, -100, -100, -100);
		Line da = new Line(-100, -100, -100, -100, 100, 0);
		Line fg = new Line(100, 100, 0, 100, -100, 100);
		Line gh = new Line(100, -100, 100, -100, -100, 100);
		Line he = new Line(-100, -100, 100, -100, 100, 0);
		Line cg = new Line(100, -100, -100, 100, -100, 100);
		Line dh = new Line(-100, -100, -100, -100, -100, 100);
		object.add(ab);
		object.add(bc);
		object.add(cd);
		object.add(da);
		object.add(fg);
		object.add(gh);
		object.add(he);
		object.add(cg);
		object.add(dh);		
		return object;
	}
	
}

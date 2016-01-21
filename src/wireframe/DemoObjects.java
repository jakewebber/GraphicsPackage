package wireframe;
import java.util.ArrayList;

public class DemoObjects {
	/* DEFINING CUBE */
	public static ArrayList<Line> packageCube(int x, int y, int z){
		ArrayList<Line> cube = new ArrayList<Line>();
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
		cube.add(ab);
		cube.add(bc);
		cube.add(cd);
		cube.add(da);
		cube.add(ef);
		cube.add(fg);
		cube.add(gh);
		cube.add(he);
		cube.add(ae);
		cube.add(bf);
		cube.add(cg);
		cube.add(dh);		
		return cube;
	}
	
	public static ArrayList<Line> packagePyramid(){
		ArrayList<Line> cube = new ArrayList<Line>();
		Line bc = new Line(0, 100, 0, 100, -100, -100);
		Line cd = new Line(100, -100, -100, -100, -100, -100);
		Line da = new Line(-100, -100, -100, 0, 100, 0);
		Line fg = new Line(0, 100, 0, 100, -100, 100);
		Line gh = new Line(100, -100, 100, -100, -100, 100);
		Line he = new Line(-100, -100, 100, 0, 100, 0);
		Line cg = new Line(100, -100, -100, 100, -100, 100);
		Line dh = new Line(-100, -100, -100, -100, -100, 100);
		cube.add(bc);
		cube.add(cd);
		cube.add(da);
		cube.add(fg);
		cube.add(gh);
		cube.add(he);
		cube.add(cg);
		cube.add(dh);		
		return cube;
	}
	
}

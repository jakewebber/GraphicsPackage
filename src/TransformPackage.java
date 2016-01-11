import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

import Jama.*;

public class TransformPackage {

	static Matrix translate = new Matrix(new double[][]{
		{1, 1, 1},
		{1, 1, 1}, 
		{1, 1, 1}
	});

	static Matrix rotate = new Matrix(new double[][]{
		{1, 1, 1},
		{1, 1, 1}, 
		{1, 1, 1}
	});

	static Matrix scale = new Matrix(new double[][]{
		{1, 1, 1},
		{1, 1, 1}, 
		{1, 1, 1}
	});

	Matrix transform = new Matrix(new double[][]{
		{1, 1, 1},
		{1, 1, 1}, 
		{1, 1, 1}
	});

	public TransformPackage(){
	}
	/* DEFINING CUBE */


	public static ArrayList<Line> packageCube(){
		ArrayList<Line> cube = new ArrayList<Line>();
		Line ab = new Line(-100, 100, -100, 100, 100, -100);
		Line bc = new Line(100, 100, -100, 100, -100, -100);
		Line cd = new Line(100, -100, -100, -100, -100, -100);
		Line da = new Line(-100, -100, -100, -100, 100, -100);
		Line ef = new Line(-100, 100, 100, 100, 100, 100);
		Line fg = new Line(100, 100, 100, 100, -100, 100);
		Line gh = new Line(100, -100, 100, -100, -100, 100);
		Line he = new Line(-100, -100, 100, -100, 100, 100);
		Line ae = new Line(-100, 100, -100, -100, 100, 100);
		Line bf = new Line(100, 100, -100, 100, 100, 100);
		Line cg = new Line(100, -100, -100, 100, -100, 100);
		Line dh = new Line(-100, -100, -100, -100, -100, 100);
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


	/** ----------------------------------------------------------
	 * rotate3D: Create a rotation matrix around the origin point (0, 0, 0)
	 * in a 3D space either along the x, y, or z axis. 
	 * @param degrees d, char axis (specifies which axis to rotate on)
	 * @return a 4x4 3D rotation matrix */
	public static Matrix rotate3D(double d, char axis){
		d = Math.toRadians(d);

		if(axis == 'x'){ //X AXIS ROTATION
			return new Matrix(new double[][]{
				{1, 	0,	 			0, 				0},
				{0, 	Math.cos(d), 	Math.sin(d), 	0}, 
				{0, 	-Math.sin(d), 	Math.cos(d), 	0},
				{0, 	0, 				0, 				1}
			});
		}else if(axis == 'y'){ //Y AXIS ROTATION
			return new Matrix(new double[][]{
				{Math.cos(d), 	0,			-Math.sin(d), 	0},
				{0, 			1, 			0, 				0}, 
				{Math.sin(d), 	0, 			Math.cos(d), 	0},
				{0, 			0, 			0, 				1}
			});
		}else if(axis == 'z'){ //Z AXIS ROTATION
			return new Matrix(new double[][]{
				{Math.cos(d), 	Math.sin(d),	0, 	0},
				{-Math.sin(d), 	Math.cos(d), 	0, 	0}, 
				{0, 			0, 				1, 	0},
				{0, 			0, 				0, 	1}
			});
		}else{
			System.out.println("No valid rotation axis specified.");
			System.out.println("Make sure x, y, or z is in lowercase. Returned null.");
			return null;
		}
	}

	/** ----------------------------------------------------------
	 * translate3D: Create a translation matrix in a 3D space to move
	 * a point along the x, y, and z axis. 
	 * @param dx, dy, and dz (respective axis translations)
	 * @return a 4x4 3D translation matrix */
	public static Matrix translate3D(double cx, double cy, double cz){
		return new Matrix(new double[][]{
			{1, 0, 0, cx},
			{0, 1, 0, cy}, 
			{0, 0, 1, cz},
			{0, 0, 0, 1}
		});

	}
	/** ----------------------------------------------------------
	 * scale3D: Create a scale matrix around the origin point (0, 0, 0)
	 * in a 3D space either along the x, y, or z axis. 
	 * @param sx, sy, sz (respective axis scaling)
	 * @return a 4x4 3D scaling matrix */
	public static Matrix scale3D(double sx, double sy, double sz){
		return new Matrix(new double[][]{
			{sx, 0, 0, 0},
			{0, sy, 0, 0}, 
			{0, 0, sz, 0},
			{0, 0, 0, 1}
		});
	}

	

	public static Matrix perspectiveProjection(double fovy, double aspect, double near, double far) {
	    double y2 = near * Math.tan(Math.toRadians(fovy));
	    double y1 = -y2;
	    double x1 = y1 * aspect;
	    double x2 = y2 * aspect;
	    return frustum(x1, x2, y1, y2, near, far);
	}
	public static Matrix frustum(final double left, final double right, final double bottom, final double top, final double near, final double far) {
	   
		return new Matrix(new double[][]{
			{ 2 * near / (right - left),          0,                               0,                           0},
			{0,                               2 * near / (top - bottom),          0,                           0}, 
			{(right + left) / (right - left),    (top + bottom) / (top - bottom),    (near + far) / (near - far),    -1},
			{0,                               0,                               2 * near * far / (near - far),  0}
		});
	}


	/** ----------------------------------------------------------
	 * Concatenate: Combine two transformation matrices.
	 * @param Matrix a - the left side matrix	Matrix b - the right side matrix
	 * @return Combined matrix of the two transformations */
	public static Matrix concatenate(Matrix a, Matrix b){
		return b.times(a);
	}

	/** ----------------------------------------------------------
	 * originCenter: Translate a line's points to be fixed around
	 * the origin point (0, 0) at the line center. Provides a starting 
	 * point for other transformations. 
	 * @param Line a - the line to be transformed. 
	 * @return a new line centered at the origin point */
	public static Line originCenter(Line a){
		Matrix center = translate3D(-a.cx, -a.cy, -a.cz);
		Matrix p1 = center.times(a.p1);
		Matrix p2 = center.times(a.p2);
		a.setMatrices(p1,  p2);
		return a;
	}

	/** ----------------------------------------------------------
	 * applyTransformation: Apply a matrix transformation to a set of lines.
	 * @param Matrix transformation - the transform Matrix to apply.
	 * @param datalines: the array of lines to be transformed.
	 * @return void, simply updates the array */
	public static void applyTransformation(Matrix transformation, ArrayList<Line> dataLines){
		for(int i = 0; i < dataLines.size(); i++){
			Line line = dataLines.get(i);
			//Storing original centers
			//	int cx = line.cx;
			//	int cy = line.cy;
			//	int cz = line.cz;
			//	line = originCenter(line); //move line center to origin
			/* Applying transformation */
			Matrix p1 = transformation.times(line.p1);
			Matrix p2 = transformation.times(line.p2);
			/* Move back to original state */
			//	p1 = translate3D(cx, cy, cz).times(p1);
			//	p2 = translate3D(cx, cy, cz).times(p2);

			line.setMatrices(p1,  p2);
			dataLines.set(i, line);
		}
	}


	/* +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++ */
	/* Starting main method */
	public static void main(String[] args) {
		TransformPackage transformpackage = new TransformPackage();
		ArrayList<Line> lines = TransformPackage.packageCube();

		for(int i = 0; i < lines.size(); i++){
			System.out.print(i + " --");
			lines.get(i).print();
		}
		Matrix rotate = rotate3D(360, 'x');
		System.out.println("applying transformation...");
		applyTransformation(rotate, lines);
		System.out.println("done.");

		for(int i = 0; i < lines.size(); i++){
			System.out.print(i + " --");
			lines.get(i).print();
		}

		// m = rows
		// n = cols


	}
}

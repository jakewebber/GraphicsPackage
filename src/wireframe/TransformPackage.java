package wireframe;
import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;
import javax.swing.JTable;

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

			/* Applying transformation */
			if(line.isEditable){
				Matrix p1 = transformation.times(line.p1);
				Matrix p2 = transformation.times(line.p2);
				line.setMatrices(p1,  p2);
				dataLines.set(i, line);
			}
		}
	}


	/** ----------------------------------------------------------
	 * intputLines - create a Line arrayList from a textfile. 
	 * The line coordinates in the textfile should be separated by spaces.
	 * One line in the file should correspond with one line to draw.
	 * @param filepath: path of the file to import. */
	public static ArrayList<Line> inputLines3D(File file) throws FileNotFoundException{
		ArrayList<Line> dataLines = new ArrayList<>();
		@SuppressWarnings("resource")
		Scanner input = new Scanner(file);
		int count = 0;
		/* Starting file read */
		while(input.hasNext()) {
			count++;
			String line = input.nextLine().replace(" ", ""); //reading textline
			String[] lineData = line.split(","); //parse
			/* Checks that the current text line has 4 coordinate values */
			if(lineData.length < 6){
				System.out.println("Line " + count + " is missing point values. Import halted.");
				return dataLines;
			}else if(lineData.length > 6){
				System.out.println("Line " + count + " has extra point values.");
			}
			/* importing coordinates, creating line, adding to ArrayList */
			double x1 = Double.valueOf(lineData[0]);
			double y1 = Double.valueOf(lineData[1]);
			double z1 = Double.valueOf(lineData[2]);
			double x2 = Double.valueOf(lineData[3]);
			double y2 = Double.valueOf(lineData[4]);
			double z2 = Double.valueOf(lineData[5]);
			System.out.println(x1 +  ", " + y1 + ", " + z1 +", " + x2 +", " + y2 +", " + z2);

			dataLines.add(new Line(x1, y1, z1, x2, y2, z2));
		}
		input.close();
		System.out.println(count + " lines read.");
		return dataLines;
	}


	/** ----------------------------------------------------------
	 * outputLines: Create a new file from the Arraylist of lines 
	 * that has been modified. Line coordinates are separated by spaces,
	 * one line in the file corresponds to one line drawn.
	 * @param filename: the name of the file to output to source folder
	 * @param datalines: the ArrayList of lines to export. */
	public static void exportLines(File file, ArrayList<Line> dataLines){
		int count = 0; //tracking line for error report

		try {
			FileWriter writer = new FileWriter(file);
			/* Adding all line data to filewriter */
			for(int i = 0; i < dataLines.size(); i++){
				count = i;
				Line line = dataLines.get(i);
				writer.append(String.valueOf(line.x1) + ", ");
				writer.append(String.valueOf(line.y1) + ", ");
				writer.append(String.valueOf(line.z1) + ", ");
				writer.append(String.valueOf(line.x2) + ", ");
				writer.append(String.valueOf(line.y2) + ", ");
				writer.append(String.valueOf(line.z2) + "\n");
			}
			writer.flush();
			writer.close();
		} catch (IOException e) {
			System.out.println("ERROR: could not export file.");
			System.out.println("Problem at line " + count);
		}
	}


	/** ----------------------------------------------------------
	 * ArrayToString - convert a dou
	 * ble array to an html formatted string
	 * that is printable in a JLabel. 
	 * @param matrix[][]: the double array to print */
	public static JTable ArrayToTable(double matrix[][]) {
		JTable table = new JTable();
		//table.setTableHeader(null);
		Object[][] values;
		Object[] header;
		if(matrix.length > 3){
			values = new Object[4][4];
		}else{
			values = new Object[3][3];
		}
		for (int row = 0; row < matrix.length; row++) {
			for (int column = 0; column < matrix[row].length; column++) {
				values[row][column] = (Object) (matrix[row][column]);	
			}
		}
		if(matrix.length > 3){
			Object [] x = { "1", "2", "3", "4"};
			header = x;
		}else{
			Object [] x = { "1", "2", "3"};
			header = x;
		}
		table.setModel(new javax.swing.table.DefaultTableModel(values, header));
		return table;
	}
	/* +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++ */
	/* Starting main method */
	public static void main(String[] args) {
		ArrayList<Line> lines = DemoObjects.packageCube(0, 0, 0);

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

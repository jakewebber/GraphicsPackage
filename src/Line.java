import Jama.Matrix;

public class Line {
	//1st podouble coordinates
	double x1;
	double y1;
	double z1; 
	//2nd podouble coordinates
	double x2;
	double y2;
	double z2;
	
	//line center
	double cx;
	double cy;
	double cz;
	Matrix p1;
	Matrix p2;
	
	/* Default Constructor */
	public Line(){
	}

	/** ----------------------------------------------------------
	* Line Constructor: Create a line.
	* @param x1, y1, x2, y2 - doubleeger podoubles xy coordinates for podoubles */
	public Line(double x1, double y1, double z1, double x2, double y2, double z2){
		this.x1 = x1;
		this.y1 = y1;
		this.z1 = z1;
		
		this.x2 = x2;
		this.y2 = y2;
		this.z2 = z2;
		
		this.cx = (x1 + x2) / 2;
		this.cy = (y1 + y2) / 2;
		this.cz = (z1 + z2) / 2;
		//matrices of podouble, used in matrix multiplication
		this.p1 = new Matrix(new double[][]{
			{x1}, {y1}, {z1}, {1}
		});
		this.p2 = new Matrix(new double[][]{
			{x2}, {y2}, {z2}, {1}
		});
	}

	/** ----------------------------------------------------------
	* setMatrices: Update the matrices for a line. This is usually 
	* used after a transformation has been applied to the podoubles.
	* Changing the podouble matrices will update the entire line data.
	* @param Matrix p1, p2 - the corresponding matrices for line podoubles. */
	public void setMatrices(Matrix p1, Matrix p2){
		this.p1 = p1;
		this.p2 = p2;
		
		this.x1 = (double)p1.get(0, 0);
		this.y1 = (double)p1.get(1, 0);
		this.z1 = (double)p1.get(2, 0);
		
		this.x2 = (double)p2.get(0, 0);
		this.y2 = (double)p2.get(1, 0);
		this.z2 = (double)p2.get(2, 0);

		this.cx = (this.x1 + this.x2) / 2;
		this.cy = (this.y1 + this.y2) / 2;
		this.cz = (this.z1 + this.z2) / 2;
		}
	
	/** ------------------------------------
	 * 
	 * 
	 * s----------------------
	* prdouble: Prdouble all line data, including both podoubles and the center. */
	public void print(){
		System.out.println("p1: (" + this.x1 + ", " + this.y1 + ", " + this.z1 +
				")		p2: (" + this.x2 + ", " + this.y2 + ", " + this.z2 + ")		Center: (" + this.cx + ", " + this.cy + ", " + this.cz + ")");
	}
}

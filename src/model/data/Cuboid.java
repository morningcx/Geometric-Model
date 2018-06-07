package model.data;

import java.io.Serializable;

public class Cuboid extends Shape implements Changeable<Shape>,Serializable{
	private float length=0;
	private float width=0;
	private float height=0;
	/**
	 * 长方形的长宽高
	 * @param length 长
	 * @param width 宽
	 * @param hegiht 高
	 */
	public Cuboid(float length,float width,float height) {
		this.width=width;
		this.height=height;
		this.length=length;
		initPoints();
		initLines();
	}

	protected void initPoints() {
		points=new Point[8];
		float lx=length/2;
		float ly=width/2;
		float lz=height/2;
		points[0]=new Point(-lx, ly, lz);
		points[1]=new Point(-lx, -ly, lz);
		points[2]=new Point(lx, -ly, lz);
		points[3]=new Point(lx, ly, lz);
		points[4]=new Point(-lx, ly, -lz);
		points[5]=new Point(lx, ly, -lz);
		points[6]=new Point(lx, -ly, -lz);
		points[7]=new Point(-lx, -ly, -lz);
	}
	
	protected void initLines() {
		lines=new Line[12];
		lines[0]=new Line(points[0], points[1]);
		lines[1]=new Line(points[1], points[2]);
		lines[2]=new Line(points[2], points[3]);
		lines[3]=new Line(points[0], points[3]);
		lines[4]=new Line(points[0], points[4]);
		lines[5]=new Line(points[1], points[7]);
		lines[6]=new Line(points[2], points[6]);
		lines[7]=new Line(points[3], points[5]);
		lines[8]=new Line(points[4], points[7]);
		lines[9]=new Line(points[7], points[6]);
		lines[10]=new Line(points[6], points[5]);
		lines[11]=new Line(points[5], points[4]);
	}
}

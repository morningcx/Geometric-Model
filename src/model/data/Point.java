package model.data;

import java.io.Serializable;

public class Point implements Serializable{
	public float x=0;
	public float y=0;
	public float z=0;
	public float topDegree=0;
	public float leftDegree=0;
	public float radius=0;	
	public Point() {}
	public Point(float x,float y,float z) {
		this.x=x;
		this.y=y;
		this.z=z;
		updateAll();
	}

	/**
	 * 更新所有数据：俯视角度、左视角度、旋转半径
	 */
	public void updateAll() {
		updateTopDegree();
		updateLeftDegree();
		updateRadius();
	}
	
	/**
	 * 更新点坐标与原点的距离，即旋转半径
	 */
	public void updateRadius() {
		radius=(float) Math.sqrt(x*x+y*y+z*z);
	}
	
	/**
	 * 更新俯视图点坐标的角度
	 */
	public void updateTopDegree(){
		topDegree=(float) Math.atan2(y, x);
	}
	/**
	 * 更新左视图点坐标的角度
	 */
	public void updateLeftDegree(){
		leftDegree=(float) Math.atan2(z, y);
	}
}

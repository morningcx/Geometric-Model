package model.data;

import java.util.ArrayList;
import java.util.List;

public class Ball {
	/**
	 * 球体点集合
	 */
	private List<Point> points=new ArrayList<Point>();
	/**
	 * 球体的半径
	 */
	private float radius;
	/**
	 * 构成球体的点数量
	 * 默认：100
	 * 最大值：625
	 */
	private int pointsQuantity=100;
	
	public Ball(float radius) {
		this.radius=radius;
		buildBall();
	}
	
	public Ball(float radius,int pointsQuantity){
		this.radius=radius;
		if (pointsQuantity>625) 
			pointsQuantity=625;
		this.pointsQuantity=pointsQuantity;
		buildBall();
	}
	
	private void buildBall(){
		int maxNum=maxIntSqrt(pointsQuantity);//一个平面最大点数
		float xyAddDegree=(float) (2*Math.PI/maxNum);//横向每次要增加的角度
		float yzAddDegree=(float) (Math.PI/(maxNum+1));//纵向每次要增加的角度
		float yzDegree=0;//纵向初始角度
		for(int i=0;i<maxNum;i++){
			yzDegree+=yzAddDegree;
			float z=(float) (Math.cos(yzDegree)*radius);
			float curRadius=(float) (Math.sin(yzDegree)*radius);//小半径
			float xyDegree=0;//横向初始角度
			for(int j=0;j<maxNum;j++){
				xyDegree+=xyAddDegree;
				float x=(float)Math.cos(xyDegree)*curRadius;
				float y=(float)Math.sin(xyDegree)*curRadius;
				points.add(new Point(x, y, z));
			}
		}
		points.add(new Point(0, 0, radius));//两极
		points.add(new Point(0, 0, -radius));
	}
	
	/**
	 * 开根取最大整数
	 * @param x 被开根数
	 * @return 开根最大整数
	 */
	private int maxIntSqrt(int x) {
		int left = 1, right = x;
		while (left <= right) {
			int mid = left + (right - left) / 2;
			if (mid == x / mid) {
				return mid;
			} else if (mid < x / mid) {
				left = mid + 1;
			} else {
				right = mid - 1;
			}
		}
		return right;
	}
	
	public List<Point> getPoints() {
		return points;
	}

	public float getRadius() {
		return radius;
	}
	
	public void setRadius(float radius) {
		this.radius = radius;
		buildBall();
	}
	
	public int getPointsQuantity() {
		return pointsQuantity;
	}
	
	public void setPointsQuantity(int pointsQuantity) {
		if (pointsQuantity>625) 
			pointsQuantity=625;
		this.pointsQuantity = pointsQuantity;
		buildBall();
	}
}

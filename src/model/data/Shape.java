package model.data;

import java.io.Serializable;

public abstract class Shape implements Changeable<Shape>,Serializable{
	protected Point[] points;
	protected Line[] lines;
	protected abstract void initPoints();
	protected abstract void initLines();

    @Override
	public Shape moveDirection(float x,float y,float z){
		for(Point point:points){
			point.x+=x;
			point.y+=y;
			point.z+=z;
			point.updateAll();//位移需要改变所有数据
		}
		return this;
	}
    
    @Override
	public Shape setSize(float multiple){
		for(Point point:points){
			point.x*=multiple;
			point.y*=multiple;
			point.z*=multiple;
			point.updateRadius();//缩放不需要改变角度
		}
		return this;
	}
    
    @Override
    public Shape deflectDegree(float xy, float yz) {
    	ShapeProcess process=new ShapeProcess();
    	for(Point point:points){
			process.topRotate(point, xy);
			process.leftRotate(point, yz);
		}
    	return this;
    }
    
	public Point[] getPoints(){
		return points;
	}
	
	public Line[] getLines(){
		return lines;
	}
}

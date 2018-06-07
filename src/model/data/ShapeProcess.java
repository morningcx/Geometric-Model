package model.data;

import java.util.List;

public class ShapeProcess {
	public final static int ROTATESPEND_SLOW=200;
	public final static int ROTATESPEND_DEFAULT=100;
	public final static int ROTATESPEND_NORMAL=50;
	public final static int ROTATESPEND_FAST=10;
	private int rotateSpend;
	private ShapeHolder holder;
	
	public ShapeProcess() {}
	
	public ShapeProcess(ShapeHolder holder) {
		rotateSpend=ROTATESPEND_DEFAULT;
		this.holder=holder;
	}
	
	/**
	 * 根据触屏横向以及纵向移动距离计算并返回旋转后的结果
	 * 
	 * @param horizontal 横向移动距离
	 * @param vertical 纵向移动距离
	 * @param rotateSpend 旋转速度
	 * @return 旋转完毕后的容器
	 */
	public ShapeHolder rotate(float horizontal,float vertical) {
		List<Shape> shapes=holder.getHolderList();
		//俯视图旋转角度
		float topRotateDegree=-horizontal/rotateSpend;
		//左视图旋转角度
		float leftRotateDegree=-vertical/rotateSpend;
		for(Shape shape:shapes){
			for(Point point:shape.getPoints()){
				topRotate(point, topRotateDegree);
				leftRotate(point, leftRotateDegree);
			}
		}
		return holder;
	}
	
	/**
	 * 对纵向旋转处理更新
	 * 
	 * @param point 旋转处理的点
	 * @param leftRotateDegree 左视旋转角度
	 */
	public void leftRotate(Point point,float leftRotateDegree){
		point.leftDegree+=leftRotateDegree;//增加左视角度
	    float leftRadius=(float) Math.sqrt((point.radius*point.radius-point.x*point.x));
	    point.y=(float) (Math.cos(point.leftDegree)*leftRadius);
	    point.z=(float) (Math.sin(point.leftDegree)*leftRadius);
	    point.updateTopDegree();//左角度的改变会影响到俯视角度
	}
	
	/**
	 * 对横向旋转处理更新
	 * 
	 * @param point 旋转处理的点
	 * @param topRotateDegree 俯视旋转角度
	 */
	public void topRotate(Point point,float topRotateDegree){
        point.topDegree+=topRotateDegree;//增加俯视旋转角度
	    float topRadius=(float) Math.sqrt((point.radius*point.radius-point.z*point.z));
		point.x=(float) Math.cos(point.topDegree)*topRadius;
		point.y=(float) Math.sin(point.topDegree)*topRadius;
		point.updateLeftDegree();//俯视角度的改变会影响到左视角度
	}
	
	/**
	 * 设置旋转速度
	 * @param rotateSpend 旋转速度
	 */
	public void setRotateSpend(int rotateSpend) {
		this.rotateSpend = rotateSpend;
	}
	
	public int getRotateSpend() {
		return rotateSpend;
	}
	public void setHolder(ShapeHolder holder) {
		this.holder = holder;
	}
}

package model.data;

public interface Changeable<T> {
	/**
	 * 移动方向坐标
	 * 
	 * @param x 向x轴方向移动的距离
	 * @param y 向y轴方向移动的距离
	 * @param z 向z轴方向移动的距离
	 */
	public T moveDirection(float x,float y,float z);
	/**
	 * 改变大小
	 * 
	 * @param multiple 缩放倍数
	 */
	public T setSize(float multiple);
	/**
	 * 旋转角度
	 * 
	 * @param xy 俯视旋转角度,即xy轴平面上的旋转角度
	 * @param yz 左视旋转角度,即yz轴平面上的旋转角度
	 */
	public T deflectDegree(float xy,float yz);
}

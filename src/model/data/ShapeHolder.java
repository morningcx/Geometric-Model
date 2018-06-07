package model.data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ShapeHolder implements Changeable<ShapeHolder>,Serializable{
	private List<Shape> holderList=new ArrayList<Shape>();
	/**
	 * 增加一个模型
	 * @param shape 增加的模型
	 */
	public void add(Shape shape){
		holderList.add(shape);
	}
	/**
	 * 获取容器中的模型列表
	 * @return 模型列表
	 */
	public List<Shape> getHolderList() {
		return holderList;
	}

	@Override
	public ShapeHolder moveDirection(float x, float y, float z) {
		for(Shape shape:holderList){
			shape.moveDirection(x, y, z);
		}
		return this;
	}

	@Override
	public ShapeHolder setSize(float multiple) {
		for(Shape shape:holderList){
			shape.setSize(multiple);
		}
		return this;
	}

	@Override
	public ShapeHolder deflectDegree(float xy, float yz) {
		for(Shape shape:holderList){
			shape.deflectDegree(xy, yz);
		}
		return this;
	}

	
}

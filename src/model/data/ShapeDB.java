package model.data;


public class ShapeDB {
	/**
	 * 餐桌
	 * @param holder
	 */
	public static void addChairsAndDesk(ShapeHolder holder) {
		//桌子
		Shape top=new Prism(6, 300, 20);
		top.moveDirection(0, 0, 200);
		Shape bottom=new Prism(4, 150, 30);
		bottom.moveDirection(0, 0, -200);
		holder.add(new Prism(4, 20, 400));
		holder.add(top);
		holder.add(bottom);
		//椅子
		int h=200;
		int h2=h/2;
		int movey=-450;
		for(int i=0;i<6;i++){
			float degree=(float) (Math.PI/3*i);
			Shape foot1=new Prism(4, 20, h);
			foot1.moveDirection(-h2, -h2+movey, -h2).deflectDegree(degree, 0);
			Shape foot2=new Prism(4, 20, h);
			foot2.moveDirection(h2, -h2+movey, -h2).deflectDegree(degree, 0);
			Shape foot3=new Prism(4, 20, h);
			foot3.moveDirection(-h2, h2+movey, -h2).deflectDegree(degree, 0);
			Shape foot4=new Prism(4, 20, h);
			foot4.moveDirection(h2, h2+movey, -h2).deflectDegree(degree, 0);
			Shape sit=new Prism(4, h+20, 30);
			sit.deflectDegree((float)Math.PI/4, 0).
			        moveDirection(0, movey, 0).
			            deflectDegree(degree, 0);
			Shape back=new Cuboid(h+20, 30, h+20);
			back.moveDirection(0, -h2+movey, h2).deflectDegree(degree, 0);
			holder.add(foot1);
			holder.add(foot2);
			holder.add(foot3);
			holder.add(foot4);
			holder.add(sit);
			holder.add(back);
		}
	}
	/**
	 * 蛋糕
	 * @param shapeHolder
	 */
	public static void addCake(ShapeHolder shapeHolder) {
		shapeHolder.add(new Prism(10, 100, 100));
		Shape second=new Prism(10, 80, 100);
		second.moveDirection(0, 0, 100);
		shapeHolder.add(second);
		Shape first=new Prism(10, 50, 100);
		first.moveDirection(0, 0, 200);
		shapeHolder.add(first);
		Shape candle=new Cuboid(20, 20, 60);
		candle.moveDirection(0, 0, 280);
		shapeHolder.add(candle);
		Shape fire=new Pyramid(4, 15, 40);
		fire.moveDirection(0, 0, 340);
		shapeHolder.add(fire);
		shapeHolder.setSize((float)2.5);
	}
}

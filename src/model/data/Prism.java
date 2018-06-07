package model.data;

public class Prism extends Shape implements Changeable<Shape>{
	private int num=0;
	private float length=0;
	private float height=0;
	/**
	 * 创建一个边长为length，高为height的正num棱柱
	 * 
	 * @param num 棱数
	 * @param length 边长
	 * @param height 高度，即棱长
	 */
	public Prism(int num,float length,float height) {
		this.num=num;
		this.length=length;
		this.height=height;
		initPoints();
		initLines();
	}

	@Override
	protected void initPoints() {
		points=new Point[2*num];
		float r=(float) (length/(2*Math.sin(Math.PI/num)));//半径
		float h=height/2;
		float degree=0;//起始度数0
		for(int i=0;i<num;i++){
			//上层形状赋值
			points[i]=new Point((float)Math.cos(degree)*r,
					                (float)Math.sin(degree)*r,h);
			//下层形状赋值
			points[i+num]=new Point((float)Math.cos(degree)*r,
					                    (float)Math.sin(degree)*r, -h);
			degree+=2*Math.PI/num;
		}
	}

	@Override
	protected void initLines() {
		lines=new Line[3*num];
		int i=0;
		for(i=0;i<num-1;i++){
			//上层当前点和下一点组成线段
			lines[i*3]=new Line(points[i], points[i+1]);
			//上下层点连接组成线段
			lines[i*3+1]=new Line(points[i+num], points[i+num+1]);
			//下层当前点和下一点组成线段
			lines[i*3+2]=new Line(points[i], points[i+num]);
		}
		lines[i*3]=new Line(points[i], points[0]);
		lines[i*3+1]=new Line(points[i+num], points[num]);
		lines[i*3+2]=new Line(points[i], points[i+num]);
	}

}

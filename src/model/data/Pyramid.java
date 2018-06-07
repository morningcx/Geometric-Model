package model.data;

public class Pyramid extends Shape implements Changeable<Shape>{
	private int num=0;
	private float length=0;
	private float height=0;
	public Pyramid(int num,float length,float height) {
		this.num=num;
		this.length=length;
		this.height=height;
		initPoints();
		initLines();
	}
	@Override
	protected void initPoints() {
		points=new Point[num+1];
		float r=(float) (length/(2*Math.sin(Math.PI/num)));//半径
		float h=height/2;
		float degree=0;//起始度数0
		points[0]=new Point(0, 0, h);
		for(int i=1;i<num+1;i++){
			points[i]=new Point((float)Math.cos(degree)*r,(float)Math.sin(degree)*r, -h);
			degree+=2*Math.PI/num;
		}
	}

	@Override
	protected void initLines() {
		lines=new Line[2*num];
		int i=0;
		for(i=0;i<num-1;i++){
			lines[i]=new Line(points[0], points[i+1]);
			lines[i+num]=new Line(points[i+1], points[i+2]);
		}
		lines[i]=new Line(points[0], points[i+1]);
		lines[i+num]=new Line(points[i+1], points[1]);
	}

}

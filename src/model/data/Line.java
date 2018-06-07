package model.data;

import java.io.Serializable;

public class Line implements Serializable{
	public Point start;
	public Point end;
	
	public Line(Point start,Point end) {
		this.start=start;
		this.end=end;
	}
}

package com.example.geometricmodel;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.util.Random;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import model.data.*;

public class Display extends Activity implements OnTouchListener,Callback{
	/**
	 * SurfaceView对象
	 */
	private SurfaceView surface;
	/**
	 * SurfaceHolder对象
	 */
	private SurfaceHolder sHolder;
	/**
	 * 画布背景
	 */
	private int background=Color.WHITE;
	/**
	 * 画线的Paint对象
	 */
	private Paint edgePaint=new Paint();
	/**
	 * 中心画笔
	 */
	private Paint centerPaint=new Paint();
	/**
	 * 手机屏幕的宽高
	 */
	float width=0,height=0;
	/**
	 * 模型容器
	 */
	private ShapeHolder shapeHolder=new ShapeHolder();
	/**
	 * 模型容器集合批处理器
	 */
	private ShapeProcess process=new ShapeProcess(shapeHolder);
	/**
	 * 判断画布是否锁定，若画布没有锁定则可以设置笔画，锁定则旋转模型
	 */
	private boolean canDraw=false;
	/**
	 * 字体笔画长方体的宽高，即立体笔画粗细
	 */
	private float wordWidth=20,wordHeight=20;
	/**
	 * 字体笔画触屏的起始和终点屏幕坐标
	 */
	private float wordStart=0,wordEnd=0;
	/**
	 * 先前鼠标所处坐标x
	 */
	private float preX=0;
	/**
	 * 先前鼠标所处坐标y
	 */
	private float preY=0;
	/**
	 * 放大倍数
	 */
	private float biggerSize=(float) 1.1;
	/**
	 * 缩小倍数
	 */
	private float smallerSize=(float) 0.9;
	/**
	 * 保存文件名
	 */
	private String fileName="fontdata.file";
	/**
	 * 锁定控件
	 */
	private Button lock;
	/**
	 * 添加形状的对话框
	 */
	AlertDialog addShapeDialog=null;
	/**
	 * 添加形状数据对话框
	 */
	AlertDialog addShapeDetailDialog=null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_display);
		//获取屏幕宽高
		DisplayMetrics dm =getResources().getDisplayMetrics(); 
		width=dm.widthPixels/2;
		height=dm.heightPixels/2;
		//初始化控件
		surface=(SurfaceView) findViewById(R.id.surfaceview);
		lock=(Button) findViewById(R.id.lock);
		sHolder=surface.getHolder();
		sHolder.addCallback(this);//生命周期
		surface.setOnTouchListener(this);
	}
	
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.bigger://放大
			shapeHolder.setSize(biggerSize);
			//在锁定情况下放大会导致无法消除跟踪路径，所以另需两步
			if (canDraw) {
				canDraw=!canDraw;
				drawDetail(0, 0);
				canDraw=!canDraw;
			}
			else 
				drawDetail(0, 0);
			break;
		case R.id.smaller://缩小
			shapeHolder.setSize(smallerSize);
			//在锁定情况下缩小会导致无法消除跟踪路径，所以另需两步
			if (canDraw) {
				canDraw=!canDraw;
				drawDetail(0, 0);
				canDraw=!canDraw;
			}
			else 
				drawDetail(0, 0);
			break;
		case R.id.add://增加
			if (addShapeDialog!=null) 
			    addShapeDialog.show();
			else 
                createAddShapeDialog();
			break;
		case R.id.clear://清除画布
			shapeHolder.getHolderList().clear();
			Canvas canvas=sHolder.lockCanvas();
			canvas.drawColor(0, PorterDuff.Mode.CLEAR);
			canvas.drawColor(background);
			sHolder.unlockCanvasAndPost(canvas);
			break;
		case R.id.lock://锁定画布
			if (lock.getText().equals("锁定")) {
				lock.setText("解锁");
				findViewById(R.id.read).setVisibility(View.INVISIBLE);
				findViewById(R.id.save).setVisibility(View.INVISIBLE);
			}
			else{
				lock.setText("锁定");
				findViewById(R.id.read).setVisibility(View.VISIBLE);
				findViewById(R.id.save).setVisibility(View.VISIBLE);
			}
			canDraw=!canDraw;
			break;
		case R.id.save:
			saveFile(fileName);//保存
			break;
		case R.id.read:
			readFile(fileName);//读取
			break;
		case R.id.random:
			//清除
			shapeHolder.getHolderList().clear();
			shapeHolder.add(createRandomShape());
			drawDetail(0, 0);
			break;
		case R.id.back:
			Display.this.finish();
			break;
		default:
			break;
		}
	}
	
	/**
	 * 创建一个随机模型
	 */
	private Shape createRandomShape() {
		Shape randomShape = null;
		Random random=new Random();
		switch (random.nextInt(6)) {
		case 1://长方形
			randomShape=new Cuboid(random.nextInt(500)+200, 
					                       random.nextInt(500)+200, 
					                           random.nextInt(500)+200);
			break;
		case 0://三棱柱
			randomShape=new Prism(3,random.nextInt(500)+200, random.nextInt(500)+200);
			break;
		case 3://正多棱柱
			randomShape=new Prism(random.nextInt(6)+5, 
					                      random.nextInt(300)+200, 
					                          random.nextInt(500)+200);
			break;
		case 2://三棱锥
			randomShape=new Pyramid(3, random.nextInt(500)+200, random.nextInt(500)+200);
			break;
		case 4://正多棱锥
			randomShape=new Pyramid(random.nextInt(6)+5, 
					                        random.nextInt(300)+200, 
					                            random.nextInt(500)+200);
			break;
		case 5://正方形
			float length=random.nextInt(500)+200;
			randomShape=new Cuboid(length, length, length);
		default:
			break;
		}
		return randomShape;
	}
	
	/**
	 * 保存的字体文件
	 * 
	 * @param fileName 文件名
	 */
	private void saveFile(String fileName){
		try {
			File file=new File(Display.this.getFilesDir()+fileName);
			if (!file.exists()) {
				file.createNewFile();
			}
			ObjectOutput wirte=new ObjectOutputStream(new FileOutputStream(file));
			wirte.writeObject(shapeHolder);
			wirte.flush();
			wirte.close();
			Toast.makeText(Display.this, "保存成功："+fileName, Toast.LENGTH_SHORT).show();
		} catch (Exception e) {
			Toast.makeText(Display.this, "写入错误："+e.getMessage(), Toast.LENGTH_SHORT).show();
		}
	}
	
	/**
	 * 读取保存的字体文件
	 * 
	 * @param fileName 文件名
	 */
	private void readFile(String fileName){
		try {
			ObjectInput read=new ObjectInputStream(new FileInputStream(Display.this.getFilesDir()+fileName));
			ShapeHolder readHolder=(ShapeHolder) read.readObject();
			read.close();
			shapeHolder=readHolder;
			process.setHolder(shapeHolder);
			drawDetail(0, 0);
			Toast.makeText(Display.this, "读取成功:"+fileName, Toast.LENGTH_SHORT).show();
		} catch (Exception e) {
			Toast.makeText(Display.this, "读取错误："+e.getMessage(), Toast.LENGTH_SHORT).show();
		}
	}
	/**
	 * 创建添加模型对话框
	 */
	private void createAddShapeDialog() {
		AlertDialog.Builder builder=new Builder(this,AlertDialog.THEME_HOLO_LIGHT);
		View view=LayoutInflater.from(this).inflate(R.layout.dialog_add_shape, null);
		//点击各个模型类别分别将进行不同处理
		OnClickListener listener=new OnClickListener() {
			@Override
			public void onClick(View v) {
                createAddShapeDetailDialog(v);
			}
		};
		view.findViewById(R.id.add_shape_cube).setOnClickListener(listener);
		view.findViewById(R.id.add_shape_cuboid).setOnClickListener(listener);
		view.findViewById(R.id.add_shape_prism).setOnClickListener(listener);
		view.findViewById(R.id.add_shape_pyramid).setOnClickListener(listener);
		//关闭对话框
		view.findViewById(R.id.add_shape_exit).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				addShapeDialog.dismiss();
			}
		});
		builder.setView(view);
		addShapeDialog=builder.show();
	}
	
	/**
	 * 添加模型数据对话框
	 * 
	 * @param v 点击控件
	 */
	private void createAddShapeDetailDialog(final View v) {
		AlertDialog.Builder builder=new Builder(this,AlertDialog.THEME_HOLO_LIGHT);
		View view=LayoutInflater.from(this).inflate(R.layout.dialog_add_shape_detail, null);
		TextView title=(TextView) view.findViewById(R.id.add_shape_detail_title);
		TextView oneText=(TextView) view.findViewById(R.id.add_shape_detail_onetext);
		TextView twoText=(TextView) view.findViewById(R.id.add_shape_detail_twotext);
		TextView threeText=(TextView) view.findViewById(R.id.add_shape_detail_threetext);
		final EditText oneEdit=(EditText) view.findViewById(R.id.add_shape_detail_oneedit);
		final EditText twoEdit=(EditText) view.findViewById(R.id.add_shape_detail_twoedit);
		final EditText threeEdit=(EditText) view.findViewById(R.id.add_shape_detail_threeedit);
		final EditText movex=(EditText) view.findViewById(R.id.add_shape_detail_movex);
		final EditText movey=(EditText) view.findViewById(R.id.add_shape_detail_movey);
		final EditText movez=(EditText) view.findViewById(R.id.add_shape_detail_movez);
		Button submit=(Button) view.findViewById(R.id.add_shape_submit);
		//取消按钮
		view.findViewById(R.id.add_shape_back).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				addShapeDetailDialog.dismiss();
			}
		});
		//判断点击的空间，隐藏和改变控件的一些值
		switch (v.getId()) {
		case R.id.add_shape_cube:
            title.setText("正方体(单位:px)");
            oneText.setText("边长");
            twoText.setVisibility(View.INVISIBLE);
            twoEdit.setVisibility(View.INVISIBLE);
            threeText.setVisibility(View.INVISIBLE);
            threeEdit.setVisibility(View.INVISIBLE);
			break;
		case R.id.add_shape_prism:
			title.setText("多棱柱(单位:px)");
			oneText.setText("棱数");
			twoText.setText("边长");
			break;
		case R.id.add_shape_pyramid:
			title.setText("多棱锥(单位:px)");
			oneText.setText("棱数");
			twoText.setText("边长");
			break;
		default:
			break;
		}
		//执行提交按钮
		submit.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v1) {
				float one = 0,two=0,three=0,x=0,y=0,z=0;
				try{
					one=Float.valueOf(oneEdit.getText().toString());
					//当选择模型为正方体时，two和three控件不显示，需要跳过
					if (v.getId()!=R.id.add_shape_cube) {
						two=Float.valueOf(twoEdit.getText().toString());
						three=Float.valueOf(threeEdit.getText().toString());
					}
					x=Float.valueOf(movex.getText().toString());
					y=Float.valueOf(movey.getText().toString());
					z=Float.valueOf(movez.getText().toString());
				}catch(Exception e){
					Toast.makeText(Display.this, "请输入正确的数据："+e.getMessage(), Toast.LENGTH_LONG).show();
					return;
				}
				switch (v.getId()) {
				case R.id.add_shape_cube:
		            shapeHolder.add(new Cuboid(one, one, one).moveDirection(x, y, z));
					break;
				case R.id.add_shape_cuboid:
					shapeHolder.add(new Cuboid(one, two, three).moveDirection(x, y, z));
					break;
				case R.id.add_shape_prism:
					if (one>20) {
						Toast.makeText(Display.this,"输入棱数过多  棱数范围必须小于等于20", Toast.LENGTH_LONG).show();
						return ;
					}
					shapeHolder.add(new Prism((int)one, two, three).moveDirection(x, y, z));
					break;
				case R.id.add_shape_pyramid:
					if (one>20) {
						Toast.makeText(Display.this,"输入棱数过多  棱数范围必须小于等于20", Toast.LENGTH_LONG).show();
						return ;
					}
					shapeHolder.add(new Pyramid((int)one, two, three).moveDirection(x, y, z));
					break;
				default:
					break;
				}
				//数据没有出错，则显示
				addShapeDialog.dismiss();
				addShapeDetailDialog.dismiss();
				drawDetail(0, 0);
			}
		});
		builder.setView(view);
		addShapeDetailDialog=builder.show();
	}
	
	@Override
	public boolean onTouch(View v, MotionEvent event) {
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			if (canDraw) {
				wordStart=event.getX();
				wordEnd=event.getY();
			}
		    break;
		case MotionEvent.ACTION_UP:
			if (canDraw) 
				drawFont(event.getX(), event.getY());
			break;
        case MotionEvent.ACTION_MOVE://第一次按下只记录鼠标的坐标，此步不进行
        	if(!canDraw)
                drawDetail(event.getX()-preX, event.getY()-preY);
        	else
				drawDetail(0, 0);
			break;
		default:
			break;
		}
		preX=event.getX();//鼠标开始的位置，或者是下次触发的先前位置
		preY=event.getY();
		return true;
	}
	
	/**
	 * 画字体笔画
	 * 
	 * @param curX 鼠标松开位置的X坐标
	 * @param curY 鼠标松开位置的Y坐标
	 */
	private void drawFont(float curX,float curY) {
		float drawX=curX-wordStart;
		float drawY=curY-wordEnd;
		float degree=(float) Math.atan2(drawX , drawY);//求出左视图旋转角度
		Shape wordLine=new Cuboid(wordWidth,wordHeight,
				                    (float)Math.sqrt(drawX*drawX+drawY*drawY));//长度
		//两次旋转和一次位移达到目的位置
		wordLine.deflectDegree(0, -degree);//左视图先旋转
		wordLine.deflectDegree((float)Math.PI/2, 0);//俯视图旋转
		wordLine.moveDirection((wordStart+curX)/2-width,0 ,//x轴移动方向
				                    height-(wordEnd+curY)/2);//z轴移动方向
        shapeHolder.add(wordLine);
        canDraw=!canDraw;//消除跟踪路径
        drawDetail(0, 0);
        canDraw=!canDraw;//重新设置为true
	}
	
	/**
	 * 逐帧画图
	 * 
	 * @param moveX 鼠标横向移动距离
	 * @param moveY 鼠标纵向移动距离
	 */
	private void drawDetail(float moveX,float moveY) {
    	Canvas canvas=sHolder.lockCanvas();
		canvas.drawColor(0, PorterDuff.Mode.CLEAR);//清除画布
		canvas.drawColor(background);//背景
		canvas.drawPoint(width, height, centerPaint);//中心
		if (canDraw) //若画布锁定，需要画出触屏过程中的跟踪路径
			canvas.drawLine(wordStart, wordEnd, moveX+preX, moveY+preY, edgePaint);
		//画所有形状的正视图，只需显示正视图的投影(x,z)
		ShapeHolder afterRotate=process.rotate(moveX, moveY);//批处理
		for(Shape shape:afterRotate.getHolderList()){//批画
			for(Line l:shape.getLines()){
				canvas.drawLine(width+l.start.x,
				                    height-l.start.z,//开始坐标(x,z)
				                        width+l.end.x,
				                            height-l.end.z,//结束坐标(x,z)
				                                edgePaint);
			}
		}
		sHolder.unlockCanvasAndPost(canvas);
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		Intent intent=getIntent();
		int startActivity = intent.getIntExtra("startActivity",1);
		//判断要启动的activity
		switch (startActivity) {
		case 1://showdemo
			shapeHolder.add(new Cuboid(400, 400, 400));
			lock.setVisibility(View.GONE);
			findViewById(R.id.add).setVisibility(View.GONE);
			findViewById(R.id.save).setVisibility(View.GONE);
			findViewById(R.id.read).setVisibility(View.GONE);
			findViewById(R.id.clear).setVisibility(View.GONE);
			break;
		case 2://designsimple
			fileName="simpledate.file";
			findViewById(R.id.random).setVisibility(View.GONE);
			lock.setVisibility(View.GONE);
			break;
		case 3://designfont
			fileName="fontdata.file";
			findViewById(R.id.add).setVisibility(View.GONE);
			findViewById(R.id.random).setVisibility(View.GONE);
			break;
		case 4://shapedb
			//判断要显示的模型库模型
			switch (intent.getIntExtra("choiceShape", 0)) {
			case 0:
				ShapeDB.addChairsAndDesk(shapeHolder);
				break;
			case 1:
				ShapeDB.addCake(shapeHolder);
				break;
			default:
				break;
			}
			lock.setVisibility(View.GONE);
			findViewById(R.id.add).setVisibility(View.GONE);
			findViewById(R.id.save).setVisibility(View.GONE);
			findViewById(R.id.read).setVisibility(View.GONE);
			findViewById(R.id.clear).setVisibility(View.GONE);
			findViewById(R.id.random).setVisibility(View.GONE);
			break;
		default:
			break;
		}
		//获取个人化数据并赋值
		int[] styleData=intent.getIntArrayExtra("styleData");
		background=styleData[0];
		edgePaint.setColor(styleData[1]);
		edgePaint.setStrokeWidth(styleData[2]*3);
		centerPaint.setStrokeWidth(styleData[2]*3);
		centerPaint.setColor(styleData[4]==1?styleData[3]:Color.TRANSPARENT);
		drawDetail(0, 0);
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {}
}

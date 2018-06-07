package com.example.geometricmodel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.ToggleButton;

public class HomePage extends Activity{
	/**
	 * 模型库对话框
	 */
    private AlertDialog dbDialog;
    /**
     * 个性化对话框
     */
    private AlertDialog styleDialog;
    /**
     * 要启动activity参数载体
     */
    private Intent intent;
    /**
     * 模型库对话框字符串组
     */
    private String[] DBitems=new String[]{"餐桌","蛋糕"};
    /**
     * 颜色图片
     */
    private int[] colorImage={
    		R.drawable.black,R.drawable.white,R.drawable.gray,
    		R.drawable.red,R.drawable.orange,R.drawable.yellow,
    		R.drawable.green,R.drawable.blue,R.drawable.purple,
    		R.drawable.pink,R.drawable.coffee	
    };
    /**
     * 个人化对话框字符串组
     */
    private String[] colorString={
    		"黑色","白色","灰色",
    		"红色","橙色","黄色",
    		"绿色","蓝色","紫色",
    		"粉红色","咖啡色"
    };
    /**
     * 颜色
     */
    private int[] color={
    		Color.BLACK,Color.WHITE,0xFF7F7F7F,
    		Color.RED,0xFFFF7F27,Color.YELLOW,
    		0xFF75C180,0xFF00A3E8,0xFFA349A4,
    		0xFFFFAEC9,0xFFB97A57
    };
    /**
     * 个性化数据(0背景,1线段颜色,2线段粗细,3中心颜色,4中心显示)
     * 中心显示     0：不显示  1：显示
     */
    private int[] styleData={color[1],color[0],1,color[0],1};
    
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home_page);
		intent=new Intent(HomePage.this,Display.class);
	}

	public void onClick(View v) {
		if (v!=null) {
			switch (v.getId()) {
			case R.id.homepage_showdemo:
				intent.putExtra("startActivity", 1);
				break;
			case R.id.homepage_designsimple:
				intent.putExtra("startActivity", 2);
				break;
			case R.id.homepage_designfont:
				intent.putExtra("startActivity", 3);
				break;
			case R.id.homepage_shapedb:
				intent.putExtra("startActivity", 4);
				if (dbDialog!=null) 
					dbDialog.show();
				else 
					createShapeDBDialog();
				return;
		    case R.id.homepage_style:
		    	if (styleDialog!=null) 
					styleDialog.show();
		    	else 
		    		createStyleDialog();
		    	return ;
			case R.id.homepage_exit:
				HomePage.this.finish();
				return;
			default:
				break;
			}
		}
		intent.putExtra("styleData", styleData);//个性化数据
		startActivity(intent);
	}
	
	/**
	 * 创建模型库对话框
	 */
	private void createShapeDBDialog() {
		AlertDialog.Builder builder=new Builder(this,AlertDialog.THEME_HOLO_LIGHT);
		builder.setTitle("模型库");
		builder.setItems(DBitems, new DialogInterface.OnClickListener(){

			@Override
			public void onClick(DialogInterface dialog, int which) {
				intent.putExtra("choiceShape", which);
				HomePage.this.onClick(null);
			}
		});
		dbDialog=builder.show();
	}
	
    /**
     * 创建个性化对话框
     */
	private void createStyleDialog() {
		AlertDialog.Builder builder=new Builder(this,AlertDialog.THEME_HOLO_LIGHT);
		View view=LayoutInflater.from(this).inflate(R.layout.dialog_style, null);
		//初始化控件
		final Spinner backgroundSpinner=(Spinner) view.findViewById(R.id.background_spinner);
		final Spinner lineColorSpinner=(Spinner) view.findViewById(R.id.linecolor_spinner);
		final Spinner centerColorSpinner=(Spinner) view.findViewById(R.id.centercolor_spinner);
		final Spinner lineThickSpinner=(Spinner) view.findViewById(R.id.linethick_spinner);
		final ToggleButton centerShowSpinner=(ToggleButton) view.findViewById(R.id.centershow_button);
		//3个颜色适配器
		SimpleAdapter colorAdapter=new SimpleAdapter(this, getColorData(), 
				R.layout.spinner_item_color, 
				new String[]{"pic","text"}, 
				new int[]{R.id.spinner_item_color_image,R.id.spinner_item_color_text});
		backgroundSpinner.setAdapter(colorAdapter);
		lineColorSpinner.setAdapter(colorAdapter);
		centerColorSpinner.setAdapter(colorAdapter);
		//线段粗细适配器
		SimpleAdapter thickAdapter=new SimpleAdapter(this, getThickData(), 
				R.layout.spinner_item_color, 
				new String[]{"text"}, 
				new int[]{R.id.spinner_item_color_text});
		lineThickSpinner.setAdapter(thickAdapter);
		//设置默认属性
		centerShowSpinner.setChecked(true);//默认显示中心
		backgroundSpinner.setSelection(1);//背景白色
		lineColorSpinner.setSelection(0);//线段黑色
		centerColorSpinner.setSelection(0);//中心黑色
		lineThickSpinner.setSelection(0);//线段宽度1
		//点击提交，读取个性化数据
        view.findViewById(R.id.stylesubmit).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				styleData[0]=color[backgroundSpinner.getSelectedItemPosition()];
				styleData[1]=color[lineColorSpinner.getSelectedItemPosition()];
				styleData[2]=lineThickSpinner.getSelectedItemPosition()+1;
				styleData[3]=color[centerColorSpinner.getSelectedItemPosition()];
				styleData[4]=centerShowSpinner.isChecked()?1:0;
				styleDialog.dismiss();
			}
		});
		builder.setView(view);
		styleDialog=builder.show();
	}
	
	/**
	 * 获取颜色数据
	 * 
	 * @return
	 */
    private List<Map<String, Object>> getColorData() {
    	List<Map<String, Object>> dataList=new ArrayList<Map<String,Object>>();
        for (int i = 0; i < colorString.length; i++) {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("pic", colorImage[i]);
            map.put("text", colorString[i]);
            dataList.add(map);
        }
        return dataList;
    }
    
    private List<Map<String, Object>> getThickData() {
    	List<Map<String, Object>> dataList=new ArrayList<Map<String,Object>>();
        for (int i = 0; i < 5; i++) {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("text", i+1+"px");
            dataList.add(map);
        }
        return dataList;
    }
}

package com.first.autoclick;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.text.InputType;
import android.view.Gravity;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {
    EditText xField, yField, delayField;
    @Override public void onCreate(Bundle b) {
        super.onCreate(b);
        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL); layout.setPadding(36,48,36,24);
        TextView heading = new TextView(this); heading.setText("S24 범용 자동 터치"); heading.setTextSize(24); heading.setGravity(Gravity.CENTER); layout.addView(heading);
        TextView help = new TextView(this); help.setText("화면의 절대 좌표를 지정합니다. 터치 중지하려면 앱으로 돌아와 중지를 누르세요. 접근성 서비스가 꺼지면 동작하지 않습니다."); layout.addView(help);
        xField = field(layout, "X 좌표", "500"); yField = field(layout, "Y 좌표", "1000"); delayField = field(layout, "반복 간격 (밀리초, 최소 100)", "5000");
        Button access = new Button(this); access.setText("접근성 설정 열기"); layout.addView(access); access.setOnClickListener(v -> startActivity(new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS)));
        Button start = new Button(this); start.setText("시작"); layout.addView(start);
        start.setOnClickListener(v -> { if (ClickService.instance == null) { Toast.makeText(this,"접근성에서 S24 Auto Clicker를 켜주세요",Toast.LENGTH_LONG).show(); return; }
            try { int x=Integer.parseInt(xField.getText().toString()); int y=Integer.parseInt(yField.getText().toString()); long delay=Long.parseLong(delayField.getText().toString());
                if(x<0||y<0||delay<100||delay>86400000) throw new IllegalArgumentException();
                getPreferences(MODE_PRIVATE).edit().putInt("x",x).putInt("y",y).putLong("delay",delay).apply();
                ClickService.instance.start(x,y,delay); Toast.makeText(this,"자동 터치 시작",Toast.LENGTH_SHORT).show();
            } catch(Exception e) { Toast.makeText(this,"좌표와 간격을 확인하세요",Toast.LENGTH_SHORT).show(); } });
        Button stop = new Button(this); stop.setText("중지"); layout.addView(stop); stop.setOnClickListener(v -> { if(ClickService.instance!=null) ClickService.instance.stop(); Toast.makeText(this,"중지됨",Toast.LENGTH_SHORT).show(); });
        setContentView(layout);
    }
    EditText field(LinearLayout layout,String label,String fallback) {
        TextView t=new TextView(this); t.setText(label); layout.addView(t);
        EditText e=new EditText(this); e.setSingleLine(true); e.setInputType(InputType.TYPE_CLASS_NUMBER);
        String key=label.startsWith("X")?"x":label.startsWith("Y")?"y":"delay";
        e.setText(String.valueOf(key.equals("delay")?getPreferences(MODE_PRIVATE).getLong(key,Long.parseLong(fallback)):getPreferences(MODE_PRIVATE).getInt(key,Integer.parseInt(fallback))));
        layout.addView(e); return e;
    }
}

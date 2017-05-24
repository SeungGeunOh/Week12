package com.example.oh.project12;

import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.Toast;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    public static boolean checkStamp = false;
    public static boolean checkBluring = false;
    public static boolean checkColoring = false;
    public static boolean checkPenWidth = false;
    public static int color = 0;
    CheckBox stamp;
    MyCanvas myCanvas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("MyCanvas");
        myCanvas = (MyCanvas) findViewById(R.id.canvas);
        stamp = (CheckBox) findViewById(R.id.checkBox);
        stamp.setOnClickListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.bluring){
            if (item.isChecked()) {
                item.setChecked(false);
                checkBluring = false;
                myCanvas.bluring();
            }
            else {
                item.setChecked(true);
                checkBluring = true;
                myCanvas.bluring();
            }
        }
        else if (item.getItemId() == R.id.coloring){
            if (item.isChecked()) {
                item.setChecked(false);
                checkColoring = false;
                myCanvas.coloring();
            }
            else {
                item.setChecked(true);
                checkColoring = true;
                myCanvas.coloring();
            }
        }
        else if (item.getItemId() == R.id.big){
            if (item.isChecked()) {
                item.setChecked(false);
                checkPenWidth = false;
                myCanvas.setStorokeWidth();
            }
            else {
                item.setChecked(true);
                checkPenWidth = true;
                myCanvas.setStorokeWidth();
            }
        }
        else if (item.getItemId() == R.id.red){
            color = 1;
            myCanvas.setPenColor();
        }
        else if (item.getItemId() == R.id.blue){
            color = 2;
            myCanvas.setPenColor();
        }

        return super.onOptionsItemSelected(item);
    }

    public void onClick(View v) {
        if (v.getId() == R.id.checkBox) {
            if (stamp.isChecked())
                checkStamp = true;
            else {
                checkStamp = false;
                MyCanvas.scale = false;
                MyCanvas.move = false;
                MyCanvas.skew = false;
                MyCanvas.rotate = false;
            }
        } else if (v.getId() == R.id.eraser) {
            myCanvas.clear();
        } else if (v.getId() == R.id.open) {
            myCanvas.openPaint();
        } else if (v.getId() == R.id.save) {
            try {
                FileOutputStream fos = openFileOutput("test.jpg", 0);
                MyCanvas.mBitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
                fos.flush();
                fos.close();
                Toast.makeText(getApplicationContext(), "저장됨", Toast.LENGTH_SHORT).show();
                myCanvas.clear();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (v.getId() == R.id.rotate) {
            setCheckStamp();
            MyCanvas.scale = false;
            MyCanvas.move = false;
            MyCanvas.skew = false;
            myCanvas.rotate();
        } else if (v.getId() == R.id.move) {
            setCheckStamp();
            MyCanvas.scale = false;
            MyCanvas.skew = false;
            MyCanvas.rotate = false;
            myCanvas.move();
        } else if (v.getId() == R.id.scale) {
            setCheckStamp();
            MyCanvas.move = false;
            MyCanvas.skew = false;
            MyCanvas.rotate = false;
            myCanvas.scale();
        } else if (v.getId() == R.id.skew) {
            setCheckStamp();
            MyCanvas.scale = false;
            MyCanvas.move = false;
            MyCanvas.rotate = false;
            myCanvas.skew();
        }
    }

    private void setCheckStamp() {
        stamp.setChecked(true);
        checkStamp = true;
    }
}



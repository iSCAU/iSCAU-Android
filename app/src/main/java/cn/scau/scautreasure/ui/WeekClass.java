package cn.scau.scautreasure.ui;

import android.app.ActionBar;
import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.util.FloatMath;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.sharedpreferences.Pref;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import cn.scau.scautreasure.R;
import cn.scau.scautreasure.helper.ClassHelper;
import cn.scau.scautreasure.model.ClassModel;
import cn.scau.scautreasure.util.DateUtil;

/**
 * Created by macroyep on 14/10/6.
 */
@EActivity
public class WeekClass extends Activity {
    Button bigger, smaller, up, donw, right, left;
    int[] color = {Color.BLACK, Color.GREEN, Color.RED, Color.BLUE, Color.DKGRAY};
    Random r = new Random();
    GridLayout gridLayout, leftGrid, topGrid;
    FrameLayout frameLayout;
    @Bean
    DateUtil dateUtil;
    @Pref
    cn.scau.scautreasure.AppConfig_ config;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test);
        findView();
        loadClass();


    }


    void loadClass() {

        for (int i = 1; i <= 7; i++) {
            List<ClassModel> dayClassList = null;
            String chineseDay = dateUtil.numDayToChinese(i);
            dayClassList = classHelper.getDayLesson(chineseDay);


            setView(adapterList(dayClassList), i);
        }

        updateTop();
        updateLeft();
    }

    List<ClassModel> adapterList(List<ClassModel> list) {
        int node[] = new int[13];
        List<ClassModel> list1 = new ArrayList<ClassModel>();
        for (int i = 0; i < list.size(); i++) {
            int start = Integer.parseInt(list.get(i).getNode().split(",")[0]);
            int count = list.get(i).getNode().split(",").length;
            node[start - 1] = start;
            for (int j = start; j < start + count; j++) {
                node[j - 1] = j;
            }

        }

        for (int i = 0, arg = 0; i < node.length; i++) {
            if (i < node.length - 1) {
                if (node[i] == 0 && node[i + 1] == 0) {
                    ClassModel c = new ClassModel();
                    c.setNode(String.valueOf(i + 1) + "," + String.valueOf(i + 1));
                    list1.add(c);
                } else {
                    if (node[i] + 1 == node[i + 1]) {
                        if (arg < list.size()) {
                            list1.add(list.get(arg));
                            arg += 1;
                        }
                    }
                }
            }


//            System.out.println(node[i]);
        }

        return list1;
    }

    @Bean
    ClassHelper classHelper;

    void findView() {
        bigger = (Button) findViewById(R.id.bigger);
        smaller = (Button) findViewById(R.id.smaller);
        up = (Button) findViewById(R.id.up);
        donw = (Button) findViewById(R.id.down);
        left = (Button) findViewById(R.id.left);
        right = (Button) findViewById(R.id.right);
        gridLayout = (GridLayout) findViewById(R.id.gridlayout);
        leftGrid = (GridLayout) findViewById(R.id.leftGrid);
        topGrid = (GridLayout) findViewById(R.id.topGrid);
        frameLayout = (FrameLayout) findViewById(R.id.frameLayout);
        bigger.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FrameLayout.LayoutParams linearParams2 = (FrameLayout.LayoutParams) gridLayout.getLayoutParams();
                linearParams2.width = gridLayout.getWidth() + 50;
                linearParams2.height = gridLayout.getHeight() + 50;
                gridLayout.setLayoutParams(linearParams2);
                gridLayout.removeAllViews();


                FrameLayout.LayoutParams linearParamst = (FrameLayout.LayoutParams) topGrid.getLayoutParams();
                linearParamst.width = gridLayout.getWidth() + 50;
                topGrid.setLayoutParams(linearParamst);
                topGrid.removeAllViews();

                FrameLayout.LayoutParams linearParamsl = (FrameLayout.LayoutParams) leftGrid.getLayoutParams();
                linearParamsl.height = gridLayout.getHeight() + 50;
                leftGrid.setLayoutParams(linearParamsl);
                leftGrid.removeAllViews();


                loadClass();
            }

        });
        smaller.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FrameLayout.LayoutParams linearParams2 = (FrameLayout.LayoutParams) gridLayout.getLayoutParams();
                linearParams2.width = gridLayout.getWidth() - 50;
                linearParams2.height = gridLayout.getHeight() - 50;
                gridLayout.setLayoutParams(linearParams2);
                gridLayout.removeAllViews();


                FrameLayout.LayoutParams linearParamst = (FrameLayout.LayoutParams) topGrid.getLayoutParams();
                linearParamst.width = gridLayout.getWidth() - 50;
                topGrid.setLayoutParams(linearParamst);
                topGrid.removeAllViews();

                FrameLayout.LayoutParams linearParamsl = (FrameLayout.LayoutParams) leftGrid.getLayoutParams();
                linearParamsl.height = gridLayout.getHeight() - 50;
                leftGrid.setLayoutParams(linearParamsl);
                leftGrid.removeAllViews();


                loadClass();

            }
        });
        up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gridLayout.setY(gridLayout.getY() - 50);
                leftGrid.setY(leftGrid.getY() - 50);
            }
        });
        donw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gridLayout.setY(gridLayout.getY() + 50);
                leftGrid.setY(leftGrid.getY() + 50);

            }
        });
        left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gridLayout.setX(gridLayout.getX() - 50);
                topGrid.setX(topGrid.getX() - 50);
            }
        });
        right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gridLayout.setX(gridLayout.getX() + 50);
                topGrid.setX(topGrid.getX() + 50);
//                gridLayout.resi
            }
        });


        gridLayout.setOnTouchListener(new View.OnTouchListener() {
            float baseValue;
            float mCurrentScale = 1;
            float last_x = -1;
            float last_y = -1;

            @Override
            public boolean onTouch(View view, MotionEvent event) {
                // TODO Auto-generated method stub
                // return ArtFilterActivity.this.mGestureDetector.onTouchEvent(event);
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    baseValue = 0;
                    float x = last_x = event.getRawX();
                    float y = last_y = event.getRawY();
                } else if (event.getAction() == MotionEvent.ACTION_MOVE) {
                    if (event.getPointerCount() == 2) {
                        float x = event.getX(0) - event.getX(1);
                        float y = event.getY(0) - event.getY(1);
                        float value = (float) Math.sqrt(x * x + y * y);// 计算两点的距离
                        if (baseValue == 0) {
                            baseValue = value;
                        } else {
                            if (value - baseValue >= 10 || value - baseValue <= -10) {
                                float scale = value / baseValue;// 当前两点间的距离除以手指落下时两点间的距离就是需要缩放的比例。
                                zoom(scale);  //缩放图片

                            }
                        }
                    } else if (event.getPointerCount() == 1) {
                        float x = event.getRawX();
                        float y = event.getRawY();
                        x -= last_x;
                        y -= last_y;
                        if (x >= 10 || y >= 10 || x <= -10 || y <= -10)
                            move(x, y); //移动图片位置
                        last_x = event.getRawX();
                        last_y = event.getRawY();
                    }
                } else if (event.getAction() == MotionEvent.ACTION_UP) {

                }
                return true;
            }

            void move(float x, float y) {
                System.out.println(x + "-" + y);

                    gridLayout.setX(x + gridLayout.getX());
                    topGrid.setX(x + gridLayout.getX());

                      gridLayout.setY(y + gridLayout.getY());
                    leftGrid.setY(y + gridLayout.getY());



            }

            void zoom(float scale) {
                gridLayout.removeAllViews();
                System.out.println("缩放倍数:" + scale);
                float s;
                if (scale > 1) {
                    s = scale * 100;
                } else {
                    s = -scale * 100;
                }
                FrameLayout.LayoutParams linearParams2 = (FrameLayout.LayoutParams) gridLayout.getLayoutParams();
                linearParams2.width = (int) (gridLayout.getWidth() + s);
                linearParams2.height = (int) (gridLayout.getHeight() + s);
                gridLayout.setLayoutParams(linearParams2);
                gridLayout.removeAllViews();


                FrameLayout.LayoutParams linearParamst = (FrameLayout.LayoutParams) topGrid.getLayoutParams();
                linearParamst.width = (int) (gridLayout.getWidth() + s);
                topGrid.setLayoutParams(linearParamst);
                topGrid.removeAllViews();

                FrameLayout.LayoutParams linearParamsl = (FrameLayout.LayoutParams) leftGrid.getLayoutParams();
                linearParamsl.height = (int) (gridLayout.getHeight() + s);
                leftGrid.setLayoutParams(linearParamsl);
                leftGrid.removeAllViews();


                loadClass();


            }
        });


    }


    @UiThread
    void setView(List<ClassModel> list, int day) {


        //课程内容
        for (int i = 0; i < list.size(); i++) {
            TextView btn = new TextView(this);
            btn.setTextSize(10.0f);
            btn.setTextColor(Color.WHITE);
            btn.setGravity(Gravity.CENTER);

            if (list.get(i).getClassname() != null) {
                btn.setBackgroundColor(color[r.nextInt(color.length)]);
            } else {
//                int colors[] = {R.color.searchbar_edittext_hint_color, R.color.actionbar_title_color};
//                btn.setBackgroundColor(getResources().getColor(colors[r.nextInt(colors.length)]));
                btn.setBackgroundColor(getResources().getColor(R.color.abc_search_url_text_holo));
            }
            btn.setHeight(LinearLayout.LayoutParams.MATCH_PARENT);
            if (list.get(i).getClassname() != null) {
                btn.setText(list.get(i).getClassname());
            } else {
//                btn.setText("无课");
//                btn.setTextColor(Color.BLACK);
            }
            int start = Integer.parseInt(list.get(i).getNode().split(",")[0]);
            int count = list.get(i).getNode().split(",").length;

            btn.setWidth(gridLayout.getWidth() / 7);
            btn.setHeight(count * gridLayout.getHeight() / 13);

//            System.out.println("星期" + day + ",开始节:" + start + ",节数" + count);
            GridLayout.Spec rowSpec = GridLayout.spec(start, count);
            GridLayout.Spec columnSpec = GridLayout.spec(day - 1, 1);

            GridLayout.LayoutParams params = new GridLayout.LayoutParams(rowSpec, columnSpec);
            params.setGravity(Gravity.CENTER);

            gridLayout.addView(btn, params);
        }

    }

    String[] weekDay = {"一", "二", "三", "四", "五", "六", "日"};

    @UiThread
    void updateTop() {
        topGrid.removeAllViews();
        for (int i = 0; i < 7; i++) {
            TextView btn = new TextView(this);
            btn.setTextSize(10.0f);
            btn.setTextColor(Color.WHITE);

            btn.setGravity(Gravity.CENTER);
            btn.setText(weekDay[i]);
            btn.setWidth(gridLayout.getWidth() / 7);
            GridLayout.Spec rowSpec = GridLayout.spec(0, 1);
            GridLayout.Spec columnSpec = GridLayout.spec(i, 1);

            GridLayout.LayoutParams params = new GridLayout.LayoutParams(rowSpec, columnSpec);
            params.setGravity(Gravity.CENTER);

            topGrid.addView(btn, params);
        }

    }

    @UiThread
    void updateLeft() {
        leftGrid.removeAllViews();
        for (int i = 0; i < 13; i++) {
            TextView btn = new TextView(this);
            btn.setTextSize(10.0f);
            btn.setTextColor(Color.WHITE);

            btn.setGravity(Gravity.CENTER);
            btn.setText(String.valueOf(i + 1));
            btn.setHeight(gridLayout.getHeight() / 13);
            GridLayout.Spec rowSpec = GridLayout.spec(i, 1);
            GridLayout.Spec columnSpec = GridLayout.spec(0, 1);

            GridLayout.LayoutParams params = new GridLayout.LayoutParams(rowSpec, columnSpec);
            params.setGravity(Gravity.CENTER);

            leftGrid.addView(btn, params);
        }

    }


}

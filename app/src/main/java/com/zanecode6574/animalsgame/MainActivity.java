package com.zanecode6574.animalsgame;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Random;

public class MainActivity extends ActivityCollector{
    ImageView imageView_1_1;
    ImageView imageView_1_2;
    ImageView imageView_1_3;
    ImageView imageView_1_4;
    ImageView imageView_2_1;
    ImageView imageView_2_2;
    ImageView imageView_2_3;
    ImageView imageView_2_4;
    ImageView imageView_3_1;
    ImageView imageView_3_2;
    ImageView imageView_3_3;
    ImageView imageView_3_4;
    ImageView imageView_4_1;
    ImageView imageView_4_2;
    ImageView imageView_4_3;
    ImageView imageView_4_4;

    TextView textView_socre;
    TextView textView_time;

    //第一次点击的图片ID和在数组中的位置以及两次点击的imageview
    int imageClick=0;
    int positionClick=0;
    ImageView[] imageViewClick=new ImageView[2];

    //判断是不是第一次启动游戏
    boolean initDone=false;
    int score=0;

    //用来保存ImageView对应的图片id
    HashMap<Integer,Integer> hashMap=new HashMap<>();
    HashMap<Integer,Integer> tempHashMap=new HashMap<>();
    int clickTime=0;

    int[] imageArtId={R.drawable.bird_artboard,R.drawable.cat_artboard,R.drawable.fish_artboard,R.drawable.honey_artboard,R.drawable.house_artboard,R.drawable.pig_artboard,R.drawable.sun_artboard,R.drawable.flower_artboard};
    int[] imageOkArtId={R.drawable.bird_ok_artboard,R.drawable.cat_ok_artboard,R.drawable.fish_ok_artboard,R.drawable.honey_ok_artboard,R.drawable.house_ok_artboard,R.drawable.pig_ok_artboard,R.drawable.sun_ok_artboard,R.drawable.flower_ok_artboard};
    int[] imageNoArtId={R.drawable.bird_no_artboard,R.drawable.cat_no_artboard,R.drawable.fish_no_artboard,R.drawable.honey_no_artboard,R.drawable.house_no_artboard,R.drawable.pig_no_artboard,R.drawable.sun_no_artboard,R.drawable.flower_no_artboard};
    ImageView[] imageViews=new ImageView[16];


    CountDownTimer countDownTimer=new CountDownTimer(4000,1000) {
        @Override
        public void onTick(long millisUntilFinished) {
            textView_time.setText(String.valueOf(millisUntilFinished/1000));
        }

        @Override
        public void onFinish() {
            FinalPopup finalPopup=new FinalPopup();
            finalPopup.show(String.valueOf(score));
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }


    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        resetGame();
    }

    private void check(View v){
        if(clickTime==0) {
            //要判断点击的是不是已经变成灰色的图标
            boolean isClicked=false;
            for(int i=0;i<8;i++){
                if (hashMap.get(v.getId()) == imageNoArtId[i]) {
                    isClicked=true;
                }
            }

            if(!isClicked) {
                for (int i = 0; i < 8; i++) {
                    if (hashMap.get(v.getId()) == imageArtId[i]) {
                        ImageView tempImageView = (ImageView) v;
                        tempImageView.setImageResource(imageOkArtId[i]);
                        tempHashMap.put(v.getId(), imageOkArtId[i]);
                        imageClick = imageArtId[i];
                        positionClick = i;
                        imageViewClick[0] = tempImageView;
                    }
                }
                clickTime += 1;
            }

        }else{
            if(hashMap.get(v.getId())==imageClick){
                //如果两次点击图片相同，设置为灰色
                ImageView tempImageView = (ImageView) v;
                tempImageView.setImageResource(imageNoArtId[positionClick]);
                imageViewClick[0].setImageResource(imageNoArtId[positionClick]);
                tempHashMap.put(v.getId(),imageNoArtId[positionClick]);
                tempHashMap.put(imageViewClick[0].getId(),imageNoArtId[positionClick]);
                hashMap=tempHashMap;
                //次数和别的都归零
                score=score+1;
                textView_socre.setText(String.valueOf(score));
                clickTime=0;
                imageViewClick[0]=null;
                positionClick=0;
            }else{
                //清除之前的
                imageViewClick[0].setImageResource(imageArtId[positionClick]);
                //次数和别的都归零
                hashMap.put(imageViewClick[0].getId(),imageArtId[positionClick]);
                tempHashMap=hashMap;
                clickTime=0;
                imageViewClick[0]=null;
                positionClick=0;
            }
        }
    }


    //分数展示
    class FinalPopup {
        // 声明PopupWindow
        private PopupWindow popupWindow;
        // 声明PopupWindow对应的视图
        private View popupView;
        // 声明平移动画
        private TranslateAnimation animation;

        private void show(String s) {
            this.popupView = View.inflate(MainActivity.this, R.layout.popup_score, null);
            TextView textView = popupView.findViewById(R.id.textView_finalScore);
            Button button=popupView.findViewById(R.id.button_finalExit);
            Button buttonRestart=popupView.findViewById(R.id.button_finalRestart);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ActivityCollector.finishAll();
                }
            });

            buttonRestart.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    initDone=false;
                    hashMap.clear();
                    tempHashMap.clear();
                    score=0;
                    dismiss();
                    resetGame();
                }
            });
            textView.setText(s);
            // 参数2,3：指明popupwindow的宽度和高度
            popupWindow = new PopupWindow(popupView, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            WindowManager.LayoutParams lp = getWindow().getAttributes();
            lp.alpha = 0.4f;
            getWindow().setAttributes(lp);
            popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
                @Override
                public void onDismiss() {
                    WindowManager.LayoutParams lp = getWindow().getAttributes();
                    lp.alpha = 1f;
                    getWindow().setAttributes(lp);
                }
            });

            // 设置点击popupwindow外屏幕其它地方消失
            popupWindow.setFocusable(false);
            popupWindow.setOutsideTouchable(false);
            // 平移动画相对于手机屏幕的底部开始，X轴不变，Y轴从1变0
            animation = new TranslateAnimation(Animation.RELATIVE_TO_PARENT, 0, Animation.RELATIVE_TO_PARENT, 0,
                    Animation.RELATIVE_TO_PARENT, 1, Animation.RELATIVE_TO_PARENT, 0);
            animation.setInterpolator(new AccelerateInterpolator());
            animation.setDuration(200);
            // 设置popupWindow的显示位置，此处是在手机屏幕底部且水平居中的位置
            popupWindow.showAtLocation(MainActivity.this.getWindow().getDecorView(), Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL, 0, 0);
            popupView.startAnimation(animation);
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        }

        private void dismiss() {
            popupWindow.dismiss();
            popupView = null;
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        }
    }


    private void resetGame(){
        if(!initDone){
            imageView_1_1=findViewById(R.id.imageView_1_1);
            imageViews[0]=imageView_1_1;
            imageView_1_2=findViewById(R.id.imageView_1_2);
            imageViews[1]=imageView_1_2;
            imageView_1_3=findViewById(R.id.imageView_1_3);
            imageViews[2]=imageView_1_3;
            imageView_1_4=findViewById(R.id.imageView_1_4);
            imageViews[3]=imageView_1_4;
            imageView_2_1=findViewById(R.id.imageView_2_1);
            imageViews[4]=imageView_2_1;
            imageView_2_2=findViewById(R.id.imageView_2_2);
            imageViews[5]=imageView_2_2;
            imageView_2_3=findViewById(R.id.imageView_2_3);
            imageViews[6]=imageView_2_3;
            imageView_2_4=findViewById(R.id.imageView_2_4);
            imageViews[7]=imageView_2_4;
            imageView_3_1=findViewById(R.id.imageView_3_1);
            imageViews[8]=imageView_3_1;
            imageView_3_2=findViewById(R.id.imageView_3_2);
            imageViews[9]=imageView_3_2;
            imageView_3_3=findViewById(R.id.imageView_3_3);
            imageViews[10]=imageView_3_3;
            imageView_3_4=findViewById(R.id.imageView_3_4);
            imageViews[11]=imageView_3_4;
            imageView_4_1=findViewById(R.id.imageView_4_1);
            imageViews[12]=imageView_4_1;
            imageView_4_2=findViewById(R.id.imageView_4_2);
            imageViews[13]=imageView_4_2;
            imageView_4_3=findViewById(R.id.imageView_4_3);
            imageViews[14]=imageView_4_3;
            imageView_4_4=findViewById(R.id.imageView_4_4);
            imageViews[15]=imageView_4_4;

            textView_socre=findViewById(R.id.textView_score);
            textView_socre.setText(String.valueOf(score));
            textView_time=findViewById(R.id.textView_time);

            for(int i=0;i<16;i++){
                imageViews[i].setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        check(v);
                    }
                });
            }
            //一共16个位置，打乱这16个位置，然后两两一组设置成相同的图片
            Random rand = new Random();
            for(int i=0;i<16;i++){
                int tempPosition=rand.nextInt(100)%15;
                ImageView temp=imageViews[tempPosition];
                imageViews[tempPosition]=imageViews[i];
                imageViews[i]=temp;
            }

            //Log.d("MainActivity", "测试: "+ Arrays.toString(position));
            //设置图片
            for(int i=0,j=0;i<16 && j<8;i++,j++){
                imageViews[i].setImageResource(imageArtId[j]);
                imageViews[i].setTag(imageArtId[j]);
                hashMap.put(imageViews[i].getId(),imageArtId[j]);
                imageViews[i+1].setImageResource(imageArtId[j]);
                imageViews[i+1].setTag(imageArtId[j]);
                hashMap.put(imageViews[i+1].getId(),imageArtId[j]);
                i+=1;
            }

            tempHashMap=hashMap;
            countDownTimer.start();
            initDone=true;
        }
    }


}

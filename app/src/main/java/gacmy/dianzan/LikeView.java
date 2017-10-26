package gacmy.dianzan;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.AttributeSet;

import android.view.View;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by gacmy on 17-10-26.
 */

public class LikeView extends View {
    Bitmap likeBitmap = BitmapFactory.decodeResource(getResources(),R.mipmap.ic_messages_like_selected);
    Bitmap unLikeBitmap = BitmapFactory.decodeResource(getResources(),R.mipmap.ic_messages_like_unselected);
    Bitmap shinningBitmap = BitmapFactory.decodeResource(getResources(),R.mipmap.ic_messages_like_selected_shining);
    private boolean isLike = false;
    private long number = 301;
    private Paint paint;
    private int maxWidth;
    private int maxHeight;
    ValueAnimator likeAnimator;
    ValueAnimator unLkieAnimator;
    private int textHeight;
    private int currentTextY = 100;
    private List<String> listBeforeText = new ArrayList();
    private List<String> listAfterText = new ArrayList();
    private long beforeNumber;//变化前的数字

    public LikeView(Context context) {
        super(context);
        init();
    }

    public LikeView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public LikeView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }
    private void init(){
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setTextSize(60);
        Paint.FontMetrics metrics = paint.getFontMetrics();
        textHeight =(int) (metrics.bottom-metrics.top);
        beforeNumber = number = 1001;
        paint.setColor(getResources().getColor(R.color.colorAccent));
        setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!isLike){
                    beforeNumber = number;
                    number++;
                    isLike = true;
                    likeAnimator.start();
                }else{
                    beforeNumber = number;
                    number--;
                    isLike= false;
                    unLkieAnimator.start();
                }

                //invalidate();
            }
        });
        setLikeAnimation();
        setUnKieAnimation();

    }
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        maxWidth = measureWidth(widthMeasureSpec);
        maxHeight = measureHeight(heightMeasureSpec);
        setMeasuredDimension(400, 120);

    }

    private int measureWidth(int measureSpec) {
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);
        //设置一个默认值，就是这个View的默认宽度为500，这个看我们自定义View的要求
        int result = 500;
        if (specMode == MeasureSpec.AT_MOST) {//相当于我们设置为wrap_content
            result = specSize;
        } else if (specMode == MeasureSpec.EXACTLY) {//相当于我们设置为match_parent或者为一个具体的值
            result = specSize;
        }
        return result;
    }

    private int measureHeight(int measureSpec) {
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);
        int result = 500;
        if (specMode == MeasureSpec.AT_MOST) {
            result = specSize;
        } else if (specMode == MeasureSpec.EXACTLY) {
            result = specSize;
        }
        return result;
    }

   public void setNumber(long number,boolean isLike){
       this.number = number;
       beforeNumber = number;
       this.isLike = isLike;
       invalidate();
   }



    //变化前的文本集合
   private void setListBeforeText(long number){
       listBeforeText.clear();
       long textNumber = number;
       while (textNumber/10 != 0){
           listBeforeText.add((textNumber % 10)+"");
           textNumber = textNumber/10;
       }
       listBeforeText.add((textNumber % 10)+"");
   }


   //变化后的文本集合
   private void setListAfterText(long number){
       listAfterText.clear();
       long textNumber = number;
       while (textNumber/10 != 0){
           listAfterText.add((textNumber % 10)+"");
           textNumber = textNumber/10;
       }
       listAfterText.add((textNumber % 10)+"");
   }


    /*
      设置点赞动画效果
    */
    private void setLikeAnimation(){
        likeAnimator = ValueAnimator.ofFloat(0,1);
        //animator.ofFloat(0,1);
        likeAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float value = (float)animation.getAnimatedValue();
                // float width = (int)maxWidth*mPercent*value;

                currentTextY =(int)(100* value);
                // Log.e("gac","value"+mPercent);
                invalidate();
            }
        });

        likeAnimator.setDuration(500);
        //animator.start();
    }


    /*
     设置取消点赞动画效果
    */
    private void setUnKieAnimation(){
        unLkieAnimator = ValueAnimator.ofFloat(2,1);
        //animator.ofFloat(0,1);
        unLkieAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float value = (float)animation.getAnimatedValue();
                currentTextY =(int)(100* value);
                invalidate();
            }
        });

        unLkieAnimator.setDuration(500);
        //animator.start();
    }


    @Override
    protected void onDraw(Canvas canvas) {
        if(isLike){
            drawLike(canvas);
        }else{
            //number--;
            drawUnLike(canvas);
        }
        drawNumber(canvas);
    }

    private void drawLike(Canvas canvas){
        canvas.drawBitmap(likeBitmap,0,40,paint);
        canvas.drawBitmap(shinningBitmap,0,0,paint);
    }

    private void drawUnLike(Canvas canvas){
        canvas.drawBitmap(unLikeBitmap,0,40,paint);
    }

    private void drawNumber(Canvas canvas){
        String[] strs = compareText();
        float width = 0;
        if(!TextUtils.isEmpty(strs[0])){
            width = paint.measureText(strs[0],0,strs[0].length());
            canvas.drawText(strs[0],95,100,paint);
        }

        if(!TextUtils.isEmpty(strs[1])){
            canvas.drawText(strs[1]+"",95+width,currentTextY,paint);
        }
        if(!TextUtils.isEmpty(strs[2])){
            if(isLike){
                canvas.drawText(strs[2]+"",95+width,currentTextY+120,paint);
            }else{
                canvas.drawText(strs[2]+"",95+width,currentTextY-120,paint);
            }

        }
    }

    //找出变化的文本信息
    private String[] compareText(){
        setListBeforeText(beforeNumber);
        setListAfterText(number);
        StringBuilder sb = new StringBuilder();//找出不一样的文本
        StringBuilder sbEquals = new StringBuilder();//找出一样的文本
        StringBuilder sb1 = new StringBuilder();//找变化文本初始数字的文本
        int count = listBeforeText.size() <= listAfterText.size() ? listBeforeText.size():listAfterText.size();
        for(int i = 0; i < count; i++){
            if(!listAfterText.get(i).equals(listBeforeText.get(i))){
                sb.append(listAfterText.get(i));
                sb1.append(listBeforeText.get(i));
            }else{
                sbEquals.append(listAfterText.get(i));
            }
        }
        for(int i = count; i < listAfterText.size(); i++){
            sb.append(listAfterText.get(i));
        }

       // Log.d("LikeView",sbEquals.reverse().toString()+"  not equal: "+sb.reverse().toString());
        return new String[]{sbEquals.reverse().toString(),sb.reverse().toString(),sb1.reverse().toString()};
    }


    @Override
    protected void onDetachedFromWindow() {
        if(likeBitmap != null){
            likeBitmap.recycle();
        }
        if(unLikeBitmap != null){
            unLikeBitmap.recycle();
        }
        if(shinningBitmap != null){
            shinningBitmap.recycle();
        }
        super.onDetachedFromWindow();
    }
}

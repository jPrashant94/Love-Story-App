package com.unbelievable.justfacts;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Build;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.RequiresApi;

public final class AutoScrollSeekbar extends View {
    private AutoScrollSeekbarOnProgressChange autoScrollSeekbarOnProgressChange;
    private Paint circlePaint;
    private Paint circlePaintActive;
    private boolean isFromUser = true;
    private boolean isTouch;
    private int progress = 2;
    private float touchX;

    public interface AutoScrollSeekbarOnProgressChange {
        void onProgressChange(int i, boolean z);
    }

    public AutoScrollSeekbar(Context context) {
        super(context);
        init();
    }

    public AutoScrollSeekbar(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        init();
    }

    public AutoScrollSeekbar(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        init();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public AutoScrollSeekbar(Context context, AttributeSet attributeSet, int i, int i2) {
        super(context, attributeSet, i, i2);
        init();
    }

    private final void init() {
        Paint paint = new Paint(1);
        this.circlePaint = paint;
        paint.setColor(Color.parseColor("#828282"));
        Paint paint2 = this.circlePaint;
        paint2.setStyle(Paint.Style.FILL);
        Paint paint3 = new Paint(1);
        this.circlePaintActive = paint3;
        paint3.setColor(Color.parseColor("#991f1f"));
        Paint paint4 = this.circlePaintActive;
        paint4.setStyle(Paint.Style.FILL);
    }

    /* access modifiers changed from: protected */
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        float height = ((float) getHeight()) / 10.0f;
        float f = (height / 4.0f) + height;
        float height2 = ((float) getHeight()) / 45.0f;
        float width = ((float) getWidth()) / 10.0f;
        float width2 = ((float) getWidth()) / 10.0f;
        float height3 = ((float) getHeight()) / 2.0f;
        float f2 = height3 - height2;
        float width3 = ((float) getWidth()) - width;
        float f3 = height2 + height3;
        Paint paint = this.circlePaint;
        float f4 = width;
        canvas.drawRect(f4, f2, width3, f3, paint);
        int i = 0;
        while (true) {
            int i2 = i + 1;
            if (i % 2 == 0) {
                float height4 = ((float) getHeight()) / 2.0f;
                int i3 = this.progress;
                float f5 = i > i3 ? height : f;
                Paint paint2 = i > i3 ? this.circlePaint : this.circlePaintActive;
                canvas.drawCircle(f4, height4, f5, paint2);
            }
            f4 += width2;
            if (i2 > 10) {
                break;
            }
            i = i2;
        }
        float f6 = f4 - width2;
        if (!this.isTouch) {
            Paint paint3 = this.circlePaintActive;
            canvas.drawRect(width, f2, f6 - (width2 * ((float) (10 - this.progress))), f3, paint3);
            return;
        }
        if (this.touchX >= ((float) getWidth()) - width) {
            this.touchX = width;
        }
        if (this.touchX <= width) {
            this.touchX = width;
        }
        float f7 = this.touchX;
        Paint paint4 = this.circlePaintActive;
        canvas.drawRect(width, f2, f7, f3, paint4);
    }

    public boolean onTouchEvent(MotionEvent motionEvent) {
        this.isFromUser = true;
        int i = 0;
        if (motionEvent.getAction() == 1) {
            this.isTouch = false;
        } else {
            this.touchX = motionEvent.getX();
            this.isTouch = true;
        }
        getHeight();
        getHeight();
        float width = ((float) getWidth()) / 10.0f;
        float width2 = ((float) getWidth()) / 10.0f;
        getHeight();
        while (true) {
            int i2 = i + 1;
            if (i % 2 == 0 && width <= motionEvent.getX()) {
                this.progress = i;
            }
            width += width2;
            if (i2 > 10) {
                break;
            }
            i = i2;
        }
        invalidate();
        AutoScrollSeekbarOnProgressChange autoScrollSeekbarOnProgressChange2 = this.autoScrollSeekbarOnProgressChange;
        if (autoScrollSeekbarOnProgressChange2 != null) {
            autoScrollSeekbarOnProgressChange2.onProgressChange(this.progress, this.isFromUser);
        }
        return true;
    }

    public final int getProgressValue() {
        return (int) ((((float) (this.progress + 1)) / 10.0f) * 100.0f);
    }

    public final void setProgressValue(int i) {
        this.isFromUser = false;
        this.progress = i;
        invalidate();
        AutoScrollSeekbarOnProgressChange autoScrollSeekbarOnProgressChange2 = this.autoScrollSeekbarOnProgressChange;
        if (autoScrollSeekbarOnProgressChange2 != null) {
            autoScrollSeekbarOnProgressChange2.onProgressChange(i, this.isFromUser);
        }
    }

    public final void setAutoScrollSeekbarOnProgressChange(AutoScrollSeekbarOnProgressChange autoScrollSeekbarOnProgressChange2) {
        this.autoScrollSeekbarOnProgressChange = autoScrollSeekbarOnProgressChange2;
    }
}

package example.com.kist.Constant;

/**
 * Created by pR0 on 18-10-2016.
 */
import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;

public class NonSwipingViewPager extends ViewPager {

    private boolean isPagingEnabled = false;

    public NonSwipingViewPager(Context context) {
        super(context);
    }

    public NonSwipingViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return this.isPagingEnabled && super.onTouchEvent(event);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        return this.isPagingEnabled && super.onInterceptTouchEvent(event);
    }

    public void setPagingEnabled(boolean b) {
        this.isPagingEnabled = b;
    }
}
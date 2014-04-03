package ca.utoronto.ece1778.baton.screens;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PaintFlagsDrawFilter;
import android.graphics.Path;
import android.graphics.Region;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.WindowManager;
import android.widget.ImageView;


public class RoundImageView extends ImageView {

	Path path;
	public PaintFlagsDrawFilter mPaintFlagsDrawFilter;
	Paint paint;
	Context context;
	float width;
	float height;
	
	public RoundImageView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		this.context = context;
		init();
	}

	public RoundImageView(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;
		init();
	}

	public RoundImageView(Context context) {
		super(context);
		this.context = context;
		init();
	}
	public void init(){
		mPaintFlagsDrawFilter = new PaintFlagsDrawFilter(0,
				Paint.ANTI_ALIAS_FLAG|Paint.FILTER_BITMAP_FLAG);
		paint = new Paint();
		paint.setAntiAlias(true);
		paint.setFilterBitmap(true);
		paint.setColor(Color.WHITE);
		
		WindowManager wm = (WindowManager)context.getSystemService(Context.WINDOW_SERVICE);
		DisplayMetrics displaymetrics = new DisplayMetrics();
		wm.getDefaultDisplay().getMetrics(displaymetrics);
		width = displaymetrics.widthPixels/10;
		height = width;
		
		
	}
	
	@Override
	protected void onDraw(Canvas cns) {
		/*float h = getMeasuredHeight()- 3.0f;
		float w = getMeasuredWidth()- 3.0f;*/
		float h = height;
		float w = width;
		if (path == null) {
			path = new Path();
	        path.addCircle(
	        		w/2.0f
	                , h/2.0f
	                , (float) Math.min(w/2.0f, (h / 2.0))
	                , Path.Direction.CCW);
	        path.close();
		}
		cns.drawCircle(w/2.0f, h/2.0f,  Math.min(w/2.0f, h / 2.0f) + 1.5f, paint);
		 int saveCount = cns.getSaveCount();
        cns.save();
        cns.setDrawFilter(mPaintFlagsDrawFilter);
        cns.clipPath(path,Region.Op.REPLACE);
        cns.setDrawFilter(mPaintFlagsDrawFilter);
        //cns.drawColor(Color.WHITE);
		super.onDraw(cns);
		cns.restoreToCount(saveCount);
	}
	
}
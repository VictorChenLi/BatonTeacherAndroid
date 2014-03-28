package ca.utoronto.ece1778.baton.screens;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.ClipData.Item;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import ca.utoronto.ece1778.baton.TEACHER.R;
import ca.utoronto.ece1778.baton.util.CommonUtilities;
import ca.utoronto.ece1778.baton.util.Constants;

import com.baton.publiclib.model.ticketmanage.TalkTicketForDisplay;
import com.baton.publiclib.model.ticketmanage.Ticket;
import com.baton.publiclib.utility.TimeHelper;

/***
 * 
 * @author Yi Zhao
 * 
 */
public class GridViewAdapter extends ArrayAdapter<TalkTicketForDisplay> {
	Context context;
	int layoutResourceId;
	float changeRate = 255/180;
	int colorcode=0xff00ff00;//red£º0xffff0000
	static final int RED_PIECE = 0x00010000;
	static final int GREEN_PIECE=0x00000100;
	
	List<TalkTicketForDisplay> data = new ArrayList<TalkTicketForDisplay>();

	public GridViewAdapter(Context context, int layoutResourceId,
			List<TalkTicketForDisplay> data) {
		super(context, layoutResourceId, data);
		this.layoutResourceId = layoutResourceId;
		this.context = context;
		this.data = data;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View row = convertView;
		StudentItemHolder holder = null;
		if (row == null) {
			LayoutInflater inflater = ((Activity) context).getLayoutInflater();
			row = inflater.inflate(layoutResourceId, parent, false);
			holder = new StudentItemHolder();
			holder.txtName = (TextView) row
					.findViewById(R.id.talk_student_name);
			holder.imgFaceOrWaitTime = (ImageView) row
					.findViewById(R.id.talk_face_or_wait_time);
			holder.imgParIntent = (ImageView) row
					.findViewById(R.id.talk_par_intent);
			holder.txtParTime = (TextView) row
					.findViewById(R.id.talk_par_times);
			row.setTag(holder);
		} else {
			holder = (StudentItemHolder) row.getTag();
		}
		TalkTicketForDisplay item = data.get(position);
				
		Typeface tf = Typeface.createFromAsset(context.getAssets(), Constants.TYPEFACE_ACTION_MAN_BOLD);
		holder.txtName.setText(item.getStudent_name());
		holder.txtName.setTypeface(tf);
//		holder.imgFaceOrWaitTime.setBackgroundResource(R.drawable.button_face2);
		holder.imgFaceOrWaitTime.setImageBitmap(CommonUtilities.getRoundedCornerBitmap(getContext(), R.drawable.button_face, 30));
		long timeElapse = System.currentTimeMillis()-Long.valueOf(TimeHelper.getDataTime(item.getStartTimeStamp()));
		colorcode = (int) (colorcode+(RED_PIECE-GREEN_PIECE)*changeRate*(timeElapse/1000));
		if(colorcode>0xffff0000)
			colorcode=0xffff0000;
		holder.imgFaceOrWaitTime.setBackgroundColor(colorcode);
		
		if (item.getParticipate_intent().equals(Ticket.TALK_INTENT_NEWIDEA_WEB_STR)) {
			holder.imgParIntent.setImageResource(R.drawable.talk_new_idea_s);
		} else if (item.getParticipate_intent().equals(Ticket.TALK_INTENT_BUILD_WEB_STR)) {
			holder.imgParIntent.setImageResource(R.drawable.talk_build_xs);
		} else if (item.getParticipate_intent().equals(Ticket.TALK_INTENT_CHALLENGE_WEB_STR)) {
			holder.imgParIntent.setImageResource(R.drawable.talk_challenge_s);
		} else if (item.getParticipate_intent().equals(Ticket.TALK_INTENT_QUESTION_WEB_STR)) {
			holder.imgParIntent.setImageResource(R.drawable.talk_question_s);
		}

		holder.txtParTime.setText(String.valueOf(item.getParticipate_times()));
		holder.txtParTime.setTypeface(tf);
		holder.uid = item.getUid();

		return row;
	}

	/* Item in the talk tab grid */
	public static class StudentItemHolder {
		ImageView imgFaceOrWaitTime;// wait time
		TextView txtName; // student name
		ImageView imgParIntent; // participate intent
		TextView txtParTime; // participate time
		int uid;
	}
}

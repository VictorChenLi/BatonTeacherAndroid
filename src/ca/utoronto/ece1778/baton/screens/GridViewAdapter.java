package ca.utoronto.ece1778.baton.screens;

import java.util.ArrayList;

import android.app.Activity;
import android.content.ClipData.Item;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import ca.utoronto.ece1778.baton.TEACHER.R;
import ca.utoronto.ece1778.baton.util.Constants;

import com.baton.publiclib.model.ticketmanage.Ticket;

/***
 * 
 * @author Yi Zhao
 * 
 */
public class GridViewAdapter extends ArrayAdapter<Item> {
	Context context;
	int layoutResourceId;
	ArrayList<Item> data = new ArrayList<Item>();

	public GridViewAdapter(Context context, int layoutResourceId,
			ArrayList<Item> data) {
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
		Item item = data.get(position);
		Intent intent = item.getIntent();
		Typeface tf = Typeface.createFromAsset(context.getAssets(), Constants.TYPEFACE_ACTION_MAN_BOLD);
		holder.txtName.setText(intent
				.getStringExtra(TalkTagFragment.INTENT_EXTRA_ITEM_STUDENT_NAME));
		holder.txtName.setTypeface(tf);
		holder.imgFaceOrWaitTime.setBackgroundResource(R.drawable.button_face2);
		if (intent.getStringExtra(TalkTagFragment.INTENT_EXTRA_ITEM_PAR_INTENT)
				.equals(Ticket.TALK_INTENT_NEWIDEA_WEB_STR)) {
			holder.imgParIntent.setImageResource(R.drawable.talk_new_idea_s);
		} else if (intent.getStringExtra(TalkTagFragment.INTENT_EXTRA_ITEM_PAR_INTENT)
				.equals(Ticket.TALK_INTENT_BUILD_WEB_STR)) {
			// TODO:根据intent设置不同的icon
		} else if (intent.getStringExtra(TalkTagFragment.INTENT_EXTRA_ITEM_PAR_INTENT)
				.equals(Ticket.TALK_INTENT_CHALLENGE_WEB_STR)) {
			// TODO 根据intent设置不同的icon
		} else if (intent.getStringExtra(TalkTagFragment.INTENT_EXTRA_ITEM_PAR_INTENT)
				.equals(Ticket.TALK_INTENT_CHALLENGE_WEB_STR)) {
			// TODO 根据intent设置不同的icon
		}
		//TODO:wait_type应该显示在中间的face的部位，目前为了查看方便，借用了participate_time的地方，之后需要修改过来
		//holder.txtParTime.setText(intent.getStringExtra(TalkTagFragment.INTENT_EXTRA_ITEM_PAR_TIMES));
		holder.txtParTime.setText(intent.getStringExtra(TalkTagFragment.INTENT_EXTRA_ITEM_WAIT_TIME));
		holder.txtParTime.setTypeface(tf);

		return row;
	}

	/* Item in the talk tab grid */
	static class StudentItemHolder {
		ImageView imgFaceOrWaitTime;// wait time
		TextView txtName; // student name
		ImageView imgParIntent; // participate intent
		TextView txtParTime; // participate time
	}
}

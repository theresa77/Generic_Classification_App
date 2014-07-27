package dialog;

import java.util.ArrayList;

import activity.UserScribbleMainActivity;
import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

import com.genericclassificationapp.R;

public class EditTextAdapter extends BaseAdapter {

	private static final String TAG = EditTextAdapter.class.getSimpleName();
	private ArrayList<EditText> itemList;
	private LayoutInflater mInflater;
	private UserScribbleMainActivity activity;
	private TextAnnotationDialog dialog;
	
	public EditTextAdapter(TextAnnotationDialog dialog) {
		this.dialog = dialog;
        mInflater = (LayoutInflater) dialog.getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        activity = (UserScribbleMainActivity)dialog.getActivity();
        itemList = new ArrayList<EditText>();
        
        for (int i = 0; i <= activity.getTextAnnotations().size(); i++) {
        	EditText listItem = new EditText(activity);
        	if(i < activity.getTextAnnotations().size()){
        		listItem.setText(activity.getTextAnnotations().get(i));
        		Log.d(TAG, "set Text for ListItem on position "+i+" and with text: "+activity.getTextAnnotations().get(i));
        	}
        	itemList.add(listItem);
        }
       
    }
	
	@Override
    public View getView(int position, View convertView, ViewGroup parent){
		String msg = "getView( position: "+position+", convertView-Text: ";
		if(convertView != null)
			msg += itemList.get(position).getText().toString();
		else
			msg += "NULL";
		msg += ", parent: ";
		if (parent != null)
			msg += parent.toString();
		msg += " ) called";
		Log.d(TAG, msg);

		final EditText editText;
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.edit_text_item_annotation_dialog, null);
			editText = (EditText) convertView .findViewById(R.id.edit_text_item_annotation);
			convertView.setTag(editText);
		} else {
			editText = (EditText) convertView.getTag();
		}
		
		editText.setText(itemList.get(position).getText().toString());
		editText.setId(position);
//		if(editText.getId() < activity.getTextAnnotations().size()){
//			editText.setText(activity.getTextAnnotations().get(editText.getId()));
//    		Log.d(TAG, "set Text for ListItem on position "+editText.getId()+" and with text: "+activity.getTextAnnotations().get(editText.getId()));
//    	}
		
//		editText.addTextChangedListener(new EditTextWatcher(position,editText));
		
		editText.addTextChangedListener(new TextWatcher(){

			@Override
			public void afterTextChanged(Editable s) {}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

			@Override
			public void onTextChanged(CharSequence text, int start, int before, int count) {
				if (text != null && text.length() > 0 && editText.isFocused()) {
					dialog.addTextAnnotation(editText.getId(), text.toString());	
				}
			}
		});
		
//		editText.setOnEditorActionListener(new OnEditorActionListener() {
//
//			@Override
//			public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
//				dialog.addTextAnnotation(editText.getId(), v.getText().toString());
//				return false;
//			}
//			
//		});
		
//		editText.setOnTouchListener(new OnTouchListener(){
//
//			@Override
//			public boolean onTouch(View v, MotionEvent event) {
//				// TODO Auto-generated method stub
//				return false;
//			}
//			
//		});
		
		return editText;
	}

	public ArrayList<EditText> getList() {
		return itemList;
	}

	@Override
	public int getCount() {
		return itemList.size();
	}

	@Override
	public EditText getItem(int position) {
		return itemList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	public String getItemText(int position) {
		return getItem(position).getText().toString();
	}
	
	private class EditTextWatcher implements TextWatcher {

		private final int id;
		private String currText;
		private EditText editText;

		public EditTextWatcher(int id, EditText editText) {
			Log.d(TAG, "new EditTextWatcher( "+id+" )");
			this.id = id;
			this.editText = editText;
		}

		@Override
		public void afterTextChanged(Editable text) {
//			if (text != null && text.length() > 0) {
//				if(currText == null)
//					currText = text.toString();
//				if(!text.toString().equals(currText)){
//					currText = text.toString();
//					dialog.addTextAnnotation(id, text.toString());
//				}
//			}
		}

		public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,int arg3) {}

		public void onTextChanged(CharSequence text, int arg1, int arg2,int arg3) {
			if (text != null && text.length() > 0 && editText.getId()==id) {
//				if(currText == null)
//					currText = text.toString();
//				if(!text.toString().equals(currText)){
//					currText = text.toString();
					dialog.addTextAnnotation(id, text.toString());
//				}
			}
		}

	}

	
}

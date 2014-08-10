package dialog;

import java.util.ArrayList;

import activity.UserScribbleMainActivity;
import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;

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

		editText.addTextChangedListener(new TextWatcher(){

			@Override
			public void afterTextChanged(Editable s) {}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

			@Override
			public void onTextChanged(CharSequence text, int start, int before, int count) {
				Log.d(TAG, "onTextChanged() called in EditTextAdapter: TEXT: "+text+", LENGTH: "+ text.length()+ ", FOCUS: "+editText.isFocused());
				if (text != null && text.length() > 0 && editText.isFocused()) {
					dialog.addTextAnnotation(editText.getId(), text.toString());	
				} 
				else if(text.length()== 0 && editText.isFocused()){
					dialog.addTextAnnotation(editText.getId(), "");
				} 
			}
		});
		
		return editText;
	}
	
	public void addNewListItem(){
		EditText listItem = new EditText(activity);
		itemList.add(listItem);
		notifyDataSetChanged();
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
	
}

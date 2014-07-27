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
//	private ArrayList<ListItem> itemList;
	private ArrayList<EditText> itemList;
	private LayoutInflater mInflater;
//	private ViewHolder holder;
	private UserScribbleMainActivity activity;
	private TextAnnotationDialog dialog;
	
	public EditTextAdapter(TextAnnotationDialog dialog) {
		this.dialog = dialog;
        mInflater = (LayoutInflater) dialog.getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        activity = (UserScribbleMainActivity)dialog.getActivity();
//      itemList = new ArrayList<ListItem>();
        itemList = new ArrayList<EditText>();
        
        for (int i = 0; i <= activity.getTextAnnotations().size(); i++) {
//        	ListItem listItem = new ListItem();
        	EditText listItem = new EditText(activity);
        	if(i < activity.getTextAnnotations().size()){
//        		listItem.itemText = activity.getTextAnnotations().get(i);
        		listItem.setText(activity.getTextAnnotations().get(i));
        		
        	}
//        	listItem.itemText = "TEST";
        	itemList.add(listItem);
        }
        notifyDataSetChanged();
    }
	
	@Override
    public View getView(int position, View convertView, ViewGroup parent){
		String msg = "getView( position: "+position+", convertView-Text: ";
		if(convertView != null)
			msg += ((EditText)convertView).getText().toString();
		else
			msg += "NULL";
		msg += ", parent: ";
		if(parent != null)
			msg += parent.toString();
		msg += " ) called";
			
		Log.d(TAG, msg);
		
//		ViewHolder holder;
		EditText editText = null;
        if (convertView == null) {
//            holder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.edit_text_item_annotation_dialog, null);
             editText = (EditText) convertView.findViewById(R.id.edit_text_item_annotation);
//           holder.editText = (EditText) convertView.findViewById(R.id.edit_text_item_annotation);
//            convertView.setTag(holder);
             convertView.setTag(editText);
             
        } else {
//            holder = (ViewHolder) convertView.getTag();
        	 editText = (EditText) convertView.getTag();
        	
        }
//        //Fill EditText with the value you have in data source
//        holder.editText.setText(itemList.get(position).itemText);
//        holder.editText.setId(position);
        editText.setText(itemList.get(position).getText().toString());
//        
//        //update adapter once user is finished with editing
//        holder.editText.addTextChangedListener(new EditTextWatcher(position));
        editText.addTextChangedListener(new EditTextWatcher(position));
  
//        return holder.editText;
        return editText;
	}
	
//	public ArrayList<ListItem> getList(){
//		return itemList;
//	}
	
	public ArrayList<EditText> getList(){
		return itemList;
	}
	
	@Override
	public int getCount() {
        return itemList.size();
    }

//	@Override
//	public ListItem getItem(int position) {
//        return itemList.get(position);
//    }
	
	@Override
	public EditText getItem(int position) {
        return itemList.get(position);
    }

	@Override
	public long getItemId(int position) {
		return position;
	}
	
//	public String getItemText(int position){
//		return getItem(position).itemText;
//	}
	
	public String getItemText(int position){
		return getItem(position).getText().toString();
	}
	
	private class EditTextWatcher implements TextWatcher{
		
		private int id;
		
		public EditTextWatcher(int id){
			this.id = id;
		}

		@Override
		public void afterTextChanged(Editable text) {
			if(text != null && text.length()>0){
				dialog.addTextAnnotation(id, text.toString());
			}
		}

		public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,int arg3) {}
		public void onTextChanged(CharSequence arg0, int arg1, int arg2,int arg3) {}
		
	}
	
	
	private class ViewHolder {
        EditText editText;
    }
 
    private class ListItem {
    	EditText editText;
        String itemText;
    }
}


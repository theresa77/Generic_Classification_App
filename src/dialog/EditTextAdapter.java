package dialog;

import java.util.ArrayList;

import activity.UserScribbleMainActivity;
import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;

import com.genericclassificationapp.R;

public class EditTextAdapter extends BaseAdapter {

	private ArrayList<ListItem> itemList;
	private LayoutInflater mInflater;
//	private ViewHolder holder;
	private UserScribbleMainActivity activity;
	private TextAnnotationDialog dialog;
	
	public EditTextAdapter(TextAnnotationDialog dialog) {
		this.dialog = dialog;
        mInflater = (LayoutInflater) dialog.getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        activity = (UserScribbleMainActivity)dialog.getActivity();
        itemList = new ArrayList<ListItem>();
        
        for (int i = 0; i <= activity.getTextAnnotations().size(); i++) {
        	ListItem listItem = new ListItem();
        	if(i < activity.getTextAnnotations().size()){
        		listItem.itemText = activity.getTextAnnotations().get(i);
        	}
//        	listItem.itemText = "TEST";
        	itemList.add(listItem);
        }
        notifyDataSetChanged();
    }
	
	@Override
    public View getView(int position, View convertView, ViewGroup parent){
		ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.edit_text_item_annotation_dialog, null);
            holder.editText = (EditText) convertView.findViewById(R.id.edit_text_item_annotation);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        //Fill EditText with the value you have in data source
        holder.editText.setText(itemList.get(position).itemText);
        holder.editText.setId(position);
        
        //update adapter once user is finished with editing
        holder.editText.addTextChangedListener(new EditTextWatcher(position));
  
        return convertView;
	}
	
	public ArrayList<ListItem> getList(){
		return itemList;
	}
	
	@Override
	public int getCount() {
        return itemList.size();
    }

	@Override
	public ListItem getItem(int position) {
        return itemList.get(position);
    }

	@Override
	public long getItemId(int position) {
		return position;
	}
	
	public String getItemText(int position){
		return getItem(position).itemText;
	}
	
	private class EditTextWatcher implements TextWatcher{
		
		private int id;
		
		public EditTextWatcher(int id){
			this.id = id;
		}

		@Override
		public void afterTextChanged(Editable text) {
			if(text != null && text.length()>0)
				dialog.addTextAnnotation(id, text.toString());
		}

		public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,int arg3) {}
		public void onTextChanged(CharSequence arg0, int arg1, int arg2,int arg3) {}
		
	}
	
	
	private class ViewHolder {
        EditText editText;
    }
 
    private class ListItem {
        String itemText;
    }
}


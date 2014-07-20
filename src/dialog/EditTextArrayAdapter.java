package dialog;

import java.util.ArrayList;

import com.genericclassificationapp.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;

public class EditTextArrayAdapter extends ArrayAdapter<EditText> {

	private int resourceId;
	private LinearLayout editTextItemView;
	
	public EditTextArrayAdapter(Context context, int resourceId, ArrayList<EditText> objects) {
        super(context, resourceId, objects);
        this.resourceId = resourceId;
    }
	
	@Override
    public View getView(int position, View convertView, ViewGroup parent){
		 View v = convertView;
		    
	        EditText editText = getItem(position);
	        
	        if(v == null){
	            editTextItemView = new LinearLayout(getContext());
	            LayoutInflater vi = (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	            vi.inflate(resourceId, editTextItemView, true);
	        }
	        else{
	            editTextItemView = (LinearLayout)v;
	        }
	        
	        if(editText != null){
	            editText = (EditText) v.findViewById(R.id.edit_text_item_annotation_dialog);
	        }
	        return editTextItemView;
	}

}

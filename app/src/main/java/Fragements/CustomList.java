package Fragements;

/**
 * Created by om on 4/25/2016.
 */
import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.adurcup.adurcuppublisher.R;

/**
 * Created by Belal on 9/22/2015.
 */

public class CustomList extends ArrayAdapter<String> {
    private String[] approve;
    private String[] added_credit;
    private String[] type;
    private String[] image_src;
    private String[] time_stamp;
    private Activity context;
    String cre_dit;
ImageView ivn;
    public CustomList(Activity context, String[] approve, String[] added_credit, String[] type, String[] image_src, String[] time_stamp) {
        super(context, R.layout.list_view_layout, approve);
        this.context = context;
        this.approve = approve;
        this.added_credit = added_credit;
        this.type = type;
        this.image_src = image_src;
        this.time_stamp = time_stamp;

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View listViewItem = inflater.inflate(R.layout.list_view_layout, null, true);
        ivn = (ImageView)listViewItem.findViewById(R.id.Thumbnail);
    //    TextView tvapprove = (TextView) listViewItem.findViewById(R.id.approve);
        TextView tvadded_credit = (TextView) listViewItem.findViewById(R.id.added_credit);
         TextView tvtype = (TextView) listViewItem.findViewById(R.id.textView4);
       // tvtype.setVisibility(View.GONE);
//        TextView tvimg_src = (TextView) listViewItem.findViewById(R.id.image_src);
        TextView tvtime = (TextView) listViewItem.findViewById(R.id.time_stamp);

      //  tvapprove.setText(approve[position]);
        tvadded_credit.setText(added_credit[position]);

        tvtype.setText(type[position]);
        cre_dit = tvtype.getText().toString();
      //  tvimg_src.setText(image_src[position]);
        tvtime.setText(time_stamp[position]);
if(cre_dit == "3"){
    ivn.setBackgroundColor(context.getResources().getColor(R.color.colorPrimary));
}
        return listViewItem;

    }
}
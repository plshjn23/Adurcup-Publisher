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
    private String[] location;
    private String[] longitude;
    private String[] latitude;
    private Activity context;
    String cre_dit;
ImageView ivn,ivn2,ivn3;
    public CustomList(Activity context, String[] approve, String[] added_credit, String[] type, String[] image_src, String[] time_stamp, String[] location, String[] latitude, String[] longitude) {
        super(context, R.layout.list_view_layout, approve);
        this.context = context;
        this.approve = approve;
        this.added_credit = added_credit;
        this.type = type;
        this.image_src = image_src;
        this.time_stamp = time_stamp;
        this.location = location;
        this.latitude = latitude;
        this.longitude = longitude;

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View listViewItem = inflater.inflate(R.layout.list_view_layout, null, true);
        ivn = (ImageView)listViewItem.findViewById(R.id.Thumbnail);
        ivn.setBackgroundResource(R.drawable.rounded_corners_status);
    //    TextView tvapprove = (TextView) listViewItem.findViewById(R.id.approve);
        TextView tvadded_credit = (TextView) listViewItem.findViewById(R.id.added_credit);
         TextView tvtype = (TextView) listViewItem.findViewById(R.id.textView4);
        tvtype.setVisibility(View.GONE);
         TextView tvlocation = (TextView) listViewItem.findViewById(R.id.Address);
        TextView tvtime = (TextView) listViewItem.findViewById(R.id.time_stamp);

      //  tvapprove.setText(approve[position]);
        tvadded_credit.setText(added_credit[position]);
        tvlocation.setText(location[position]);
        tvtype.setText(type[position]);
        cre_dit = tvtype.getText().toString();
      //  tvimg_src.setText(image_src[position]);
        tvtime.setText(time_stamp[position]);
if(cre_dit == "2"){
    //ivn.setBackgroundColor(context.getResources().getColor(R.color.colorPrimary));
    ivn.setBackgroundResource(R.drawable.type2);

} if (cre_dit == "3"){
            ivn.setBackgroundResource(R.drawable.type3);
        }

        return listViewItem;

    }
}
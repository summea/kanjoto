
package summea.kanjoto.adapter;

import java.util.List;

import summea.kanjoto.model.Apprentice;

import summea.kanjoto.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class ApprenticeAdapter extends BaseAdapter {

    private Context mContext;
    private List<Apprentice> apprentices;

    public ApprenticeAdapter(Context context, List<Apprentice> allApprentices) {
        mContext = context;
        apprentices = allApprentices;
    }

    @Override
    public int getCount() {
        return apprentices.size();
    }

    @Override
    public Object getItem(int position) {
        return apprentices.get(position);
    }

    @Override
    public long getItemId(int position) {
        return apprentices.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = ((LayoutInflater) mContext
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(
                    R.layout.row_apprentice,
                    null);
        }

        TextView apprentice = (TextView) convertView.findViewById(R.id.apprentice);
        apprentice.setText(apprentices.get(position).getId() + " "
                + apprentices.get(position).getName());

        return convertView;
    }

    public Object removeItem(int position) {
        return apprentices.remove(position);
    }

    public void clear() {
        apprentices.clear();
    }
}

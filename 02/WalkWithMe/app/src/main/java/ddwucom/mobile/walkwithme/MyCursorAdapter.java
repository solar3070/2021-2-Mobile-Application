package ddwucom.mobile.walkwithme;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

public class MyCursorAdapter extends CursorAdapter {

    LayoutInflater inflater;
    int layout;

    public MyCursorAdapter(Context context, int layout, Cursor c) {
        super(context, c, CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.layout = layout;
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        View view = inflater.inflate(layout, parent, false);

        ViewHolder holder = new ViewHolder();
        view.setTag(holder);

        return view;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {

        ViewHolder holder = (ViewHolder) view.getTag();

        if (holder.tvDogName == null) {
            holder.tvDogName = view.findViewById(R.id.tvDogName);
            holder.tvWalkDate = view.findViewById(R.id.tvWalkDate);
            holder.tvWalkPlace = view.findViewById(R.id.tvWalkPlace);
        }

        holder.tvDogName.setText(cursor.getString(cursor.getColumnIndex(WalklogDBHelper.COL_NAME)));
        holder.tvWalkDate.setText(cursor.getString(cursor.getColumnIndex(WalklogDBHelper.COL_DATE)));
        holder.tvWalkPlace.setText(cursor.getString(cursor.getColumnIndex(WalklogDBHelper.COL_PLACE)));
    }

    // 정적멤버변수는 한 번 값을 정해두면 그 값을 유지되어서 지속적으로 사용 가능
    static class ViewHolder {
        TextView tvDogName;
        TextView tvWalkDate;
        TextView tvWalkPlace;

        // 기본적으로 static 클래스 멤버는 null로 초기화되지만 명시적으로
        public ViewHolder() {
            tvDogName = null;
            tvWalkDate = null;
            tvWalkPlace = null;
        }
    }
}
package mobile.database.dbtest02;

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

    // newView는 화면에 보이는 view개수 만큼만 호출 (스크롤을 내리면 가려지는 뷰를 재사용)
    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        View view = inflater.inflate(layout, parent, false);

        // ViewHolder 생성 직후 findViewById()를 수행하여 view를 바로 저장하는 방법도 가능
        ViewHolder holder = new ViewHolder();
        view.setTag(holder);

        // newView에는 view정보가 없기 때문에 view안에서 findViewById같은 항목 찾기 기능을 수행할 수 없음

        // newView에서 view를 새로 만들 때 view의 항목들을 findViewById해서 찾아놓은 다음에 바로 저장해놓으면 좋겠는데
        // 항목을 찾기 위해선 view객체가 전달되어야 하는데 newView에선 전달이 안되어서 못 찾음

        // Q. 왜 view 정보가 없다고 하는 거지?

        return view;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {

        // 한 번이라도 view가 사용이 되었다면 view의 holder안에 findViewById로 찾아놓은 요소들이 있음
        ViewHolder holder = (ViewHolder) view.getTag();

        if (holder.tvContactName == null) {
            holder.tvContactName = view.findViewById(R.id.tvContactName);
            holder.tvContactPhone = view.findViewById(R.id.tvContactPhone);
        }

//        TextView tvContactName = view.findViewById(R.id.tvContactName);
//        TextView tvContactPhone = view.findViewById(R.id.tvContactPhone);

        holder.tvContactName.setText(cursor.getString(cursor.getColumnIndex(ContactDBHelper.COL_NAME)));
        holder.tvContactPhone.setText(cursor.getString(cursor.getColumnIndex(ContactDBHelper.COL_PHONE)));
    }

    // 정적멤버변수는 한 번 값을 정해두면 그 값을 유지되어서 지속적으로 사용 가능
    static class ViewHolder {
        TextView tvContactName;
        TextView tvContactPhone;

        // 기본적으로 static 클래스 멤버는 null로 초기화되지만 명시적으로
        public ViewHolder() {
            tvContactName = null;
            tvContactPhone = null;
        }
    }
}

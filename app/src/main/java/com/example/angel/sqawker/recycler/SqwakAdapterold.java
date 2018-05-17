package com.example.angel.sqawker.recycler;

import android.content.Context;
import android.database.Cursor;

public class SqwakAdapterold {

    private Context mContext;
    private Cursor mCursor;

    public SqwakAdapterold(Context context) {

        this.mContext = context;


    }
/*
    public void setCursor(Cursor cursor) {

        mCursor = cursor;
        notifyDataSetChanged();
    }

    @Override
    public SqwakViewHolder newFooterHolder(View view) {
        return null;
    }

    @Override
    public SqwakViewHolder newHeaderHolder(View view) {
        return null;
    }

    @Override
    public SqwakViewHolder onCreateViewHolder(ViewGroup parent) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recycler_sqwak_item_list, parent, false);
        return new SqwakViewHolder(v, true);
    }

    @Override
    public void onBindViewHolder(@NonNull SqwakViewHolder holder, int position) {
        Bitmap bmp = BitmapFactory.decodeResource(mContext.getResources(),
                R.drawable.cezanne);


        mCursor.moveToFirst();
        mCursor.moveToPosition(position);

        int idColAutor = mCursor.getColumnIndex(SqawkContract.COLUMN_AUTOR);
        int idColDate = mCursor.getColumnIndex(SqawkContract.COLUMN_DATE);
        int idColMessage = mCursor.getColumnIndex(SqawkContract.COLUMN_MESSAGE);

        String autorName = mCursor.getString(idColAutor);
        int dateTimeInt = mCursor.getInt(idColDate);
        String message = mCursor.getString(idColMessage);

        DateTime dateTime = new DateTime(dateTimeInt);
        holder.setData(bmp, dateTime, autorName, message);
    }


    @Override
    public int getAdapterItemCount() {
        if (mCursor == null) return 0;
        return mCursor.getCount();
    }

    @Override
    public long generateHeaderId(int position) {
        return 0;
    }

    @Override
    public RecyclerView.ViewHolder onCreateHeaderViewHolder(ViewGroup parent) {
        return null;
    }

    @Override
    public void onBindHeaderViewHolder(RecyclerView.ViewHolder holder, int position) {

    }


    public class SqwakViewHolder extends UltimateRecyclerviewViewHolder {


        @BindView(R.id.autor_imageView)
        public ImageView autorImageView;
        @BindView(R.id.autor_textView)
        public TextView autorNameTextView;
        @BindView(R.id.date_textView)
        public TextView dateTextView;
        @BindView(R.id.message_textView)
        public TextView messageTextView;

        View view;

        public SqwakViewHolder(View view, boolean isItem) {
            super(view);
            if (isItem) ButterKnife.bind(this, view);
            this.view = view;
        }

        @Override
        public void onItemSelected() {
            view.setBackgroundColor(Color.LTGRAY);
        }

        @Override
        public void onItemClear() {
            view.setBackgroundColor(0);
        }

        public void setData(Bitmap bitmap, DateTime dateTime, String autor_name, String mess) {

            autorImageView.setImageBitmap(bitmap);
            autorNameTextView.setText(autor_name);
            String date = DateUtils.getDaysAgoString(dateTime);
            dateTextView.setText(date);
            messageTextView.setText(mess);
        }

    }*/
}

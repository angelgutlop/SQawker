package com.example.angel.sqawker.recycler;

import android.graphics.Bitmap;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.angel.sqawker.R;
import com.example.angel.sqawker.utils.DateUtils;

import org.joda.time.DateTime;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem;
import eu.davidea.flexibleadapter.items.IFlexible;
import eu.davidea.viewholders.FlexibleViewHolder;

public class SqwakItem extends AbstractFlexibleItem<SqwakItem.SqwakViewHolver> {


    private static int idStatic = -1;


    private int id;
    private Bitmap bmp;
    private DateTime date;
    private String authorName;
    private String message;

    public SqwakItem(Bitmap bmp, DateTime date, String authorName, String message) {
        this.id = idStatic++;

        this.bmp = bmp;
        this.date = date;
        this.authorName = authorName;
        this.message = message;
    }

    /**
     * When an item is equals to another?
     * Write your own concept of equals, mandatory to implement or use
     * default java implementation (return this == o;) if you don't have unique IDs!
     * This will be explained in the "Item interfaces" Wiki page.
     */
    @Override
    public boolean equals(Object inObject) {
        if (inObject instanceof SqwakItem) {
            SqwakItem inItem = (SqwakItem) inObject;
            return this.id == inItem.id;
        }
        return false;
    }

    /**
     * You should implement also this method if equals() is implemented.
     * This method, if implemented, has several implications that Adapter handles better:
     * - The Hash, increases performance in big list during Update & Filter operations.
     * - You might want to activate stable ids via Constructor for RV, if your id
     * is unique (read more in the wiki page: "Setting Up Advanced") you will benefit
     * of the animations also if notifyDataSetChanged() is invoked.
     */
    @Override
    public int hashCode() {
        return String.valueOf(id).hashCode();
    }

    /**
     * For the item type we need an int value: the layoutResID is sufficient.
     */
    @Override
    public int getLayoutRes() {
        return R.layout.recycler_sqwak_item_list;
    }

    @Override
    public SqwakViewHolver createViewHolder(View view, FlexibleAdapter<IFlexible> adapter) {
        return new SqwakViewHolver(view, adapter);

    }

    @Override
    public void bindViewHolder(FlexibleAdapter<IFlexible> adapter, SqwakViewHolver sqwakViewHolver, int position, List<Object> payloads) {
        sqwakViewHolver.setFields(bmp, date, authorName, message);
    }

    class SqwakViewHolver extends FlexibleViewHolder {


        @BindView(R.id.autor_imageView)
        public ImageView autorImageView;
        @BindView(R.id.autor_textView)
        public TextView autorNameTextView;
        @BindView(R.id.date_textView)
        public TextView dateTextView;
        @BindView(R.id.message_textView)
        public TextView messageTextView;


        SqwakViewHolver(View view, FlexibleAdapter adapter) {
            super(view, adapter);
            ButterKnife.bind(this, view);
        }

        void setFields(Bitmap bmp, DateTime date, String autorName, String message) {

            autorImageView.setImageBitmap(bmp);
            autorNameTextView.setText(autorName);
            messageTextView.setText(message);
            dateTextView.setText(DateUtils.getDaysAgoString(date));

        }
    }
}

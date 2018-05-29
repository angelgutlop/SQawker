package com.example.angel.sqawker.recycler;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.support.constraint.ConstraintLayout;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.daimajia.swipe.SwipeLayout;
import com.example.angel.sqawker.R;
import com.example.angel.sqawker.utils.DataBaseControl;
import com.example.angel.sqawker.utils.DateUtils;
import com.example.angel.sqawker.utils.Instructor;
import com.example.angel.sqawker.utils.InstructorsInfo;

import org.joda.time.DateTime;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem;
import eu.davidea.flexibleadapter.items.IFlexible;
import eu.davidea.viewholders.FlexibleViewHolder;

public class SqwakItem extends AbstractFlexibleItem<SqwakItem.SqwakViewHolver> {


    private static int idStatic = -1;

    private static final int SECONDS_CLOSE = 2;
    private int id;
    public Bitmap bmp;
    public DateTime date;
    public String authorName;
    public String authorKey;
    Context context;
    public String message;

    public SqwakItem(Context context, DateTime date, String authorKey, String message) {
        this.context = context;
        this.id = idStatic++;
        this.bmp = InstructorsInfo.getInstructorImage(authorKey);
        this.date = date;
        this.authorName = InstructorsInfo.getInstructorName(authorKey);
        this.authorKey = authorKey;
        this.message = message;
    }

    public SqwakItem(Context context, DateTime date, String authorKey, String autorName, Bitmap bmp, String message) {
        this.context = context;
        this.id = idStatic++;
        this.bmp = bmp;
        this.date = date;
        this.authorName = autorName;
        this.authorKey = authorKey;
        this.message = message;
    }


    @Override
    public boolean equals(Object inObject) {
        if (inObject instanceof SqwakItem) {
            SqwakItem inItem = (SqwakItem) inObject;
            return this.id == inItem.id;
        }
        return false;
    }


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
        //viewBinderHelper.bind(sqwakViewHolver.swipeRevealLayout, this.id + "");

        sqwakViewHolver.setFields(bmp, date, authorKey, authorName, message);
    }


    public class SqwakViewHolver extends FlexibleViewHolder implements View.OnClickListener, SwipeLayout.SwipeListener {


        @BindView(R.id.autor_imageView)
        public ImageView autorImageView;
        @BindView(R.id.autor_textView)
        public TextView autorNameTextView;
        @BindView(R.id.date_textView)
        public TextView dateTextView;
        @BindView(R.id.message_textView)
        public TextView messageTextView;

        @BindView(R.id.imagen_unfollow)
        public ImageView imagenUnfollow;
        String autorkey;

        @BindView(R.id.swipeRevealLayout)
        public SwipeLayout swipeLayout;

        @BindView(R.id.vista_frontal)
        public ConstraintLayout vistaFrontalLayout;

        @BindView(R.id.layout_derecho_unfollow)
        public LinearLayout unfollowLayout;

        Timer timer = new Timer();


        SqwakViewHolver(View view, FlexibleAdapter adapter) {
            super(view, adapter);
            ButterKnife.bind(this, view);

            imagenUnfollow.setOnClickListener(this);
            swipeLayout.addSwipeListener(this);

//

        }

        @Override
        public void onClick(View view) {
            super.onClick(view);
            int id = view.getId();

            switch (id) {
                case R.id.imagen_unfollow:
                    DataBaseControl.followUnfollowInstructor(view.getContext(), this.autorkey, false);
                    Instructor.subcribe2Instructor(authorName, false);

                    break;
            }
        }


        void setFields(Bitmap bmp, DateTime date, String autorkey, String autorName, String message) {
            this.autorkey = autorkey;
            autorImageView.setImageBitmap(bmp);
            autorNameTextView.setText(autorName);
            messageTextView.setText(message + "\n");
            dateTextView.setText(DateUtils.getDaysAgoString(date));


        }


        @Override
        public void onStartOpen(final SwipeLayout layout) {
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    ((Activity) layout.getContext()).runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            layout.close(true);
                        }
                    });


                }
            }, SECONDS_CLOSE * 1000);

        }

        @Override
        public void onOpen(SwipeLayout layout) {

        }

        @Override
        public void onStartClose(SwipeLayout layout) {

        }

        @Override
        public void onClose(SwipeLayout layout) {

        }

        @Override
        public void onUpdate(SwipeLayout layout, int leftOffset, int topOffset) {

        }

        @Override
        public void onHandRelease(SwipeLayout layout, float xvel, float yvel) {

        }
    }
}

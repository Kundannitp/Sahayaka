package com.pranks.doctalk.BlogsAndHome.Adapters;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.pranks.doctalk.BlogsAndHome.UploadingClass.BlogRetriveClass;
import com.pranks.doctalk.BlogsAndHome.UploadingClass.BlogUploadClass;
import com.pranks.doctalk.BlogsAndHome.UploadingClass.CommentClass;
import com.pranks.doctalk.BlogsAndHome.UploadingClass.ProfileClass;
import com.pranks.doctalk.BlogsAndHome.UploadingClass.Replyformatt;
import com.pranks.doctalk.R;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;

import de.hdodenhof.circleimageview.CircleImageView;

public class BlogAdapter extends RecyclerView.Adapter<BlogAdapter.MyViewHolder> {
    private ArrayList<BlogRetriveClass> list1;
    Context context;

    public BlogAdapter(Context context, ArrayList<BlogRetriveClass> list1) {
        this.context = context;
        this.list1 = list1;
        Collections.sort(list1,new Sortbyroll());
    }

    class Sortbyroll implements Comparator<BlogRetriveClass>
    {
        public int compare(BlogRetriveClass a, BlogRetriveClass b)
        {
            return (int)(-a.timestamp + b.timestamp);
        }
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView personname,personemail,blogcontent,likes,daysago,noOfComments;
        Button likebutton,deletebtn,commentSend;
        EditText blog_comment;
        CircleImageView personimg;
        LinearLayout comment_layout;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            personname = itemView.findViewById(R.id.cardView_commodityName);
            personimg = itemView.findViewById(R.id.cardView_commodityImageView);
            personemail=itemView.findViewById(R.id.cardView_commodityemail);
            likebutton=itemView.findViewById(R.id.card_like_button);
            blogcontent=itemView.findViewById(R.id.blog_content);
            likes=itemView.findViewById(R.id.card_likes);
            daysago=itemView.findViewById(R.id.card_daysago);
            deletebtn=itemView.findViewById(R.id.delete_btn);
            blog_comment=itemView.findViewById(R.id.blog_comment_box);
            commentSend=itemView.findViewById(R.id.blog_comment_send_btn);
            comment_layout=itemView.findViewById(R.id.comentLayout);
            noOfComments=itemView.findViewById(R.id.no_of_comments);
        }
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.designofblog, viewGroup, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder viewHolder, int i) {
        viewHolder.itemView.setTag((list1.get(i)));
        BlogRetriveClass blg = list1.get(i);
        viewHolder.personname.setText(blg.getName());
        viewHolder.personemail.setText(blg.getMail());
        viewHolder.blogcontent.setText(blg.getBlogdata());
        long no_of_comments = blg.getNoofcomments();
        String str_no_of_comments = "0 comment";
        if (no_of_comments > 1) str_no_of_comments = no_of_comments + "comments";
        else str_no_of_comments = no_of_comments + "comment";
        viewHolder.noOfComments.setText(str_no_of_comments);
        viewHolder.noOfComments.setOnClickListener(v -> {
            loadCommentsLayout(blg);
        });
        viewHolder.comment_layout.setVisibility(View.VISIBLE);
        viewHolder.blog_comment.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(viewHolder.blog_comment.getText().toString().equals("")) {
                    viewHolder.commentSend.setText("COMMENTS");
                    viewHolder.commentSend.setOnClickListener(v -> {
//                            Toast.makeText(context, "Comments clicked", Toast.LENGTH_SHORT).show();
                        loadCommentsLayout(blg);
                    });
                }else {
                    viewHolder.commentSend.setText("POST");
                    viewHolder.commentSend.setOnClickListener(v -> {
//                            Toast.makeText(context, "Posted", Toast.LENGTH_SHORT).show();
                        sendComments(viewHolder,blg);
                    });
                }
            }
        });

        if(viewHolder.blog_comment.getText().toString().equals("")) {
            viewHolder.commentSend.setText("COMMENTS");
            viewHolder.commentSend.setOnClickListener(v -> {
//                    Toast.makeText(context, "Comments clicked", Toast.LENGTH_SHORT).show();
                loadCommentsLayout(blg);
            });
        }

//        viewHolder.commentSend.setText("POST");
//        viewHolder.commentSend.setOnClickListener(v -> {
//            sendComments(viewHolder, blg);
//        });
        if(blg.isThumbcolor()){
            viewHolder.likebutton.setBackground(ContextCompat.getDrawable(context, R.drawable.ic_thumb_blue));
        }
        long timestm=blg.getTimestamp();
        viewHolder.daysago.setText(getDate(timestm));
        if(blg.getLikes()>1)
            viewHolder.likes.setText(blg.getLikes()+" likes");
        else
            viewHolder.likes.setText(blg.getLikes()+" like");

        Glide.with(context)
                .load(blg.getProfilepic())
                .into(viewHolder.personimg);
            viewHolder.likebutton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DatabaseReference mref1 = FirebaseDatabase.getInstance().getReference();
                    mref1.child("Blogs").child(blg.getUid()).child("likedperson").addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(context);
                            String uidper = "";
                            if (acct != null) {
                                uidper = acct.getId();
                            }
                            boolean bool = true;
                            for (DataSnapshot ds : dataSnapshot.getChildren()) {
                                if (ds.getValue().toString() .equals( uidper)) {
                                    bool = false;
                                    break;
                                }
                            }
                            if (bool) {
                                viewHolder.likebutton.setBackground(ContextCompat.getDrawable(context, R.drawable.ic_thumb_blue));
                                DatabaseReference mref = FirebaseDatabase.getInstance().getReference();
                                mref.child("Blogs").child(blg.getUid()).child("likes").setValue(blg.getLikes() + 1);
                                mref.child("Blogs").child(blg.getUid()).child("likedperson").push().setValue(uidper);
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }
            });

        /*For showing which people liked which posts*/
        try {
            DatabaseReference mrefs = FirebaseDatabase.getInstance().getReference();
            viewHolder.likes.setOnClickListener(v -> {
                final Dialog customView = new Dialog(context);
                customView.setContentView(R.layout.likedpersoncustom);
                customView.setTitle("People liked");
                customView.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                customView.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation_bottom;
                RecyclerView recyclerView = customView.findViewById(R.id.liked_recycler);
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
                mrefs.child("Blogs").child(blg.getUid()).child("likedperson").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        ArrayList<ProfileClass> arr_pro = new ArrayList<>();
                        DatabaseReference perref = FirebaseDatabase.getInstance().getReference();
                        for (DataSnapshot ds : dataSnapshot.getChildren()) {
                            String uidper = ds.getValue().toString();
                            perref.child("Profiles").child(uidper).addValueEventListener(new ValueEventListener() {

                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    String name = dataSnapshot.child("name").getValue().toString();
                                    String image = dataSnapshot.child("profileimg").getValue().toString();
                                    String email = dataSnapshot.child("email").getValue().toString();
                                    ProfileClass prf = new ProfileClass(name, email, image);
                                    arr_pro.add(prf);
                                    AdapterLiked adapterLiked = new AdapterLiked(context, arr_pro);
                                    recyclerView.setAdapter(adapterLiked);
                                    ProgressBar progressBar = customView.findViewById(R.id.progress_circle1);
                                    progressBar.setVisibility(View.GONE);
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
                customView.show();
            });
        }catch (Exception e){

        }


    }

    @Override
    public int getItemCount() {
        return list1.size();
    }

    public String getDate(long timestm){
        long todaystime=Calendar.getInstance().getTimeInMillis();
        if (((todaystime-timestm)/86400000)==1){
            return ((todaystime-timestm)/86400000)+" day ago";
        }else{
            return ((todaystime-timestm)/86400000)+" days ago";
        }
    }

    public void updateList(ArrayList<BlogRetriveClass> list){
        this.list1 = list;
        Collections.sort(list1,new Sortbyroll());
        notifyDataSetChanged();
    }

    public void loadCommentsLayout(BlogRetriveClass blg){
        final Dialog customView = new Dialog(context);
        customView.setContentView(R.layout.custom_comments_show);
        customView.setTitle("Comments");
        customView.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        customView.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation_bottom;
            customView.getWindow().setGravity(Gravity.BOTTOM);

        // Get a reference for the custom view close button
        ImageButton closeButton = customView.findViewById(R.id.ib_close_comments);
        closeButton.setOnClickListener(v -> customView.dismiss());
        final ScrollView scrollview = customView.findViewById(R.id.chatScrollView);
        scrollview.post(() -> scrollview.fullScroll(ScrollView.FOCUS_DOWN));

        DatabaseReference mref=FirebaseDatabase.getInstance().getReference();
        mref.child("Blogs").child(blg.getUid()).child("Comments").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                LinearLayout chatLayout=customView.findViewById(R.id.chat_commentsLayout);
                chatLayout.removeAllViews();
                LayoutInflater inflater = LayoutInflater.from(context);
                CardView layout = (CardView) inflater.inflate(R.layout.designofblog, null);
                layout.setFocusableInTouchMode(true);
                chatLayout.addView(layout); // move focus to text view to automatically make it scroll up if softfocus
                TextView personname1,personemail1,blogcontent1,likes1,daysago1,noOfComments;
                Button likebutton1;
                ImageView personimg1;
                personname1 = layout.findViewById(R.id.cardView_commodityName);
                personimg1 = layout.findViewById(R.id.cardView_commodityImageView);
                personemail1=layout.findViewById(R.id.cardView_commodityemail);
                likebutton1=layout.findViewById(R.id.card_like_button);
                blogcontent1=layout.findViewById(R.id.blog_content);
                likes1=layout.findViewById(R.id.card_likes);
                daysago1=layout.findViewById(R.id.card_daysago);
                noOfComments=layout.findViewById(R.id.no_of_comments);
                long no_of_comments=blg.getNoofcomments();
                String str_no_of_comments = "0 comment";
                if (no_of_comments > 1) str_no_of_comments = no_of_comments + "comments";
                else str_no_of_comments = no_of_comments + "comment";
                noOfComments.setText(str_no_of_comments);
                personname1.setText(blg.getName());
                personemail1.setText(blg.getMail());
                Glide.with(context.getApplicationContext()).load(blg.getProfilepic()).into(personimg1);
                blogcontent1.setText(blg.getBlogdata());
                daysago1.setText(getDate(blg.getTimestamp()));
                if(blg.isThumbcolor())
                    likebutton1.setBackground(ContextCompat.getDrawable(context, R.drawable.ic_thumb_blue));
                if(blg.getLikes()>1)
                    likes1.setText(blg.getLikes()+" likes");
                else
                    likes1.setText(blg.getLikes()+" like");

                for(DataSnapshot ds:dataSnapshot.getChildren()){
                    try {
                        RelativeLayout layout1 = (RelativeLayout) inflater.inflate(R.layout.comment_design_layout, null);
                        layout1.setFocusableInTouchMode(true);
                        chatLayout.addView(layout1); // move focus to text view to automatically make it scroll up if softfocus
                        TextView name_of_person = layout1.findViewById(R.id.comment_person_Name);
                        ImageView profilepic = layout1.findViewById(R.id.comment_person_ImageView);
                        TextView comment_text = layout1.findViewById(R.id.comment_msg);
                        name_of_person.setText(ds.child("name_of_person").getValue().toString());
                        comment_text.setText(ds.child("comment_stm").getValue().toString());
                        Glide.with(context.getApplicationContext())
                                .load(ds.child("imgurl").getValue().toString())
                                .into(profilepic);
                        LinearLayout layout_reply=layout1.findViewById(R.id.reply_layout_linear);
                        EditText reply_text=layout1.findViewById(R.id.reply_text_to_send);
                        Button reply_btn=layout1.findViewById(R.id.reply_btn_send);
                        TextView reply_click=layout1.findViewById(R.id.reply_clicked);
                        reply_click.setOnClickListener(v->{
                            if(layout_reply.getVisibility()!=View.VISIBLE)
                                layout_reply.setVisibility(View.VISIBLE);
                            else
                                layout_reply.setVisibility(View.GONE);
                        });
                        reply_btn.setOnClickListener(v->{
                            DatabaseReference mref3=FirebaseDatabase.getInstance().getReference();
                            GoogleSignInAccount acct1 = GoogleSignIn.getLastSignedInAccount(context);
                            String name = acct1.getDisplayName();
                            String img_url=acct1.getPhotoUrl().toString();
                            mref3.child("Blogs").child(blg.getUid()).child("Comments").child(ds.getKey()).child("Replies")
                                    .push().setValue(new Replyformatt(reply_text.getText().toString(),name,img_url));
                            Toast.makeText(context, "Reply sent", Toast.LENGTH_SHORT).show();
                            reply_text.setText("");
                        });
                        for(DataSnapshot ds1:ds.child("Replies").getChildren()){
                            RelativeLayout layout2 = (RelativeLayout) inflater.inflate(R.layout.comments_reply_design, null);
                            layout2.setFocusableInTouchMode(true);
                            chatLayout.addView(layout2);
                            TextView nameofPerson=layout2.findViewById(R.id.reply_person_Name);
                            ImageView img_per=layout2.findViewById(R.id.reply_person_ImageView);
                            TextView replyShow=layout2.findViewById(R.id.reply_msg_show);
                            nameofPerson.setText(ds1.child("nameofreplier").getValue().toString());
                            Glide.with(context.getApplicationContext()).load(ds1.child("replierimg").getValue().toString()).into(img_per);
                            replyShow.setText(ds1.child("replyText").getValue().toString());
                        }

                    }catch (Exception e){}
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        customView.show();
    }

    public void sendComments(MyViewHolder viewHolder,BlogRetriveClass blg){
        String comment_to_send=viewHolder.blog_comment.getText().toString();
        DatabaseReference mref=FirebaseDatabase.getInstance().getReference();
        GoogleSignInAccount acct1 = GoogleSignIn.getLastSignedInAccount(context);
        String name = acct1.getDisplayName();
        String img_url=acct1.getPhotoUrl().toString();
        CommentClass commentClass=new CommentClass(img_url,comment_to_send,name);
        mref.child("Blogs").child(blg.getUid()).child("Comments").push().setValue(commentClass);
        Toast.makeText(context, "Comment Posted", Toast.LENGTH_SHORT).show();
        viewHolder.blog_comment.setText("");
    }

}



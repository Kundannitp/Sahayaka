package com.pranks.doctalk.BlogsAndHome.Adapters;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.pranks.doctalk.BlogsAndHome.UploadingClass.BlogUploadClass;
import com.pranks.doctalk.BlogsAndHome.UploadingClass.CommentClass;
import com.pranks.doctalk.BlogsAndHome.UploadingClass.ProfileClass;
import com.pranks.doctalk.BlogsAndHome.UploadingClass.Replyformatt;
import com.pranks.doctalk.R;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;

import de.hdodenhof.circleimageview.CircleImageView;

public class BlogAdapterMyBlog extends RecyclerView.Adapter<BlogAdapterMyBlog.MyViewHolder> {
    private ArrayList<BlogUploadClass> list1;
    private ArrayList<String> uids=new ArrayList<>();
    private ArrayList<String> key=new ArrayList<>();
    Context context;
    int g=1;

    public BlogAdapterMyBlog(Context context, ArrayList<BlogUploadClass> list1) {
        this.context = context;
        this.list1 = list1;
        Collections.sort(list1,new Sortbyroll());
        for(BlogUploadClass data:list1){
            this.key.add(data.getKeysort());
            this.uids.add(data.getUidsort());
        }
    }

    class Sortbyroll implements Comparator<BlogUploadClass>
    {
        public int compare(BlogUploadClass a, BlogUploadClass b)
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
        BlogUploadClass blg = list1.get(i);
        viewHolder.personname.setText(blg.getName());
        viewHolder.personemail.setText(blg.getMail());
        viewHolder.blogcontent.setText(blg.getBlogdata());
        DatabaseReference mref= FirebaseDatabase.getInstance().getReference();
        String keytoget="";
        keytoget=key.get(i);
        try {
            mref.child("Blogs").child(keytoget).child("Comments").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if(dataSnapshot.getChildrenCount()>1)
                        viewHolder.noOfComments.setText(dataSnapshot.getChildrenCount()+" comments");
                    if(dataSnapshot.getChildrenCount()==1)
                        viewHolder.noOfComments.setText(dataSnapshot.getChildrenCount()+" comment");
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                }
            });
        }catch (Exception e){} viewHolder.noOfComments.setOnClickListener(v->{

            loadCommentsLayout(viewHolder,i);
        });

        viewHolder.deletebtn.setVisibility(View.VISIBLE);
        viewHolder.deletebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDeleteDialog(i);
            }
        });


        long timestm=blg.getTimestamp();
        viewHolder.daysago.setText(getDate(timestm));
        viewHolder.likes.setText(blg.getLikes()+" likes");

        viewHolder.likebutton.setBackground(ContextCompat.getDrawable(context, R.drawable.ic_thumb_blue));
        DatabaseReference mref2=FirebaseDatabase.getInstance().getReference();
        mref2.child("Blogs").child(key.get(i)).child("likes").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                try {
                    viewHolder.likes.setText(dataSnapshot.getValue().toString() + " likes");
                }catch (Exception e){

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        Glide.with(context)
                .load(blg.getProfilepic())
                .into(viewHolder.personimg);

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
                GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(context);
                mrefs.child("Blogs").child(key.get(i)).child("likedperson").addValueEventListener(new ValueEventListener() {
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
        long todaystime= Calendar.getInstance().getTimeInMillis();
        if (((todaystime-timestm)/86400000)==1){
            return ((todaystime-timestm)/86400000)+" day ago";
        }else{
            return ((todaystime-timestm)/86400000)+" days ago";
        }
    }

    private void showDeleteDialog(int i)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage("Sure to Delete?");
        builder.setCancelable(false);

        builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                try {
                    DatabaseReference mref = FirebaseDatabase.getInstance().getReference();
                    GoogleSignInAccount acct1 = GoogleSignIn.getLastSignedInAccount(context);
                    String uidper1 = acct1.getId();
//                    mref.child("Blogs").child(key.get(i)).child("likedperson").removeValue();
                    mref.child("Blogs").child(key.get(i)).child("havetoshow").setValue("false");
                    mref.child("PersonBlogs").child(uidper1).child(uids.get(i)).child("havetoshow").setValue("false");
                    Toast.makeText(context, "Deleted Successfully", Toast.LENGTH_SHORT).show();
                }catch (Exception e){
                    Toast.makeText(context, "Some error", Toast.LENGTH_SHORT).show();
                }
                dialog.cancel();
            }
        });

        builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                dialog.cancel();
            }
        });

        builder.show();

    }

    public void loadCommentsLayout(MyViewHolder viewHolder,int i){
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
        mref.child("Blogs").child(key.get(i)).child("Comments").addValueEventListener(new ValueEventListener() {
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
                DatabaseReference mref1=FirebaseDatabase.getInstance().getReference();
                String keytoget="";
                keytoget=key.get(i);
                try {
                    mref1.child("Blogs").child(keytoget).child("Comments").addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if(dataSnapshot.getChildrenCount()>1)
                                noOfComments.setText(dataSnapshot.getChildrenCount()+" comments");
                            if(dataSnapshot.getChildrenCount()==1)
                                noOfComments.setText(dataSnapshot.getChildrenCount()+" comment");
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                        }
                    });
                }catch (Exception e){}
                BlogUploadClass blg = list1.get(i);
                personname1.setText(blg.getName());
                personemail1.setText(blg.getMail());
                Glide.with(context).load(blg.getProfilepic()).into(personimg1);
                blogcontent1.setText(blg.getBlogdata());
                daysago1.setText(getDate(blg.getTimestamp()));
                likebutton1.setBackground(ContextCompat.getDrawable(context, R.drawable.ic_thumb_blue));
                DatabaseReference mref2=FirebaseDatabase.getInstance().getReference();
                mref2.child("Blogs").child(key.get(i)).child("likes").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        try {
                            likes1.setText(dataSnapshot.getValue().toString() + " likes");
                        }catch (Exception e){

                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });


                for(DataSnapshot ds:dataSnapshot.getChildren()){
                    try {
                        LayoutInflater inflater1 = LayoutInflater.from(context);
                        RelativeLayout layout1 = (RelativeLayout) inflater1.inflate(R.layout.comment_design_layout, null);
                        layout1.setFocusableInTouchMode(true);
                        chatLayout.addView(layout1); // move focus to text view to automatically make it scroll up if softfocus
                        TextView name_of_person = layout1.findViewById(R.id.comment_person_Name);
                        ImageView profilepic = layout1.findViewById(R.id.comment_person_ImageView);
                        TextView comment_text = layout1.findViewById(R.id.comment_msg);
                        name_of_person.setText(ds.child("name_of_person").getValue().toString());
                        comment_text.setText(ds.child("comment_stm").getValue().toString());
                        Glide.with(context)
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
                            mref3.child("Blogs").child(key.get(i)).child("Comments").child(ds.getKey()).child("Replies")
                                    .push().setValue(new Replyformatt(reply_text.getText().toString(),name,img_url));
                            Toast.makeText(context, "Reply sent", Toast.LENGTH_SHORT).show();
                            reply_text.setText("");
                        });
                        for(DataSnapshot ds1:ds.child("Replies").getChildren()){
                            LayoutInflater inflater2 = LayoutInflater.from(context);
                            RelativeLayout layout2 = (RelativeLayout) inflater2.inflate(R.layout.comments_reply_design, null);
                            layout2.setFocusableInTouchMode(true);
                            chatLayout.addView(layout2);
                            TextView nameofPerson=layout2.findViewById(R.id.reply_person_Name);
                            ImageView img_per=layout2.findViewById(R.id.reply_person_ImageView);
                            TextView replyShow=layout2.findViewById(R.id.reply_msg_show);
                            nameofPerson.setText(ds1.child("nameofreplier").getValue().toString());
                            Glide.with(context).load(ds1.child("replierimg").getValue().toString()).into(img_per);
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

}



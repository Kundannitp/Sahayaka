package com.pranks.doctalk.Education;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.OpenableColumns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.pranks.doctalk.Education.AdaptersEducation.AdapterNotes;
import com.pranks.doctalk.Education.UploaderClass.UploadNotes;
import com.pranks.doctalk.Home;
import com.pranks.doctalk.R;

import java.io.File;
import java.util.ArrayList;

import static android.app.Activity.RESULT_OK;

public class Notes extends Fragment {
    View v;
    Context mContext;
    final static int PICK_PDF_CODE = 2342;
    Uri pdf_uri=null;
    String displayName;
    TextView selectedFile;
    RecyclerView notes_recycler;
    AdapterNotes adapterNotes;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_notes, container, false);
        ((Home) getActivity()).getSupportActionBar().setTitle("Notes");
        mContext = getContext();
        FloatingActionButton fab = v.findViewById(R.id.upload_notes);
        fab.setOnClickListener(view -> {
            openUploadContainer();
        });

        notes_recycler=v.findViewById(R.id.notes_recycler);
        notes_recycler.setLayoutManager(new LinearLayoutManager(getContext()));
        DatabaseReference notesref=FirebaseDatabase.getInstance().getReference().child("Notes");
        notesref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ArrayList<UploadNotes> notes_list=new ArrayList<>();
                for(DataSnapshot ds:dataSnapshot.getChildren()){
                    String title=ds.child("noteTitle").getValue().toString();
                    String dis=ds.child("noteDis").getValue().toString();
                    String filepath=ds.child("filepath").getValue().toString();
                    String key=ds.getKey();
                    String uploadername=ds.child("uploadername").getValue().toString();
                    long timestamp=(long)(ds.child("timestamp").getValue());
                    UploadNotes up=new UploadNotes(filepath,title,dis,key,timestamp,uploadername);
                    notes_list.add(up);
                }
                adapterNotes=new AdapterNotes(mContext,notes_list);
                notes_recycler.setAdapter(adapterNotes);
                ProgressBar progressBar=v.findViewById(R.id.progress_circle_notes);
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        return v;
    }

    void openUploadContainer(){
        final Dialog customView = new Dialog(mContext);
        customView.setContentView(R.layout.custom_upload_notes);
        customView.setTitle("Upload Notes");

        // Get a reference for the custom view close button
        ImageButton closeButton = customView.findViewById(R.id.ib_close_notices);
        closeButton.setOnClickListener(v -> customView.dismiss());

        EditText notetitle=customView.findViewById(R.id.note_title_edit);
        EditText notedis=customView.findViewById(R.id.notes_discription_edit);
        Button selectPdf=customView.findViewById(R.id.selectPdf_btn);
        Button uploadPdf=customView.findViewById(R.id.Upload_notes_btn);
        selectedFile=customView.findViewById(R.id.selected_file_name);

        selectPdf.setOnClickListener(v -> {
            getPDF();
        });

        uploadPdf.setOnClickListener(v->{
            if(pdf_uri!=null) {
                customView.dismiss();
                uploadFile(pdf_uri, notetitle.getText().toString(), notedis.getText().toString());
            }
            else
                Toast.makeText(mContext, "Please select a file", Toast.LENGTH_SHORT).show();
        });

        customView.show();
    }


    //this function will get the pdf from the storage
    private void getPDF() {

        //creating an intent for file chooser
        Intent intent = new Intent();
        intent.setType("application/pdf");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_PDF_CODE);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //when the user choses the file
        if (requestCode == PICK_PDF_CODE && resultCode == RESULT_OK && data != null && data.getData() != null) {
            //if a file is selected
            if (data.getData() != null) {
                //uploading the file
                //uploadFile(data.getData());
                pdf_uri=data.getData();

                Uri uri = data.getData();
                String uriString = uri.toString();
                File myFile = new File(uriString);
                String path = myFile.getAbsolutePath();
                displayName = null;

                if (uriString.startsWith("content://")) {
                    Cursor cursor = null;
                    try {
                        cursor = getActivity().getContentResolver().query(uri, null, null, null, null);
                        if (cursor != null && cursor.moveToFirst()) {
                            displayName = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                        }
                    } finally {
                        cursor.close();
                    }
                } else if (uriString.startsWith("file://")) {
                    displayName = myFile.getName();
                }
                selectedFile.setText(displayName);
                Toast.makeText(mContext, displayName, Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(mContext, "No file chosen", Toast.LENGTH_SHORT).show();
            }
        }
    }


    private void uploadFile(Uri filePath,String note_title,String note_dis) {
        //checking if file is available
        if (filePath != null) {
            //displaying progress dialog while image is uploading
            final ProgressDialog progressDialog = new ProgressDialog(mContext);
            progressDialog.setTitle("Uploading");
            progressDialog.show();
            StorageReference storageReference=FirebaseStorage.getInstance().getReference();
            final StorageReference sRef = storageReference.child(System.currentTimeMillis() + ".pdf");

            //adding the file to reference
            sRef.putFile(filePath)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            progressDialog.dismiss();
                            sRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    try {
                                        GoogleSignInAccount acct1 = GoogleSignIn.getLastSignedInAccount(mContext);
                                        String nameofper = acct1.getDisplayName();
                                        UploadNotes up = new UploadNotes(uri.toString(), note_title, note_dis, System.currentTimeMillis(), nameofper);
                                        DatabaseReference mref = FirebaseDatabase.getInstance().getReference();
                                        mref.child("Notes").push().setValue(up);
                                    }catch (Exception e){
                                        Toast.makeText(mContext, e.toString(), Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        }
                    })

                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            progressDialog.dismiss();
                            Toast.makeText(mContext, exception.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            //displaying the upload progress
                            double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                            progressDialog.setMessage("Uploaded " + ((int) progress) + "%...");
                        }
                    });
        } else {
            Toast.makeText(mContext, "Please select a file", Toast.LENGTH_SHORT).show();
        }
    }

}

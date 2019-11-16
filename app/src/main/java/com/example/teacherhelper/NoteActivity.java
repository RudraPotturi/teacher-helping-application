package com.example.teacherhelper;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class NoteActivity extends AppCompatActivity {

    EditText mTitleEt, mDescriptionEt;
    Button mSaveBtn;

    ProgressDialog pd;
    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note);

        mTitleEt = findViewById(R.id.titleET);
        mDescriptionEt = findViewById(R.id.descriptionET);
        mSaveBtn = findViewById(R.id.saveBtn);

        pd = new ProgressDialog(this);
        db = FirebaseFirestore.getInstance();

        mSaveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String title = mTitleEt.getText().toString().trim();
                String description = mDescriptionEt.getText().toString().trim();
                uploadData(title,description);
            }
        });
    }

    private void uploadData(String title, String description) {
        pd.setTitle("Adding data");
        pd.show();
        String id = UUID.randomUUID().toString();

        Map<String, Object> doc = new HashMap<>();
        doc.put("id",id);
        doc.put("title",title);
        doc.put("description",description);

        db.collection("Documents").document(id).set(doc)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        pd.dismiss();
                        Toast.makeText(NoteActivity.this,"Added..",Toast.LENGTH_SHORT).show();

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        pd.dismiss();
                        Toast.makeText(NoteActivity.this,e.getMessage(),Toast.LENGTH_SHORT).show();

                    }
                });

    }
}
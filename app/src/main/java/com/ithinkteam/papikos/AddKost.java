package com.ithinkteam.papikos;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.InputStream;
import java.util.ArrayList;

public class AddKost extends AppCompatActivity {
    EditText et_namaKost, et_alamatKost, et_noHP, et_kotaKost, et_linkGMap, et_deskripsi;
    TextView btn_tambah;
    ImageView coverImage;
    CheckBox fasl1, fasl2, fasl3, fasl4, fasl5, fasl6;
    ArrayList<String> mFasilitas;
    String fasilitas;

    AutoCompleteTextView et_jenisKost;
    ArrayAdapter<String> itemJenisKost;
    String[] JenisKos = {"Kost Campur", "Kost Cewek", "Kost Cowok"};

    private DatabaseReference mDatabaseReference;
    private FirebaseDatabase mFirebaseDatabase;
    private StorageReference sReference;

    private Uri resultUri;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_kost);

        init();
        initFirebase();
    }

    public void initFirebase() {
        FirebaseApp.initializeApp(AddKost.this);
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mDatabaseReference = mFirebaseDatabase.getReference("POST");
        sReference = FirebaseStorage.getInstance().getReference("POST");
    }

    public void init() {
        et_namaKost = findViewById(R.id.et_namaKos);
        et_alamatKost = findViewById(R.id.et_alamat);
        et_noHP = findViewById(R.id.et_noTelp);
        et_kotaKost = findViewById(R.id.et_kota);
        et_linkGMap = findViewById(R.id.et_linkGMap);
        et_deskripsi = findViewById(R.id.et_deskripsi);
        coverImage = findViewById(R.id.coverImage);
        coverImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CropImage.activity()
                        .setGuidelines(CropImageView.Guidelines.ON)
                        .setAspectRatio(343,220)
                        .start(AddKost.this);
            }
        });


        btn_tambah = findViewById(R.id.btn_tambah);

        et_jenisKost = findViewById(R.id.et_jenisKost);
        itemJenisKost = new ArrayAdapter<String>(this, R.layout.list_items, JenisKos);
        et_jenisKost.setAdapter(itemJenisKost);
        et_jenisKost.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String item = parent.getItemAtPosition(position).toString();
            }
        });

        fasl1 = findViewById(R.id.fslts1);
        fasl2 = findViewById(R.id.fslts2);
        fasl3 = findViewById(R.id.fslts3);
        fasl4 = findViewById(R.id.fslts4);
        fasl5 = findViewById(R.id.fslts5);
        fasl6 = findViewById(R.id.fslts6);
        mFasilitas = new ArrayList<>();

        fasl1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (fasl1.isChecked()) {
                    mFasilitas.add(fasl1.getText().toString());
                } else {
                    mFasilitas.add(fasl1.getText().toString());
                }
            }
        });

        fasl2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (fasl2.isChecked()) {
                    mFasilitas.add(fasl2.getText().toString());
                } else {
                    mFasilitas.add(fasl2.getText().toString());
                }
            }
        });

        fasl3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (fasl3.isChecked()) {
                    mFasilitas.add(fasl3.getText().toString());
                } else {
                    mFasilitas.add(fasl3.getText().toString());
                }
            }
        });

        fasl4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (fasl4.isChecked()) {
                    mFasilitas.add(fasl4.getText().toString());
                } else {
                    mFasilitas.add(fasl4.getText().toString());
                }
            }
        });

        fasl5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (fasl5.isChecked()) {
                    mFasilitas.add(fasl5.getText().toString());
                } else {
                    mFasilitas.add(fasl5.getText().toString());
                }
            }
        });

        fasl6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (fasl6.isChecked()) {
                    mFasilitas.add(fasl6.getText().toString());
                } else {
                    mFasilitas.add(fasl6.getText().toString());
                }
            }
        });


        btn_tambah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                susun fasilitas
                StringBuilder stringBuilder = new StringBuilder();
                for (String s : mFasilitas)
                    stringBuilder.append(s).append("+");

//                value
                fasilitas = stringBuilder.toString();
                String namaKost = et_namaKost.getText().toString();
                String NoHP = et_noHP.getText().toString();
                String alamat = et_alamatKost.getText().toString();
                String kota = et_kotaKost.getText().toString();
                String linkGMap = et_linkGMap.getText().toString();
                String jenisKost = et_jenisKost.getText().toString();
                String deskripsi = et_deskripsi.getText().toString();

                savePost();
            }
        });
    }


    private void savePost() {
        if (resultUri != null) {
            // Progress dialog
            final ProgressDialog progressDialog = new ProgressDialog(this);

            // Ambil ObjectModel
            com.ithinkteam.papikos.ModelDB post = new com.ithinkteam.papikos.ModelDB();
            post.setId(mDatabaseReference.push().getKey());

            // Munculkan proses upload
            UploadTask uploadTask = sReference.child(post.getId()).putFile(resultUri);
            uploadTask.addOnProgressListener(taskSnapshot -> {
                // Memunculkan loading 0% - 100%
                double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                        progressDialog.setMessage("Uploading.." + ((int) progress) + "%");
            }).addOnPausedListener(taskSnapshot -> {
                // Membuat progress jeda

                System.out.println("Upload terjeda");
            }).addOnFailureListener(taskSnapshot -> {
                // Membuat progress gagal

                Toast.makeText(getApplicationContext(), "Gagal menambah data!", Toast.LENGTH_SHORT).show();

            }).addOnSuccessListener(taskSnapshot -> {
                // Membuat progress sukses

                Task<Uri> uri = taskSnapshot.getStorage().getDownloadUrl();
                while (!uri.isComplete()) ;
                Uri url = uri.getResult();
//                Proses input database
                post.setNamaKost(et_namaKost.getText().toString());
                post.setNoHPKost(et_noHP.getText().toString());
                post.setAlamatKost(et_alamatKost.getText().toString());
                post.setKotaKost(et_kotaKost.getText().toString());
                post.setLinkGMapKost(et_linkGMap.getText().toString());
                post.setJenisKost(et_jenisKost.getText().toString());
                post.setFasilitasKost(fasilitas);
                post.setDeskripsiKost(et_deskripsi.getText().toString());
                post.setImage_url(url.toString());


                mDatabaseReference.child(post.getId()).setValue(post).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
//                                progressDialog.dismiss();

                        Toast.makeText(getApplicationContext(), "Berhasil menambahkan data kost anda!", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(getApplicationContext(), MainMenu.class));
                        finish();
                    }
                });
            });
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                resultUri = result.getUri();

                try {
                    // Mengambil gambar dari storage
                    InputStream stream = getContentResolver().openInputStream(resultUri);
                    // Konversi kedalam bitmap
                    Bitmap bitmap = BitmapFactory.decodeStream(stream);
                    // Menampilkan gambar
                    coverImage.setImageBitmap(bitmap);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }
    }

    public void balik(View view) {
        finish();
    }
}
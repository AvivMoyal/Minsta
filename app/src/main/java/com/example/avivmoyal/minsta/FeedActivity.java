package com.example.avivmoyal.minsta;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.example.avivmoyal.minsta.model.Like;
import com.example.avivmoyal.minsta.model.Post;
import com.example.avivmoyal.minsta.model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import com.google.firebase.database.Query;

import java.util.ArrayList;

public class FeedActivity extends AppCompatActivity {

//    static final int RC_PERMISSION_READ_EXTERNAL_STORAGE = 1;
//    static final int RC_IMAGE_GALLERY = 2;
    DatabaseReference database;
    RecyclerView recyclerView;
    RecyclerView.LayoutManager mLayoutManager;
    ImageAdapter mAdapter;
    ArrayList<Post> posts = new ArrayList<>();

    FirebaseUser fbUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed);

        fbUser = FirebaseAuth.getInstance().getCurrentUser();
        if (fbUser == null) {
            finish();
        }

        database = FirebaseDatabase.getInstance().getReference();

        // Setup the RecyclerView
        recyclerView = findViewById(R.id.recyclerView);
        mLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new ImageAdapter(posts, this);
        recyclerView.setAdapter(mAdapter);

        // Get the latest 100 images
        Query postsQuery = database.child("posts").orderByKey().limitToFirst(100);
        postsQuery.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                // A new image has been added, add it to the displayed list
                final Post post = dataSnapshot.getValue(Post.class);

                // get the post user
                database.child("users/" + post.userId).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        User user = dataSnapshot.getValue(User.class);
                        post.user = user;
                        mAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

                // get post likes
                Query likesQuery = database.child("likes").orderByChild("imageId").equalTo(post.key);
                likesQuery.addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                        Like like = dataSnapshot.getValue(Like.class);
                        post.addLike();
                        if(like.userId.equals(fbUser.getUid())) {
                            post.hasLiked = true;
                            post.userLike = dataSnapshot.getKey();
                        }
                        mAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                    }

                    @Override
                    public void onChildRemoved(DataSnapshot dataSnapshot) {
                        Like like = dataSnapshot.getValue(Like.class);
                        post.removeLike();
                        if(like.userId.equals(fbUser.getUid())) {
                            post.hasLiked = false;
                            post.userLike = null;
                        }
                        mAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

                mAdapter.addPost(post);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void  addPost(View view) {
        // go to new post activity
        Intent intent = new Intent(this, NewPostActivity.class);
        startActivity(intent);
    }
//    public void uploadImage(View view) {
//        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
//            ActivityCompat.requestPermissions(this, new String[] {android.Manifest.permission.READ_EXTERNAL_STORAGE}, RC_PERMISSION_READ_EXTERNAL_STORAGE);
//        } else {
//            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
//            intent.setType("image/*");
//            startActivityForResult(intent, RC_IMAGE_GALLERY);
//        }
//    }

//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        if (requestCode == RC_PERMISSION_READ_EXTERNAL_STORAGE) {
//            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
//                intent.setType("image/*");
//                startActivityForResult(intent, RC_IMAGE_GALLERY);
//            }
//        }
//    }

//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        if (requestCode == RC_IMAGE_GALLERY && resultCode == RESULT_OK) {
//            Uri uri = data.getData();
//
//            StorageReference storageRef = FirebaseStorage.getInstance().getReference();
//            StorageReference imagesRef = storageRef.child("images");
//            StorageReference userRef = imagesRef.child(fbUser.getUid());
//            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
//            String filename = fbUser.getUid() + "_" + timeStamp;
//            StorageReference fileRef = userRef.child(filename);
//
//            UploadTask uploadTask = fileRef.putFile(uri);
//            uploadTask.addOnFailureListener(new OnFailureListener() {
//                @Override
//                public void onFailure(@NonNull Exception exception) {
//                    // Handle unsuccessful uploads
//                    Toast.makeText(FeedActivity.this, "Upload failed!\n" + exception.getMessage(), Toast.LENGTH_LONG).show();
//                }
//            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
//                @Override
//                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//                    Uri downloadUrl = taskSnapshot.getDownloadUrl();
//                    Toast.makeText(FeedActivity.this, "Upload finished!", Toast.LENGTH_SHORT).show();
//
//                    // save image to database
//                    String key = database.child("posts").push().getKey();
//                    Post post = new Post(key, fbUser.getUid(), downloadUrl.toString());
//                    database.child("posts").child(key).setValue(post);
//                }
//            });
//        }
//    }

    public void setLiked(Post post) {
        if(!post.hasLiked) {
            // add new Like
            post.hasLiked = true;
            Like like = new Like(post.key, fbUser.getUid());
            String key = database.child("likes").push().getKey();
            database.child("likes").child(key).setValue(like);
            post.userLike = key;
        } else {
            // remove Like
            post.hasLiked = false;
            if (post.userLike != null) {
                database.child("likes").child(post.userLike).removeValue();
            }
        }
    }
}

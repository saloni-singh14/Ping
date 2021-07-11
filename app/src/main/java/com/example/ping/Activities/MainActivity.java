package com.example.ping.Activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.ping.Adapters.TopStatusAdapter;
import com.example.ping.Models.Story;
import com.example.ping.Models.UserStory;
import com.example.ping.R;
import com.example.ping.Models.User;
import com.example.ping.Adapters.UsersAdapter;
import com.example.ping.databinding.ActivityMainBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import static androidx.constraintlayout.motion.utils.Oscillator.TAG;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "-1000";
    ActivityMainBinding binding;
    FirebaseDatabase database;
    ArrayList<User> users;
    UsersAdapter usersAdapter;
    TopStatusAdapter statusAdapter;
    ArrayList<UserStory> userStories;
    ProgressDialog dialog;

    FirebaseAuth auth;
    User user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        dialog = new ProgressDialog(this);
        dialog.setMessage("Uploading Image...");
        dialog.setCancelable(false);

        auth=FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        users = new ArrayList<>();
        userStories = new ArrayList<>();
        database.getReference().child("users").child(FirebaseAuth.getInstance().getUid())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        user = snapshot.getValue(User.class);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });


        usersAdapter = new UsersAdapter(this, users);
        statusAdapter = new TopStatusAdapter(this, userStories);
        //binding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(RecyclerView.HORIZONTAL);
        binding.statusList.setLayoutManager(layoutManager);
        binding.statusList.setAdapter(statusAdapter);

        binding.recyclerView.setAdapter(usersAdapter);

        binding.recyclerView.showShimmerAdapter();
        binding.statusList.showShimmerAdapter();

        database.getReference().child("users").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                users.clear();
                for(DataSnapshot snapshot1 : snapshot.getChildren()) {
                    User user = snapshot1.getValue(User.class);
                    if(user==null)
                    {

                        Log.d(TAG,"User is null");

                    }
                    else if(FirebaseAuth.getInstance().getCurrentUser().getUid()==null)
                    {
                        Log.d(TAG,"FirebaseAuth returns null");
                    }
                    else if(user.getUid()==null)
                    {
                        Log.d(TAG,"User ID is null");
                    }
                    else
                    {
                        //if(FirebaseAuth.getInstance().getCurrentUser().getUid()!=null)
                        if(!user.getUid().equals(FirebaseAuth.getInstance().getCurrentUser().getUid()))
                            users.add(user);
                    }

                }
                binding.recyclerView.hideShimmerAdapter();
                usersAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        database.getReference().child("stories").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()) {
                    if (userStories!=null)
                    {
                        userStories.clear();
                    }

                    for(DataSnapshot storySnapshot : snapshot.getChildren()) {
                        UserStory status = new UserStory();
                        status.setName(storySnapshot.child("name").getValue(String.class));
                        status.setProfileImage(storySnapshot.child("profileImage").getValue(String.class));
                        status.setLastUpdated(storySnapshot.child("lastUpdated").getValue(Long.class));

                        ArrayList<Story> statuses = new ArrayList<>();

                        for(DataSnapshot statusSnapshot : storySnapshot.child("statuses").getChildren()) {
                            Story sampleStatus = statusSnapshot.getValue(Story.class);
                            statuses.add(sampleStatus);
                        }

                        status.setStatuses(statuses);
                        userStories.add(status);
                    }
                    binding.statusList.hideShimmerAdapter();
                    statusAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        binding.bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.status:
                        Intent intent = new Intent();
                        intent.setType("image/*");
                        intent.setAction(Intent.ACTION_GET_CONTENT);
                        startActivityForResult(intent, 75);
                        break;
                }
                return false;
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(data != null) {
            if(data.getData() != null) {
                dialog.show();
                FirebaseStorage storage = FirebaseStorage.getInstance();
                Date date = new Date();
                StorageReference reference = storage.getReference().child("status").child(date.getTime() + "");

                reference.putFile(data.getData()).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                        if(task.isSuccessful()) {
                            reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    UserStory userStatus = new UserStory();
                                    userStatus.setName(user.getName());
                                    userStatus.setProfileImage(user.getProfileImage());
                                    userStatus.setLastUpdated(date.getTime());

                                    HashMap<String, Object> obj = new HashMap<>();
                                    obj.put("name", userStatus.getName());
                                    obj.put("profileImage", userStatus.getProfileImage());
                                    obj.put("lastUpdated", userStatus.getLastUpdated());

                                    String imageUrl = uri.toString();
                                    Story status = new Story(imageUrl, userStatus.getLastUpdated());

                                    database.getReference()
                                            .child("stories")
                                            .child(FirebaseAuth.getInstance().getUid())
                                            .updateChildren(obj);

                                    database.getReference().child("stories")
                                            .child(FirebaseAuth.getInstance().getUid())
                                            .child("statuses")
                                            .push()
                                            .setValue(status);

                                    dialog.dismiss();
                                }
                            });
                        }
                    }
                });
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        String currentId = FirebaseAuth.getInstance().getUid();
        //database.getReference().child("presence").child(currentId).setValue("Online");
    }

    @Override
    protected void onPause() {
        super.onPause();
        String currentId = FirebaseAuth.getInstance().getUid();
        //database.getReference().child("presence").child(currentId).setValue("Offline");
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            /*case R.id.group:
                startActivity(new Intent(MainActivity.this, GroupChatActivity.class));
                break;*/
            case R.id.videoCall:
                onVideoCallClick();
                break;
            case R.id.settings:
                OnSettingsClicked();
                 break;

            case R.id.Logout:
                OnLogoutClicked(auth);
                Toast.makeText(this, "Logout clicked", Toast.LENGTH_SHORT).show();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void OnSettingsClicked() {
        Intent intent = new Intent(getApplicationContext(), SetupProfileActivity.class);
        startActivity(intent);
    }

    private void OnLogoutClicked(FirebaseAuth auth) {
        auth.signOut();
        Intent intent=new Intent(MainActivity.this, SignInActivity.class);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.topmenu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    public void onToDoCLick(MenuItem item) {
        FirebaseUser user=FirebaseAuth.getInstance().getCurrentUser();
        String userID=user.getUid();
        FirebaseDatabase database=FirebaseDatabase.getInstance();
        database.getReference().child("users").child(userID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                User user=snapshot.getValue(User.class);
                String name = user.getName();
                sendToTaskActivity(name);


            }

            private void sendToTaskActivity(String userName) {
                Intent intent=new Intent(MainActivity.this,ToDoList.class);
                //Toast.makeText(getApplicationContext(),"The username being sent from Chat Activity is "+userName,Toast.LENGTH_SHORT).show();
                intent.putExtra("username", userName);
                startActivity(intent);

            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

                Log.d(TAG, "User already exists");

            }
        });

    }

    public void onVideoCallClick() {
        FirebaseUser user=FirebaseAuth.getInstance().getCurrentUser();
        String userID=user.getUid();
        FirebaseDatabase database=FirebaseDatabase.getInstance();
        database.getReference().child("users").child(userID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                User user=snapshot.getValue(User.class);
                String name = user.getName();
                sendToVideoCallActivity(name);


            }

            private void sendToVideoCallActivity(String userName) {
                Intent intent=new Intent(MainActivity.this,VideoCallActivity.class);
                //Toast.makeText(getApplicationContext(),"The username being sent from Chat Activity is "+userName,Toast.LENGTH_SHORT).show();
                intent.putExtra("userName", userName);
                startActivity(intent);

            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

                Log.d(TAG, "User already exists");

            }
        });
    }
}
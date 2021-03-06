package com.example.semestro_projektas;

import android.content.DialogInterface;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class WarningsFragment extends Fragment {

    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference myRef;
    private RecyclerView mRecyclerView;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_warnings, container, false);
        super.onCreate(savedInstanceState);
        getActivity().setTitle("Įspėjimai");
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        myRef = mFirebaseDatabase.getReference("Pranešimas");

        myRef.addValueEventListener(new ValueEventListener() {

            final TextView text = new TextView(getActivity());
            Button btn = new Button(getActivity());
            final LinearLayout layout = (LinearLayout) rootView.findViewById(R.id.layout);

            @Override
            public void onDataChange(final DataSnapshot dataSnapshot) {

                layout.removeAllViews();
                if (getActivity() != null) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        final String myValues = snapshot.getKey();
                        layout.setOrientation(LinearLayout.VERTICAL);
                        btn = new Button(getActivity());
                        layout.addView(btn);
                        btn.setText(myValues);
                        btn.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                        btn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                final String data = dataSnapshot.child(myValues).child("Tekstas").getValue(String.class);
                                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                                builder.create();
                                builder.setTitle(myValues);
                                builder.setMessage("Įspėjimas: " + data)
                                        .setPositiveButton("Ištrinti", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                dataSnapshot.child(myValues).getRef().removeValue();

                                            }
                                        }).setNegativeButton("Atšaukti", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                    }
                                });
                                builder.show();
                            }
                        });
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                layout.removeAllViews();
            }
        });
        return rootView;
    }
}
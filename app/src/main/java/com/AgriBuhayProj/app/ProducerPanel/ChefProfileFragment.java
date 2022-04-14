package com.AgriBuhayProj.app.ProducerPanel;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import com.AgriBuhayProj.app.R;

public class ChefProfileFragment extends Fragment {

    Button post;
    ConstraintLayout bgimage;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_chef_profile, container, false);
        getActivity().setTitle("Post Product");

        setHasOptionsMenu(true);

        post = (Button) v.findViewById(R.id.post_dish);
        post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), Chef_PostDish.class));
            }
        });

        return v;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.add_crop, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int idd = item.getItemId();
        if (idd == R.id.addCrop) {
            addCrop();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void addCrop() {
        Intent intent = new Intent(getActivity(), ProducerAddCropList.class);
        /*intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK );*/
        startActivity(intent);
    }
}
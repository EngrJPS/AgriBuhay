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

//PROFILE FRAGMENT
public class ProducerProfileFragment extends Fragment {
    //VARIABLES
    Button post;
    ConstraintLayout bgimage;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_producer_profile, container, false);
        getActivity().setTitle("Post Product");

        //SET OPTIONS
        setHasOptionsMenu(true);

        //CONNECT XML
        post = v.findViewById(R.id.post_dish);

        //POST PRODUCT
        post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), ProducerPostProduct.class));
            }
        });

        return v;
    }

    //OPTIONS
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.add_crop, menu);
    }

    //OPTION SELECTED
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int idd = item.getItemId();
        if (idd == R.id.addCrop) {
            addCrop();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    //ADD CROP
    private void addCrop() {
        //direct to add crop
        Intent intent = new Intent(getActivity(), ProducerAddCropList.class);
        startActivity(intent);
    }
}
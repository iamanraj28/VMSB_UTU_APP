package com.example.vmsb_utu.admin;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.vmsb_utu.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;

import de.hdodenhof.circleimageview.CircleImageView;

public class AllCollegesAdapter extends FirebaseRecyclerAdapter<AllCollegesData, AllCollegesAdapter.AllCollegesViewHolder> {

    public AllCollegesAdapter(@NonNull FirebaseRecyclerOptions<AllCollegesData> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull AllCollegesViewHolder holder, int position, @NonNull AllCollegesData model) {

        holder.collegeName.setText(model.getCollegeName());
        holder.collegePhone.setText(model.getCollegePhone());
        holder.collegeEmail.setText(model.getCollegeEmail());

        Glide.with(holder.collegeImage.getContext()).load(model.getPurl()).into(holder.collegeImage);

    }

    @NonNull
    @Override
    public AllCollegesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.allcolleges_item,parent,false);
        return new AllCollegesViewHolder(view);
    }

    class AllCollegesViewHolder extends RecyclerView.ViewHolder{

        CircleImageView collegeImage;
        TextView collegeName, collegeEmail, collegePhone;

        public AllCollegesViewHolder(@NonNull View itemView) {
            super(itemView);
            collegeImage = (CircleImageView)itemView.findViewById(R.id.collegeImage);
            collegeName = (TextView) itemView.findViewById(R.id.collegeName);
            collegeEmail = (TextView) itemView.findViewById(R.id.collegeEmail);
            collegePhone = (TextView) itemView.findViewById(R.id.collegePhone);
        }
    }


}

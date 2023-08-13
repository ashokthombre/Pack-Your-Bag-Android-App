package com.example.packyourbagvideo.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.packyourbagvideo.CheckListActivity;
import com.example.packyourbagvideo.R;
import com.example.packyourbagvideo.constants.MConstants;

import java.util.List;

public class Adapter extends RecyclerView.Adapter<Adapter.ViewHolder> {
   Context context;
    List<String>titles;
   List<Integer> images;
   LayoutInflater inflater;
   Activity activity;

    public Adapter(Context contxt, List<String> titles, List<Integer> images,  Activity activity) {
        this.context=contxt;
        this.titles = titles;
        this.images = images;
        this.inflater = LayoutInflater.from(context);
        this.activity = activity;
    }

    @NonNull
    @Override
    public Adapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
       View view=inflater.inflate(R.layout.main_item,parent,false);
       return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Adapter.ViewHolder holder, int position) {
 
        holder.title.setText(titles.get(position));
        holder.img.setImageResource(images.get(position));
        holder.linearLayout.setAlpha(0.8f);
        
        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(context, "clicked on card", Toast.LENGTH_SHORT).show();
                Intent intent=new Intent(context, CheckListActivity.class);
                intent.putExtra(MConstants.HEADER_SMALL,titles.get(position));
                if (MConstants.MY_SELECTIONS.equals(titles.get(position)))
                {
                    intent.putExtra(MConstants.SHOW_SMALL,MConstants.FALSE_STRING);
                }
                else
                {
                    intent.putExtra(MConstants.SHOW_SMALL,MConstants.TRUE_STRING);
                }
                context.startActivity(intent);
            }
        });
        
    }

    @Override
    public int getItemCount() {
        return titles.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView title;
        ImageView img;
        LinearLayout linearLayout;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            title=itemView.findViewById(R.id.title);
            img=itemView.findViewById(R.id.img);
            linearLayout=itemView.findViewById(R.id.linearLayout);

        }
    }
}

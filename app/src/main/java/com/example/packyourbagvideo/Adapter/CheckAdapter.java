package com.example.packyourbagvideo.Adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.packyourbagvideo.Database.RoomDB;
import com.example.packyourbagvideo.Models.Items;
import com.example.packyourbagvideo.R;
import com.example.packyourbagvideo.constants.MConstants;

import java.util.List;

public class CheckAdapter extends RecyclerView.Adapter<CheckAdapter.Viewolder> {
    Context context;
    List<Items> itemsList;
    RoomDB database;

    String show;

    public CheckAdapter() {
    }

    public CheckAdapter(Context context, List<Items> itemsList, RoomDB database, String show) {
        this.context = context;
        this.itemsList = itemsList;
        this.database = database;
        this.show = show;
        if (itemsList.size()==0)
        {
            Toast.makeText(context.getApplicationContext(), "Nothing to Show", Toast.LENGTH_SHORT).show();
        }
    }

    @NonNull
    @Override
    public CheckAdapter.Viewolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.check_list,parent,false);
        return new Viewolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CheckAdapter.Viewolder holder, int position) {

        holder.checkBox.setText(itemsList.get(position).getItemname());
        holder.checkBox.setChecked(itemsList.get(position).getChecked());
        if (MConstants.FALSE_STRING.equals(show))
        {
            holder.btnDelete.setVisibility(View.GONE);
            holder.layout.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.border_one));
        }
        else
        {
            if (itemsList.get(position).getChecked())
            {
                holder.layout.setBackgroundColor(Color.parseColor("#Be456f"));
            }
            else
            {
                holder.layout.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.border_one));
            }
            holder.checkBox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Boolean check=holder.checkBox.isChecked();
                    database.mainDao().checkUncheck(itemsList.get(position).getID(),check);
                    if (MConstants.FALSE_STRING.equals(show))
                    {
                        itemsList=database.mainDao().getALlSelected(true);
                        notifyDataSetChanged();
                    }
                    else
                    {
                       itemsList.get(position).setChecked(check);
                       notifyDataSetChanged();
                       Toast tostMessage=null;
                       if (tostMessage!=null)
                       {
                           tostMessage.cancel();
                       }
                       if (itemsList.get(position).getChecked())
                       {
                           tostMessage=Toast.makeText(context,"("+holder.checkBox.getText() +") Packed",Toast.LENGTH_SHORT );
                       }
                       else
                       {
                           tostMessage=Toast.makeText(context,"("+holder.checkBox.getText() +") Un-Packed",Toast.LENGTH_SHORT );
                       }
                       tostMessage.show();

                    }

                }
            });
            holder.btnDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog dialog = new AlertDialog.Builder(context)
                            .setTitle("Delete (" +itemsList.get(position).getItemname()+")")
                            .setMessage("Are you sure ?")
                            .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    database.mainDao().delete(itemsList.get(position).getID());
                                    itemsList.remove(itemsList.get(position));
                                    notifyDataSetChanged();
                                }
                            })
                            .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Toast.makeText(context, "Cancel", Toast.LENGTH_SHORT).show();
                                  dialog.cancel();
                                }
                            })
                            .setIcon(R.drawable.baseline_delete_24)
                            .show();
                }

            });
        }
    }

    @Override
    public int getItemCount() {
        return itemsList.size();
    }

    public class Viewolder extends RecyclerView.ViewHolder {

        LinearLayout layout;
        CheckBox checkBox;
        Button btnDelete;
        public Viewolder(@NonNull View itemView) {
            super(itemView);

            layout=itemView.findViewById(R.id.linearLayout);
            checkBox=itemView.findViewById(R.id.checkbox);
            btnDelete=itemView.findViewById(R.id.btnDelete);

        }
    }
}

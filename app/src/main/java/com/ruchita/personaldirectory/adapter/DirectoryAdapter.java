package com.ruchita.personaldirectory.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.ruchita.personaldirectory.R;
import com.ruchita.personaldirectory.activity.AddDirectoryScreen;
import com.ruchita.personaldirectory.activity.HomeScreen;
import com.ruchita.personaldirectory.activity.MapScreen;
import com.ruchita.personaldirectory.models.User;

import java.io.Serializable;
import java.util.List;

public class DirectoryAdapter extends RecyclerView.Adapter<DirectoryAdapter.MyViewHolder> {

    private final List<User> userList;
    private final Context mContext;

    public DirectoryAdapter(Context context, List<User> list) {
        this.mContext = context;
        this.userList = list;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.items_directory, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {

        holder.tvName.setText(userList.get(position).getName());
        holder.tvPhone.setText(userList.get(position).getPhone());
        holder.tvAddress.setText(userList.get(position).getAddress());

        holder.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                List<User> user = User.find(User.class, "phone = ?", userList.get(holder.getAdapterPosition()).getPhone());
                user.get(0).delete();
                userList.remove(holder.getAdapterPosition());
                notifyItemRemoved(holder.getAdapterPosition());
                notifyDataSetChanged();
            }
        });

        holder.btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(mContext, AddDirectoryScreen.class);
                i.putExtra("user_data", (Serializable) userList.get(position));
                ((HomeScreen)mContext).startActivityForResult(i, 200);
            }
        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(mContext, MapScreen.class);
                i.putExtra("user_data", (Serializable) userList.get(position));
                mContext.startActivity(i);
            }
        });
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private final TextView tvName;
        private final TextView tvPhone;
        private final TextView tvAddress;
        private final Button btnEdit;
        private final Button btnDelete;

        public MyViewHolder(View itemView) {
            super(itemView);
            tvName = (TextView) itemView.findViewById(R.id.tvName);
            tvPhone = (TextView) itemView.findViewById(R.id.tvPhone);
            tvAddress = (TextView) itemView.findViewById(R.id.tvAddress);
            btnEdit = (Button)itemView.findViewById(R.id.btnEdit);
            btnDelete = (Button)itemView.findViewById(R.id.btnDelete);

        }
    }


    public interface CategorySelect {
        void onItemClick(int selectedPosition);
    }
}

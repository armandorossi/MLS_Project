package com.example.mls_project.Models;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.RecyclerView;
import com.example.mls_project.Database.ConnectionSQL;
import com.example.mls_project.Entities.User;
import com.example.mls_project.R;
import com.google.android.material.tabs.TabLayout;
import java.util.List;
import java.util.Objects;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder> {

    private final Context context;
    private final List<User> userList;
    TabLayout tabLayoutUser;

    public UserAdapter(Context current, List<User> userList, TabLayout tabLayoutUser) {
        this.context = current;
        this.userList = userList;
        this.tabLayoutUser = tabLayoutUser;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType){
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.user_item, viewGroup, false);
        return new ViewHolder(v, context, tabLayoutUser);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        User user = userList.get(position);
        holder.fullName.setText(user.getFullName());
        holder.email.setText(user.getEmail());
        holder.admin.setText(user.getAdmin());
        holder.status.setText(user.getStatus());

        if (user.getAdmin().equals("Yes")) {
            holder.admin.setTextColor(Color.RED);
        }

        if (user.getStatus().equals("Active")) {
            holder.padlock.setImageResource(R.drawable.padlock_closed);
        }
        else {
            holder.status.setTextColor(Color.RED);
            holder.padlock.setImageResource(R.drawable.padlock_open);
        }
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView fullName, email, admin, status;
        ImageView padlock;
        public ViewHolder(@NonNull View itemView, Context context, TabLayout tabLayoutUser) {
            super(itemView);
            fullName = itemView.findViewById(R.id.txt_user_full_name);
            email = itemView.findViewById(R.id.txt_user_email);
            admin = itemView.findViewById(R.id.txt_user_admin_nd);
            status = itemView.findViewById(R.id.txt_user_status_nd);
            padlock = itemView.findViewById(R.id.user_padlock);
            padlock.setOnClickListener(v -> {
                if (padlock.getDrawable().getConstantState() == Objects.requireNonNull(ResourcesCompat.getDrawable(context.getResources(), R.drawable.padlock_closed, context.getTheme())).getConstantState()) {
                    new AlertDialog.Builder(context)
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .setTitle("User status")
                            .setMessage("Are you sure you want to deactivate " + fullName.getText() + "?")
                            .setPositiveButton("Yes", (dialog, which) -> {
                                ConnectionSQL con = new ConnectionSQL();
                                if (con.updateUserStatus(email.getText().toString(), 0) > 0) {
                                    Toast.makeText(context, fullName.getText().toString() + " deactivated.", Toast.LENGTH_LONG).show();
                                    tabLayoutUser.selectTab(tabLayoutUser.getTabAt(0));
                                }
                                else {
                                    Toast.makeText(context, "Something went wrong while trying to deactivate this user", Toast.LENGTH_LONG).show();
                                }
                            })
                            .setNegativeButton("No", null)
                            .show();
                }
                else {
                    new AlertDialog.Builder(context)
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .setTitle("User status")
                            .setMessage("Are you sure you want to activate " + fullName.getText() + "?")
                            .setPositiveButton("Yes", (dialog, which) -> {
                                ConnectionSQL con = new ConnectionSQL();
                                if (con.updateUserStatus(email.getText().toString(), 1) > 0) {
                                    Toast.makeText(context, fullName.getText().toString() + " activated.", Toast.LENGTH_LONG).show();
                                    tabLayoutUser.selectTab(tabLayoutUser.getTabAt(1));
                                }
                                else {
                                    Toast.makeText(context, "Something went wrong while trying to activate this user", Toast.LENGTH_LONG).show();
                                }
                            })
                            .setNegativeButton("No", null)
                            .show();
                }
            });
        }
    }
}

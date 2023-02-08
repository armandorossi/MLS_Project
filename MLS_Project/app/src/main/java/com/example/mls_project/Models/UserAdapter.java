package com.example.mls_project.Models;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mls_project.Database.ConnectionSQL;
import com.example.mls_project.R;

import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder> {

    private final Context context;
    private final List<String> list;
    private final Spinner spn;

    public UserAdapter(Context current, List<String> list, Spinner spn) {
        this.context = current;
        this.list = list;
        this.spn = spn;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType){
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.user_item, viewGroup, false);
        return new ViewHolder(v, context, this.spn);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String[] userList = list.get(position).split(";");
        holder.fullName.setText(userList[0]); //Full name
        holder.email.setText(userList[1]); //Email
        String text;
        if (userList[2].equals("1")) { //Admin - Yes or No
            text = "Yes";
            holder.admin.setTextColor(Color.RED);
        }
        else {
            text = "No";
            holder.admin.setTextColor(Color.BLUE);
        }
        holder.admin.setText(text);

        if (userList[3].equals("1")) { //Status - Yes or No
            text = "Active";
            holder.status.setTextColor(Color.BLUE);
            holder.padlock.setImageResource(R.drawable.padlock_closed);
        }
        else {
            text = "Inactive";
            holder.status.setTextColor(Color.RED);
            holder.padlock.setImageResource(R.drawable.padlock_open);
        }
        holder.status.setText(text);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView fullName, email, admin, status;
        ImageView padlock;
        public ViewHolder(@NonNull View itemView, Context context, Spinner spn) {
            super(itemView);
            fullName = itemView.findViewById(R.id.txt_user_full_name);
            email = itemView.findViewById(R.id.txt_user_email);
            admin = itemView.findViewById(R.id.txt_user_admin_nd);
            status = itemView.findViewById(R.id.txt_user_status_nd);
            padlock = itemView.findViewById(R.id.user_padlock);
            padlock.setOnClickListener(v -> {
                if (padlock.getDrawable().getConstantState() == context.getResources().getDrawable( R.drawable.padlock_closed).getConstantState()) {
                    new AlertDialog.Builder(context)
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .setTitle("User status")
                            .setMessage("Are you sure you want to deactivate " + fullName.getText() + "?")
                            .setPositiveButton("Yes", (dialog, which) -> {
                                ConnectionSQL con = new ConnectionSQL();
                                if (con.updateUserStatus(email.getText().toString(), 0) > 0) {
                                    Toast.makeText(context, fullName.getText().toString() + " deactivated.", Toast.LENGTH_LONG).show();
                                    spn.setSelection(1);
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
                                    spn.setSelection(0);
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

package com.example.mls_project;

import android.content.Context;
import android.content.DialogInterface;
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

import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder> {

    private Context context;
    private List<String> list;
    private Spinner spn;

    public UserAdapter(Context current, List<String> list, Spinner spn) {
        this.context = current;
        this.list = list;
        this.spn = spn;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType){
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.user_item, viewGroup, false);
        UserAdapter.ViewHolder viewHolder = new ViewHolder(v, context, this.spn);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String[] userList = list.get(position).split(";");
        holder.fullname.setText(userList[0]); //Full name
        holder.email.setText(userList[1]); //Email
        if (userList[2].equals("1")) { //Admin - Yes or No
            holder.admin.setText(context.getResources().getString(R.string.txt_user_admin) + " Yes");
        }
        else {
            holder.admin.setText(context.getResources().getString(R.string.txt_user_admin) + " No");
        }
        if (userList[3].equals("1")) { //Status - Yes or No
            holder.status.setText(context.getResources().getString(R.string.txt_user_status) + " Active");
            holder.padlock.setImageResource(R.drawable.padlock_closed);
        }
        else {
            holder.status.setText(context.getResources().getString(R.string.txt_user_status) + " Inactive");
            holder.padlock.setImageResource(R.drawable.padlock_open);
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView fullname, email, admin, status;
        ImageView padlock;
        public ViewHolder(@NonNull View itemView, Context context, Spinner spn) {
            super(itemView);
            fullname = itemView.findViewById(R.id.txt_user_full_name);
            email = itemView.findViewById(R.id.txt_user_email);
            admin = itemView.findViewById(R.id.txt_user_admin);
            status = itemView.findViewById(R.id.txt_user_status);
            padlock = itemView.findViewById(R.id.user_padlock);
            padlock.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (padlock.getDrawable().getConstantState() == context.getResources().getDrawable( R.drawable.padlock_closed).getConstantState()) {
                        new AlertDialog.Builder(context)
                                .setIcon(android.R.drawable.ic_dialog_alert)
                                .setTitle("User status")
                                .setMessage("Are you sure you want to deactivate " + fullname.getText() + "?")
                                .setPositiveButton("Yes", new DialogInterface.OnClickListener()
                                {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        ConnectionSQL con = new ConnectionSQL();
                                        if (con.updateUserStatus(email.getText().toString(), 0) > 0) {
                                            Toast.makeText(context, fullname.getText().toString() + " deactivated.", Toast.LENGTH_LONG).show();
                                            spn.setSelection(1);
                                        }
                                        else {
                                            Toast.makeText(context, "Something went wrong while trying to deactivate this user", Toast.LENGTH_LONG).show();
                                        }
                                    }
                                })
                                .setNegativeButton("No", null)
                                .show();
                    }
                    else {
                        new AlertDialog.Builder(context)
                                .setIcon(android.R.drawable.ic_dialog_alert)
                                .setTitle("User status")
                                .setMessage("Are you sure you want to activate " + fullname.getText() + "?")
                                .setPositiveButton("Yes", new DialogInterface.OnClickListener()
                                {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        ConnectionSQL con = new ConnectionSQL();
                                        if (con.updateUserStatus(email.getText().toString(), 1) > 0) {
                                            Toast.makeText(context, fullname.getText().toString() + " activated.", Toast.LENGTH_LONG).show();
                                            spn.setSelection(0);
                                        }
                                        else {
                                            Toast.makeText(context, "Something went wrong while trying to activate this user", Toast.LENGTH_LONG).show();
                                        }
                                    }
                                })
                                .setNegativeButton("No", null)
                                .show();
                    }
                }
            });
        }
    }
}

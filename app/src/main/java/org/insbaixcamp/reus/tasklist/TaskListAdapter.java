package org.insbaixcamp.reus.tasklist;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;

import androidx.annotation.NonNull;


import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import androidx.recyclerview.widget.ItemTouchHelper.SimpleCallback;

import java.util.ArrayList;

public class TaskListAdapter extends RecyclerView.Adapter<TaskListAdapter.ViewHolder> {

    private ArrayList<Task> tasks;
    private static OnItemClickListener listener;

    Context context;


    public interface OnItemClickListener {
        void onItemClick(int position);

        void onItemLongClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView taskName;
        public TextView taskDesc;
        public TextView taskResp;
        public TextView taskUrg;

        public ViewHolder(View itemView) {
            super(itemView);
            taskName = itemView.findViewById(R.id.tvTaskName);
            taskDesc = itemView.findViewById(R.id.txtDescription);
            taskResp = itemView.findViewById(R.id.txtResponsable);
            taskUrg = itemView.findViewById(R.id.txtUrgency);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION && listener != null) {
                        listener.onItemClick(position);
                    }
                }
            });

            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION && listener != null) {
                        listener.onItemLongClick(position);
                        return true;
                    }
                    return false;
                }
            });
        }
    }

    public void clearTasks() {
        tasks.clear();
        notifyDataSetChanged();
    }


    public TaskListAdapter(Context context, ArrayList<Task> tasks) {
        this.context = context;
        this.tasks = tasks;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_task, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        // Obtener la tarea en la posición correspondiente de la lista
        Task task = tasks.get(position);

        // Establecer los valores de los TextViews en función de los datos de la tarea
        holder.taskName.setText(task.getName());
        holder.taskDesc.setText(task.getDescription());
        holder.taskUrg.setText(task.getUrgency());
        holder.taskResp.setText(task.getResponsable());


    }

    public void addTask(Task task) {
        tasks.add(task);
        notifyItemInserted(tasks.size() - 1);
    }


    @Override
    public int getItemCount() {
        return tasks.size();
    }
}


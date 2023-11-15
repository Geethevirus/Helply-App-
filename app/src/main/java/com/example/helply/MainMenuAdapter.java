package com.example.helply;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class MainMenuAdapter extends RecyclerView.Adapter<MainMenuAdapter.UseCaseHolder> {
    
    private List<String> usecaseName;
    private Context context;
    public UserClickListener userClickListener;
    
    public MainMenuAdapter(Context context, List<String> usecaseNames,UserClickListener userClickListener)
    {
        
        this.usecaseName = usecaseNames;
        this.context = context;
        this.userClickListener = userClickListener;
        
    }
    public interface UserClickListener
    {
        void selectedUser(String useCase);
        
    }
    
    
    @NonNull
    @Override
    public MainMenuAdapter.UseCaseHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        
        View view = LayoutInflater.from(context).inflate(R.layout.help_opions_view,parent, false);
        return new UseCaseHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MainMenuAdapter.UseCaseHolder holder, int position) {
        String useCaseName = usecaseName.get(position);
        holder.textView.setText(usecaseName.get(position));

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                userClickListener.selectedUser(useCaseName);

            }
        });

    }

    @Override
    public int getItemCount() {
        return usecaseName.size();
    }

    public class UseCaseHolder extends RecyclerView.ViewHolder {

        TextView textView;

        public UseCaseHolder(@NonNull View itemView) {
            super(itemView);

            textView = itemView.findViewById(R.id.useCaseName);

        }
    }
}

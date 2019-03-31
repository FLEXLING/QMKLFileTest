package com.best.huge.qmklfiletest;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class FileAdapter extends RecyclerView.Adapter<FileAdapter.ViewHolder> {

    private OnClickListener onClickListener;
    private String path="/";

    public OnClickListener getOnClickListener() {
        return onClickListener;
    }

    public void setOnClickListener(OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    private List<FileItem> mFileItemList;
    static class ViewHolder extends RecyclerView.ViewHolder{
        TextView fileName;
        TextView fileSize;
        public ViewHolder(View view){
            super(view);
            fileName=(TextView) view.findViewById(R.id.name);
            fileSize=(TextView) view.findViewById(R.id.size);
        }
    }

    public FileAdapter(List<FileItem> fileItemList){
        mFileItemList=fileItemList;
    }

    @Override
    public FileAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.file_item,viewGroup,false);
        ViewHolder holder=new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, int i) {
        final FileItem file=mFileItemList.get(i);
        viewHolder.fileName.setText(file.getName());
        viewHolder.fileSize.setText(file.getSize());
        final View itemView=viewHolder.itemView;
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                path=path.concat(file.getName()+"/");
                final String finalPath = path;
                onClickListener.onClick(itemView,viewHolder.getLayoutPosition(), finalPath);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mFileItemList.size();
    }

    interface OnClickListener {
        void onClick(View itemView,int i,String cate);
    }
}

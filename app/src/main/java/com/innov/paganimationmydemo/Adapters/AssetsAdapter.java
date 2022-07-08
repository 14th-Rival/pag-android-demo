package com.innov.paganimationmydemo.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.innov.paganimationmydemo.Interfaces.OnClickListener;
import com.innov.paganimationmydemo.MainActivity;
import com.innov.paganimationmydemo.Models.AssetsModel;
import com.innov.paganimationmydemo.R;

import org.libpag.PAGFile;
import org.libpag.PAGView;

import java.util.ArrayList;
import java.util.List;

public class AssetsAdapter extends RecyclerView.Adapter<AssetsAdapter.AssetsViewHolder> {

    private OnClickListener onClickListenerl;
    private List<AssetsModel> assetsModelList= new ArrayList<>();
    private Context mContext;
    private PAGFile mPAGFile;
    private int selectedItem;
    private MainActivity mMainActivity;

    public void setOnClickListenerl(OnClickListener onClickListenerl) {
        this.onClickListenerl = onClickListenerl;
    }

    public AssetsAdapter(List<AssetsModel> assetsModelList, Context mContext, MainActivity activity) {
        this.assetsModelList = assetsModelList;
        this.mContext = mContext;
        this.mMainActivity = activity;
        selectedItem = 0;
    }

    @Override
    public int getItemCount() {
        return assetsModelList.size();
    }

    @NonNull
    @Override
    public AssetsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View rootView = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_asset, parent, false);
        return new AssetsViewHolder(rootView, onClickListenerl);
    }

    @Override
    public void onBindViewHolder(@NonNull AssetsViewHolder holder, int position) {
        AssetsModel currentItem = assetsModelList.get(position);

        try {
            if (selectedItem == holder.getAdapterPosition()) {
                holder.mPAGViewCarrier.setBackground(
                        ContextCompat.getDrawable(
                                holder.mPAGViewCarrier.getContext(),
                                R.drawable.recycler_selected)
                );
                mMainActivity.setSelectedAsset(currentItem.getmAsset());
            }

            else {
                holder.mPAGViewCarrier.setBackground(
                        ContextCompat.getDrawable(
                                holder.mPAGViewCarrier.getContext(),
                                R.drawable.recycler_not_selected
                        )
                );
            }

            holder.itemView.setOnClickListener(v -> {

                int previousItem = selectedItem;
                selectedItem = holder.getAdapterPosition();

                notifyItemChanged(previousItem);
                notifyItemChanged(holder.getAdapterPosition());

            });

            mPAGFile = PAGFile.Load(mContext.getAssets(), currentItem.getmAsset());
            holder.mPAGView.setComposition(mPAGFile);
            holder.mPAGView.setRepeatCount(0);
            holder.mPAGView.play();

        } catch (Exception e){
            e.printStackTrace();
        }
    }

    public static class AssetsViewHolder extends RecyclerView.ViewHolder{

        private RelativeLayout mPAGViewCarrier;
        private PAGView mPAGView;

        public AssetsViewHolder(@NonNull View itemView, OnClickListener onClickListener) {
            super(itemView);

            mPAGViewCarrier = itemView.findViewById(R.id.pag_view_carrier);
            mPAGView = new PAGView(itemView.getContext());
            mPAGView.setLayoutParams (
                            new RelativeLayout.LayoutParams (
                                    ViewGroup.LayoutParams.MATCH_PARENT,
                                    ViewGroup.LayoutParams.MATCH_PARENT
                            )
                    );
            mPAGViewCarrier.addView(mPAGView);

            itemView.setOnClickListener(v -> {
                if (onClickListener != null){
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION){
                        onClickListener.onClick(v, position);
                    }
                }
            });
        }
    }
}

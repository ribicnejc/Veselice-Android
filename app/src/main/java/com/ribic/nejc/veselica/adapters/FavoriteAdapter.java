package com.ribic.nejc.veselica.adapters;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import com.ribic.nejc.party.R;
import com.ribic.nejc.veselica.objects.Party;
import com.ribic.nejc.veselica.utils.Constants;
import com.ribic.nejc.veselica.utils.PrefUtils;

import java.util.ArrayList;

public class FavoriteAdapter extends RecyclerView.Adapter<FavoriteAdapter.FavoriteAdapterViewHolder> {
    private ArrayList<Party> mParties;
    private final FavoriteAdapter.FavoriteAdapterOnClickHandler mClickHandler;

    public interface FavoriteAdapterOnClickHandler{
        void partyOnClick(int clickedItemIndex);
    }

    public FavoriteAdapter(ArrayList<Party> mParties, FavoriteAdapterOnClickHandler mClickHandler){
        this.mParties = mParties;
        this.mClickHandler = mClickHandler;
    }

    @Override
    public FavoriteAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.item, parent, false);
        return new FavoriteAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final FavoriteAdapterViewHolder holder, int position) {
        holder.mTextViewParty.setText(mParties.get(position).getPlace());
        holder.mTextViewDate.setText(mParties.get(position).getDate());
        if (mParties.get(position) != null){
            Party party = mParties.get(position);
            if (PrefUtils.exitsts(party.toString(), holder.itemView.getContext()))
                holder.mImageViewFavorite.setImageResource(R.drawable.icon_stared);
            else
                holder.mImageViewFavorite.setImageResource(R.drawable.icon_unstared);
        }
        final Party party = mParties.get(position);
        holder.mImageViewFavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Constants.fragmentContentChanged = !Constants.fragmentContentChanged;
                if (PrefUtils.exitsts(party.toString(), holder.itemView.getContext())){
                    PrefUtils.remove(party.toString(), holder.itemView.getContext());
                    holder.mImageViewFavorite.setImageResource(R.drawable.icon_unstared);
                }else{
                    PrefUtils.saveName(party.toString(), holder.itemView.getContext());
                    holder.mImageViewFavorite.setImageResource(R.drawable.icon_stared);
                }
            }
        });
        setAnimation(holder.itemView, position);

    }

    @Override
    public int getItemCount() {
        return mParties.size();
    }

    class FavoriteAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView mTextViewDate;
        private TextView mTextViewParty;
        private ImageView mImageViewFavorite;

        private FavoriteAdapterViewHolder(View itemView) {
            super(itemView);
            mTextViewDate = (TextView) itemView.findViewById(R.id.tv_party_date);
            mTextViewParty = (TextView) itemView.findViewById(R.id.tv_party_name);
            mImageViewFavorite = (ImageView) itemView.findViewById(R.id.image_view_icon_favorite);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();
            mClickHandler.partyOnClick(position);
        }
    }
    private int lastPosition = -1;


    private void setAnimation(View viewToAnimate, int position) {
        // If the bound view wasn't previously displayed on screen, it's animated
        if (position > lastPosition) {
            ScaleAnimation anim = new ScaleAnimation(0.0f, 1.0f, 0.0f, 1.0f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
            anim.setDuration(100);//to make duration random number between [0,501)
            viewToAnimate.startAnimation(anim);
            lastPosition = position;
        }
    }



}

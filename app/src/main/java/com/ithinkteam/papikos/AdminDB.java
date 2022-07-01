package com.ithinkteam.papikos;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class AdminDB extends RecyclerView.Adapter<AdminDB.FavoriteViewHolder> {
    public static MClickListener nlistener;
    private List<ModelDB> list;
    SharedPreferences sharedPreferences;

    public AdminDB(MClickListener listener) {
        list = new ArrayList<>();
        nlistener = listener;
    }

    public interface MClickListener {
        void onClick(int position);
    }

    public static class FavoriteViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView et_namaKost, et_fasilitas, et_kotaKost, et_jenisKost,et_harga,btn_hapus;
        ImageView coverImage;

        public FavoriteViewHolder(@NonNull View view) {
            super(view);
            et_namaKost = view.findViewById(R.id.namaKos);
            et_kotaKost = view.findViewById(R.id.lokasiKos);
            et_harga = view.findViewById(R.id.hargaKost);
            et_jenisKost = view.findViewById(R.id.tipeKos);
            et_fasilitas = view.findViewById(R.id.fasilitasKos);
            btn_hapus = view.findViewById(R.id.btn_hapus);

            coverImage = view.findViewById(R.id.gambarKos);


            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            nlistener.onClick(getLayoutPosition());
        }
    }

    @NonNull
    @SuppressLint("InflateParams")
    @Override
    public FavoriteViewHolder onCreateViewHolder(@NonNull ViewGroup p1, int p2) {
        return new FavoriteViewHolder(LayoutInflater.from(p1.getContext())
                .inflate(R.layout.item_menu, null));
    }

    @SuppressLint("SetText18n")
    @Override
    public void onBindViewHolder(@NonNull FavoriteViewHolder holder, int position) {
        ModelDB mp = list.get(position);

        Picasso.get().load(mp.getImage_url()).into(holder.coverImage);
        holder.et_namaKost.setText(mp.getNamaKost());
        holder.et_kotaKost.setText(mp.getKotaKost());
        holder.et_jenisKost.setText(mp.getJenisKost());
        holder.et_harga.setText("Rp. "+mp.getHargaKost()+"/bln");
        holder.et_fasilitas.setText(mp.getFasilitasKost().replaceAll("\n"," • ").substring(0,mp.getFasilitasKost().length() - 2));
    }

    @Override
    public int getItemCount() {
        return list != null ? list.size() : 0;
    }

    public void addModelDB(ModelDB op) {
        list.add(op);
        notifyDataSetChanged();
    }

    public ModelDB getModelDB(int position) {
        return list.get(position);
    }
}

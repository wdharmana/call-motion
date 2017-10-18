package com.colonylabs.callmotionsample.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.colonylabs.callmotionsample.CallActivity;
import com.colonylabs.callmotionsample.R;
import com.colonylabs.callmotionsample.model.Contact;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dharmana on 10/18/17.
 */

public class ListContactAdapter extends RecyclerView.Adapter<ListContactAdapter.ContactViewHolder>{
    private Context context;
    private List<Contact> listContact =  new ArrayList<>();
    public List<Contact> getListContact() {
        return listContact;
    }
    public void setListContact(List<Contact> listContact) {
        this.listContact = listContact;
        notifyDataSetChanged();
    }
    public ListContactAdapter(Context context) {
        this.context = context;
    }
    @Override
    public ContactViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemRow = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_row_contact, parent, false);
        return new ContactViewHolder(itemRow);
    }
    @Override
    public void onBindViewHolder(ContactViewHolder holder, int position) {
        holder.tvName.setText(getListContact().get(position).getName());
        holder.tvNumber.setText(getListContact().get(position).getNumber());
    }
    @Override
    public int getItemCount() {
        return getListContact().size();
    }
    class ContactViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView tvName;
        TextView tvNumber;
        public ContactViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            tvName = (TextView)itemView.findViewById(R.id.tv_item_name);
            tvNumber = (TextView)itemView.findViewById(R.id.tv_item_number);
        }
        @Override
        public void onClick(View view) {

            int position = getAdapterPosition();
            Contact contact = listContact.get(position);
            Intent callActivity = new Intent(context,CallActivity.class);
            callActivity.putExtra(CallActivity.EXTRAS_NAME,contact.getName());
            callActivity.putExtra(CallActivity.EXTRAS_NUMBER,contact.getNumber());
            context.startActivity(callActivity);
        }
    }
}
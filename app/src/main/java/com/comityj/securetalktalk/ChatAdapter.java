package com.comityj.securetalktalk;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.w3c.dom.CDATASection;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChatAdapter extends BaseAdapter {

    ArrayList<ChatDTO> chatdto;
    LayoutInflater layoutInflater;
    //List<String> chatlist;


//    public ChatAdapter(ArrayList<ChatDTO> chatdto, List<String> chatlist,LayoutInflater layoutInflater){
    public ChatAdapter(ArrayList<ChatDTO> chatdto, LayoutInflater layoutInflater){
        this.chatdto = chatdto;
        //this.chatlist = chatlist;
        this.layoutInflater = layoutInflater;
    }

    @Override
    public int getCount() {
        return chatdto.size();
    }

    @Override
    public Object getItem(int position) {
        return chatdto.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ChatDTO item = chatdto.get(position);
        View itemview = null;                               //재활용할 뷰는 사용X

        //itemview = layoutInflater.inflate(R.layout.chat_you, parent, false);
/*        if(item.getUserEmail().equals(ChatActivity.useremail)){
            itemview = layoutInflater.inflate(R.layout.chat_me, parent, false);
        }else{
            itemview = layoutInflater.inflate(R.layout.chat_you, parent, false);
        }*/
        itemview = layoutInflater.inflate(R.layout.chat_me, parent, false);

        CircleImageView youpro = itemview.findViewById(R.id.chat_profile);
        TextView youname = itemview.findViewById(R.id.chat_name);
        TextView youmsg = itemview.findViewById(R.id.chat_msg);
/*        TextView mename = itemview.findViewById(R.id.me_name);
        TextView memsg = itemview.findViewById(R.id.me_msg);*/

        //Picasso.get().load(item.getProfileUrl().toString()).into(youpro);
        //Picasso.get().load(item.getProfileUrl()).into(youpro);
        youname.setText(item.getUsername());
        youmsg.setText(item.getMsg());
/*        mename.setText(item.getUsername());
        memsg.setText(item.getMsg());*/

/*        Picasso.get().load(chatDTO.getProfileUrl().toString()).into(youpro);
        youname.setText(chatDTO.getUsername());
        youmsg.setText(chatDTO.getMsg());*/

        return itemview;
    }

    public void addItem(ChatDTO item) {
        chatdto.add(item);
    }
    public void removeItem(ChatDTO item) {
        chatdto.remove(item);
    }
    public void setItem(ChatDTO item) {
        this.chatdto = chatdto;
    }

}

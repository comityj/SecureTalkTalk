package com.comityj.securetalktalk;

public class RoomDTO {

    private String useruid;
    private String chat;

    public RoomDTO(){}

    public RoomDTO(String useruid, String chat){
        this.useruid = useruid;
        this.chat = chat;
    }

    public String getUser(){ return useruid; }

    public void setUser(String user){ this.useruid = useruid; }

    public String getChat(){ return chat; }

    public void setChat(String chat){ this.chat = chat; }
}

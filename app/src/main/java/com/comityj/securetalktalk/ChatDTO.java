package com.comityj.securetalktalk;

public class ChatDTO {

    private String msg;
    private String username;
    private Boolean encrypt;

    public ChatDTO(){}

    public ChatDTO(String username, String msg, boolean encrypt){
        this.username = username;       //username = this.username;
        this.msg = msg;
        this.encrypt = encrypt;
    }

    public String getMsg(){
        return msg;
    }

    public void setMsg(String msg){
        this.msg = msg;
    }

    public String getUsername(){
        return username;
    }

    public void getUsername(String nickname){
        this.username = username;
    }

    public Boolean getEncrypt(){return encrypt;}

    public void setEncrypt(Boolean encrypt) { this.encrypt = encrypt; }

}

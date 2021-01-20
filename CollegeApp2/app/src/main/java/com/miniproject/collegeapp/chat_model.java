package com.miniproject.collegeapp;
public class chat_model
{
    String name;
    String sender_id;
    String chat_id;
    String time_stamp;
    String message_id;
    String message;



    chat_model()
    {

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSender_id() {
        return sender_id;
    }

    public void setSender_id(String sender_id) {
        this.sender_id = sender_id;
    }

    public String getChat_id() {
        return chat_id;
    }

    public void setChat_id(String chat_id) {
        this.chat_id = chat_id;
    }

    public String getTime_stamp() {
        return time_stamp;
    }

    public void setTime_stamp(String time_stamp) {
        this.time_stamp = time_stamp;
    }

    public String getMessage_id() {
        return message_id;
    }

    public void setMessage_id(String message_id) {
        this.message_id = message_id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    chat_model(String name, String sender_id , String chat_id, String time_stamp , String message_id , String message){


        this.chat_id = chat_id;
        this.sender_id = sender_id;
        this.message = message;
        this.time_stamp = time_stamp;
        this.name = name;
        this.message_id = message_id;
    }

}

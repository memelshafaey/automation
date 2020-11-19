package com.arabydeals.messages;

import java.util.List;

public class MessagesResponse
{
    private int errorCode;

    private List<MobileMessage> messages;

    public void setErrorCode(int errorCode){
        this.errorCode = errorCode;
    }
    public int getErrorCode(){
        return this.errorCode;
    }

    public List<MobileMessage> getMessages() {
        return messages;
    }

    public void setMessages(List<MobileMessage> messages) {
        this.messages = messages;
    }
}

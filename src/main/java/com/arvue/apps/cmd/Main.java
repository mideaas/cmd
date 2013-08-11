package com.arvue.apps.cmd;

import org.vaadin.mideaas.MideaasComponent;
import org.vaadin.chatbox.ChatBox;
import org.vaadin.teemu.clara.binder.annotation.UiField;
import org.vaadin.chatbox.SharedChat;
import com.vaadin.ui.VerticalLayout;
import org.vaadin.chatbox.client.ChatUser;
import org.vaadin.chatbox.client.ChatLine;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;

public class Main extends MideaasComponent implements SharedChat.ChatListener {

    private ChatBox cb1;
    
    private SharedChat chat = new SharedChat();

    @UiField("layout1")
    private VerticalLayout layout1;

    @Override
    public void attach() {
        super.attach();
        
        cb1 = new ChatBox(chat);
        cb1.setSizeFull();
        layout1.addComponent(cb1);
        cb1.setUser(new ChatUser("User", "user1", "user1"));
        cb1.setShowSendButton(false);
        chat.addListener(this);
    }
    
    public void lineAdded(ChatLine line) {
        System.out.println("Line: " + line.getText());
        if (line.getUser()==null) {
            return;
        }
        
        String text = line.getText();

        try {
            Process tr = Runtime.getRuntime().exec(text.split("\\s+"));
            String s;
            
            BufferedReader rd = new BufferedReader(new InputStreamReader(tr.getInputStream()));
            while( (s = rd.readLine()) != null) {
                chat.addLine(s);
            }
            
            BufferedReader rdErr = new BufferedReader(new InputStreamReader(tr.getErrorStream()));
            while( (s = rdErr.readLine()) != null) {
                chat.addLine("ERROR: "+s);
            }
            
            
            
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hhz.graphicaluserinterface;

import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

/**
 *
 * @author Valerij
 */
public class MessagingBotAPI extends TelegramLongPollingBot{

    public static void main(String[] args) {
            ApiContextInitializer.init();
            TelegramBotsApi telegramBotsApi = new TelegramBotsApi();
            try {
                telegramBotsApi.registerBot(new MessagingBotAPI());
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
        }
    
    @Override
    public String getBotToken() {
        return "680951345:AAGVFtRd4kuWcmyyl-Hrp77C7vCIYHyttn0";
    }

    @Override
    public void onUpdateReceived(Update update) {
        SendMessage message = new SendMessage();
        SendPhoto photo = new SendPhoto();
   
        String command = update.getMessage().getText();
       
        if (command.equals("/report")){
           photo.setPhoto(new File ("C:\\Users\\Valerij\\Desktop\\Projekt 2\\OCR\\cam1\\IMG_3789.JPG"));
        }
         if (command.equals("/myname")){
           message.setText("Ich heiße \"OOSProjekt2Bot\"");
        } else {
             message.setText("Chill, ist noch nicht ausprogrammiert ;)");
         }
        photo.setChatId(update.getMessage().getChatId());
        message.setChatId(update.getMessage().getChatId());
        try {
            if(photo.getPhoto() != null){
            execute(photo);
            
            }
            if(!message.getText().isEmpty()){
            execute(message);
            
            }
        } catch (TelegramApiException ex) {
            Logger.getLogger(MessagingBotAPI.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public String getBotUsername() {
        return "OOSProjekt2Bot";
    }
    
}

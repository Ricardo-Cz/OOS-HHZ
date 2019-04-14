/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hhz.graphicaluserinterface;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import sqlite.DBController;

/**
 *
 * @author Mustafa
 */
public class MessagingBotAPI extends TelegramLongPollingBot {
    static DBController dbc = DBController.getInstance();
        
    public static void main(String[] args) {
        ApiContextInitializer.init();
        dbc.initDBConnection();
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
 private void setLedColors(String name_status, String price_status, int shelf_id, int row_id, int place_id){
     if (name_status.equals("Fehlplatzierung") && price_status.equals("Falscher Preis")) {
           // gesamt_status = "Rot"; //platz rot
            WebServiceLED.webserviceCall("{\"rgb\" : \"#110000\"}", shelf_id + "" +row_id + "" +place_id);
            // WebServiceLED.shelfLedMapping("{\"rgb\" : \"#FF0000\"}", row_id, place_id-7);
        } else if (name_status.equals("Fehlplatzierung") && price_status.equals("Preis korrekt")) {
           // gesamt_status = "Gelb"; //platz gelb
            WebServiceLED.webserviceCall("{\"rgb\" : \"#111100\"}", shelf_id + "" +row_id + "" +place_id);
            // WebServiceLED.shelfLedMapping("{\"rgb\" : \"#FFFF00\"}", row_id, place_id);
        } else if (name_status.equals("Platz korrekt") && price_status.equals("Falscher Preis")) {
            WebServiceLED.webserviceCall("{\"rgb\" : \"#110500\"}", shelf_id + "" +row_id + "" +place_id);
          //  gesamt_status = "Orange"; //platz orange
            // WebServiceLED.shelfLedMapping("{\"rgb\" : \"#ffa500\"}", row_id, 8-place_id);
        } else if (name_status.equals("Platz korrekt") && price_status.equals("Preis korrekt")) {
            WebServiceLED.webserviceCall("{\"rgb\" : \"#001100\"}", shelf_id + "" +row_id + "" +place_id);
          //  gesamt_status = "Grün"; //platz grün
            // WebServiceLED.shelfLedMapping("{\"rgb\" : \"#00ff00\"}", row_id, 8-place_id); 
        }
 }
    @Override
    public void onUpdateReceived(Update update) {
        Message message = update.getMessage();
        String chatMsg = "";
        if (message != null && message.hasText()) {
            String command = message.getText();
            Home home = new Home();
            switch (command){
                case "Fehlmeldung": {
                    if (message.isReply()) {
                        String text = message.getReplyToMessage().getText();
                        if (text.contains("falschen Preis")) {
                            String[] dividedText = text.split(" ");
                            DBController dbc = DBController.getInstance();
                            dbc.initDBConnection();
                            int shelf_id = Integer.parseInt(dividedText[2]);
                            int row_id = Integer.parseInt(dividedText[6]);
                            int place_id = Integer.parseInt(dividedText[9]);
                            dbc.handleUpdateDB2("status_price", shelf_id, row_id, place_id, "Preis OK");
                            //WebServiceLED.webserviceCall("{\"rgb\" : \"#001100\"}", shelf_id + "" +row_id + "" +place_id); //Farbe setzten
                            home.show_status(shelf_id, row_id, place_id);
                            sendMsg(message, "Wurde vermerkt!");
                        } else if (text.contains("Fehlplatzierung")) {
                            String[] dividedText = text.split(" ");
                            DBController dbc = DBController.getInstance();
                            dbc.initDBConnection();
                            int shelf_id = Integer.parseInt(dividedText[2]);
                            int row_id = Integer.parseInt(dividedText[6]);
                            int place_id = Integer.parseInt(dividedText[9]);
                            dbc.handleUpdateDB2("status_name", shelf_id, row_id, place_id, "Platz OK");
                            home.show_status(shelf_id, row_id, place_id);
                            sendMsg(message, "Wurde vermerkt!");
                        } else {
                            sendMsg(message, "Das ist keine Meldung!");
                        }
                    } else {
                        sendMsg(message, "Bitte verweisen Sie auf eine Meldung!");
                    }

                    break;
                }
                case "Behoben": {
                    if (message.isReply()) {
                        String text = message.getReplyToMessage().getText();
                        if (text.contains("falschen Preis")){
                            String[] dividedText = text.split(" ");
                            DBController dbc = DBController.getInstance();
                            dbc.initDBConnection();
                            int shelf_id = Integer.parseInt(dividedText[2]);
                            int row_id = Integer.parseInt(dividedText[6]);
                            int place_id = Integer.parseInt(dividedText[9]);
                            dbc.handleUpdateDB2("status_price", shelf_id, row_id, place_id, "Preis OK");
                            
                            home.show_status(shelf_id, row_id, place_id);
                           // WebServiceLED.webserviceCall("{\"rgb\" : \"#001100\"}", shelf_id + "" +row_id + "" +place_id); //Regal LED Grün setzten
                            sendMsg(message, "Wurde vermerkt!");
                        }
                        else if (text.contains("Fehlplatzierung")){
                            String[] dividedText = text.split(" ");
                            DBController dbc = DBController.getInstance();
                            dbc.initDBConnection();
                             int shelf_id = Integer.parseInt(dividedText[2]);
                            int row_id = Integer.parseInt(dividedText[6]);
                            int place_id = Integer.parseInt(dividedText[9]);
                            dbc.handleUpdateDB2("status_name", shelf_id, row_id, place_id, "Platz OK");
                            home.show_status(shelf_id, row_id, place_id);
                            //WebServiceLED.webserviceCall("{\"rgb\" : \"#001100\"}", shelf_id + "" +row_id + "" +place_id); //Farbe setzten
                            sendMsg(message, "Wurde vermerkt!");
                        }
                        else {
                            sendMsg(message, "Das ist keine Meldung!");
                        }
                    }
                    else {
                        sendMsg(message, "Bitte verweisen Sie auf eine Meldung!");
                    }
         
                    break;
                }
                case "Report": {
                    sendPhoto(message, new File("C:\\Users\\Valerij\\Desktop\\Projekt 2\\OCR\\cam1\\IMG_3789.JPG"));
                    break;
                }
                case "Preisschilder": {
                    ArrayList<String[]> result = dbc.handleGetDBBotMislabeling(); 
                    for (int i = 0; i < result.size(); i++) {
                        String[] row = result.get(i);
                        if (row[7] == null){row[7] = "Kein Preis erkannt";}
                        chatMsg = "Im Regal " + row[1] + " in der Reihe " + row[2] + " am Platz " + row[3] + " ist das Produkt " + row[4] + " " + row[5] + " mit einem falschen Preis ausgezeichent!\n Preisauszeichnung: " + row[7] + "\n Richtiger Preis: " + row[6];
                        sendMsg (message, chatMsg);
                    }
                    break;
                }
                case "Fehlplatzierungen": {
                    ArrayList<String[]> result = dbc.handleGetDBBotMisplacement();
                    for (int i = 0; i < result.size(); i++) {
                        String[] row = result.get(i);
                        if (row[8] == null && row[9] == null) {
                            row[8] = "Kein Produkt erkannt";
                            row[9] = "";
                        } else if (row[9] == null) {
                            row[9] = "";
                        }
                        chatMsg = "Im Regal " + row[1] + " in der Reihe " + row[2] + " am Platz " + row[3] + " gibt es eine Fehlplatzierung!" + "\nFalsches Produkt: " + row[8] + " " + row[9] + "\nRichtiges Produkt: " + row[4] + " " + row[5];
                        sendMsg (message, chatMsg);
                    }
                    break;
                }
                default: {
                sendMsg(message, "Das habe ich nicht verstanden!");
                break;
                } 
            }
        }
    }

    @Override
    public String getBotUsername() {
        return "OOSProjekt2Bot";
    }
    
    public void setButtons(SendMessage sendMessage){
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        sendMessage.setReplyMarkup(replyKeyboardMarkup);
        replyKeyboardMarkup.setSelective(true);
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setOneTimeKeyboard(false);
        
        List<KeyboardRow> keyboardRowList = new ArrayList();
        KeyboardRow keyboardFirstRow = new KeyboardRow();
        KeyboardRow keyboardSecondRow = new KeyboardRow();
        KeyboardRow keyboardThirdRow = new KeyboardRow();
        
        keyboardFirstRow.add(new KeyboardButton("Preisschilder"));
        
        keyboardSecondRow.add(new KeyboardButton("Fehlplatzierungen"));
        //keyboardSecondRow.add(new KeyboardButton("Leerbestände"));
        
        keyboardThirdRow.add(new KeyboardButton("Behoben"));
        //keyboardThirdRow.add(new KeyboardButton("Nicht behoben"));
        keyboardThirdRow.add(new KeyboardButton("Fehlmeldung"));
        
        
        keyboardRowList.add(keyboardFirstRow);
        keyboardRowList.add(keyboardSecondRow);
        keyboardRowList.add(keyboardThirdRow);
        replyKeyboardMarkup.setKeyboard(keyboardRowList);
    }
    private void sendMsg(Message message, String textMsg) {
        SendMessage sendMessage = new SendMessage();
        // sendMessage.enableMarkdown(true);
        sendMessage.setChatId(message.getChatId().toString());
        // sendMessage.setReplyToMessageId(message.getMessageId());
        sendMessage.setText(textMsg);
        try {
            setButtons(sendMessage);
            execute(sendMessage);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }

    }

    private void sendPhoto(Message message, File file) {
        SendPhoto sendPhoto = new SendPhoto();
        sendPhoto.setChatId(message.getChatId().toString());
        sendPhoto.setPhoto(file);
        try {
            execute(sendPhoto);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

}

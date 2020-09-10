import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.google.inject.internal.cglib.core.$LocalVariablesSorter;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Message;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.api.objects.User;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.exceptions.TelegramApiException;

public class bot extends TelegramLongPollingBot {


    public void sendMsg(Message message, String text) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.enableMarkdown(true);
        sendMessage.setChatId(message.getChatId().toString());
        sendMessage.setText(text);
        try {
            sendMessage(sendMessage);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }


    Map<Long, ArrayList<String>> map = new HashMap<Long, ArrayList<String>>();


    @Override
    public void onUpdateReceived(Update update) {
        Message message = update.getMessage();
        long id = message.getChatId();

        if (!message.getText().startsWith("/")) {
            if (!map.containsKey(id)) {
                ArrayList<String> list = new ArrayList<String>();
                list.add(message.getText());
                map.put(id, list);
            } else {
                map.get(id).add(message.getText());
            }

        }

        if (message.getText().equals("/get")) {

            for (Map.Entry<Long, ArrayList<String>> pair : map.entrySet()) {
                Long key = pair.getKey();
                ArrayList<String> value = pair.getValue();
                if (id == key) {
                    for (int i = 0; i < value.size(); i++) {
                        sendMsg(message, i + 1 + ". " + value.get(i));
                    }
                }
            }

        }

        if (message.getText().equals("/start") || message.getText().equals("/help")) {
            sendMsg(message, "/add -- add notes \n/get -- get notes\n/clear -- remove all notes\n/rem num -- remove notes number");
        }

        if (message.getText().equals("/clear")) {
            map.remove(id);
            sendMsg(message, "all notes removed");

        }

        if (message.getText().startsWith("/rem")) {
            try {
                String[] num = message.getText().split(" ");

                for (Map.Entry<Long, ArrayList<String>> pair : map.entrySet()) {
                    Long key = pair.getKey();
                    ArrayList<String> value = pair.getValue();
                    if (id == key) {
                        value.remove(Integer.parseInt(num[1]) - 1);
                        for (int i = 0; i < value.size(); i++) {
                            sendMsg(message, i + 1 + ". " + value.get(i));
                        }
                    }
                }

            } catch (Exception e) {
                sendMsg(message, "incorrect");
            }
        }
    }


    @Override
    public String getBotUsername() {
        return "ufanetTeatBot";
    }

    @Override
    public String getBotToken() {
        return "1368155300:AAGM0qVdSsrH5NBhIh1Xf2BekrfydchHUAg";
    }


}



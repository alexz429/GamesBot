package com.github.alexz429.Bot.Coup;

import java.util.ArrayList;

import org.javacord.api.entity.user.User;
import org.javacord.api.event.message.MessageCreateEvent;
import org.javacord.api.listener.message.MessageCreateListener;

import com.github.alexz429.Bot.Generics.Player;

public class CPlayer extends Player {
	ArrayList<String> cards=new ArrayList<String>();
	public CPlayer(User user) {
		super(user);
	}
	public ArrayList<String> getCards(){
		return cards;
	}
	class UserListener implements MessageCreateListener{

		@Override
		public void onMessageCreate(MessageCreateEvent event) {
			// TODO Auto-generated method stub
			if(!event.isPrivateMessage()||!(event.getMessageContent().length()>6&&event.getMessageContent().substring(0,5).equals("coup!")))return;
			String[] input=event.getMessageContent().substring(6).toLowerCase().split(" ");
			switch(input[0]) {
			case "active":
				ArrayList<Integer> args=new ArrayList<>();
				for(int count=1;count<input.length;count++) {
					try {
						args.add(Integer.parseInt(input[count]));
					}
					catch(NumberFormatException e) {}
				}
				System.out.println(args);
				break;
			
			}
			
		}
		
	}
}

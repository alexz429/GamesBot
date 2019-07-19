package com.github.alexz429.Bot.Werewolf;

import java.util.ArrayList;
import java.util.List;

import org.javacord.api.entity.user.User;
import org.javacord.api.event.message.MessageCreateEvent;
import org.javacord.api.listener.message.MessageCreateListener;

import com.github.alexz429.Bot.Generics.Game;
import com.github.alexz429.Bot.Generics.Player;

public abstract class WWPlayer extends Player{
	
	Game game;
	String role;
	String roleInfo;
	int activePhase=-1;
	UserListener ul=new UserListener();
	int faction;
	
	//game state variables
	boolean alive;
	long voteChoice=-1;
	boolean silenced;
	boolean mute;
	boolean saved;
	List<Integer> args;
	
	
	public void setArgs(List<Integer> args) {
		this.args=args;
	}
	abstract public void useAbility();
	abstract public boolean checkWin();
	abstract public void prompt();
	
	public WWPlayer(User user, Game game, String role,int faction,int activePhase, String roleInfo) {
		super(user);
		this.game=game;
		this.faction=faction;
		this.role=role;
		this.roleInfo=roleInfo;
		alive=true;
		this.activePhase=activePhase;
		silenced=false;
		mute=false;
		user.addMessageCreateListener(ul);
	}
	 public String getFaction() {
		 return faction==0? "Villager": faction==1? "Werewolf":"Neutral?";
	 }
	
	public void lynch() {
		die();
	}
	 public void kill() {
		 die();
	 }
	
	public void die() {
		alive=false;
		
		
	}
	
	class UserListener implements MessageCreateListener{

		@Override
		public void onMessageCreate(MessageCreateEvent event) {
			// TODO Auto-generated method stub
			if(!event.isPrivateMessage()||!(event.getMessageContent().length()>3&&event.getMessageContent().substring(0,3).equals("ww!")))return;
			String[] input=event.getMessageContent().substring(3).toLowerCase().split(" ");
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
				setArgs(args);
				break;
			
			}
			
		}
		
	}
	
}

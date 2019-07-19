package com.github.alexz429.Bot.Coup;

import java.util.ArrayList;
import java.util.List;

import org.javacord.api.entity.channel.TextChannel;
import org.javacord.api.entity.user.User;

import com.github.alexz429.Bot.Generics.Game;

public class CGame extends Game{
	TextChannel main;
	List<String> items=new ArrayList<>();
	ArrayList<CPlayer> players=new ArrayList<>();
	public CGame(TextChannel main,User admin, boolean isPublic) {
		super(admin, isPublic);
		this.main=main;
		// TODO Auto-generated constructor stub
	}
	public void quit() {
		// TODO Auto-generated method stub
		
	}
	public void start() {
		// TODO Auto-generated method stub
		System.out.println("one");
		for(int count=0;count<3;count++) {
			items.add("Assassin");
			items.add("Duke");
			items.add("Captain");
			items.add("Ambassador");
			items.add("Contessa");
		}
		System.out.println("two");
		
		Shuffle();
		System.out.println("three");
		
		for(User next:getUserList()) {
			CPlayer newPlayer=new CPlayer(next);
			newPlayer.getCards().add(items.remove(0));
			newPlayer.getCards().add(items.remove(0));
			players.add(newPlayer);
		}
		
		for(CPlayer next:players) {
			String build="You are: "+next.getCards().get(0)+" and "+next.getCards().get(1);
			next.send(build);
		}
	}
	public void Shuffle() {
		for(int count=0;count<items.size();count++) {
			items.add(items.remove((int)(Math.random()*items.size())));
		}
	}

}

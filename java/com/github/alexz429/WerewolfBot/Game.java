package com.github.alexz429.WerewolfBot;

import java.util.HashSet;
import java.util.List;

import org.javacord.api.DiscordApi;
import org.javacord.api.entity.channel.TextChannel;
import org.javacord.api.entity.user.User;

public class Game {
	
	TextChannel general;
	TextChannel evil;
	TextChannel dead;
	Player[] players;
	HashSet<User> userList=new HashSet<>();
	User admin;
	DiscordApi api;
	boolean isPublic;
	public Game(TextChannel g,TextChannel e,TextChannel d,User admin, boolean isPublic, DiscordApi api) {
		super();
		general=g;
		evil=e;
		dead=d;
		this.isPublic=isPublic;
		this.admin=admin;
		userList.add(admin);
		this.api=api;
	}
	public String addPlayers(List<User> input, boolean power) {
		if(power||isPublic) {
			for(User next:input)
			userList.add(next);
			return "Success";
		}
		return "Game is private. Only admin can add.";
		
		
	}
	public void removePlayers(List<User> input) {
		for(User next: input) {
			if(next!=admin)userList.remove(next);
		}
	}
	public void start() {
		int wolves=(int) (0.5*userList.size());
		boolean[] set=new boolean[userList.size()];
		User[] convert=new User[userList.size()];
		convert=userList.toArray(convert);
		int tally=0;
		while(tally<wolves) {
			int next=(int)(Math.random()*userList.size());
			if(!set[next]) {
				players[next]=new Wolf(convert[next], this);

				set[next]=true;
			}
		}
		for(int count=0;count<userList.size();count++) {
			if(players[count]==null) {
				players[count]=new Villager(convert[count], this);
			}
		}
		
	}
	
	

}

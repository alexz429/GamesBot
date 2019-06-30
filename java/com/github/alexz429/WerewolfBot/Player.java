package com.github.alexz429.WerewolfBot;

import org.javacord.api.entity.user.User;

public abstract class Player {
	User user;
	Game game;
	String role;
	String roleInfo;
	
	boolean alive;
	boolean silenced;
	boolean mute;
	int faction;
	
	abstract public boolean useAbility(Player target, int mode);
	abstract public boolean checkWin();
	
	
	public Player(User user, Game game) {
		this.user=user;
		this.game=game;
		alive=true;
		silenced=false;
		mute=false;
	}
	 public String getInfo() {
		return roleInfo;
	}
	
	 public String getFaction() {
		 return faction==0? "Villager": faction==1? "Werewolf":"Chaos";
	 }
	 public String showRole() {
		 return role;
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
	public boolean isAlive() {
		return alive;
	}
	
	
}

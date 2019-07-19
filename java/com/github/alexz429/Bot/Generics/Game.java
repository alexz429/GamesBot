package com.github.alexz429.Bot.Generics;

import java.util.HashSet;
import java.util.List;

import org.javacord.api.entity.user.User;

public abstract class Game {
	User admin;
	HashSet<User> userList = new HashSet<>();
	boolean isPublic;
	boolean inProgress = false;
	public User getAdmin() {
		return admin;
	}

	public void setAdmin(User admin) {
		this.admin = admin;
	}

	public HashSet<User> getUserList() {
		return userList;
	}

	public void setUserList(HashSet<User> userList) {
		this.userList = userList;
	}

	public boolean isPublic() {
		return isPublic;
	}

	public void setPublic(boolean isPublic) {
		this.isPublic = isPublic;
	}

	public boolean isInProgress() {
		return inProgress;
	}

	public void setInProgress(boolean inProgress) {
		this.inProgress = inProgress;
	}
	public String addPlayers(List<User> input, User requester) {
		if (requester == admin || isPublic) {
			for (User next : input)
				userList.add(next);
			return "Success";
		}
		return "Game is private. Only admin can add.";

	}

	
	
	public void removePlayers(List<User> input) {
		for (User next : input) {
			if (next != admin)
				userList.remove(next);
		}
	}
	
	
	public Game(User admin,boolean isPublic) {
		this.isPublic = isPublic;
		this.admin = admin;
		userList.add(admin);

	}
	
	public abstract void quit();
	public abstract void start();

}

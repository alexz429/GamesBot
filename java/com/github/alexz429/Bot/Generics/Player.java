package com.github.alexz429.Bot.Generics;

import org.javacord.api.entity.user.User;

public class Player {
	User user;
	public Player(User user) {
		this.user=user;
	}
	public void send(String message) {
		user.sendMessage(message);
	}
}

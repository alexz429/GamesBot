package com.github.alexz429.WerewolfBot;

import org.javacord.api.entity.user.User;

public class Wolf extends Player {

	public Wolf(User user, Game game) {
		super(user, game);
		role="Werewolf";
		roleInfo="Active: along with the other wolves, kill another person at night";
		
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean useAbility(Player target, int mode) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean checkWin() {
		// TODO Auto-generated method stub
		
		return false;
	}

	

	
	
}

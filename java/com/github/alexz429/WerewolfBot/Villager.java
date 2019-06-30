package com.github.alexz429.WerewolfBot;

import org.javacord.api.entity.user.User;

public class Villager extends Player{

	public Villager(User user, Game game) {
		super(user, game);
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

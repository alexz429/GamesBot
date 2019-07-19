package com.github.alexz429.Bot.Werewolf;

import org.javacord.api.entity.user.User;

import com.github.alexz429.Bot.Generics.Game;

public class Villager extends Player{

	
	public Villager(User user, Game game) {
		super(user, game, "Villager",0,0, "A general member of the village, he/she must figure out who the werewolves are and vote with the rest of the village in an attempt to eliminate them\n"
				+ "Ability: ahahahahahah");
		// TODO Auto-generated constructor stub
	}

	@Override
	public void useAbility() {
		// TODO Auto-generated method stub
		
	}
	@Override
	public boolean checkWin() {
		// TODO Auto-generated method stub
		return game.evilCount==0;
	}

	@Override
	public void prompt() {
		// TODO Auto-generated method stub
	}
}

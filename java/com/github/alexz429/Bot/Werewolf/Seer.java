package com.github.alexz429.Bot.Werewolf;

import java.util.HashSet;

import org.javacord.api.entity.user.User;

import com.github.alexz429.Bot.Generics.Game;

public class Seer extends Player{

	HashSet<Integer > allowedChoices=new HashSet<>();
	public Seer(User user, Game game) {
		super(user, game, "Seer", 0, 3,"" );
		// TODO Auto-generated constructor stub
	}

	@Override
	public void useAbility() {
		// TODO Auto-generated method stub
		if(args.size()>0&&allowedChoices.contains(args.get(0))) {
			send( game.indexToPlayer[args.get(0)].getFaction());
		}
	}

	@Override
	public boolean checkWin() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void prompt() {
		allowedChoices=new HashSet<>();
		String build="";
		for(int count=0;count<game.indexToPlayer.length;count++) {
			Player next=game.indexToPlayer[count];
			if(next.alive) {
				allowedChoices.add(count);
				build+=count+": "+next.user.getDiscriminatedName()+"\n";
			}
		}
		send("Choose who to see with the NUMBER beside each player in this channel.\n"+build);
	}

}

package com.github.alexz429.Bot.Werewolf;

import java.util.HashSet;

import org.javacord.api.entity.user.User;

import com.github.alexz429.Bot.Generics.Game;

public class Wolf extends Player {
	HashSet<Integer > allowedChoices=new HashSet<>();
	
	public Wolf(User user, Game game) {
		super(user, game, "werewolf",1,3,"");
	}
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
		send("Vote on who to kill with the NUMBER beside each player in this channel.\n"+build);
	}
	@Override
	public void useAbility() {
		if(args.size()>0&&allowedChoices.contains(args.get(0))) {
			game.wolfVotes[args.get(0)]++;
		}
	}

	@Override
	public boolean checkWin() {
		return game.evilCount>=(game.alive+1)/2;
	}
	public String toString() {
		return super.toString();
	}
	

	
	
}

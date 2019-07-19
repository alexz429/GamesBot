package com.github.alexz429.Bot.Werewolf;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;

import org.javacord.api.entity.channel.TextChannel;
import org.javacord.api.entity.message.Message;
import org.javacord.api.entity.user.User;
import org.javacord.api.event.message.MessageCreateEvent;
import org.javacord.api.listener.message.MessageCreateListener;

import com.github.alexz429.Bot.Generics.Game;
import com.github.alexz429.Bot.Generics.Lib;

public class WWGame extends Game{
HashSet<Player> pendingDeath=new HashSet<>();
	
	TextChannel general;
	TextChannel evil;
	TextChannel dead;
	HashMap<Long, Player> idToPlayer = new HashMap<>();
	Player[] indexToPlayer;
	
	GameListener activeListener;
	boolean skip = false;
	int phase = -1;// 0:day 1:trials 2: interrupters night 3:active role's night 4: reactive role's night

	String help = "```\n" + "Useful commands: \n"
			+ "!roleInfo: sends you a brief overview of your role's abilities and lore. This won't be visible to anybody else.\n"
			+ "```";

	// gameState variables
	Queue<String> events=new LinkedList<>();
	int dayNumber = 0;
	int evilCount = 0;
	int neutralCount = 0;
	int goodCount = 0;
	int died = 0;
	int alive = 0;
	int[] wolfVotes;
	
	// only used during game setup
	
	class GameListener implements MessageCreateListener {

		@Override
		public void onMessageCreate(MessageCreateEvent event) {

			// TODO Auto-generated method stub
			Message message = event.getMessage();
			User author = message.getUserAuthor().get();

			if (!idToPlayer.containsKey(author.getId()))
				return;
			Player target = idToPlayer.get(author.getId());
			if (message.getContent().length()>3&&message.getContent().substring(0, 3).equals("ww!")) {

				String[] data = message.getContent().substring(3).toLowerCase().split(" ");
				if (author == admin) {
					switch (data[0]) {
					case "skip":
						skip = true;
					}
				}
				switch (data[0]) {
				case "nom":
					if (phase !=0)
						return;
					if (message.getMentionedUsers().size() > 0) {
						Long id=message.getMentionedUsers().get(0).getId();
						if(!idToPlayer.containsKey(id))return;
						Player voteTarget=idToPlayer.get(id);
						if(!voteTarget.alive)return;
						target.voteChoice = message.getMentionedUsers().get(0).getId();
						Lib.reply(message, "Success");
					}
					break;
				case "vote":
					if (phase != 1)
						return;
					if (message.getMentionedUsers().size() > 0) {
						target.voteChoice = message.getMentionedUsers().get(0).getId();
					}
					break;
				}
			}
		}

	}

	public WWGame(TextChannel g, TextChannel e, TextChannel d, User admin, boolean isPublic) {
		super(admin,isPublic);
		general = g;
		evil = e;
		dead = d;
		
	}

	

	public void start() {
		inProgress = true;
		int wolves = (int) (1 * userList.size());
		boolean[] set = new boolean[userList.size()];
		User[] convert = new User[userList.size()];
		wolfVotes=new int[userList.size()];
		convert = userList.toArray(convert);
		int tally = 0;
		while (tally < wolves) {
			int next = (int) (Math.random() * userList.size());
			System.out.println(next);
			if (!idToPlayer.containsKey(convert[next].getId())) {
				idToPlayer.put(convert[next].getId(), new Wolf(convert[next], this));
				tally++;
				set[next] = true;
			}
		}
		for (User next : convert) {
			if (!idToPlayer.containsKey(next.getId())) {
				idToPlayer.put(next.getId(), new Villager(next, this));
			}
		}
		indexToPlayer = new Player[idToPlayer.size()];
		int index = 0;
		for (Player next : idToPlayer.values()) {
			indexToPlayer[index++] = next;
			System.out.println(next);
		}
		activeListener = new GameListener();
		general.addMessageCreateListener(activeListener);
		new runGame().start();
	}

	public class runGame extends Thread {
		public void wait(int seconds) {
			int tally = 0;
			while (!skip && tally++ < seconds) {
				try {
					Thread.sleep(1000);
				} catch (Exception e) {

				}
			}
			skip = false;

		}
		public void debugAll() {
			for(Player next:indexToPlayer) {
				System.out.println(next.toString());
			}
		}
		public void run() {
			// inform everybody of their roles
			
			for (Player next : idToPlayer.values()) {
				next.send("You are a " + next.role);
				next.send(help);
			}
			wait(15);
			// start the days
			while (true) {

				alertDay();
				phase=0;
				dayNumber++;
				// each day is 2 min long
				debugAll();
				wait(90);
				// warning
				general.sendMessage("30 seconds left");
				wait(30);
				evaluateDay();
				general.sendMessage("Moving into night!");
				reset();
				// evaluation code for trials

				// each night is 1.5 min long
				//interrupters
				debugAll();
				

				System.out.println("PHASE 2");
				phase=2;
				for(Player next:indexToPlayer) {
					if(next.activePhase==phase) {
						next.prompt();
					}
				}
				wait(30);
				evaluateNight();
				
				System.out.println("PHASE 3");
				//actives
				phase=3;
				for(Player next:indexToPlayer) {
					if(next.activePhase==phase) {
						next.prompt();
					}
				}
				wait(30);
				evaluateNight();
				
				
				//reactives

				System.out.println("PHASE 4");
				phase=4;
				for(Player next:indexToPlayer) {
					if(next.activePhase==phase) {
						next.prompt();
					}
				}
				wait(30);
				evaluateNight();
				
				
				reset();
				// evaluation code for all actions at night
				for(Player next:pendingDeath) {
					next.kill();
					events.add(next.user.getDiscriminatedName()+" has died!");
				}
			}
		}

		public void reset() {
			for (Player next : idToPlayer.values()) {
				next.voteChoice = -1;
				next.mute = false;
				next.silenced = false;
				next.args=new ArrayList<>();
				next.saved=false;
			}
			Arrays.fill(wolfVotes, 0);
		}

		public void evaluateDay() {
			HashMap<Long, Integer> voteCount = new HashMap<>();
			HashSet<Long> nominated = new HashSet<>();
			for (Player next : indexToPlayer) {
				if (next.voteChoice != -1) {
					voteCount.put(next.voteChoice,
							voteCount.containsKey(next.voteChoice) ? voteCount.get(next.voteChoice) + 1 : 1);
					if (voteCount.get(next.voteChoice) >= 2) {
						nominated.add(next.voteChoice);
					}
				}
				next.voteChoice = -1;
			}
			if (nominated.size() == 0) {
				general.sendMessage("Nobody has been nominated!");
				return;
			}
			String reply = "People have been nominated! !vote and mention to lynch the following:\n";
			for (long next : nominated) {
				reply += idToPlayer.get(next).user.getDiscriminatedName() + "\n";
			}
			phase=1;
			reply += "or do nothing to abstain\n";
			general.sendMessage(reply);
			wait(15);
			voteCount = new HashMap<>();
			for (Player next : indexToPlayer) {
				if (next.voteChoice != -1) {
					voteCount.put(next.voteChoice,
							voteCount.containsKey(next.voteChoice) ? voteCount.get(next.voteChoice) + 1 : 1);
					if (voteCount.get(next.voteChoice) >= alive / 2) {
						general.sendMessage(
								idToPlayer.get(next.voteChoice).user.getDiscriminatedName() + " has been Lynched!");

					}
				}
			}
			general.sendMessage("Nobody has been outvoted!");
		}

		public void Lynch(Player target) {
			general.sendMessage(target.user.getMentionTag() + ", you have 10 seconds to write your last words.");
			wait(10);
			target.lynch();
			general.sendMessage(target.user.getDiscriminatedName() + " has died...");
		}
		
		public void evaluateNight() {
			for(Player next:indexToPlayer) {
				if(next.activePhase==phase)
				next.useAbility();
			}
			int target=-1;
			int best=0;
			for(int count=0;count<wolfVotes.length;count++) {
				if(best<wolfVotes[count]) {
					target=count;
					best=wolfVotes[count];
				}
			}
			if(target!=-1) {
				pendingDeath.add(indexToPlayer[target]);
			}
			
			
		}

		public void alertDay() {
			String build="It is now day "+dayNumber+"!\n";
			while(!events.isEmpty()) {
				build+=events.poll()+"\n";
			}
			general.sendMessage(build);
		}



	}
}

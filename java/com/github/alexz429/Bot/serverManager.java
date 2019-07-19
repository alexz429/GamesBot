package com.github.alexz429.Bot;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import org.javacord.api.entity.channel.ServerTextChannel;
import org.javacord.api.entity.channel.TextChannel;
import org.javacord.api.entity.message.Message;
import org.javacord.api.entity.server.Server;
import org.javacord.api.entity.user.User;
import org.javacord.api.event.message.MessageCreateEvent;
import org.javacord.api.listener.message.MessageCreateListener;

import com.github.alexz429.Bot.Coup.CGame;
import com.github.alexz429.Bot.Generics.Game;
import com.github.alexz429.Bot.Generics.Lib;

public class serverManager {
	// TODO: refactor code to allow channel based listeners
	//
	serverListener sl=new serverListener();
	 HashMap<Long, Game> activeGames = new HashMap<>();
	 HashSet<Long> occupied = new HashSet<>();
	 TextChannel mainChannel;
	 Server server;
	 TextChannel[] werewolfDefaults=new TextChannel[3];
	 TextChannel coupDefault=null;
	 String help = "```\n!setup #gameChannel #wolfChannel #deadChannel: starts a game with you as admin and the three channels.\n"
			+ "!cancel: cancels your game.\n"
			+ "!invite (-P) @person @person2....: adds mentioned people into your game.\n"
			+ "\tOptionals: -P sets game to public\n" + "!join: joins currently active game (if public).\n"
			+ "!kick: kicks player from game\n" + "!makePublic: sets the game to public\n"
			+ "!makePrivate: sets the game to private\n"
			+ "!start: starts your current game\n"
			+ "!quit: quits your game mid setup or mid game"
			+ "```";
	 class serverListener implements MessageCreateListener {

		@Override
		public void onMessageCreate(MessageCreateEvent event) {
			// TODO Auto-generated method stub
			Message message = event.getMessage();
			TextChannel from = message.getChannel();
			User author = message.getUserAuthor().get();
			long id = author.getId();
			if (occupied.contains(id))
				return;
			if (message.getContent().length()>3&&message.getContent().substring(0,3).equals("gb!")) {
				String[] data = message.getContent().substring(4).toLowerCase().split(" ");
				switch (data[0]) {
				case "pong":
					Lib.reply(message, "ping!");
					break;
				case "quit":
					if (activeGames.containsKey(id)) {
						activeGames.get(id).quit();
						activeGames.remove(id);
					} else {
						Lib.reply(message, "You are not admin of a game");
					}
				case "makeprivate":
					if (activeGames.containsKey(id)) {
						activeGames.get(id).setPublic(false);
					}
					break;
				case "makepublic":
					if (activeGames.containsKey(id)) {
						activeGames.get(id).setPublic(true);
					}
					break;
				case "help":
					Lib.reply(message, help);
					break;
				case "cancel":
					activeGames.remove(id);
					break;
				case "setup":
					if (activeGames.containsKey(id)) {
						Lib.reply(message, "You are already running a game");
						return;
					}
					if(data.length<2) {
						Lib.reply(message, "The available games are: coup");
					}
					switch(data[1]) {
					
					case "werewolf":
						Lib.reply(message, "Work in progress, come back later!");
						return;
						/*
						List<ServerTextChannel> channels = message.getMentionedChannels();
						if (channels.size() < 3) {
							if(channels.size()==0) {
								activeGames.put(id, new WWGame(werewolfDefaults[0],werewolfDefaults[1], werewolfDefaults[2], author,
										data.length >= 2 && data[1].equals("-p") ? true : false));
								Lib.reply(message, "Game ready! Waiting for players.");
								return;
							}
							Lib.reply(message, "invalid setup");
							return;
						}
						activeGames.put(id, new WWGame(channels.get(0), channels.get(1), channels.get(2), author,
								data.length >= 2 && data[1].equals("-p") ? true : false));
						Lib.reply(message, "Game ready! Waiting for players.");
						
						break;
						*/
					case "coup":
						activeGames.put(id, new CGame(coupDefault,author,data.length>=3&&data[3].equals("-p")?true:false));
						Lib.reply(message,"success!");
						break;
				default:
					Lib.reply(message, "The available games are: coup");
					}
					
					

					break;
				case "start":
					if (activeGames.containsKey(id)) {
						for (User next : activeGames.get(id).getUserList()) {
							occupied.add(next.getId());
						}
						activeGames.get(id).start();

					}
					break;
				case "invite":
					if (activeGames.containsKey(id)) {
						Lib.reply(message, activeGames.get(id).addPlayers(message.getMentionedUsers(), author));
					}

					break;
				case "join":
					if (message.getMentionedUsers().size() > 0) {
						long targetId = message.getMentionedUsers().get(0).getId();
						if (activeGames.containsKey(targetId)) {
							Lib.reply(message, activeGames.get(targetId)
									.addPlayers(new ArrayList<User>(Arrays.asList(author)), author));
						} else {
							Lib.reply(message, "User is not admin for a game");
						}
					} else {
						Lib.reply(message, "Please @ the game admin");
					}

					break;
				case "kick":
					if (activeGames.containsKey(id)){
						activeGames.get(id).removePlayers(message.getMentionedUsers());
						Lib.reply(message, "Kick Successful");
					}
					break;
				case "players":
					if (activeGames.containsKey(id)) {
						String output = "\n";
						for (User next : activeGames.get(id).getUserList()) {
							output += next.getDiscriminatedName() + "\n";
						}
						Lib.reply(message, output);
					}
					else {
						Lib.reply(message, "No game present");
					}
					break;
				case "lmao":
					Lib.reply(message, "Justin go kys");
					break;
				}
			}
		}
		 
	 }
	 public serverManager(TextChannel[] config) {
		mainChannel=config[0];
		for(int count=1;count<=3;count++) {
			werewolfDefaults[count-1]=config[count];
		}
		coupDefault=config[4];
		mainChannel.addMessageCreateListener(sl);
		System.out.println("something is up!");
	}
	 public void quit() {
		 mainChannel.removeTextChannelAttachableListener(sl);
	 }
	 public void switchChannel(List<ServerTextChannel> alts) {
		 if(alts.size()==0)return;
		 TextChannel other=alts.get(0);
		 mainChannel.removeTextChannelAttachableListener(sl);
		mainChannel=other;
		 mainChannel.addMessageCreateListener(sl);
		 for(int count=1;count<alts.size();count++) {
			 werewolfDefaults[count-1]=alts.get(count);
		 }
	 }

}

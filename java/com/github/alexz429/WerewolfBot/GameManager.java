package com.github.alexz429.WerewolfBot;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.javacord.api.DiscordApi;
import org.javacord.api.DiscordApiBuilder;
import org.javacord.api.entity.channel.ServerTextChannel;
import org.javacord.api.entity.channel.TextChannel;
import org.javacord.api.entity.message.Message;
import org.javacord.api.entity.user.User;

public class GameManager {

	static int phase = -1;// -1: no game. 0: standby.
	
	static Game curGame=null;
	static String help="```\n!setup #gameChannel #wolfChannel #deadChannel: starts a game with you as admin and the three channels.\n"
			+ "!cancel: cancels your game.\n"
			+ "!invite (-P) @person @person2....: adds mentioned people into your game.\n"
			+ "\tOptionals: -P sets game to public\n"
			+ "!join: joins currently active game (if public).\n"
			+ "!kick: kicks player from game\n"
			+ "!makePublic: sets the game to public\n"
			+ "!makePrivate: sets the game to private\n"
			+ "```";
	public static void main(String[] args) throws IOException{
		// TODO Auto-generated method stub
		BufferedReader br=new BufferedReader(new FileReader("C:\\Users\\alex.zhang\\WorkspaceMaster\\WerewolfBot\\src\\main\\java\\com\\github\\alexz429\\WerewolfBot\\Token"));
		DiscordApi api = new DiscordApiBuilder().setToken(br.readLine())
				.login().join();
		br.close();
		api.addMessageCreateListener(event -> {
			Message message = event.getMessage();
			TextChannel from = message.getChannel();
			User author = message.getUserAuthor().get();
			if (message.getContent().charAt(0) == '!') {
				
				String[] data = message.getContent().substring(1).toLowerCase().split(" ");
				switch (data[0]) {
				case "makeprivate":
					if(curGame!=null&&curGame.admin==author) {
						curGame.isPublic=false;
					}
					break;
				case "makepublic":
					if(curGame!=null&&curGame.admin==author) {
						curGame.isPublic=true;
					}
					break;
				case "help":
					Lib.reply(message, help);
					break;
				case "cancel":
					if(curGame!=null&&curGame.admin==author) {
						curGame=null;
					}
					break;
				case "setup":
					if (curGame != null) {
						Lib.reply(message, "Game already going. Admin is " + curGame.admin.getDiscriminatedName());
					} else {
						List<ServerTextChannel> channels=message.getMentionedChannels();
						if(channels.size()<3) {
							Lib.reply(message, "invalid setup");
							return;
						}
						curGame=new Game(channels.get(0), channels.get(1), channels.get(2), author,data.length>=2&&data[1].equals("-p")?true:false, api);
						Lib.reply(message, "Game ready! Waiting for players.");
					}
					break;
				case "start":
					curGame.start();
					
					break;
				case "invite":
					Lib.reply(message, curGame.addPlayers(message.getMentionedUsers(),author==curGame.admin));
					break;
				case "join":
					if(curGame!=null) {
						Lib.reply(message, curGame.addPlayers(new ArrayList<User>(Arrays.asList(author)), false));
					}
					else {
						Lib.reply(message, "No game available at the time");
					}
					break;
				case "kick":
					if(curGame!=null&&author==curGame.admin)
					curGame.removePlayers(message.getMentionedUsers());
					break;
				case "players":
					if(curGame!=null&&curGame.admin==author)
					for(User next:curGame.userList) {
						curGame.admin.sendMessage(next.getDiscriminatedName());
					}
					break;
				}
			}

		});

		System.out.println("Logged in!");
	}

}

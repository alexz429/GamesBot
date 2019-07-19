package com.github.alexz429.Bot;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;

import org.javacord.api.DiscordApi;
import org.javacord.api.DiscordApiBuilder;
import org.javacord.api.entity.channel.ServerTextChannel;
import org.javacord.api.entity.channel.TextChannel;
import org.javacord.api.entity.message.Message;
import org.javacord.api.entity.server.Server;
import org.javacord.api.entity.user.User;

import com.github.alexz429.Bot.Generics.Lib;

public class MasterManager {
	//TODO: write debug output containing all the game states;
	static DiscordApi api;
	static HashMap<Long, serverManager> Servers=new HashMap<>();
	public static void main(String[] args) throws Exception{
		BufferedReader br = new BufferedReader(new FileReader(
				"C:\\Users\\alex.zhang\\WorkspaceMaster\\Bot\\src\\main\\resources\\Token.wwb"));
		 api = new DiscordApiBuilder().setToken(br.readLine()).login().join();
		System.out.println("Connected");
		BufferedReader settings=new BufferedReader(new FileReader("C:\\Users\\alex.zhang\\WorkspaceMaster\\Bot\\src\\main\\resources\\ServerSettings.wwb"));
		String nextLine=settings.readLine();
//		System.out.println(nextLine);
		while(!nextLine.equals("end")) {
//			System.out.println(nextLine);
			String[] inputs=nextLine.split(" ");
			TextChannel[] config=new TextChannel[5];
			for(int count=1;count<inputs.length;count++) {
				config[count-1]=api.getTextChannelById(Long.parseLong(inputs[count])).get();
			}
			Servers.put(Long.parseLong(inputs[0]), new serverManager(config));
			nextLine=settings.readLine();
		}
		
		for(Server next:api.getServers()) {
			if(!Servers.containsKey(next.getId())) {
				next.getOwner().sendMessage("It appears you have not set up your server "+next.getName()+" yet. Please !init and add your main and default channels before starting a werewolf game" );
			}
		}
		br.close();
		settings.close();
		api.addMessageCreateListener(event->{
			User author=event.getMessage().getUserAuthor().get();
			
			Message message=event.getMessage();
			if(message.isServerMessage()&&message.getContent().length()>3&&message.getContent().substring(0,3).equals("gb!")) {
				
				Server target=event.getServer().get();
				if(!target.isAdmin(author)) {
					return;
				}
				String[] input=message.getContent().substring(4).toLowerCase().split(" ");
				System.out.println("test"+input[0]);
				switch(input[0]) {
				case "init":
					
					List<ServerTextChannel> channels=message.getMentionedChannels();
					if(channels.size()!=5) {
						Lib.reply(message, "You need to set a main channel, 3 werewolf channels, and a coup channel");
						return;
					}
					TextChannel[] config=new TextChannel[5];
					channels.toArray(config);
					
					Servers.put(target.getId(), new serverManager(config));
					Lib.reply(message, "Success!");
					try {
						refresh();
					} catch (Exception e) {
						e.printStackTrace();
					}
					break;
				case "config":
					List<ServerTextChannel> inputs=message.getMentionedChannels();
					Servers.get(target.getId()).switchChannel(inputs);
				}
			}
		});
	}
	static void refresh() throws Exception{
		PrintWriter pw=new PrintWriter(new FileOutputStream("C:\\Users\\alex.zhang\\WorkspaceMaster\\Bot\\src\\main\\resources\\ServerSettings.wwb", false));
		for(Long next:Servers.keySet()) {
			serverManager sm=Servers.get(next);
			pw.println(next+" "+sm.mainChannel.getId()+" "+sm.werewolfDefaults[0].getId()+" "+sm.werewolfDefaults[1].getId()+" "+sm.werewolfDefaults[2].getId()+" "+sm.coupDefault.getId());
		}
		pw.print("end");
		pw.close();
	}
}

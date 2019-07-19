package com.github.alexz429.Bot.Generics;

import org.javacord.api.entity.message.Message;

public class Lib {
	public static void reply(Message m, String out) {
		m.getChannel().sendMessage(m.getUserAuthor().get().getMentionTag()+" "+out);
	}
}

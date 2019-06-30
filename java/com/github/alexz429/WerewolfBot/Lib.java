package com.github.alexz429.WerewolfBot;

import org.javacord.api.entity.message.Message;

public class Lib {
	static void reply(Message m, String out) {
		m.getChannel().sendMessage(m.getUserAuthor().get().getMentionTag()+" "+out);
	}
}

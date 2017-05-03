/*
 * Copyright 2016 LINE Corporation
 *
 * LINE Corporation licenses this file to you under the Apache License,
 * version 2.0 (the "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at:
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations
 * under the License.
 */

package com.example.bot.spring.echo;

import static java.util.Collections.singletonList;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.linecorp.bot.client.LineMessagingService;
import com.linecorp.bot.model.ReplyMessage;
import com.linecorp.bot.model.action.Action;
import com.linecorp.bot.model.action.MessageAction;
import com.linecorp.bot.model.event.Event;
import com.linecorp.bot.model.event.MessageEvent;
import com.linecorp.bot.model.event.message.TextMessageContent;
import com.linecorp.bot.model.message.TemplateMessage;
import com.linecorp.bot.model.message.TextMessage;
import com.linecorp.bot.model.message.template.ButtonsTemplate;
import com.linecorp.bot.model.message.template.ConfirmTemplate;
import com.linecorp.bot.model.message.template.Template;
import com.linecorp.bot.model.response.BotApiResponse;
import com.linecorp.bot.spring.boot.annotation.EventMapping;
import com.linecorp.bot.spring.boot.annotation.LineMessageHandler;

@SpringBootApplication
@LineMessageHandler
public class EchoApplication {
	@Autowired
    private static LineMessagingService lineMessagingService;
	private static HashMap<String,Object> userMap;
    public static void main(String[] args) {
        SpringApplication.run(EchoApplication.class, args);
        userMap = new HashMap<String,Object>();
        System.out.println("加入HashMap");
    }

    @EventMapping
    public Object handleTextMessageEvent(MessageEvent<TextMessageContent> event) {
    	System.out.println("event: " + event);
    	String msg = event.getMessage().getText();
    	if(msg.indexOf("#help")>=0){
    		List<Action> ac = new ArrayList<Action>();
        	Action a1 = new MessageAction("yes", "yes~");
        	Action a2 = new MessageAction("no", "no~");
        	ac.add(a1);
        	ac.add(a2);
        	TemplateMessage nb = new TemplateMessage(msg, new ConfirmTemplate("你好?", ac));
        	return nb;
    	}
    	
    	TextMessage tm = new TextMessage(msg+"!@#$%");
    	return tm;
    }
    
    
    @EventMapping
    public void handleDefaultMessageEvent(Event event) {
        System.out.println("event: " + event);
    }
}

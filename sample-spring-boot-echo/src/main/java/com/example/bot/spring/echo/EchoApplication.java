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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.linecorp.bot.model.event.Event;
import com.linecorp.bot.model.event.MessageEvent;
import com.linecorp.bot.model.event.message.TextMessageContent;
import com.linecorp.bot.model.message.TextMessage;
import com.linecorp.bot.spring.boot.annotation.EventMapping;
import com.linecorp.bot.spring.boot.annotation.LineMessageHandler;

@SpringBootApplication
@LineMessageHandler
public class EchoApplication {
	private static HashMap<String,List<String>> eatMap;
	private static HashMap<String,List<String>> foodMap;
	private static HashMap<String, String> addFood;
    public static void main(String[] args) {
        SpringApplication.run(EchoApplication.class, args);
        eatMap = new HashMap<String, List<String>>();
        foodMap = new HashMap<String, List<String>>();
        System.out.println("加入HashMap");
    }

    @EventMapping
    public TextMessage handleTextMessageEvent(MessageEvent<TextMessageContent> event) {
    	System.out.println("event: " + event);
    	String usrid = event.getSource().getSenderId();
    	List<String> eatList = (ArrayList<String>)eatMap.get(usrid);
    	List<String> foodList = (ArrayList<String>)foodMap.get(usrid);
    	String needAddFood = "N";
    	if(addFood.get(usrid)==null){
    		addFood.put(usrid, needAddFood);
    	}else{
    		needAddFood = addFood.get(usrid);
    	}
    	if(eatList==null)eatMap.put(usrid, new ArrayList<String>());
    	if(foodList==null)foodMap.put(usrid, new ArrayList<String>());
    	System.out.println("needAddFood="+needAddFood);
        String msg = event.getMessage().getText();
        if(msg.indexOf("吃什麼")>=0){
        	//msg = "吃大便💩💩💩💩💩💩💩💩💩";
        	foodList =  (List<String>)foodMap.get(usrid);
        	System.out.println("foodList="+foodList);
        	if(foodList.size()<=0){
        		msg = "你附近有什麼可以吃(請用\",\"分隔)";
        		addFood.put(usrid, "Y");
        	}else{
        		
        	}
        }
        if("Y".equals(needAddFood)){
        	String[] foods = msg.split(",");
        	for(String x : foods){
        		foodList.add(x);
        	}
        	foodMap.put(usrid, foodList);
        }
        return new TextMessage(msg+"~姆咪姆咪~");
    }

    @EventMapping
    public void handleDefaultMessageEvent(Event event) {
        System.out.println("event: " + event);
    }
}

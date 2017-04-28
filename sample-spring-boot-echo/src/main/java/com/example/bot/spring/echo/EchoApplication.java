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
import java.util.Random;

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
	//private static HashMap eatMap;//<String,List<String>>
	//private static HashMap foodMap;//<String,List<String>>
	//private static HashMap addFood;//<String, String>
	private static HashMap<String,Object> userMap;
    public static void main(String[] args) {
        SpringApplication.run(EchoApplication.class, args);
        //eatMap = new HashMap();//<String, List<String>>
        //foodMap = new HashMap();//<String, List<String>>
        //addFood = new HashMap();
        userMap = new HashMap<String,Object>();
        System.out.println("加入HashMap");
    }

    @EventMapping
    public TextMessage handleTextMessageEvent(MessageEvent<TextMessageContent> event) {
    	System.out.println("event: " + event);
    	String usrid = event.getSource().getSenderId();
    	String msg = event.getMessage().getText();
    	/**取得吃什麼資訊並初始化**/
    	@SuppressWarnings("unchecked")
		HashMap<String,Object> myMap = (HashMap<String,Object>)userMap.get(usrid);
    	if(myMap==null){
    		myMap = new HashMap<String,Object>();
    		myMap.put("eatMap", null);
    		myMap.put("foodMap", null);
    		myMap.put("addFood", "N");
    		myMap.put("delFood", "N");
    		myMap.put("afterAdd", "N");
    		userMap.put(usrid, myMap);
    	}
    	System.out.println("start_myMap="+myMap);
		@SuppressWarnings("unchecked")
		List<String> eatList = (List<String>)myMap.get("eatMap");
		@SuppressWarnings("unchecked")
		List<String> foodList = (List<String>)myMap.get("foodMap");
    	String needAddFood = (String)myMap.get("addFood");
    	String needDelFood = (String)myMap.get("delFood");
    	String needAfterAdd = (String)myMap.get("afterAdd");
    	System.out.println("needAddFood="+needAddFood);
    	System.out.println("needDelFood="+needDelFood);
    	System.out.println("needAfterAdd="+needAfterAdd);
    	
    	if(eatList==null)myMap.put("eatMap", new ArrayList<String>());
    	if(foodList==null)myMap.put("foodMap", new ArrayList<String>());
    	
    	
        /**吃什麼???**/
        if(msg.indexOf("吃什麼")>=0){
        	//msg = "吃大便💩💩💩💩💩💩💩💩💩";
        	System.out.println("foodList="+foodList);
        	if(foodList.size()<=0){
        		msg = "你附近有什麼可以吃(請用\"、\"分隔)";
        		myMap.put("addFood", "Y");
        		myMap.put("afterAdd","Y");
        	}else{
        		msg = randomFoodStr(foodList);
        	}
        }
        if(msg.indexOf("#add")>=0){
        	msg = "請輸入想吃的食物清單(請用\"、\"分隔)";
        	myMap.put("addFood", "Y");
        }
        /**加入想吃的清單**/
        if("Y".equals(needAddFood)&&"N".equals(needDelFood)){
        	String[] foods = msg.split("、");
        	for(String x : foods){
        		foodList.add(x);
        	}
        	myMap.put("foodMap",foodList);
        	myMap.put("addFood", "N");
        	msg = randomFoodStr(foodList);
        }
        
        System.out.println("end_myMap="+myMap);
        userMap.put(usrid, myMap);
        return new TextMessage(msg+"~姆咪姆咪~");
    }
    private String randomFoodStr(List<String> fds){
    	String msg = "";
    	Random ran = new Random();
		int foodsize = fds.size();
		int rx = ran.nextInt(foodsize);
		msg = "那就吃個"+fds.get(rx);
		return msg;
    }
    @EventMapping
    public void handleDefaultMessageEvent(Event event) {
        System.out.println("event: " + event);
    }
}

package fr.utc.sr03.chat.websocket;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import javax.websocket.server.ServerEndpointConfig;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Component
@ServerEndpoint(value = "/webSocket/{canalid}/{userid}")
public class WebSocketServer {
    private static final Map<Long, Set<Session>> canals = new ConcurrentHashMap();
    @OnOpen
    public void connect(@PathParam("canalid") Long canalid, Session session,@PathParam("userid") String userid) throws Exception {
        // 将session按照房间名来存储，将各个房间的用户隔离
        if (!canals.containsKey(canalid)) {
            // 创建房间不存在时，创建房间
            Set<Session> canal = new HashSet<>();
            // 添加用户
            canal.add(session);
            canals.put(canalid, canal);
        } else {
            // 房间已存在，直接添加用户到相应的房间
            canals.get(canalid).add(session);
        }
        System.out.println("canalid:"+canalid);
        System.out.println(userid+" has connected!");
        broadcast(canalid,"======================");
        broadcast(canalid,userid+" has connected!");
        broadcast(canalid,"======================");
    }
    @OnClose
    public void disConnect(@PathParam("canalid") Long canalid, Session session,@PathParam("userid") String userid) throws Exception {

        canals.get(canalid).remove(session);
        System.out.println(userid +" has disconnected!");
        broadcast(canalid,"=======================");
        broadcast(canalid,userid+" has disconnected!");
        broadcast(canalid,"=======================");
    }
    @OnMessage
    public void receiveMsg(@PathParam("canalid") Long canalid,
                           String msg, Session session,@PathParam("userid") String userid) throws Exception {
        // 此处应该有html过滤
        msg = userid + ":" + msg;
        System.out.println(msg);
        // 接收到信息后进行广播
        broadcast(canalid, msg);
    }
    public static void broadcast(Long canalid, String msg) throws Exception {
        for (Session session : canals.get(canalid)) {
            session.getBasicRemote().sendText(msg);
        }
    }
}
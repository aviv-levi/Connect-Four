/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package con_4;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;
import javax.json.*;
import javax.websocket.EncodeException;

/**
 *
 * @author The Levi
 */
@ServerEndpoint("/endpoint")
public class Presenter {
    
    
    Model.Player user,computer;
    Model model;
    
    @OnMessage
    public void onMessage(String message,Session session) {
        
        int col = Integer.valueOf(message);
        if (model.isLegal(col)) {
            int row = model.doMove(col, user);
            JsonObjectBuilder updateBuilder = Json.createObjectBuilder();
            updateBuilder.add("user_row", row);
            updateBuilder.add("user_col", col);
            if(!model.CheckWin(col, user))
            {
                
                AiTurn ai_move = model.startAi(Model.Player.YELLOW, true, -1, 6);
                int comp_row = model.doMove(ai_move.getCol(),computer);
                if(!model.CheckWin(ai_move.getCol(), computer))
                {
                    updateBuilder.add("gameover", false);
                }
                else{
                    updateBuilder.add("gameover", true);
                    model.startGame();
                }
                updateBuilder.add("computer_row", comp_row);
                updateBuilder.add("computer_col", ai_move.getCol());
            }
            else
            {
                updateBuilder.add("gameover", true);
                model.startGame();
            }
            JsonObject update = updateBuilder.build();
            try
            {
                session.getBasicRemote().sendText(update.toString());
            }
            catch(Exception e)
            {
                System.out.println("");
            }
        }
        
    }
    
    
    @OnOpen
    public void onOpen( Session session)
    {
        startGame();
    }

    @OnClose
    public void onClose (Session session)
    { 
        try
        {
            session.close();
        }
        catch (Exception ex) {
            
        }
        
    }
    
    public void startGame() 
    {
        model = new Model();
        model.startGame();
        user = Model.Player.RED;
        computer= Model.Player.YELLOW;
    }
    
   
       
}

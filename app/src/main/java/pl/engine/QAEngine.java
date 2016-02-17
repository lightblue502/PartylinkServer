package pl.engine;

import android.content.Context;
import android.util.Log;

import com.partylinkserver.GameActivity;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by lucksikalosuvalna on 1/18/16 AD.
 */
public class QAEngine extends GameEngine{
    private JSONArray json_array;
    private String question;
    private String[] choices;
    private String correct_ans;
    private Context context;
    private int playerAmount;
    private int cntPlayer = 0;
    private int cntResumePlayer = 0;
    private int random_team = new Random().nextInt(1);
    private List<Team> teams = gc.getTeams();
    public GameManager gameManager;
    private ResultScore resultScore = new ResultScore();
    private boolean isInit = false;
    private boolean gamePaused = false;

    public QAEngine(GameContext gc, int playerAmount, String name, Context context, Class activityClass, String clientStart) {
        super(gc, name, activityClass, clientStart);
        this.playerAmount = playerAmount;
        this.context = context;
        this.gameManager = new GameManager(resultScore, gc, playerAmount, 3);
    }

    @Override
    public void startEngine() {
        Utils.debug("========================================");
        Utils.debug("=======  Q/A GAME START !!! ========");
        Utils.debug("========================================");
        for (Team team: teams) {
            team.printPlayers();
        }
        // gc.sendGameEvent("qa_start");

        readJson();
    }
    public void randomQuestion(int length){
        int randomIndex = new Random().nextInt(length);

        try {
            // random get question //
            JSONObject in_question = json_array.getJSONObject(randomIndex);
            question = in_question.getString("question");

            // array of choices for the question //
            JSONArray J_choices = in_question.getJSONArray("choices");
            choices = new String[J_choices.length()];
            for (int j = 0; j < J_choices.length(); j++) {
                JSONObject in_choices = J_choices.getJSONObject(j);
                choices[j] = in_choices.getString("choice");
                if(in_choices.getString("isTrue").equals("true")){
                    correct_ans = in_choices.getString("choice");
                    Utils.debug("correct ans" + correct_ans);
                }
            }
        }catch (JSONException e){
            e.printStackTrace();
        }

        // remove that randomIndex //
        json_array.remove(randomIndex);

    }

    @Override
    public void endEngine() {
        Utils.debug("END GAME");
        gameManager.summaryScoreByGame(this, teams);
        gc.nextEngine();
    }
    public void sendGameEventToClient(String event, String[] param ){
        if(!gamePaused){
            gc.sendGameEvent(event, param);
        }
    }
    public void sendGameEventToClient(Player player, String event, String[] param ) {
        if(!gamePaused){
            gc.sendGameEvent(player, event, param);
        }
    }
    @Override
    public void onPlayerReady(int playerAmount) {
        int length = json_array.length();
        if(cntPlayer == playerAmount && isInit){
            Utils.debug("Player ready");
            gameManager.printReportRound();
            if(length > 0) {
                randomQuestion(length);
                sendQuestionToggleTeam();
            }else{
                endEngine();
            }
        }
    }
    public void sendQuestionToggleTeam(){
        Team team = teams.get(random_team % 2);
        random_team++;
        int randomPlayers = new Random().nextInt(team.getPlayerAmount());
        Player ask_player = team.getPlayers().get(randomPlayers);
        Utils.debug("Question is : " + question);
        gc.getGameLister().onIncommingEvent("qa_asking", new String[]{
                String.valueOf(ask_player.getCliendId()), team.getName()
        });
        sendGameEventToClient(ask_player, "qa_question", new String[]{question});
//        gc.sendGameEvent(ask_player, "qa_question", new String[]{question});
        broadcastChoice();

    }

    public void broadcastChoice(){
        sendGameEventToClient("qa_choices", choices);
    }
    @Override
    public void onIncomingEvent(int clientId, String event, String[] params) {
        if(!gamePaused) {
            if (event.equals("qaUI_Start")) {
                initPlayerstoUI();
                isInit = true;
                onPlayerReady(playerAmount);
                sendGameEventToClient(gc.getCurrentGameEngine().getClientStart(), new String[]{});
            } else if (event.equals("qa_ready")) {
                Utils.debug("Ready");
                cntPlayer++;
                onPlayerReady(playerAmount);
            } else if (event.equals("qa_ans")) {
                if (correct_ans.equals(params[0])) {
                    Utils.debug("CORRECT !!!!!!!");
                    gc.getGameLister().onIncommingEvent("qa_correct", new String[]{
                            String.valueOf(clientId)
                    });
                    cntPlayer = 0;
                    gameManager.scoreManage(clientId, 2);
                    sendGameEventToClient("qa_change", new String[]{});
                } else {
                    gc.getGameLister().onIncommingEvent("qa_wrong", new String[]{
                            String.valueOf(clientId)
                    });
                    gameManager.scoreManage(clientId, -1);
                }
                gameManager.printScoreToNumber();
            }
        }
        if(event.equals("game_pause")) {
            sendGameEventToClient("game_pause", new String[]{});
            gc.getGameLister().onIncommingEvent("game_pause", new String[]{});
            gamePaused = true;
        }else if(event.equals("game_resume")){
            cntResumePlayer++;
            Player player = new Player(clientId, params[0]);
            gc.sendGameEvent(player, "resume_ok");
            if(super.onPlayerResumeReady(playerAmount,cntResumePlayer)) {
                cntResumePlayer = 0;
                gamePaused = false;
                sendGameEventToClient("game_resume", new String[]{});
                gc.getGameLister().onIncommingEvent("game_resume", new String[]{});
            }
        }
    }

    ////////////////////////////////////// read JSON file //////////////////////////////////
    public void readJson(){
        Utils.debug("readJson");
        try {
            JSONObject obj = new JSONObject(loadJSONFromAsset());
            json_array = obj.getJSONArray("questions");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    public String loadJSONFromAsset() {
        String json = null;
        try {

            InputStream is = context.getAssets().open("questions.json");

            int size = is.available();

            byte[] buffer = new byte[size];

            is.read(buffer);

            is.close();

            json = new String(buffer, "UTF-8");


        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;

    }

    public void initPlayerstoUI(){

        String strs = "[";
        for (Team team: teams) {
            strs += "[";
            for(Player player : team.getPlayers()){
                strs += "{'id':" + player.getCliendId();
                strs += ",'name':'" + player.getName();
                strs += "'},";
            }
            if(strs.charAt(strs.length()-1) == ',')
                strs = strs.substring(0,strs.length()-1);
            strs += "],";
        }
        if(strs.charAt(strs.length()-1) == ',')
            strs = strs.substring(0,strs.length()-1);
        strs += "]";
        Log.d("DEBUG_init_PL", strs + "");
        gc.getGameLister().onIncommingEvent("initPlayer", new String[]{strs});
    }
}

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
    private int random_team = new Random().nextInt(1);
    private List<Team> teams = gc.getTeams();
    public GameManager gameManager;
    private ResultScore resultScore = new ResultScore();
    public QAEngine(GameContext gc, int playerAmount, String name, Context context) {
        super(gc, name);
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
        gc.sendGameEvent("qa_start");

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
        gameManager.printScoreToWIN();
        if(gameManager.getTeamWin() != null){
            Team team = gameManager.getTeamWin().equals("teamA")?teams.get(0) :teams.get(1);
            resultScore.setResult(team, this);
            gc.addResultScore(resultScore);
        }
        gameManager.resetWinRound();
        gc.nextEngine();
    }

    @Override
    public void onPlayerReady(int playerAmount) {
        int length = json_array.length();
        if(cntPlayer == playerAmount){
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
        gc.getGameLister().onIncommingEvent("qa_asking", new String[]{ask_player.getName()});
        gc.sendGameEvent(ask_player, "qa_question", new String[]{question});
        broadcastChoice();

    }

    public void broadcastChoice(){
        gc.sendGameEvent("qa_choices", choices);
    }
    @Override
    public void onIncomingEvent(int clientId, String event, String[] params) {
        if(event.equals("qa_ready")){
            Utils.debug("Ready");
            cntPlayer++;
            onPlayerReady(playerAmount);
        }else if(event.equals("qa_ans")){
            if(correct_ans.equals(params[0])) {
                gameManager.printScoreToNumber();
                Utils.debug("CORRECT !!!!!!!");
                cntPlayer = 0;
                gameManager.scoreManage(clientId, 2);
                gc.sendGameEvent("qa_change");
            }else{
                gameManager.scoreManage(clientId, -1);
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
}

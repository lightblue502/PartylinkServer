package pl.engine;


/**
 * Created by nuhwi_000 on 8/3/2559.
 */
import android.util.Log;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class BallEngine extends GameEngine{

    private int playerAmount;
    private int cntPlayer = 0;
    private int cntResumePlayer = 0;
    private Team players, enemys;
    private int toggleNumber = 1;
    private List<Team> teams = gc.getTeams();
    private GameManager gameManager;
    private boolean gamePaused = false;
    private ResultScore resultScore = new ResultScore();
    private Event jump,bomb;
    private int all_bar = 20;
    private String[] bombs;
    public BallEngine(GameContext gc) {
        super(gc);
    }

    public BallEngine(GameContext gc,int playerAmount, String name, Class activityClass, String clientStart) {
        super(gc, name, activityClass, clientStart);
        this.playerAmount = playerAmount;
        players = teams.get(0);
        enemys = teams.get(1);
        this.gameManager = new GameManager(resultScore, gc, 2, 3);
        bombs = new String[all_bar];
    }

    @Override
    public void startEngine() {
        Utils.debug("========================================");
        Utils.debug("======= 	BALL GAME START !!! ========");
        Utils.debug("========================================");
        for (Team team: teams) {
            team.printPlayers();
        }
        createBombs();
        gc.sendGameEvent("ball_start");
    }

    @Override
    public void endEngine() {
        gameManager.stopTimer();
        gameManager.summaryScoreByGame(this, teams);
    }

    @Override
    public void onPlayerReady(int playerAmount) {
        if(cntPlayer == playerAmount){
            if(gameManager.getRound() <= 3) {
                    Log.d("DEBUG", "NEW ROUND");

                    init();
                    gc.getGameLister().onIncommingEvent("start", new String[]{});
                    sendGameEventToClient("ball_newRound", new String[]{});

                    gameManager.printReportRound();
                    gameManager.countDownGameReady(5);
                    gameManager.setOnGameReadyListener(new GameManager.OnGameReadyListener() {
                        @Override
                        public void ready() {
                            if(gameManager.timerWasStarted())
                                gameManager.runTimerAgain();
                            sendEventToTeams();
                        }
                    });
            }else{
                endEngine();
            }


        }
    }
    public void init(){
        jump = new Event("jump");
        bomb = new Event("bomb");
    }

    public void createBombs(){
        int group = 4;
        int tmp = 0;
        // per five bar
        int[] bomb_amount = {1,2,3,2,1};

        while(tmp < 20){
            for(int i = 0; i < bomb_amount.length; i++){
                float[] co_or = new float[bomb_amount[i]];
                bombs[i+(tmp > 1 ? tmp : 0)] = randomBombPoint(co_or, bomb_amount[i]);
            }
            shuffle(bomb_amount);
            tmp+=5;
        }
    }
    public String randomBombPoint(float[] co_or, int amount){
        Random random = new Random();
        int num;
        int tmp = 0;
        int length = 10;
        String point;
        String temp;
        int[] used = new int[length];
        while(tmp < amount) {
            num = random.nextInt(10) + 1;
            int index = num - 1;
            if(used[index] == 0){
                used[index]++;
                co_or[tmp] = (float)((index + 1) * 0.1);
                tmp++;
            }
        }
        Arrays.sort(co_or);
        temp = Arrays.toString(co_or);

        return temp;
    }
    public void shuffle(int[] bomb_amount){
        Random random = new Random();
        int index, temp;
        for (int i = bomb_amount.length - 1; i > 0; i--){
            index = random.nextInt(i + 1);
            temp = bomb_amount[index];
            bomb_amount[index] = bomb_amount[i];
            bomb_amount[i] = temp;
        }
    }
    public void swapTeams(){
        Team temp;
        temp = players;
        players = enemys;
        enemys = temp;
    }
    public void sendEventToTeams(){
        swapTeams();
        sendEventToEnemy();
        sendEventToPlayer();
        gameManager.resetTimer();
//        gameManager.stopTimer();
        if(!gameManager.timerWasStarted())
            gameManager.startTimer(20, "change_ball");


        cntPlayer = 0;
    }
    public void sendEventToEnemy(){
        sendEventToTeam(enemys.getPlayers(), "enemy_start");
    };
    public void sendEventToPlayer(){
        sendEventToTeam(players.getPlayers(), "player_start");
    }
    public void sendEventToTeam(List<Player> players,String event){
        if(!gamePaused){
            for(Player player: players){
                gc.sendGameEvent(player, event);
            }
        }
    }
    public void sendGameEventToClient( String event, String[] param ){
        if(!gamePaused){
            gc.sendGameEvent(event, param);
        }
    }
    public void sendGameEventToClient(Player player, String event, String[] param ){
        if(!gamePaused){
            gc.sendGameEvent(player, event, param);
        }
    }
    public void combineEvent(final Event event, Long currentTime, int clientId){

        event.update(currentTime);

        if(!event.haveClientIds(clientId)) {
            event.counter(clientId);
            event.addTimes(currentTime);
        }

        Team team = gc.getTeamByClientId(clientId);
        int playerAmountByTeam = team.getMaxPlayerAmount();
        if(event.getCount() >= playerAmountByTeam){
            gc.getGameLister().onIncommingEvent(event.getName(),new String[]{});
            event.reset();
        }
        Utils.debug("EVENT : "+event.getName()+" | count "+event.getCount());
    }
    public void scoreProcess(Integer distance){
        Player player = players.getPlayers().get(0);
        gameManager.scoreManage(player.getCliendId(),distance);
    }
    @Override
    public void onIncomingEvent(int clientId, String event, String[] params) {
        if(!gamePaused) {
            if (event.equals("ballUI_Start")) {
                gameManager.initPlayerstoUI(teams);
                gc.getGameLister().onIncommingEvent("initial_bomb", bombs);
                gc.getGameLister().onIncommingEvent("game_pause",new String[]{});
            } else if (event.equals("ball_ready")) {
                cntPlayer++;
                onPlayerReady(playerAmount);
            } else if (event.equals("ball_game")) {
//                gameManager.printScoreToNumber();
//                gameManager.scoreManage(clientId, 10);
            }else if(event.equals("moveEvent")){
                Utils.debug("IN-BALLENGINE: moveEvent"+params[0]);
                gc.getGameLister().onIncommingEvent("move", params);
            }else if(event.equals("jumpEvent")){
                combineEvent(jump, Long.parseLong(params[0]), clientId);
            }else if(event.equals("bombEvent")){
                combineEvent(bomb, Long.parseLong(params[0]),clientId);
            }else if(event.equals("getDistance")){
                scoreProcess(Integer.parseInt(params[0]));
            }
        }
        if(event.equals("game_pause")) {
            Utils.debug("============ game is pause ============");
            sendGameEventToClient("game_pause", new String[]{});
            gc.getGameLister().onIncommingEvent("game_pause", new String[]{});
            gameManager.setCurrentTimer();
//            gameManager.stopTimer();
            gamePaused = true;
        }else if(event.equals("game_resume")){
            Utils.debug("============ game is resume ============");
            cntResumePlayer++;
            Player player = new Player(clientId, params[0],null);
            gc.sendGameEvent(player, "resume_ok");
            if(super.onPlayerResumeReady(playerAmount,cntResumePlayer)) {
                cntResumePlayer = 0;
                gameManager.getCurrentTimer();
                gamePaused = false;
                Utils.debug("============ start timer ============");
                gameManager.runTimerAgain();
                sendGameEventToClient("game_resume", new String[]{});
                gc.getGameLister().onIncommingEvent("game_resume", new String[]{});
            }
        }
    }

}

//class Event
class Event{
    private String name;
    private List<Integer> clientIds = new ArrayList<Integer>();
    private List<Long> times = new ArrayList<Long>();

    public Event(String name){
        this.name = name;
    }
    public String getName(){
        return name;
    }
    public void update(Long currentime){
        while ( times.size()>0 && currentime - times.get(0) > 500 ) {
            killCounter();
        }
    }
    public int getCount(){
        return clientIds.size();
    }
    public void killCounter(){
        clientIds.remove(0);
        times.remove(0);
    }
    public void counter(int clientId){
        clientIds.add(clientId);
    }
    public void addTimes(Long time){
        times.add(time);
    }

    public boolean haveClientIds(int clientId){
        return clientIds.contains(clientId);
    }
    public void reset(){
        clientIds.clear();
        times.clear();
    }



}
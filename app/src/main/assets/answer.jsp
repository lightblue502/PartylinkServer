<html>
<head>
  <title>Time's up.</title>
  <meta charset="utf-8">
  <meta name="viewport" content="width=device-width, initial-scale=1">
  <link rel="stylesheet" type="text/css" href="css/answerCSS.css">
  <link rel="stylesheet" href="css/bootstrap.min.css">
  <script src="js/jquery.min.js"></script>
  <script src="js/answerJS.js"></script>
  <script src="js/bootstrap.min.js"></script>
</head>
<body >
  <div id="myInput">
    <button type="button" class="close" id="time" autofocus> 
      <span class="glyphicon glyphicon-repeat" ></span>
    </button>
    <% 
      String text;
      Integer number1 = Integer.parseInt(request.getParameter("number1"));
      Integer number2 = Integer.parseInt(request.getParameter("number2"));
      Integer sum = number1 + number2;
      Integer answer = Integer.parseInt(request.getParameter("answer") == ""?"0":request.getParameter("answer"));
      if(request.getParameter("answer") == ""){
        text = "<div style=\"color: #F9A825;\"><b>none Typing.</b></div>";
      }
      else if((number1 + number2) == answer ){
        text = "<div style=\"color: #00C853;\"><b>correct !!</b></div>";
      }
      else{
        text = "<div style=\"color: #B71C1C;\"><b>wrong answer</b></div>";
      } 
    %>
    <div class="display">
      <h1><b><u>Time's up.</u></b></h1>
      <b style="font-size: 40px;"><i><u>ANSWER</u> : </i></b>
        <i style="color: #1DE9B6;">
          <b><%= number1 %> + <%= number2 %> = <%= sum %></b>
        </i><br>
      <div><%=text %></div>
      <div style="font-size: 25px;">PRESS Enter, Ese Or Spacebar.</div>
    </div>
  </div>
</body>
</html>
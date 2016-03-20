var cells = document.getElementsByClassName("cell");
var isPlayerOne = true;

for (var i = 0; i < cells.length; i++) {
    cells[i].addEventListener("click", listener, false);
}

function listener() {
    var color;
    this.style.fontSize = "70px";    
    if (this.innerHTML == "") {
        if (isPlayerOne) {
            this.innerHTML = "X";
            color = "green";
        } else {
            this.innerHTML = "O";
            color = "orange";
        }
        
        this.style.backgroundColor = color;
        console.log(isPlayerOne);
        checkForWinner();
        isPlayerOne = !isPlayerOne;
        console.log(isPlayerOne);
    }
}

function checkForWinner() {
    checkRows();
    checkColumns();
    checkDiagonals();
}

function checkRows() {
    for (var row = 0; row < 4; row++) {
        var cells = [];
        
        for (var col = 0; col < 4; col++) {
            console.log(row+""+col);
            var cell = document.getElementById(row+""+col).innerHTML;
            cells[col] = cell;
        }

        if (cells[0] != "" && cells[0] == cells[1] && cells[0] == cells[2] && cells[0] == cells[3]) {
            announceWinner();
            return;
        }
    }
}

function checkColumns() {
    for (var col = 0; col < 4; col++) {
        var cells = [];
        
        for (var row = 0; row < 4; row++) {
            var cell = document.getElementById(row+""+col).innerHTML;
            cells[row] = cell;
        }

        if (cells[0] != "" && cells[0] == cells[1] && cells[0] == cells[2] && cells[0] == cells[3]) {
            announceWinner();
            return;
        }
    }
}

function checkDiagonals() {
    checkLeftDiagonal();
    checkRightDiagonal();
}

function checkLeftDiagonal() {
    var leftDiagonal = [];
    leftDiagonal[0] = document.getElementById("30").innerHTML;
    leftDiagonal[1] = document.getElementById("21").innerHTML;
    leftDiagonal[2] = document.getElementById("12").innerHTML;
    leftDiagonal[3] = document.getElementById("03").innerHTML;

    if (leftDiagonal[0] != "" && leftDiagonal[0] == leftDiagonal[1] && leftDiagonal[0] == leftDiagonal[2] && leftDiagonal[0] == leftDiagonal[3]) {
        announceWinner();
        return;
    }
}

function checkRightDiagonal() {
    var rightDiagonal = [];
    rightDiagonal[0] = document.getElementById("00").innerHTML;
    rightDiagonal[1] = document.getElementById("11").innerHTML;
    rightDiagonal[2] = document.getElementById("22").innerHTML;
    rightDiagonal[3] = document.getElementById("33").innerHTML;

     if (rightDiagonal[0] != "" && rightDiagonal[0] == rightDiagonal[1] && rightDiagonal[0] == rightDiagonal[2] && rightDiagonal[0] == rightDiagonal[3]) {
        announceWinner();
        return;
    }
}

function announceWinner() {
    if (isPlayerOne) {
        document.getElementById("winner").innerHTML = "Player one wins the game."
    } else {
        document.getElementById("winner").innerHTML = "Player two wins the game."
    }

    exit();
}

function reset(){
    document.getElementById("winner").innerHTML = "";
    isPlayerOne = true;
    for (var i = 0; i < cells.length; i++) {
        cells[i].innerHTML = "";
        cells[i].style.backgroundColor = "";
    }

    for (var i = 0; i < cells.length; i++) {
        cells[i].addEventListener("click", listener, false);
    }
}

function exit() {
    for (var i = 0; i < cells.length; i++) {   
        cells[i].removeEventListener("click", listener, false);
    }
}
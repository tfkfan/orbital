var cnvs = document.getElementById("cnvs");
cnvs.width = window.innerWidth - 350;
cnvs.height = window.innerHeight - 50;

var updateMsg = document.getElementById("updateMsg");
var playerStats = document.getElementById("playerStats");
var debugMsg = document.getElementById("debugMsg");

var ctx = cnvs.getContext("2d");


let upKeys = {'w': true, 'ц': true, 'W': true, 'Ц': true};
let downKeys = {'s': true, 'ы': true, 'S': true, 'Ы': true};
let rightKeys = {'d': true, 'D': true, 'В': true, 'в': true};
let leftKeys = {'a': true, 'A': true, 'ф': true, 'Ф': true};
let players = {};
let selfId = null;
let text = null;

function drawText(x, y, text) {
    ctx.font = "48px serif";
    const lines = text.split('\n')
    for (let i = 0; i < lines.length; i++)
        ctx.strokeText(lines[i], x, y + (i * 48));
}

function drawPlayer(x, y) {
    ctx.beginPath();
    ctx.arc(x, y, 50, 0, 2 * Math.PI);
    ctx.stroke();
}

function clear() {
    ctx.clearRect(0, 0, cnvs.width, cnvs.height);
}

function drawPlayers() {
    for (let i in players) {
        let p = players[i];
        drawPlayer(p.position.x, p.position.y);
    }
}

document.onkeydown = function (event) {
    text = null
    if (upKeys[event.key])
        send(PLAYER_KEY_DOWN, {key: "UP", state: true});
    if (downKeys[event.key])
        send(PLAYER_KEY_DOWN, {key: "DOWN", state: true});
    if (rightKeys[event.key])
        send(PLAYER_KEY_DOWN, {key: "RIGHT", state: true});
    if (leftKeys[event.key])
        send(PLAYER_KEY_DOWN, {key: "LEFT", state: true});
};

document.onkeyup = function (event) {
    if (upKeys[event.key])
        send(PLAYER_KEY_DOWN, {key: "UP", state: false});
    if (downKeys[event.key])
        send(PLAYER_KEY_DOWN, {key: "DOWN", state: false});
    if (rightKeys[event.key])
        send(PLAYER_KEY_DOWN, {key: "RIGHT", state: false});
    if (leftKeys[event.key])
        send(PLAYER_KEY_DOWN, {key: "LEFT", state: false});
};

document.getElementById("joinBtn").onclick = function () {
    send(JOIN, {});
}
initializeWebsocket();
on(JOIN_WAIT, function (evt) {
    debugMsg.innerText = "Wait"
});
on(JOIN_SUCCESS, function (evt) {
    debugMsg.innerText = "Connected to game room"
    document.getElementById("joinBtn").setAttribute("disabled", "disabled");
});
on(ROOM_START, function (evt) {
    debugMsg.innerText = "Room started, please wait for battle start"
});

on(ROOM_CLOSE, function (evt) {
    debugMsg.innerText = "Room closed."
    updateMsg.innerText = ""
    playerStats.innerText = ""
    clear();
    const joinBtn = document.getElementById("joinBtn")
    joinBtn.removeAttribute("disabled");
    joinBtn.innerText = "Join again"
});
on(BATTLE_START, function (evt) {
    const msg = "Battle started. Click on battlefield.\nUse WASD to move."
    debugMsg.innerText = msg
    text = msg
});
on(FAILURE, function (evt) {
    debugMsg.innerText = "Internal error occurred"
});
on(UPDATE, function (evt) {
    clear();
    players = evt.players.reduce(function (map, obj) {
        map[obj.id] = obj;
        return map;
    }, {});
    selfId = evt.player.id;
    playerStats.innerText = JSON.stringify(players[selfId]);
    updateMsg.innerText = JSON.stringify(evt);
    drawPlayers();
    if (text)
        drawText(200, 300, text)
});
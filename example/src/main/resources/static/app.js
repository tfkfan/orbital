let cnvs = document.getElementById("cnvs");
cnvs.width = window.innerWidth - 350;
cnvs.height = window.innerHeight - 50;

let updateMsg = document.getElementById("updateMsg");
let playerStats = document.getElementById("playerStats");
let debugMsg = document.getElementById("debugMsg");

let ctx = cnvs.getContext("2d");

let upKeys = {'w': true, 'ц': true, 'W': true, 'Ц': true};
let downKeys = {'s': true, 'ы': true, 'S': true, 'Ы': true};
let rightKeys = {'d': true, 'D': true, 'В': true, 'в': true};
let leftKeys = {'a': true, 'A': true, 'ф': true, 'Ф': true};
let players = {};
let selfId = null;
let text = null;

let ws = new Websocket();

function drawText(x, y, text) {
    ctx.font = "48px serif";
    const lines = text.split('\n')
    for (let i = 0; i < lines.length; i++)
        ctx.strokeText(lines[i], x, y + (i * 48));
}

function fillCircle(x, y, color, radius) {
    ctx.fillStyle = color;
    ctx.beginPath();
    ctx.arc(x, y, radius, 0, 2 * Math.PI);
    ctx.fill();
}

function clear() {
    ctx.clearRect(0, 0, cnvs.width, cnvs.height);
}

function drawPlayers() {
    for (let i in players) {
        let p = players[i];
        const color = selfId !== p.id ? "red" : "black";
        fillCircle(p.position.x, p.position.y, color, 50);
    }
}

document.onmousedown = (event) => ws.send(ws.PLAYER_MOUSE_DOWN, {
    key: "lkb",
    target: {x: event.clientX, y: event.clientY}
})
document.onkeydown = (event) => {
    text = null
    if (upKeys[event.key])
        ws.send(ws.PLAYER_KEY_DOWN, {key: "UP", state: true});
    if (downKeys[event.key])
        ws.send(ws.PLAYER_KEY_DOWN, {key: "DOWN", state: true});
    if (rightKeys[event.key])
        ws.send(ws.PLAYER_KEY_DOWN, {key: "RIGHT", state: true});
    if (leftKeys[event.key])
        ws.send(ws.PLAYER_KEY_DOWN, {key: "LEFT", state: true});
};

document.onkeyup = (event) => {
    if (upKeys[event.key])
        ws.send(ws.PLAYER_KEY_DOWN, {key: "UP", state: false});
    if (downKeys[event.key])
        ws.send(ws.PLAYER_KEY_DOWN, {key: "DOWN", state: false});
    if (rightKeys[event.key])
        ws.send(ws.PLAYER_KEY_DOWN, {key: "RIGHT", state: false});
    if (leftKeys[event.key])
        ws.send(ws.PLAYER_KEY_DOWN, {key: "LEFT", state: false});
};

document.getElementById("joinBtn").onclick = () => {
    const isTraining = document.getElementById("isTrainingCheckBox").value
    const roomType = isTraining ? "TRAINING" : "BATTLE_ROYALE";
    ws.send(ws.JOIN, {roomType})
}

ws.init("ws://localhost:8085/game")
    .then(() => console.log("Connection established"));

ws.on(ws.JOIN_WAIT, (evt) => {
    debugMsg.innerText = "Wait"
});

ws.on(ws.JOIN_SUCCESS, (evt) => {
    debugMsg.innerText = "Connected to game room"
    document.getElementById("joinBtn").setAttribute("disabled", "disabled");
});

ws.on(ws.ROOM_START, (evt) => {
    debugMsg.innerText = "Room started, please wait for battle start"
});

ws.on(ws.ROOM_CLOSE, (evt) => {
    debugMsg.innerText = "Room closed."
    updateMsg.innerText = ""
    playerStats.innerText = ""
    clear();
    const joinBtn = document.getElementById("joinBtn")
    joinBtn.removeAttribute("disabled");
    joinBtn.innerText = "Join again"
});
ws.on(ws.BATTLE_START, (evt) => {
    const msg = "Battle started. Click on battlefield.\nUse WASD to move."
    debugMsg.innerText = msg
    text = msg
});
ws.on(ws.FAILURE, (evt) => {
    debugMsg.innerText = "Internal error occurred"
});
ws.on(ws.UPDATE, (evt) => {
    clear();
    players = evt.players.reduce((map, obj) => {
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
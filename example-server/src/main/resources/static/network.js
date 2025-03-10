var socket;
var events = {};

function initializeWebsocket(wsEndpoint = "ws://localhost:8085/game") {
    if (!window.WebSocket) {
        // @ts-ignore
        window.WebSocket = window.MozWebSocket;
    }
    if (window.WebSocket) {
        socket = new WebSocket(wsEndpoint);
    } else {
        alert("Your browser does not support Web Socket.");
    }

    socket.addEventListener('open', function () {
        console.log("Connection established");
    });

    socket.addEventListener('error', function (event) {
        console.log(event.message);
    });

    socket.addEventListener('close', function () {
        console.log("Web Socket closed");
    });
    socket.addEventListener('message', function (evt) {
        const eventData = JSON.parse(evt.data);
        console.log(`Message ${eventData.type} accepted`);
        if (events[eventData.type] !== undefined) {
            const arr = events[eventData.type]
            arr[1].call(arr[0], eventData.data);
        }
    });
}

function on(type, handler, thisArg) {
    events[type] = [thisArg, handler];
}

function send(type, data) {
    if (socket.readyState !== WebSocket.OPEN) {
        console.log("Socket is not ready");
        return;
    }

    socket.send(createEvent(type, data));
}

function createEvent(eventType, payload) {
    const obj = {
        type: eventType,
        data: null
    };
    if (payload) {
        obj.data = payload
    }
    return JSON.stringify(obj);
}
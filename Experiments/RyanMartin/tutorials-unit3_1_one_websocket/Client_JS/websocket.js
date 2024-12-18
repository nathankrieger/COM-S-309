var ws;

function connect() {
    var username = document.getElementById("username").value;
    var wsserver = document.getElementById("wsserver").value;

    // Remove any trailing slash
    if (wsserver.endsWith("/")) {
        wsserver = wsserver.slice(0, -1);
    }

    var url = wsserver + "/comments";
    console.log("Connecting to WebSocket server at URL:", url);

    ws = new WebSocket(url);

    ws.onopen = function(event) {
        console.log("Connected to WebSocket server at", event.currentTarget.url);
        var log = document.getElementById("log");
        log.innerHTML += "Connected to " + event.currentTarget.url + "\n";
    };

    ws.onmessage = function(event) {
        console.log("Message received:", event.data);
        var log = document.getElementById("log");
        log.innerHTML += event.data + "\n";
    };

    ws.onerror = function(event) {
        console.error("WebSocket error:", event);
        var log = document.getElementById("log");
        log.innerHTML += "WebSocket error\n";
    };

    ws.onclose = function(event) {
        console.log("WebSocket connection closed:", event);
        var log = document.getElementById("log");
        log.innerHTML += "Connection closed\n";
    };
}
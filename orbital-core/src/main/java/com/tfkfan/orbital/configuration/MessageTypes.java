package com.tfkfan.orbital.configuration;

public interface MessageTypes {
    int MESSAGE = 1;
    int GAME_ROOM_JOIN = 2;
    int GAME_ROOM_JOIN_WAIT = 3;
    int GAME_ROOM_JOIN_SUCCESS = 4;
    int GAME_ROOM_JOIN_FAILURE = 5;
    int GAME_ROOM_BATTLE_START = 6;
    int GAME_ROOM_START = 7;
    int GAME_ROOM_INFO = 8;
    int GAME_ROOM_CLOSE = 9;
    int AUTHENTICATION = 10;
    int FAILURE = 11;
    int UPDATE = 100;
    int INIT = 101;
    int REMOVE = 102;
    int PLAYER_KEY_DOWN = 200;
    int PLAYER_MOUSE_DOWN = 201;
    int PLAYER_MOUSE_MOVE = 202;
    int PLAYER_KILL = 203;
    int PLAYER_CHOICE = 204;
}

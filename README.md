<h1 align="center">
  <br>
  <a href="#"><img src="logo.svg" alt="header" width="600"></a>
  <br>
  Orbital
  <br>
</h1>

<h4 align="center">
This is high-performance vert.x based distributed java game server designed for realtime 2D/3D shooters,
rpg games and any others requiring low-latency realtime computations</h4>

<p align="center">
  <a href="https://opensource.org/licenses/MIT" title="License: MIT" >
    <img src="https://img.shields.io/badge/License-MIT-greenbright.svg?style=flat-square">
  </a>
</p>

---

**Project is in active development.**

The server contains example server, basic game loops, matchmaking, room management functionality and it is about to have further improvements.

Supports:
* HTTP
* websocket

Features currently in development:
* 2D/3D geometry and grid systems
* TCP/UDP server mode
* Advanced room management
* HTTP monitoring
* Advanced player management
* Auth-protected REST API
* Advanced basic game objects (strikes, loot, handlers)

## Core and architecture

The solution is based on Vert.X "Actor" approach and EventBus features. It allows to have Indefinite amount of
room management verticles as workers to process game messages.
The code of "Orbital" is conceived to be extended and modernized on your own.

For the moment core contains:
* Gateway verticle basic functionality
* Room verticle basic functionality
* EventBus-based room listener system
* 2D geometry
* Basic rooms
* Package classes
* Annotation-based incoming message handlers
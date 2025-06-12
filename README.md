<h1 align="center">
  <br>
  <a href="#"><img src="logo.svg" alt="header" width="600"></a>
  <br>
  Orbital
  <br>
</h1>

<h4 align="center">
This is high-performance vert.x based distributed java game server designed for realtime indie multiplayers development</h4>

<p align="center">
  <a href="https://opensource.org/licenses/MIT" title="License: MIT" >
    <img src="https://img.shields.io/badge/License-MIT-greenbright.svg?style=flat-square">
  </a>
</p>

---

**Project is in active development. Looking for contributors and sponsors to make this stuff widely used in future**

Supports:

* HTTP
* Websocket

Features:

* Orbital launcher
* Gateway verticle basic functionality
* Room verticle basic functionality
* EventBus-based room listener system
* 2D/3D geometry
* Lucene and spatial JTS geo indices
* Basic rooms
* Package classes
* Micrometer + prometheus metrics and cluster monitor web page https://github.com/tfkfan/orbital-monitor
* Annotation-based incoming message handlers
* Clustered game server mode (Infinispan)
* English/Russian web app localization

Backlog:

* Unit and module tests since stable versions
* Resources processing
* GraalVM native image optimizations
* TCP/UDP server mode
* Advanced room management, player management, admin page https://github.com/tfkfan/orbital-monitor
* Auth-protected REST API and social accounts oidc providers (google,vk,facebook,X etc...)
* Advanced basic game objects
* Payments api integrations
* Meta game logic tools

Attention: versions 1.*.* are unstable.

## Core and features

The solution is based on Vert.X "Actor" approach and EventBus features. It allows to have Indefinite amount of
room management verticles as workers to process game messages.
The code of "Orbital" is conceived to be extended and modernized on your own.
The basic features are verticles and managers encapsulating business logic of the game.
Following image represents internal framework architecture

![orbital.chart.png](orbital.chart.png)

Orbital cluster is easy reachable according this schema:

![orbital-cluster.chart.png](orbital-cluster.chart.png)

## Usage

See "example" module for complete starter. Please pay attention example already contains frontend resources.

### Requirements

Before running your first orbital game server your app should have:

- GameManager implementation
- Player model implementation
- Game room implementation

### Launch

To run orbital microcluster with gateway verticle and N room verticles write:

```
public static void main(String[] args) {
    final Vertx vertx = Vertx.vertx();

    int N = 10;
    Orbital.newCluster(new OrbitalBuilderImpl(vertx)
                  .withConfig(N, new RoomConfig())
                        .withWebsocketGateway(it ->
                                it.withRouterInitializer(router -> router.route().handler(StaticHandler.create("static")))
                                        .withRouterInitializer(new MonitorEndpoint(CorsHandler.create("*"))::create))
                        .withGameManagerFactory(config -> DefaultGameManager.factory(new GeometryResources().load(), config.second().getRoom()))
                        .withRoomClusterLauncher(config -> new RoomDeploymentConfig(new DeploymentOptions()
                                .setThreadingModel(ThreadingModel.VIRTUAL_THREAD)
                                .setWorkerPoolSize(100))))
                .onSuccess(orbital -> log.info("Orbital cluster is ready"));
}
```

## Monitoring

### Setup

To setup monitor-related instance add required dependency:

```
<dependency>
    <groupId>io.github.tfkfan</groupId>
    <artifactId>orbital-monitor</artifactId>
    <version>1.2.2</version>
</dependency>
```

Your vertx instance should be monitorable this way:

```
final Future<Vertx> vertx = new MonitorableVertx().build();
```

You can specify certain metrics binders and registries on your own:

```
final Future<Vertx> vertx = new MonitorableVertx(registry).build();
```

```
final Future<Vertx> vertx = new MonitorableVertx(registry, new JvmHeapPressureMetrics()).build();
```

Final step is static monitor resources linking with gateway verticle:

```
final GatewayVerticle gatewayVerticle = new WebsocketGatewayVerticle(serverConfig, roomConfig)
                            .withRouterInitializer(new MonitorEndpoint()::create);
```

### Web app

The monitor app allows you to check every metrics:

![orbital.monitor-1.png](orbital.monitor-1.png)
![orbital.monitor-2.png](orbital.monitor-2.png)
![orbital.monitor-3.png](orbital.monitor-3.png)

### Endpoints

Server port is 8085 by default

- Monitor web app is available at http://localhost:8085/monitor
- Prometheus metrics are available at http://localhost:8085/prometheus
- JSON metrics are available at http://localhost:8085/metrics

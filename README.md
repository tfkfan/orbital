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
  <a href="https://github.com/tfkfan/orbital">
            <img src="https://img.shields.io/badge/github-orbital-blue?logo=github" alt="github"></img>
          </a>
  &nbsp;
  <a href="https://opensource.org/licenses/MIT">
    <img src="https://img.shields.io/badge/License-MIT-greenbright.svg" alt="MIT"></img>
  </a>
  &nbsp;
  <a href="https://central.sonatype.com/artifact/io.github.tfkfan/orbital-core">
    <img src="https://img.shields.io/maven-central/v/io.github.tfkfan/orbital-core.svg" alt="version unstable"></img>
  </a>
</p>

---

**Project is in active development. Looking for contributors and sponsors to make this stuff widely used in future**

<a href="https://tfkfan.github.io/orbital">Documentation</a>


Attention: current versions are unstable.

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
- Player model implementation (optional)
- Game room implementation

### Launch

To run orbital microcluster with gateway verticle and N room verticles write:

```
public static void main(String[] args) {
     int N = 10;
     Orbital.newCluster(OrbitalBuilder
                        .create(new MonitorableVertx().build())
                        .withGameManagerFactory(new DeploymentOptions().setInstances(N),DefaultGameManager.factory()))
                .onFailure(th -> log.error("Startup error", th))
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

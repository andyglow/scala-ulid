Scala ULID [![scaladex-badge][]][scaladex] [![maven-badge][]][maven] [![travis-badge][]][travis] [![coveralls-badge][]][coveralls]
==========

[scaladex]:            https://index.scala-lang.org/com.github.andyglow/scala-ulid
[scaladex-badge]:      https://index.scala-lang.org/com.github.andyglow/scala-ulid/latest.svg
[travis]:              https://travis-ci.org/andyglow/scala-ulid
[travis-badge]:        https://travis-ci.org/andyglow/scala-ulid.svg?branch=master
[coveralls]:           https://coveralls.io/github/andyglow/scala-ulid?branch=master
[coveralls-badge]:     https://coveralls.io/repos/github/andyglow/scala-ulid/badge.svg?branch=master
[maven]:               https://search.maven.org/#search%7Cga%7C1%7Cscala-ulid
[maven-badge]:         https://maven-badges.herokuapp.com/maven-central/com.github.andyglow/ulid_2.13/badge.svg

Scala implementation of ULID spec (https://github.com/ulid/spec)

Scala version 2.11, 2.12 as well as 2.13 are supported.

Features
--------
- fast
- has several implementations
  - **weak**. based in `java.util.Random`
  - **secure**. based in `java.security.SecureRandom`
  - **uuid**. based in `java.util.UUID` which is on it's on based on `java.security.SecureRandom`
  - **fastWeakSeed**. relies on `java.util.Random` for source of pseudo unique node id and a seed for sequential counter. Also mixes in nano time. 
  - **fastSecureSeed**. relies on `java.security.SecureRandom` for source of pseudo unique node id and a seed for sequential counter. Also mixes in nano time. 
- type-safe. ULID as a newtype (thanks to https://github.com/estatico/scala-newtype for the inspiration)
- configurable default `Rnd` implementation aka `-Dulid.rnd=secure`. Apart from that it can be overridden at implicit scope level.

Benchmarks
----------
Computer
```
Model Name:              MacBook Pro
Model Identifier:        MacBookPro14,2
Processor Name:          Intel Core i5
Processor Speed:         3.1 GHz
Number of Processors:    1
Total Number of Cores:   2
L2 Cache (per Core):     256 KB
L3 Cache:                4 MB
Memory:                  16 GB
```

Results 
```
[info] Benchmark                     Mode  Cnt        Score        Error  Units
[info] TheBenchmark.fastWeakSeed    thrpt    3  2875568.257 ±  58957.624  ops/s
[info] TheBenchmark.fastSecureSeed  thrpt    3  2878916.344 ± 125614.809  ops/s
[info] TheBenchmark.weak            thrpt    3  2794375.364 ±  27769.778  ops/s
[info] TheBenchmark.secure          thrpt    3   760376.000 ±  84277.655  ops/s
[info] TheBenchmark.uuid            thrpt    3   923135.293 ± 104787.680  ops/s
```

Example
-------
```scala
scala> import ulid._
import ulid._

scala> val id = ULID() // default Rnd was used
id: ulid.ULID = QQZ0ZXDD106C2WZZZZQGA4T700

scala> implicit val rnd: Rnd = Rnd.fastWeakSeed
rnd: ulid.Rnd = ulid.Rnd$$anon$1@5d508d22

scala> val id1 = ULID() // fastWeakSeed Rnd was used implicitly
id1: ulid.ULID = 08J3ZXDD107C2WZZZZ4Z2GD700

scala> val id2 = ULID()(Rnd.secure) // secure Rnd was used explicitly
id2: ulid.ULID = EZ75ZXDD10BP5WQX4RTJK2W8NP
```

Dependency
----------
```scala
libraryDependencies += "com.github.andyglow"  %% "ulid"  % $latestVersion
```
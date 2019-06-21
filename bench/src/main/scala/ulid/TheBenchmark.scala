package ulid

import org.openjdk.jmh.annotations._

@State(Scope.Thread)
@BenchmarkMode(Array(Mode.Throughput))
class TheBenchmark {

  @Benchmark def fastWeakSeed(): Unit = ULID()(Rnd.fastWeakSeed)
  @Benchmark def fastSecureSeed(): Unit = ULID()(Rnd.fastSecureSeed)
  @Benchmark def weak(): Unit = ULID()(Rnd.weak)
  @Benchmark def secure(): Unit = ULID()(Rnd.secure)
  @Benchmark def uuid(): Unit = ULID()(Rnd.uuid)
}
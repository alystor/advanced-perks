package art.flieda.advancedperks.testutil.testdatageneration

import net.datafaker.Faker
import kotlin.math.max
import kotlin.random.Random
import kotlin.random.asJavaRandom

object TestDataGeneration {

    var seed = Random.nextLong()
    var random = Random(seed)
    var faker = Faker(random.asJavaRandom())

    fun <T> T.orNullWithProbability(probability: Double) = if (random.nextDouble() < probability) null else this

    fun <T> T.orSometimesNull(): T? = orNullWithProbability(0.1)

    fun <T> T.orUsuallyNull(): T? = orNullWithProbability(0.9)

    fun aString(maxLength: Int = 100_000): String = faker.harryPotter().quote().take(maxLength)

    fun aBoolean(): Boolean = random.nextBoolean()

    fun aPositiveDouble(): Double = random.nextDouble(1.0, 100_000_000.0)

    inline fun <reified T : Enum<T>> aRandomEnumValue() = enumValues<T>().random(random)

    fun <T> some(min: Int = 3, max: Int = min + 3, init: (index: Int) -> T): List<T> =
        List(random.nextInt(min, max), init)
}

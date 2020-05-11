package rationals

import java.lang.IllegalArgumentException
import java.math.BigInteger
import java.math.BigInteger.*


fun main() {
    val half = 1 divBy 2
    val third = 1 divBy 3

    val sum: Rational = half + third
    println(5 divBy 6 == sum)

    val difference: Rational = half - third
    println(1 divBy 6 == difference)

    val product: Rational = half * third
    println(1 divBy 6 == product)

    val quotient: Rational = half / third
    println(3 divBy 2 == quotient)

    val negation: Rational = -half
    println(-1 divBy 2 == negation)

    println((2 divBy 1).toString() == "2")
    println((-2 divBy 4).toString() == "-1/2")
    println("117/1098".toRational().toString() == "13/122")

    val twoThirds = 2 divBy 3
    println(half < twoThirds)

    println(half in third..twoThirds)

    println(2000000000L divBy 4000000000L == 1 divBy 2)

    println("912016490186296920119201192141970416029".toBigInteger() divBy
            "1824032980372593840238402384283940832058".toBigInteger() == 1 divBy 2)
}

operator fun Rational.unaryMinus(): Rational {
    val n = -this.n
    return Rational(n, d)
}

operator fun Rational.plus(r: Rational): Rational {
    val d = this.d * r.d
    val n = this.n * r.d + r.n * this.d
    return Rational(n, d)
}

operator fun Rational.minus(r: Rational): Rational {
    val d = this.d * r.d
    val n = this.n * r.d - r.n * this.d
    return Rational(n, d)
}

operator fun Rational.times(r: Rational): Rational {
    val d = this.d * r.d
    val n = this.n * r.n
    return Rational(n, d)
}

operator fun Rational.div(r: Rational): Rational {
    val n = this.n * r.d
    val d = r.n * this.d
    return Rational(n, d)
}

fun String.toRational(): Rational {
    val split = split("/")
    return when (split.size) {
        1 -> Rational(BigInteger(split[0]))
        2 -> Rational(BigInteger(split[0]), BigInteger(split[1]))
        else -> throw IllegalArgumentException("Wrong number of arguments")
    }
}

infix fun BigInteger.divBy(i: BigInteger): Rational = Rational(this, i)
infix fun Int.divBy(i: Int): Rational = Rational(this, i)
infix fun Long.divBy(l: Long): Rational = Rational(this.toBigInteger(), l.toBigInteger())

class Rational(val n: BigInteger, val d: BigInteger) : Comparable<Rational> {
    constructor(n: Int, d: Int) : this(n.toBigInteger(), d.toBigInteger())
    constructor(i: BigInteger) : this(i, ONE)

    override fun compareTo(other: Rational): Int {
        return when {
            d == other.d -> n.compareTo(other.n)
            n == other.n -> other.n.compareTo(n)
            else -> {
                val a = normalize(this)
                val b = normalize(other)
                (a.n * b.d).compareTo(a.d * b.n)
            }
        }
    }

    override fun equals(other: Any?): Boolean {
        return when (other) {
            is Rational -> compareTo(other) == 0
            else -> false
        }
    }

    override fun toString(): String {
        val divisor: BigInteger
        val numerator: BigInteger
        when {
            d < ZERO && n < ZERO -> {
                divisor = d.abs()
                numerator = n.abs()
            }
            d < ZERO || n < ZERO -> {
                divisor = d.abs()
                numerator = ZERO - n.abs()
            }
            else -> {
                divisor = d
                numerator = n
            }
        }

        return (printNormalized(numerator, divisor))
    }

    private fun normalize(r: Rational): Rational {
        val gcd = r.d.gcd(r.n)
        val d = r.d / gcd
        val n = r.n / gcd

        return return when {
            d < ZERO && n < ZERO -> Rational(n.abs(), d.abs())
            (d < ZERO || n < ZERO) -> Rational(ZERO - n.abs(), d.abs())
            else -> Rational(n,d)
        }
    }

    private fun printNormalized(numerator: BigInteger, divisor: BigInteger): String {
        val gcd = divisor.gcd(numerator)
        val d = divisor / gcd
        val n = numerator / gcd
        return if (d == ONE) "$n" else "$n/$d"
    }
}

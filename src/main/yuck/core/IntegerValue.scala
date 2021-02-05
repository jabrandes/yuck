package yuck.core

import java.lang.Math.pow

/**
 * Implements immutable integer values.
 *
 * @author Michael Marte
 */
final class IntegerValue(val value: Int) extends IntegralValue[IntegerValue] {
    @inline override def hashCode = value
    override def equals(that: Any) = that match {
        case rhs: IntegerValue => {
            val lhs = this
            lhs.value == rhs.value
        }
        case _ => false
    }
    @inline override def compare(that: IntegerValue) =
        if (this.value < that.value) -1
        else if (this.value > that.value) 1
        else 0
    override def toString = value.toString
    override def +(that: IntegerValue) = IntegerValue(safeAdd(this.value, that.value))
    override def -(that: IntegerValue) = IntegerValue(safeSub(this.value, that.value))
    override def *(that: IntegerValue) = IntegerValue(safeMul(this.value, that.value))
    override def /(that: IntegerValue) = IntegerValue(this.value / that.value)
    override def %(that: IntegerValue) = IntegerValue(this.value % that.value)
    override def ^(that: IntegerValue) = {
        val result: Double = pow(this.value, that.value)
        if (result == java.lang.Double.NEGATIVE_INFINITY || result == java.lang.Double.POSITIVE_INFINITY ||
            result < Int.MinValue || result > Int.MaxValue)
        {
            throw new java.lang.ArithmeticException("integer overflow")
        }
        IntegerValue(result.toInt)
    }
    override def addAndSub(a: IntegerValue, b: IntegerValue) =
        IntegerValue(safeAdd(this.value, safeSub(a.value, b.value)))
    override def addAndSub(s: IntegerValue, a: IntegerValue, b: IntegerValue) =
        IntegerValue(safeAdd(this.value, safeMul(s.value, safeSub(a.value, b.value))))
    override def abs = if (value < 0) IntegerValue(safeNeg(value)) else this
    override def negate = IntegerValue(safeNeg(value))
    override def toInt = value
    override def toLong = value.toLong
    override def toFloat = value.toFloat
    override def toDouble = value.toDouble
    override def isEven = value % 2 == 0
}

/**
 * Companion object to IntegerValue.
 *
 * @author Michael Marte
 */
object IntegerValue {

    def min(a: IntegerValue, b: IntegerValue): IntegerValue = if (a < b) a else b

    def max(a: IntegerValue, b: IntegerValue): IntegerValue = if (a > b) a else b

    implicit def valueTraits = IntegerValueTraits
    implicit def numericalOperations = IntegerValueOperations
    implicit def domainOrdering = IntegerDomainOrdering

    private val valueRange = Range(-10000, 10000, 1)
    private val valueCache = valueRange.iterator.map(new IntegerValue(_)).toArray

    /**
     * Returns an IntegerValue instance for the given integer.
     *
     * Values in valueRange are used as index into an array of prefabricated
     * IntegerValue instances, so the operation is cheap for them.
     * For other values, a new IntegerValue instance is created.
     */
    def apply(a: Int): IntegerValue =
       if (valueRange.contains(a)) valueCache(a - valueRange.start) else new IntegerValue(a)

}

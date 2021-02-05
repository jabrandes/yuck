package yuck.constraints

import scala.collection._

import yuck.core._

/**
 * @author Michael Marte
 *
 */
final class CountVar
    [Value <: AnyValue]
    (id: Id[Constraint], override val maybeGoal: Option[Goal],
     xs: immutable.Seq[Variable[Value]], y: Variable[Value], n: IntegerVariable)
    (implicit valueTraits: ValueTraits[Value])
    extends ValueFrequencyTracker[Value, IntegerValue](
        id, xs, n,
        immutable.TreeMap[AnyVariable, Int](), immutable.HashMap[Value, Int]())(
        valueTraits)
{

    override def toString = "%s = count(%s, [%s])".format(n, y, xs.mkString(", "))

    override def inVariables = xs.iterator.filter(_.domain.intersects(y.domain)).toBuffer[AnyVariable].addOne(y)
    override def todo(move: Move) = super.todo(move).filter(x => x != y)
    override protected def computeResult(searchState: SearchState, valueRegistry: ValueRegistry) =
        IntegerValue(valueRegistry.getOrElse(searchState.value(y), 0))

}

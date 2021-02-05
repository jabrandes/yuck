package yuck.constraints

import scala.annotation.tailrec
import scala.collection._

import yuck.core._

/**
 * Implements the ''lex_less_int'' constraint as specified by MiniZinc.
 *
 * @author Michael Marte
 */
final class LexLess
    [Value <: OrderedValue[Value]]
    (id: Id[Constraint], override val maybeGoal: Option[Goal],
     xs: immutable.IndexedSeq[OrderedVariable[Value]], ys: immutable.IndexedSeq[OrderedVariable[Value]],
     costs: BooleanVariable)
    (implicit val ord: Ordering[Value])
    extends Constraint(id)
{

    override def toString = "lex_less([%s], [%s])".format(xs.mkString(", "), ys.mkString(", "))

    override def inVariables = xs.view ++ ys.view
    override def outVariables = List(costs)

    private val effect = new ReusableMoveEffectWithFixedVariable(costs)

    @tailrec
    private def findFailurePosition(searchState: SearchState, i: Int): Option[Int] =
        if (i == ys.size) {
            Some(i - 1)
        } else if (i == xs.size) {
            None
        } else {
            val a = searchState.value(xs(i))
            val b = searchState.value(ys(i))
            val cmp = ord.compare(a, b).sign
            if (cmp < 0) None
            else if (cmp > 0) Some(i)
            else findFailurePosition(searchState, i + 1)
        }

    override def initialize(now: SearchState) = {
        val maybeFailurePos = findFailurePosition(now, 0)
        effect.a = if (maybeFailurePos.isEmpty) True else BooleanValue(min(xs.size, ys.size) - maybeFailurePos.get)
        effect
    }

    override def consult(before: SearchState, after: SearchState, move: Move) =
        initialize(after)

}

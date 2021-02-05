package yuck.flatzinc.test

import org.junit._
import org.junit.experimental.categories._

import scala.language.implicitConversions

import yuck.constraints.{Alldistinct, ElementConst, ElementVar}
import yuck.core._
import yuck.flatzinc.compiler.VariableWithInfiniteDomainException
import yuck.flatzinc.test.util._

/**
 * Tests that cover edge cases and rarely used features of the FlatZinc language
 *
 * @author Michael Marte
 */
@Test
@FixMethodOrder(runners.MethodSorters.NAME_ASCENDING)
final class FlatZincBaseTest extends FrontEndTest {

    @Test
    @Category(Array(classOf[SatisfiabilityProblem]))
    def testVarArrayAccess: Unit = {
        val result = solveWithResult(task.copy(problemName = "var_array_access"))
        assertEq(result.space.numberOfConstraints(_.isInstanceOf[ElementVar[_]]), 3)
    }

    @Test
    @Category(Array(classOf[SatisfiabilityProblem]))
    def testConstArrayAccess: Unit = {
        val result = solveWithResult(task.copy(problemName = "const_array_access"))
        assertEq(result.space.numberOfConstraints(_.isInstanceOf[ElementConst[_]]), 3)
    }

    @Test
    @Category(Array(classOf[SatisfiabilityProblem]))
    def testArrayAccessWhereResultMustEqualIndex: Unit = {
        solve(task.copy(problemName = "array_access_where_result_must_equal_index"))
    }

    @Test
    @Category(Array(classOf[SatisfiabilityProblem]))
    def testArrayAccessWhereIndexIsChannelVariable: Unit = {
        solve(task.copy(problemName = "array_access_where_index_is_channel_variable"))
    }

    @Test
    @Category(Array(classOf[SatisfiabilityProblem], classOf[HasTableConstraint]))
    def testInconsistentProblem: Unit = {
        assertEx(
            solve(task.copy(problemName = "empty_table_int_test")),
            classOf[InconsistentProblemException])
    }

    @Test
    @Category(Array(classOf[MinimizationProblem]))
    def testMinimizationOfSumWithNegativeAddends: Unit = {
        solve(task.copy(problemName = "minimization_of_sum_with_negative_addends"))
    }

    @Test
    @Category(Array(classOf[MinimizationProblem]))
    def testMinimizationProblemWithBoundedDanglingObjectiveVariable: Unit = {
        solve(task.copy(problemName = "minimization_problem_with_bounded_dangling_objective_variable"))
    }

    @Test
    @Category(Array(classOf[MinimizationProblem]))
    def testMinimizationProblemWithUnboundedDanglingObjectiveVariable: Unit = {
        solve(task.copy(problemName = "minimization_problem_with_unbounded_dangling_objective_variable"))
    }

    @Test
    @Category(Array(classOf[MinimizationProblem], classOf[HasAlldifferentConstraint]))
    def testMinimizationProblemWithImplicitlyConstrainedObjectiveVariable: Unit = {
        val result = solveWithResult(task.copy(problemName = "minimization_problem_with_implicitly_constrained_objective_variable"))
        assertEq(result.space.numberOfConstraints(_.isInstanceOf[Alldistinct[_]]), 1)
        assert(result.space.isImplicitlyConstrainedSearchVariable(result.objective.objectiveVariables(1)))
        assertEq(quality(result), One)
    }

    @Test
    @Category(Array(classOf[MaximizationProblem]))
    def testMaximizationProblemWithBoundedDanglingObjectiveVariable: Unit = {
        solve(task.copy(problemName = "maximization_problem_with_bounded_dangling_objective_variable"))
    }

    @Test
    @Category(Array(classOf[MaximizationProblem]))
    def testMaximizationProblemWithUnboundedDanglingObjectiveVariable: Unit = {
        solve(task.copy(problemName = "maximization_problem_with_unbounded_dangling_objective_variable"))
    }

    @Test
    @Category(Array(classOf[MaximizationProblem], classOf[HasAlldifferentConstraint]))
    def testMaximizationProblemWithImplicitlyConstrainedObjectiveVariable: Unit = {
        val result = solveWithResult(task.copy(problemName = "maximization_problem_with_implicitly_constrained_objective_variable"))
        assertEq(result.space.numberOfConstraints(_.isInstanceOf[Alldistinct[_]]), 1)
        assert(result.space.isImplicitlyConstrainedSearchVariable(result.objective.objectiveVariables(1)))
        assertEq(quality(result), IntegerValue(512))
    }

    @Test
    @Category(Array(classOf[SatisfiabilityProblem]))
    def testProblemWithBoundedDanglingVariable: Unit = {
        solve(task.copy(problemName = "problem_with_bounded_dangling_variable"))
    }

    @Test
    @Category(Array(classOf[SatisfiabilityProblem]))
    def testProblemWithUnboundedDanglingVariable: Unit = {
        assertEx(
            solve(task.copy(problemName = "problem_with_unbounded_dangling_variable")),
            classOf[VariableWithInfiniteDomainException])
    }

    @Test
    @Category(Array(classOf[SatisfiabilityProblem]))
    def testProblemWithBoundedIrrelevantSearchVariable: Unit = {
        solve(task.copy(problemName = "problem_with_bounded_irrelevant_search_variable"))
    }

    @Test
    @Category(Array(classOf[SatisfiabilityProblem]))
    def testProblemWithUnboundedIrrelevantSearchVariable: Unit = {
        assertEx(
            solve(task.copy(problemName = "problem_with_unbounded_irrelevant_search_variable")),
            classOf[VariableWithInfiniteDomainException])
    }

    @Test
    @Category(Array(classOf[SatisfiabilityProblem]))
    def testProblemWithUnboundedRelevantSearchVariable: Unit = {
        assertEx(
            solve(task.copy(problemName = "problem_with_unbounded_relevant_search_variable")),
            classOf[VariableWithInfiniteDomainException])
    }

}

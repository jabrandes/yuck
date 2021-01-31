package yuck.flatzinc

import yuck.core.{DefaultRestartLimit, DefaultSeed, Distribution, DistributionFactory, Probability}

/**
 * @author Michael Marte
 *
 */
case class FlatZincLevelConfiguration(
    val guideOptimization: Boolean,
    val maybeFairVariableChoiceRate: Option[Probability])
{
    require(! guideOptimization || maybeFairVariableChoiceRate.getOrElse(Probability.from(0)).value < 1)
    require(maybeFairVariableChoiceRate.isEmpty || guideOptimization)
}

/**
 * @author Michael Marte
 *
 */
case class FlatZincSolverConfiguration(
    val attachGoals: Boolean = false,
    val seed: Long = DefaultSeed,
    val restartLimit: Int = DefaultRestartLimit,
    val numberOfThreads: Int = Runtime.getRuntime.availableProcessors,
    val maybeRoundLimit: Option[Int] = None,
    val maybeRuntimeLimitInSeconds: Option[Int] = None,
    val maybeTargetObjectiveValue: Option[Int] = None,
    val focusOnTopObjective: Boolean = true,
    val stopOnFirstSolution: Boolean = false,
    val runPresolver: Boolean = true,
    val useImplicitSolving: Boolean = true,
    val useProgressiveTightening: Boolean = true,
    val checkIncrementalCostUpdate: Boolean = false,
    val checkAssignmentsToNonChannelVariables: Boolean = false,
    val moveSizeDistribution: Distribution = DistributionFactory.createDistribution(1, List(90, 10)),
    val level0Configuration: FlatZincLevelConfiguration = FlatZincLevelConfiguration(true, Some(Probability.from(9))),
    val level1Configuration: FlatZincLevelConfiguration = FlatZincLevelConfiguration(true, Some(Probability.from(13))))
{
    require(restartLimit >= 0)
    require(numberOfThreads > 0)
    require(maybeRoundLimit.getOrElse(0) >= 0)
    require(maybeRuntimeLimitInSeconds.getOrElse(0) >= 0)
}

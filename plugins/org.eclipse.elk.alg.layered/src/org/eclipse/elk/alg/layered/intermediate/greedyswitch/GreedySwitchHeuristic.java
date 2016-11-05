/*******************************************************************************
 * Copyright (c) 2015 Kiel University and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Kiel University - initial API and implementation
 *******************************************************************************/
package org.eclipse.elk.alg.layered.intermediate.greedyswitch;

import org.eclipse.elk.alg.layered.graph.LNode;
import org.eclipse.elk.alg.layered.intermediate.greedyswitch.SwitchDecider.CrossingCountSide;
import org.eclipse.elk.alg.layered.p3order.GraphInfoHolder;
import org.eclipse.elk.alg.layered.p3order.ICrossingMinimizationHeuristic;
import org.eclipse.elk.alg.layered.p3order.LayerSweepCrossingMinimizer.CrossMinType;
import org.eclipse.elk.alg.layered.p3order.counting.AbstractInitializer;

/**
 * <p>
 * Implements the greedy switch heuristic: For two neighboring nodes, check to see if by exchanging their positions
 * ("switching" them) the number of crossings is reduced. If it is, switch them, if it is not, don't. This principle is
 * continued throughout the graph for all nodes in each layer.
 * </p>
 * <p>
 * Configuration depends on the {@link CrossMinType} set on the graph:
 * </p>
 * <ul>
 * <li>{@link CrossMinType#ONE_SIDED_GREEDY_SWITCH}: fixes the order of one layer and changes the order in a neighboring
 * layer using the number of crossings to this neighboring layer. To prevent increasing the number of crosses, after
 * each forward and backward sweep the number of crossings in the graph are recounted.
 * <li>{@link CrossMinType#TWO_SIDED_GREEDY_SWITCH}: If one sided is not false, it sets a layer as free and counts
 * crossings to both neighboring layers.
 *
 * </ul>
 *
 * @author alan
 */
public class GreedySwitchHeuristic implements ICrossingMinimizationHeuristic {

    private final CrossMinType greedySwitchType;
    private LNode[][] currentNodeOrder;
    private SwitchDecider switchDecider;
    private int[] portPositions;
    private GraphInfoHolder graphData;
    private AbstractInitializer initializer;

    /**
     * Create GreedySwitchHeuristic.
     *
     * @param greedyType
     *            The greedy switch type.
     * @param graphData
     *            Graph information holder for crossing minimization.
     */
    public GreedySwitchHeuristic(final CrossMinType greedyType, final GraphInfoHolder graphData) {
        initializer = new Initializer(graphData.currentNodeOrder());
        this.graphData = graphData;
        greedySwitchType = greedyType;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean minimizeCrossings(final LNode[][] order, final int freeLayerIndex,
            final boolean forwardSweep, final boolean isFirstSweep) {
        setUp(order, freeLayerIndex, forwardSweep);
        return continueSwitchingUntilNoImprovementInLayer(freeLayerIndex);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean setFirstLayerOrder(final LNode[][] currentOrder, final boolean isForwardSweep) {
        int startIndex = startIndex(isForwardSweep, currentOrder.length);
        setUp(currentOrder, startIndex, isForwardSweep);
        return sweepDownwardInLayer(startIndex);
    }

    private void setUp(final LNode[][] order, final int freeLayerIndex,
            final boolean forwardSweep) {
        currentNodeOrder = order;
        CrossingCountSide side = forwardSweep ? CrossingCountSide.WEST : CrossingCountSide.EAST;
        switchDecider = getNewSwitchDecider(freeLayerIndex, side);
    }

    private SwitchDecider getNewSwitchDecider(final int freeLayerIndex, final CrossingCountSide side) {
        CrossingMatrixFiller crossingMatrixFiller =
                new CrossingMatrixFiller(greedySwitchType, currentNodeOrder, freeLayerIndex, side);
        return new SwitchDecider(freeLayerIndex, currentNodeOrder, crossingMatrixFiller, portPositions,
                graphData, greedySwitchType == CrossMinType.ONE_SIDED_GREEDY_SWITCH);
    }

    private boolean continueSwitchingUntilNoImprovementInLayer(final int freeLayerIndex) {
        boolean improved = false;
        boolean continueSwitching;
        do {
            continueSwitching = sweepDownwardInLayer(freeLayerIndex);
            improved |= continueSwitching;
        } while (continueSwitching);
        return improved;
    }

    private boolean sweepDownwardInLayer(final int layerIndex) {
        boolean continueSwitching = false;
        int lengthOfFreeLayer = currentNodeOrder[layerIndex].length;
        for (int upperNodeIndex = 0; upperNodeIndex < lengthOfFreeLayer - 1; upperNodeIndex++) {
            int lowerNodeIndex = upperNodeIndex + 1;

            continueSwitching |= switchIfImproves(layerIndex, upperNodeIndex, lowerNodeIndex);
        }
        return continueSwitching;
    }

    private boolean switchIfImproves(final int layerIndex, final int upperNodeIndex, final int lowerNodeIndex) {
        boolean continueSwitching = false;

        if (switchDecider.doesSwitchReduceCrossings(upperNodeIndex, lowerNodeIndex)) {
            exchangeNodes(upperNodeIndex, lowerNodeIndex, layerIndex);

            continueSwitching = true;
        }
        return continueSwitching;
    }

    private void exchangeNodes(final int indexOne, final int indexTwo, final int layerIndex) {
        switchDecider.notifyOfSwitch(currentNodeOrder[layerIndex][indexOne],
                currentNodeOrder[layerIndex][indexTwo]);
        LNode[] layer = currentNodeOrder[layerIndex];
        LNode temp = layer[indexTwo];
        layer[indexTwo] = layer[indexOne];
        layer[indexOne] = temp;
    }

    private int startIndex(final boolean isForwardSweep, final int length) {
        return isForwardSweep ? 0 : length - 1;
    }

    @Override
    public boolean alwaysImproves() {
        return !(greedySwitchType == CrossMinType.ONE_SIDED_GREEDY_SWITCH);
    }

    @Override
    public boolean isDeterministic() {
        return true;
    }

    @Override
    public AbstractInitializer initializer() {
        return initializer;
    }

    /** Defines information to be initialized during graph traversal. */
    private final class Initializer extends AbstractInitializer {
        private int nPorts;
        private Initializer(final LNode[][] graph) {
            super(graph);
        }

        @Override
        public void initAtPortLevel(final int l, final int n, final int p) {
            nPorts++;
        }

        @Override
        public void initAtLayerLevel(final int l) {
            nodeOrder()[l][0].getLayer().id = l;
        }

        @Override
        public void initAfterTraversal() {
            portPositions = new int[nPorts];
        }
    }

}

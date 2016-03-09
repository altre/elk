/*
 * KIELER - Kiel Integrated Environment for Layout Eclipse RichClient
 *
 * http://www.informatik.uni-kiel.de/rtsys/kieler/
 *
 * Copyright 2014 by
 * + Kiel University
 *   + Department of Computer Science
 *     + Real-Time and Embedded Systems Group
 *
 * This code is provided under the terms of the Eclipse Public License (EPL).
 * See the file epl-v10.html for the license text.
 */
package org.eclipse.elk.alg.layered.intermediate.greedyswitch;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import org.eclipse.elk.alg.layered.graph.LNode;
import org.eclipse.elk.alg.layered.graph.LPort;
import org.eclipse.elk.alg.layered.graph.Layer;
import org.eclipse.elk.alg.layered.p3order.counting.NorthSouthEdgeAllCrossingsCounter;
import org.eclipse.elk.core.options.CoreOptions;
import org.eclipse.elk.core.options.EdgeRouting;
import org.eclipse.elk.core.options.PortConstraints;
import org.eclipse.elk.core.options.PortSide;
import org.junit.Test;

/**
 * Tests {@link NorthSouthEdgeAllCrossingsCounter}.
 *
 * @author alan
 *
 */
public class NorthSouthEdgeAllCrossingsCounterTest extends NorthSouthEdgeTestGraphCreator {
	private NorthSouthEdgeAllCrossingsCounter counter;

	// CHECKSTYLEOFF javadoc
	// CHECKSTYLEOFF MagicNumber

	@Test
	public void northernNorthSouthNodeSingleCrossing() {
		getNorthSouthUpwardCrossingGraph();
		int crossingCount = initCounterForLayerWithIndexAndCountInLayer(0);
		assertThat(crossingCount, is(1));
	}

	@Test
	public void northernNorthSouthNodeMultipleCrossings() {
		getNorthSouthUpwardMultipleCrossingGraph();
		int crossingCount = initCounterForLayerWithIndexAndCountInLayer(0);
		assertThat(crossingCount, is(3));
	}

	@Test
	public void southernTowEdgeEastCrossing() {
		getNorthSouthDownwardCrossingGraph();
		int crossingCount = initCounterForLayerWithIndexAndCountInLayer(0);
		assertThat(crossingCount, is(1));
	}

	@Test
	public void southernNorthSouthMultipleNodeCrossing() {
		getNorthSouthDownwardMultipleCrossingGraph();
		int crossingCount = initCounterForLayerWithIndexAndCountInLayer(0);
		assertThat(crossingCount, is(3));
	}

	@Test
	public void southernTwoWesternEdges() {
		getNorthSouthSouthernTwoWesternEdges();
		int crossingCount = initCounterForLayerWithIndexAndCountInLayer(1);
		assertThat(crossingCount, is(1));
	}

	@Test
	public void southernThreeWesternEdges() {
		getNorthSouthSouthernThreeWesternEdges();
		int crossingCount = initCounterForLayerWithIndexAndCountInLayer(1);
		assertThat(crossingCount, is(3));
	}

	@Test
	public void northSouthEdgesComeFromBothSidesDontCross() {
		getSouthernNorthSouthGraphEdgesFromEastAndWestNoCrossings();
		int crossingCount = initCounterForLayerWithIndexAndCountInLayer(1);
		assertThat(crossingCount, is(0));
	}

	@Test
	public void southernNorthSouthEdgesBothToEastDontCross() {
		getSouthernNorthSouthEdgesBothToEast();
		int crossingCount = initCounterForLayerWithIndexAndCountInLayer(0);
		assertThat(crossingCount, is(0));
	}

	@Test
	public void northSouthEdgesComeFromBothSidesDoCross() {
		getNorthSouthEdgesFromEastAndWestAndCross();

		int crossingCount = initCounterForLayerWithIndexAndCountInLayer(1);

		assertThat(crossingCount, is(1));
	}

	@Test
	public void northernBothEdgesWestern() {
		getNorthSouthNorthernWesternEdges();
		int crossingCount = initCounterForLayerWithIndexAndCountInLayer(0);
		assertThat(crossingCount, is(0));
	}

	@Test
	public void northernEasternPortToWestWesternPortToEast() {
		getNorthSouthNorthernEasternPortToWestWesternPortToEast();
		int crossingCount = initCounterForLayerWithIndexAndCountInLayer(1);

		assertThat(crossingCount, is(1));
	}

	@Test
	public void allSidesMultipleCrossings() {
		getNorthSouthAllSidesMultipleCrossings();
		int crossingCount = initCounterForLayerWithIndexAndCountInLayer(1);
		assertThat(crossingCount, is(4));
	}

	@Test
	public void noFixedOrderConstraint() {
		getNorthSouthDownwardCrossingGraph();
		graph.toNodeArray()[0][0].setProperty(CoreOptions.PORT_CONSTRAINTS, PortConstraints.FIXED_SIDE);
		int crossingCount = initCounterForLayerWithIndexAndCountInLayer(0);
		assertThat(crossingCount, is(0));
	}

	@Test
	public void oneEdgeDummyIsCrossedByOneSouthernNorthSouthPortEdge() {
		getSouthernNorthSouthDummyEdgeCrossingGraph();
		int crossingCount = initCounterForLayerWithIndexAndCountInLayer(1);
		assertThat(crossingCount, is(1));
	}

	@Test
	public void oneEdgeDummyIsCrossedByTwoSouthernNorthSouthPortEdges() {
		getSouthernNorthSouthDummyEdgeTwoCrossingGraph();
		int crossingCount = initCounterForLayerWithIndexAndCountInLayer(1);
		assertThat(crossingCount, is(2));
	}

	@Test
	public void southernTwoDummyEdgeAndTwoNorthSouthShouldCrossFourTimes() {
		getSouthernTwoDummyEdgeAndNorthSouthCrossingGraph();
		int crossingCount = initCounterForLayerWithIndexAndCountInLayer(1);
		assertThat(crossingCount, is(4));
	}

	@Test
	public void normalNodesNorthSouthEdgesHaveCrossingsToLongEdgeDummyOnBothSides() {
		getMultipleNorthSouthAndLongEdgeDummiesOnBothSides();
		int crossingCount = initCounterForLayerWithIndexAndCountInLayer(1);
		assertThat(crossingCount, is(4));
	}

	@Test
	public void ignoresUnconnectedPortsForNormalNodeAndLongEdgeDummies() {
		getLongEdgeDummyAndNormalNodeWithUnusedPortsOnSouthernSide();
		int crossingCount = initCounterForLayerWithIndexAndCountInLayer(1);
		assertThat(crossingCount, is(0));
	}

	@Test
	public void noNorthSouthNode() {
		getCrossFormedGraph();
		int crossingCount = initCounterForLayerWithIndexAndCountInLayer(0);
		assertThat(crossingCount, is(0));
	}

	@Test
	public void northSouthCrossingRemovedAfterSwitch() {
		getNorthSouthUpwardCrossingGraph();
		LNode[] layer = graph.toNodeArray()[0];

		initCounterForLayerWithIndexAndCountInLayer(0);
		switchNodes(layer, 0, 1);
		counter.notifyNodeSwitch(layer[0], layer[1]);

		int crossingCount = counter.countCrossings(layer);

		assertThat(crossingCount, is(0));
	}

	/**
	 * <pre>
	 *
	 * *----
	 *    /+--*
	 *   --+--*
	 *   | |
	 *  _|_|_
	 *  |   |
	 *  |___|
	 *  .
	 * </pre>
	 *
	 */
	@Test
	public void givenPolylineRoutingWhenMoreThanOneEdgeIntoNSNode_countsTheseToo() {
		LNode leftNode = addNodeToLayer(makeLayer(graph));
		LNode[] middleNodes = addNodesToLayer(3, makeLayer(graph));
		LNode[] rightNodes = addNodesToLayer(2, makeLayer(graph));

		setFixedOrderConstraint(middleNodes[2]);

		// ports are added in clockwise fashion!
		addNorthSouthEdge(PortSide.NORTH, middleNodes[2], middleNodes[1], rightNodes[0], false);
		addNorthSouthEdge(PortSide.NORTH, middleNodes[2], middleNodes[0], leftNode, true);
		// second edge on middle node
		LPort middleNodePort = middleNodes[1].getPorts().get(0);
		eastWestEdgeFromTo(middleNodePort, rightNodes[1]);
		getGraph().setProperty(CoreOptions.EDGE_ROUTING, EdgeRouting.POLYLINE);

		int crossingCount = initCounterForLayerWithIndexAndCountInLayer(1);

		assertThat(crossingCount, is(2));
	}

	private void switchNodes(final LNode[] layer, final int i, final int j) {
		LNode firstNode = layer[i];
		layer[i] = layer[j];
		layer[j] = firstNode;
	}

	private int initCounterForLayerWithIndexAndCountInLayer(final int layerIndex) {
		setUpIds();
		int numPorts = 0;
		for (Layer l : graph) {
			for (LNode lNode : l) {
				numPorts += lNode.getPorts().size();
			}
		}
		counter = new NorthSouthEdgeAllCrossingsCounter(new int[numPorts]);
		return counter.countCrossings(graph.toNodeArray()[layerIndex]);
	}

}

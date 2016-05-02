/*******************************************************************************
 * Copyright (c) 2010, 2015 Kiel University and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Kiel University - initial API and implementation
 *******************************************************************************/
package org.eclipse.elk.alg.layered.intermediate;

import java.util.List;

import org.eclipse.elk.alg.layered.ILayoutProcessor;
import org.eclipse.elk.alg.layered.graph.LGraph;
import org.eclipse.elk.alg.layered.graph.LNode;
import org.eclipse.elk.alg.layered.graph.LNode.NodeType;
import org.eclipse.elk.alg.layered.graph.LPort;
import org.eclipse.elk.alg.layered.graph.Layer;
import org.eclipse.elk.alg.layered.properties.InternalProperties;
import org.eclipse.elk.alg.layered.properties.LayeredOptions;
import org.eclipse.elk.core.options.Alignment;
import org.eclipse.elk.core.options.PortSide;
import org.eclipse.elk.core.util.IElkProgressMonitor;

import com.google.common.collect.Lists;

/**
 * Sets the width of hierarchical port dummies and sets the layer alignment of North/South port dummies
 * to Center.
 * 
 * <p>To see why this is necessary, please refer to the processor's Wiki documentation.</p>
 * 
 * <dl>
 *   <dt>Precondition:</dt>
 *     <dd>A layered graph with finished node ordering</dd>
 *     <dd>Node order respects in-layer constraints.</dd>
 *   <dt>Postcondition:</dt>
 *     <dd>External port dummies are assigned a width.</dd>
 *     <dd>Layer alignment of North/South external port dummies is set to Center.</dd> 
 *   <dt>Slots:</dt>
 *     <dd>Before phase 4.</dd>
 *   <dt>Same-slot dependencies:</dt>
 *     <dd>None.</dd>
 * </dl>
 * 
 * @see HierarchicalPortConstraintProcessor
 * @see HierarchicalPortOrthogonalEdgeRouter
 * @see HierarchicalPortPositionProcessor
 * @author cds
 * @kieler.design 2012-08-10 chsch grh
 * @kieler.rating proposed yellow by msp
 */
public final class HierarchicalPortDummySizeProcessor implements ILayoutProcessor {

    /**
     * {@inheritDoc}
     */
    public void process(final LGraph layeredGraph, final IElkProgressMonitor monitor) {
        monitor.begin("Hierarchical port dummy size processing", 1);

        List<LNode> northernDummies = Lists.newArrayList();
        List<LNode> southernDummies = Lists.newArrayList();
        
        // Calculate the width difference (this assumes CENTER node alignment)
        double normalSpacing = layeredGraph.getProperty(LayeredOptions.SPACING_NODE).doubleValue();
        double smallSpacing = normalSpacing * layeredGraph.getProperty(LayeredOptions.SPACING_EDGE_SPACING_FACTOR);
        double delta = smallSpacing * 2;
        
        // Iterate through the layers
        for (Layer layer : layeredGraph) {
            northernDummies.clear();
            southernDummies.clear();
            
            // Collect northern and southern hierarchical port dummies
            for (LNode node : layer) {
                if (node.getType() == NodeType.EXTERNAL_PORT) {
                    PortSide side = node.getProperty(InternalProperties.EXT_PORT_SIDE);
                    
                    if (side == PortSide.NORTH) {
                        northernDummies.add(node);
                    } else if (side == PortSide.SOUTH) {
                        southernDummies.add(node);
                    }
                }
            }
            
            // Set widths
            setWidths(northernDummies, true, delta);
            setWidths(southernDummies, false, delta);
        }
        
        monitor.done();
    }
    
    /**
     * Sets the widths of the given list of nodes and sets their layer alignment properly.
     * 
     * @param nodes the list of nodes.
     * @param topDown {@code true} if the nodes should widen with increasing index, {@code false}
     *                if it should be the other way round.
     * @param delta the width difference from one node to the next.
     */
    private void setWidths(final List<LNode> nodes, final boolean topDown, final double delta) {
        double currentWidth = 0.0;
        double step = delta;
        
        if (!topDown) {
            // Start with the widest node, decreasing node size
            currentWidth = delta * (nodes.size() - 1);
            step *= -1.0;
        }
        
        for (LNode node : nodes) {
            node.setProperty(LayeredOptions.ALIGNMENT, Alignment.CENTER);
            node.getSize().x = currentWidth;
            
            // Move eastern ports to the node's right border
            for (LPort port : node.getPorts(PortSide.EAST)) {
                port.getPosition().x = currentWidth;
            }
            
            currentWidth += step;
        }
    }
    
}

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
package org.eclipse.elk.alg.layered;

import org.eclipse.elk.alg.layered.graph.LGraph;
import org.eclipse.elk.alg.layered.graph.transform.IGraphTransformer;
import org.eclipse.elk.alg.layered.graph.transform.KGraphTransformer;
import org.eclipse.elk.alg.layered.properties.LayeredOptions;
import org.eclipse.elk.core.AbstractLayoutProvider;
import org.eclipse.elk.core.klayoutdata.KShapeLayout;
import org.eclipse.elk.core.options.HierarchyHandling;
import org.eclipse.elk.core.util.IElkProgressMonitor;
import org.eclipse.elk.graph.KNode;

/**
 * Layout provider to connect the layered layouter to the Eclipse based layout services.
 * 
 * @see KlayLayered
 * 
 * @author msp
 * @author cds
 * @kieler.design 2012-08-10 chsch grh
 * @kieler.rating yellow 2014-11-09 review KI-56 by chsch, als
 */
public final class LayeredLayoutProvider extends AbstractLayoutProvider {

    ///////////////////////////////////////////////////////////////////////////////
    // Variables

    /** the layout algorithm used for regular layout runs. */
    private final KlayLayered klayLayered = new KlayLayered();


    ///////////////////////////////////////////////////////////////////////////////
    // Regular Layout

    /**
     * {@inheritDoc}
     */
    @Override
    public void layout(final KNode kgraph, final IElkProgressMonitor progressMonitor) {
        // Import the graph (layeredGraph won't be null since the KGraphImporter always returns an
        // LGraph instance, even though the IGraphImporter interface would allow null as a return
        // value)
        IGraphTransformer<KNode> graphImporter = new KGraphTransformer();
        LGraph layeredGraph = graphImporter.importGraph(kgraph);

        // Check if hierarchy handling for a compound graph is requested
        KShapeLayout kgraphLayout = kgraph.getData(KShapeLayout.class);
        if (kgraphLayout.getProperty(LayeredOptions.HIERARCHY_HANDLING) 
                == HierarchyHandling.INCLUDE_CHILDREN) {

            // Layout for all hierarchy levels is requested
            klayLayered.doCompoundLayout(layeredGraph, progressMonitor);
            
        } else {
            
            // Only the top-level graph is processed
            klayLayered.doLayout(layeredGraph, progressMonitor);
        }
        
        if (!progressMonitor.isCanceled()) {
            // Apply the layout results to the original graph
            graphImporter.applyLayout(layeredGraph);
        }
    }


    ///////////////////////////////////////////////////////////////////////////////
    // Layout Testing
    
    /**
     * Import the given KGraph and return a test execution state prepared for a test run with the
     * resulting {@link LGraph}. The layout test run methods can immediately be called on the
     * returned object.
     * 
     * <p><strong>Note:</strong> This method does not apply the layout back to the original KGraph!</p>
     * 
     * @param kgraph the KGraph to be used for the layout test run.
     * @return an initialized test execution state
     */
    public KlayLayered.TestExecutionState startLayoutTest(final KNode kgraph) {
        // Import the graph (layeredGraph won't be null since the KGraphImporter always returns an
        // LGraph instance, even though the IGraphImporter interface would allow null as a return
        // value)
        IGraphTransformer<KNode> graphImporter = new KGraphTransformer();
        LGraph layeredGraph = graphImporter.importGraph(kgraph);
        
        // Prepare a layout test and return the test execution state
        return klayLayered.prepareLayoutTest(layeredGraph);
    }
    
    /**
     * Return the layered layout algorithm.
     * 
     * @return the layout algorithm
     */
    public KlayLayered getLayoutAlgorithm() {
        return klayLayered;
    }

}

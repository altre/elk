/**
 * Copyright (c) 2015 Kiel University and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *    spoenemann - initial API and implementation
 */
package org.eclipse.elk.alg.graphviz.layouter;

import java.util.EnumSet;
import org.eclipse.elk.alg.graphviz.dot.transform.NeatoModel;
import org.eclipse.elk.alg.graphviz.dot.transform.OverlapMode;
import org.eclipse.elk.core.data.ILayoutMetaDataProvider;
import org.eclipse.elk.graph.properties.IProperty;
import org.eclipse.elk.graph.properties.Property;

@SuppressWarnings("all")
public class GraphvizMetaDataProvider implements ILayoutMetaDataProvider {
  /**
   * Default value for {@link #ADAPT_PORT_POSITIONS}.
   */
  private final static boolean ADAPT_PORT_POSITIONS_DEFAULT = true;
  
  /**
   * Whether ports should be moved to the point where edges cross the node's bounds.
   */
  public final static IProperty<Boolean> ADAPT_PORT_POSITIONS = new Property<Boolean>(
            "org.eclipse.elk.graphviz.adaptPortPositions",
            ADAPT_PORT_POSITIONS_DEFAULT,
            null,
            null);
  
  /**
   * Default value for {@link #CONCENTRATE}.
   */
  private final static boolean CONCENTRATE_DEFAULT = false;
  
  /**
   * Merges multiedges into a single edge and causes partially parallel edges to share part of
   * their paths.
   */
  public final static IProperty<Boolean> CONCENTRATE = new Property<Boolean>(
            "org.eclipse.elk.graphviz.concentrate",
            CONCENTRATE_DEFAULT,
            null,
            null);
  
  /**
   * Terminating condition. If the length squared of all energy gradients are less than
   * epsilon, the algorithm stops.
   */
  public final static IProperty<Float> EPSILON = new Property<Float>(
            "org.eclipse.elk.graphviz.epsilon");
  
  /**
   * Multiplicative scale factor for the maximal number of iterations used during crossing
   * minimization, node ranking, and node positioning.
   */
  public final static IProperty<Float> ITERATIONS_FACTOR = new Property<Float>(
            "org.eclipse.elk.graphviz.iterationsFactor");
  
  /**
   * Default value for {@link #LABEL_ANGLE}.
   */
  private final static float LABEL_ANGLE_DEFAULT = (-25);
  
  /**
   * Angle between head / tail positioned edge labels and the corresponding edge.
   */
  public final static IProperty<Float> LABEL_ANGLE = new Property<Float>(
            "org.eclipse.elk.graphviz.labelAngle",
            LABEL_ANGLE_DEFAULT,
            null,
            null);
  
  /**
   * Default value for {@link #LABEL_DISTANCE}.
   */
  private final static float LABEL_DISTANCE_DEFAULT = 1;
  
  /**
   * Distance of head / tail positioned edge labels to the source or target node.
   */
  public final static IProperty<Float> LABEL_DISTANCE = new Property<Float>(
            "org.eclipse.elk.graphviz.labelDistance",
            LABEL_DISTANCE_DEFAULT,
            null,
            null);
  
  /**
   * Default value for {@link #LAYER_SPACING_FACTOR}.
   */
  private final static float LAYER_SPACING_FACTOR_DEFAULT = 1;
  
  /**
   * Factor for the spacing of different layers (ranks).
   */
  public final static IProperty<Float> LAYER_SPACING_FACTOR = new Property<Float>(
            "org.eclipse.elk.graphviz.layerSpacingFactor",
            LAYER_SPACING_FACTOR_DEFAULT,
            null,
            null);
  
  /**
   * The maximum number of iterations.
   */
  public final static IProperty<Integer> MAXITER = new Property<Integer>(
            "org.eclipse.elk.graphviz.maxiter");
  
  /**
   * Default value for {@link #NEATO_MODEL}.
   */
  private final static NeatoModel NEATO_MODEL_DEFAULT = NeatoModel.SHORTPATH;
  
  /**
   * Specifies how the distance matrix is computed for the input graph.
   */
  public final static IProperty<NeatoModel> NEATO_MODEL = new Property<NeatoModel>(
            "org.eclipse.elk.graphviz.neatoModel",
            NEATO_MODEL_DEFAULT,
            null,
            null);
  
  /**
   * Default value for {@link #OVERLAP_MODE}.
   */
  private final static OverlapMode OVERLAP_MODE_DEFAULT = OverlapMode.PRISM;
  
  /**
   * Determines if and how node overlaps should be removed.
   */
  public final static IProperty<OverlapMode> OVERLAP_MODE = new Property<OverlapMode>(
            "org.eclipse.elk.graphviz.overlapMode",
            OVERLAP_MODE_DEFAULT,
            null,
            null);
  
  public void apply(final org.eclipse.elk.core.data.ILayoutMetaDataProvider.Registry registry) {
    registry.register(new org.eclipse.elk.core.data.LayoutOptionData(
        "org.eclipse.elk.graphviz.adaptPortPositions",
        "",
        "Adapt Port Positions",
        "Whether ports should be moved to the point where edges cross the node\'s bounds.",
        ADAPT_PORT_POSITIONS_DEFAULT,
        null,
        null,
        org.eclipse.elk.core.data.LayoutOptionData.Type.BOOLEAN,
        Boolean.class,
        EnumSet.of(org.eclipse.elk.core.data.LayoutOptionData.Target.PARENTS),
        org.eclipse.elk.core.data.LayoutOptionData.Visibility.ADVANCED
    ));
    registry.register(new org.eclipse.elk.core.data.LayoutOptionData(
        "org.eclipse.elk.graphviz.concentrate",
        "",
        "Concentrate Edges",
        "Merges multiedges into a single edge and causes partially parallel edges to share part of their paths.",
        CONCENTRATE_DEFAULT,
        null,
        null,
        org.eclipse.elk.core.data.LayoutOptionData.Type.BOOLEAN,
        Boolean.class,
        EnumSet.of(org.eclipse.elk.core.data.LayoutOptionData.Target.PARENTS),
        org.eclipse.elk.core.data.LayoutOptionData.Visibility.ADVANCED
    ));
    registry.register(new org.eclipse.elk.core.data.LayoutOptionData(
        "org.eclipse.elk.graphviz.epsilon",
        "",
        "Epsilon",
        "Terminating condition. If the length squared of all energy gradients are less than epsilon, the algorithm stops.",
        null,
        null,
        null,
        org.eclipse.elk.core.data.LayoutOptionData.Type.FLOAT,
        Float.class,
        EnumSet.of(org.eclipse.elk.core.data.LayoutOptionData.Target.PARENTS),
        org.eclipse.elk.core.data.LayoutOptionData.Visibility.VISIBLE
    ));
    registry.register(new org.eclipse.elk.core.data.LayoutOptionData(
        "org.eclipse.elk.graphviz.iterationsFactor",
        "",
        "Iterations Factor",
        "Multiplicative scale factor for the maximal number of iterations used during crossing minimization, node ranking, and node positioning.",
        null,
        null,
        null,
        org.eclipse.elk.core.data.LayoutOptionData.Type.FLOAT,
        Float.class,
        EnumSet.of(org.eclipse.elk.core.data.LayoutOptionData.Target.PARENTS),
        org.eclipse.elk.core.data.LayoutOptionData.Visibility.ADVANCED
    ));
    registry.register(new org.eclipse.elk.core.data.LayoutOptionData(
        "org.eclipse.elk.graphviz.labelAngle",
        "",
        "Label Angle",
        "Angle between head / tail positioned edge labels and the corresponding edge.",
        LABEL_ANGLE_DEFAULT,
        null,
        null,
        org.eclipse.elk.core.data.LayoutOptionData.Type.FLOAT,
        Float.class,
        EnumSet.of(org.eclipse.elk.core.data.LayoutOptionData.Target.EDGES),
        org.eclipse.elk.core.data.LayoutOptionData.Visibility.VISIBLE
    ));
    registry.register(new org.eclipse.elk.core.data.LayoutOptionData(
        "org.eclipse.elk.graphviz.labelDistance",
        "",
        "Label Distance",
        "Distance of head / tail positioned edge labels to the source or target node.",
        LABEL_DISTANCE_DEFAULT,
        null,
        null,
        org.eclipse.elk.core.data.LayoutOptionData.Type.FLOAT,
        Float.class,
        EnumSet.of(org.eclipse.elk.core.data.LayoutOptionData.Target.EDGES),
        org.eclipse.elk.core.data.LayoutOptionData.Visibility.VISIBLE
    ));
    registry.register(new org.eclipse.elk.core.data.LayoutOptionData(
        "org.eclipse.elk.graphviz.layerSpacingFactor",
        "",
        "Layer Spacing Factor",
        "Factor for the spacing of different layers (ranks).",
        LAYER_SPACING_FACTOR_DEFAULT,
        null,
        null,
        org.eclipse.elk.core.data.LayoutOptionData.Type.FLOAT,
        Float.class,
        EnumSet.of(org.eclipse.elk.core.data.LayoutOptionData.Target.PARENTS),
        org.eclipse.elk.core.data.LayoutOptionData.Visibility.VISIBLE
    ));
    registry.register(new org.eclipse.elk.core.data.LayoutOptionData(
        "org.eclipse.elk.graphviz.maxiter",
        "",
        "Max. Iterations",
        "The maximum number of iterations.",
        null,
        null,
        null,
        org.eclipse.elk.core.data.LayoutOptionData.Type.INT,
        Integer.class,
        EnumSet.of(org.eclipse.elk.core.data.LayoutOptionData.Target.PARENTS),
        org.eclipse.elk.core.data.LayoutOptionData.Visibility.ADVANCED
    ));
    registry.register(new org.eclipse.elk.core.data.LayoutOptionData(
        "org.eclipse.elk.graphviz.neatoModel",
        "",
        "Distance Model",
        "Specifies how the distance matrix is computed for the input graph.",
        NEATO_MODEL_DEFAULT,
        null,
        null,
        org.eclipse.elk.core.data.LayoutOptionData.Type.ENUM,
        NeatoModel.class,
        EnumSet.of(org.eclipse.elk.core.data.LayoutOptionData.Target.PARENTS),
        org.eclipse.elk.core.data.LayoutOptionData.Visibility.ADVANCED
    ));
    registry.register(new org.eclipse.elk.core.data.LayoutOptionData(
        "org.eclipse.elk.graphviz.overlapMode",
        "",
        "Overlap Removal",
        "Determines if and how node overlaps should be removed.",
        OVERLAP_MODE_DEFAULT,
        null,
        null,
        org.eclipse.elk.core.data.LayoutOptionData.Type.ENUM,
        OverlapMode.class,
        EnumSet.of(org.eclipse.elk.core.data.LayoutOptionData.Target.PARENTS),
        org.eclipse.elk.core.data.LayoutOptionData.Visibility.VISIBLE
    ));
    new org.eclipse.elk.alg.graphviz.layouter.DotOptions().apply(registry);
    new org.eclipse.elk.alg.graphviz.layouter.NeatoOptions().apply(registry);
    new org.eclipse.elk.alg.graphviz.layouter.FdpOptions().apply(registry);
    new org.eclipse.elk.alg.graphviz.layouter.TwopiOptions().apply(registry);
    new org.eclipse.elk.alg.graphviz.layouter.CircoOptions().apply(registry);
  }
}

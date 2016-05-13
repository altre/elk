/*******************************************************************************
 * Copyright (c) 2016 TypeFox GmbH and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    spoenemann - initial API and implementation
 *******************************************************************************/
package org.eclipse.elk.core;

import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumSet;
import java.util.List;
import java.util.Map;

import org.eclipse.elk.core.data.LayoutOptionData;
import org.eclipse.elk.core.klayoutdata.KEdgeLayout;
import org.eclipse.elk.core.klayoutdata.KLayoutData;
import org.eclipse.elk.core.klayoutdata.KShapeLayout;
import org.eclipse.elk.core.util.IValidatingGraphElementVisitor;
import org.eclipse.elk.graph.KEdge;
import org.eclipse.elk.graph.KGraphElement;
import org.eclipse.elk.graph.properties.IProperty;

/**
 * A validator for lower and upper bounds of layout options.
 */
public class LayoutOptionValidator implements IValidatingGraphElementVisitor {
    
    /** The issues found by this validator. */
    private final List<GraphIssue> issues = new ArrayList<GraphIssue>();

    /**
     * Check the lower and upper bounds of all properties attached to graph elements.
     */
    @SuppressWarnings("unchecked")
    @Override
    public void visit(final KGraphElement element) {
        KLayoutData layoutData = element instanceof KEdge
                ? element.getData(KEdgeLayout.class)
                : element.getData(KShapeLayout.class);
        for (Map.Entry<IProperty<?>, Object> entry : layoutData.getProperties()) {
            issues.addAll(checkProperty((IProperty<Object>) entry.getKey(), entry.getValue(), element));
        }
    }
    
    /**
     * Check the lower and upper bounds of the given property.
     */
    public List<GraphIssue> checkProperty(final IProperty<Object> property, final Object value,
            final KGraphElement element) {
        String optionName = null;
        if (property instanceof LayoutOptionData) {
            LayoutOptionData optionData = (LayoutOptionData) property;
            optionName = optionData.getName();
            if (value != null) {
                if (!isValidType(optionData, value)) {
                    String message = "The assigned value " + value.toString()
                            + " of the option '" + optionName
                            + "' does not match the type " + optionData.getOptionClass().getSimpleName() + ".";
                    return Collections.singletonList(new GraphIssue(element, message, GraphIssue.Severity.ERROR));
                }
            }
        }
        if (value != null) {
            if (property.getLowerBound().compareTo(value) > 0) {
                if (optionName == null) {
                    optionName = property.getId();
                }
                String message = "The assigned value " + value.toString()
                        + " of the option '" + optionName
                        + "' is less than the lower bound " + property.getLowerBound().toString() + ".";
                return Collections.singletonList(new GraphIssue(element, message, GraphIssue.Severity.ERROR));
            } else if (property.getUpperBound().compareTo(value) < 0) {
                if (optionName == null) {
                    optionName = property.getId();
                }
                String message = "The assigned value " + value.toString()
                        + " of the option '" + optionName
                        + "' is greater than the upper bound " + property.getUpperBound().toString() + ".";
                return Collections.singletonList(new GraphIssue(element, message, GraphIssue.Severity.ERROR));
            }
        }
        return Collections.emptyList();
    }
    
    /**
     * @return whether the given value has the correct type
     */
    protected boolean isValidType(final LayoutOptionData optionData, final Object value) {
        switch (optionData.getType()) {
            case STRING:
                return value instanceof String;
            case BOOLEAN:
                return value instanceof Boolean;
            case INT:
                return value instanceof Integer;
            case FLOAT:
                return value instanceof Float;
            case ENUMSET:
                return value instanceof EnumSet<?>;
            default:
                return optionData.getOptionClass().isInstance(value);
        }
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public List<GraphIssue> getIssues() {
        return issues;
    }

}

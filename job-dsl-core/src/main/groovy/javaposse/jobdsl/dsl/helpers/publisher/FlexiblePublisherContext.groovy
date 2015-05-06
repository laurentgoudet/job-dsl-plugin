package javaposse.jobdsl.dsl.helpers.publisher

import javaposse.jobdsl.dsl.AbstractContext
import javaposse.jobdsl.dsl.ContextHelper
import javaposse.jobdsl.dsl.DslContext
import javaposse.jobdsl.dsl.Item
import javaposse.jobdsl.dsl.JobManagement
import javaposse.jobdsl.dsl.RequiresPlugin
import javaposse.jobdsl.dsl.helpers.step.RunConditionContext
import javaposse.jobdsl.dsl.helpers.step.StepContext
import javaposse.jobdsl.dsl.helpers.step.condition.AlwaysRunCondition
import javaposse.jobdsl.dsl.helpers.step.condition.RunCondition
import javaposse.jobdsl.dsl.helpers.step.condition.RunConditionFactory

class FlexiblePublisherContext extends AbstractContext {
    protected final Item item
    RunCondition condition = new AlwaysRunCondition()
    List<Node> actions = []

    FlexiblePublisherContext(JobManagement jobManagement, Item item) {
        super(jobManagement)
        this.item = item
    }

    void condition(@DslContext(RunConditionContext) Closure closure) {
        condition = RunConditionFactory.of(closure)
    }

    @RequiresPlugin(id = 'any-buildstep')
    void step(@DslContext(StepContext) Closure closure) {
        StepContext stepContext = new StepContext(jobManagement, item)
        ContextHelper.executeInContext(closure, stepContext)
        actions.addAll(stepContext.stepNodes)
    }

    void publisher(@DslContext(PublisherContext) Closure closure) {
        PublisherContext publisherContext = new PublisherContext(jobManagement, item)
        ContextHelper.executeInContext(closure, publisherContext)
        actions.addAll(publisherContext.publisherNodes)
    }
}

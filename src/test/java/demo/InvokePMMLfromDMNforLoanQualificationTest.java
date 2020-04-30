package demo;

import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.kie.api.KieServices;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieRuntimeFactory;
import org.kie.dmn.api.core.DMNContext;
import org.kie.dmn.api.core.DMNModel;
import org.kie.dmn.api.core.DMNResult;
import org.kie.dmn.api.core.DMNRuntime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class InvokePMMLfromDMNforLoanQualificationTest {

    public static final Logger LOG = LoggerFactory.getLogger(InvokePMMLfromDMNforLoanQualificationTest.class);

    @Test
    public void testDryRun() {
        KieServices kieServices = KieServices.Factory.get();

        KieContainer kieContainer = kieServices.getKieClasspathContainer();

        DMNRuntime dmnRuntime = KieRuntimeFactory.of(kieContainer.getKieBase()).get(DMNRuntime.class);

        String namespace = "http://www.trisotech.com/definitions/_ca466dbe-20b4-4e88-a43f-4ce3aff26e4f";
        String modelName = "KiePMMLScoreCard";

        DMNModel dmnModel = dmnRuntime.getModels().get(0);
        DMNContext emptyContext = dmnRuntime.newContext();

        DMNResult evaluateAll = dmnRuntime.evaluateAll(dmnModel, emptyContext);
        LOG.info("{}", evaluateAll);
    }

    /*
    
{
    "dmn-context": {
        "Age": 37,
        "Married?": true,
        "Income": 10000,
        "College degree?": true,
        "Executive role?": false,
        "Loan Amount": 20000
    }
}
    
     */

    /**
     * ==> Previously Audited?: false
     */
    @Test
    public void testPreviouslyAuditedFALSE() {
        KieServices kieServices = KieServices.Factory.get();

        KieContainer kieContainer = kieServices.getKieClasspathContainer();

        DMNRuntime dmnRuntime = KieRuntimeFactory.of(kieContainer.getKieBase()).get(DMNRuntime.class);

        String namespace = "http://www.trisotech.com/definitions/_ca466dbe-20b4-4e88-a43f-4ce3aff26e4f";
        String modelName = "KiePMMLScoreCard";

        DMNModel dmnModel = dmnRuntime.getModels().get(0);
        DMNContext context = dmnRuntime.newContext();
        context.set("Age", 37);
        context.set("Married?", true);
        context.set("Income", 10_000);
        context.set("College degree?", true);
        context.set("Executive role?", false);
        context.set("Loan Amount", 20_000);

        DMNResult evaluateAll = dmnRuntime.evaluateAll(dmnModel, context);
        LOG.info("{}", evaluateAll);
        Assertions.assertThat(evaluateAll.getDecisionResultByName("Previously Audited?").getResult()).isEqualTo(false);
        Assertions.assertThat(evaluateAll.getDecisionResultByName("AutoLoan result").getResult()).isEqualTo("AutoLoan");
    }

    /*
     
{
    "dmn-context": {
        "Age": 37,
        "Married?": true,
        "Income": 10000,
        "College degree?": true,
        "Executive role?": true,
        "Loan Amount": 40000
    }
}
    
     */

    /**
     * ==> Previously Audited?: true
     */
    @Test
    public void testPreviouslyAuditedTRUE() {
        KieServices kieServices = KieServices.Factory.get();

        KieContainer kieContainer = kieServices.getKieClasspathContainer();

        DMNRuntime dmnRuntime = KieRuntimeFactory.of(kieContainer.getKieBase()).get(DMNRuntime.class);

        String namespace = "http://www.trisotech.com/definitions/_ca466dbe-20b4-4e88-a43f-4ce3aff26e4f";
        String modelName = "KiePMMLScoreCard";

        DMNModel dmnModel = dmnRuntime.getModels().get(0);
        DMNContext context = dmnRuntime.newContext();
        context.set("Age", 37);
        context.set("Married?", true);
        context.set("Income", 10_000);
        context.set("College degree?", true);
        context.set("Executive role?", true);
        context.set("Loan Amount", 40_000);

        DMNResult evaluateAll = dmnRuntime.evaluateAll(dmnModel, context);
        LOG.info("{}", evaluateAll);
        Assertions.assertThat(evaluateAll.getDecisionResultByName("Previously Audited?").getResult()).isEqualTo(true);
        Assertions.assertThat(evaluateAll.getDecisionResultByName("AutoLoan result").getResult()).isEqualTo("Manual");
    }
}

/*
 * Created on 11/12/2004
 */
package br.thejungle;

import junit.framework.Test;
import junit.framework.TestSuite;
import br.thejungle.util.TestMathUtil;

/**
 * @author Flávio Stutz (flaviostutz@uol.com.br)
 */
public class AllTests {

    public static void main(String[] args) {
        junit.textui.TestRunner.run(AllTests.suite());
    }

    public static Test suite() {
        TestSuite suite = new TestSuite("Test for br.thejungle");
        //$JUnit-BEGIN$
        suite.addTestSuite(TestMathUtil.class);
        //$JUnit-END$
        return suite;
    }
}

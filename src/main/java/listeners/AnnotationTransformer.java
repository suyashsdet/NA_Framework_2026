package listeners;

// ============================================================
// NEW FILE — does NOT exist in FrameworkD11
// ============================================================
//
// WHY THIS CLASS EXISTS:
//   In FrameworkD11, if a test failed it stayed failed — there was
//   no automatic retry mechanism at all.
//
//   Framework2026 adds a retry-on-failure feature. To wire it up
//   WITHOUT having to add retryAnalyzer = Retry.class manually on
//   every single @Test annotation, this class hooks into TestNG's
//   annotation-processing phase and injects the retry analyser
//   automatically for every test method at runtime.
//
// HOW IT WORKS:
//   TestNG calls transform() once for every @Test method it finds
//   before the suite starts. We use that opportunity to attach
//   Retry.class as the retryAnalyzer on every annotation, so every
//   test gets retry behaviour without touching the test classes.
//
// HOW IT IS REGISTERED:
//   Added as a <listener> in all 4 TestNG XML suite files:
//     <listener class-name="listeners.AnnotationTransformer"/>
//   TestNG must see it as a listener (not just a class) so it can
//   call transform() during the annotation-processing phase.
// ============================================================

import org.testng.IAnnotationTransformer;
import org.testng.IRetryAnalyzer;
import org.testng.annotations.ITestAnnotation;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

public class AnnotationTransformer implements IAnnotationTransformer {


    @Override
    public void transform(ITestAnnotation annotation,
                          Class testClass,
                          Constructor testConstructor,
                          Method testMethod) {
        annotation.setRetryAnalyzer(Retry.class);
    }
}

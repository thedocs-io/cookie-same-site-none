package io.thedocs.cookie;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class CookieSameSiteNoneCheckerTest {

    private CookieSameSiteNoneChecker cookieSameSiteNoneChecker = new CookieSameSiteNoneChecker();

    @ParameterizedTest()
    @MethodSource("provideSameSiteNoneCompatibleParams")
    void isSameSiteNoneIncompatible(String useragent, boolean isCompatible) {
        //setup
        boolean isIncompatible = !isCompatible;

        //where
        assertEquals(isIncompatible, cookieSameSiteNoneChecker.isSameSiteNoneIncompatible(useragent));
    }

    private static Stream<Arguments> provideSameSiteNoneCompatibleParams() {
        return Stream.of(
            Arguments.of("", true),
            Arguments.of("  ", true),
            Arguments.of("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_2) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/66.0.3359.117 Safari/537.36", false),
            Arguments.of("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_4) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/67.0.3396.56 Safari/537.36", true),
            Arguments.of("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_5) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/79.0.3945.130 Safari/537.36", true)
        );
    }
}

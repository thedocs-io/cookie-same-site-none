package io.thedocs.cookie;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * original: https://www.chromium.org/updates/same-site/incompatible-clients
 */
public class CookieSameSiteNoneChecker {

    private UserAgentDataExtractor userAgentDataExtractor;

    public CookieSameSiteNoneChecker() {
        this.userAgentDataExtractor = new UserAgentDataExtractor();
    }

    public boolean shouldSendSameSiteNone(String useragent) {
        return !isSameSiteNoneIncompatible(useragent);
    }

    public boolean isSameSiteNoneIncompatible(String useragent) {
        return hasWebKitSameSiteBug(useragent) || dropsUnrecognizedSameSiteCookies(useragent);
    }

    private boolean hasWebKitSameSiteBug(String useragent) {
        return userAgentDataExtractor.isIosVersion(12, useragent) ||
            (userAgentDataExtractor.isMacosxVersion(10, 14, useragent) &&
                (userAgentDataExtractor.isSafari(useragent) || userAgentDataExtractor.isMacEmbeddedBrowser(useragent)));
    }

    private boolean dropsUnrecognizedSameSiteCookies(String useragent) {
        if (userAgentDataExtractor.isUcBrowser(useragent)) {
            return !userAgentDataExtractor.isUcBrowserVersionAtLeast(12, 13, 2, useragent);
        } else {
            return userAgentDataExtractor.isChromiumBased(useragent) &&
                userAgentDataExtractor.isChromiumVersionAtLeast(51, useragent) &&
                !userAgentDataExtractor.isChromiumVersionAtLeast(67, useragent);
        }
    }

    public static class UserAgentDataExtractor {

        private static final Pattern IOS_VERSION_REGEX = Pattern.compile("\\(iP.+; CPU .*OS (\\d+)[_\\d]*.*\\) AppleWebKit\\/");
        private static final Pattern MACOSX_VERSION_REGEX = Pattern.compile("\\(Macintosh;.*Mac OS X (\\d+)_(\\d+)[_\\d]*.*\\) AppleWebKit\\/");
        private static final Pattern SAFARI_REGEX = Pattern.compile("Version\\/.* Safari\\/");
        private static final Pattern MAC_EMBEDDED_REGEX = Pattern.compile("^Mozilla\\/[\\.\\d]+ \\(Macintosh;.*Mac OS X [_\\d]+\\) AppleWebKit\\/[\\.\\d]+ \\(KHTML, like Gecko\\)$");
        private static final Pattern CHROMIUM_REGEX = Pattern.compile("Chrom(e|ium)");
        private static final Pattern CHROMIUM_VERSION_REGEX = Pattern.compile("Chrom[^ \\/]+\\/(\\d+)[\\.\\d]* ");
        private static final Pattern UC_BROWSER_REGEX = Pattern.compile("UCBrowser\\/");
        private static final Pattern UC_BROWSER_VERSION_REGEX = Pattern.compile("UCBrowser\\/(\\d+)\\.(\\d+)\\.(\\d+)[\\.\\d]* ");

        // Regex parsing of User-Agent string. (See note above!)
        public boolean isIosVersion(int major, String useragent) {
            Matcher matcher = IOS_VERSION_REGEX.matcher(useragent);

            if (matcher.find()) {
                return String.valueOf(major).equals(matcher.group(1));
            } else {
                return false;
            }
        }

        public boolean isMacosxVersion(int major, int minor, String useragent) {
            Matcher matcher = MACOSX_VERSION_REGEX.matcher(useragent);

            if (matcher.find()) {
                return String.valueOf(major).equals(matcher.group(1)) && String.valueOf(minor).equals(matcher.group(2));
            } else {
                return false;
            }
        }

        public boolean isSafari(String useragent) {
            return SAFARI_REGEX.matcher(useragent).find() && !isChromiumBased(useragent);
        }

        public boolean isMacEmbeddedBrowser(String useragent) {
            return MAC_EMBEDDED_REGEX.matcher(useragent).find();
        }

        public boolean isChromiumBased(String useragent) {
            return CHROMIUM_REGEX.matcher(useragent).find();
        }

        public boolean isChromiumVersionAtLeast(int major, String useragent) {
            Matcher matcher = CHROMIUM_VERSION_REGEX.matcher(useragent);

            if (matcher.find()) {
                Integer version = stringToInt(matcher.group(1));

                return version != null && version >= major;
            } else {
                return false;
            }
        }

        public boolean isUcBrowser(String useragent) {
            return UC_BROWSER_REGEX.matcher(useragent).find();
        }

        public boolean isUcBrowserVersionAtLeast(int major, int minor, int build, String useragent) {
            Matcher matcher = UC_BROWSER_VERSION_REGEX.matcher(useragent);

            if (matcher.find()) {
                Integer majorVersion = stringToInt(matcher.group(1));
                Integer minorVersion = stringToInt(matcher.group(2));
                Integer buildVersion = stringToInt(matcher.group(3));

                if (majorVersion != null && minorVersion != null && buildVersion != null) {
                    if (majorVersion != major) {
                        return majorVersion > major;
                    } else if (minorVersion != minor) {
                        return minorVersion > minor;
                    } else {
                        return buildVersion >= build;
                    }
                }
            }

            return false;
        }

        private Integer stringToInt(String value) {
            try {
                if (value == null || "".equals(value)) {
                    return null;
                } else {
                    return Integer.parseInt(value);
                }
            } catch (Exception e) {
                return null;
            }
        }
    }

}

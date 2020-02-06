# Intro
Java port of cookie `SameSite=None` support check:

* https://web.dev/samesite-cookies-explained/
* https://www.chromium.org/updates/same-site/incompatible-clients

## How to use
```java
new CookieSameSiteNoneChecker().isSameSiteNoneIncompatible("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_2) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/66.0.3359.117 Safari/537.36");
```

### Maven
```
<dependency>
    <groupId>io.thedocs</groupId>
    <artifactId>cookie-same-site-none</artifactId>
    <version>1.01</version>
</dependency>
```

### Gradle
```
repositories {
    mavenCentral()
}

dependencies {
    implementation 'io.thedocs:cookie-same-site-none:1.01'
}
```

## License
MIT

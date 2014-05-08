# JemVer 0.2.1 &mdash; SemVer for Java

[dgn1nja/JemVer](https://github.com/dgn1nja/JemVer)

JemVer is a Java implementation of the [SemVer 2.0.0](http://semver.org/spec/v2.0.0.html) specification.
It provides a `SemanticVersion` class and `@Version` annotation for specifying and handling semantic versions.

##Using JemVer

Using JemVer to mark up your own packages is designed to be as easy as possible. JemVer provides the `@Version` annotation, which takes a `String` containing the package&rsquo;s semantic version. That&rsquo;s all you need to do to get started&mdash;just remember to update the version when your public API changes.

	@Version("2.0.0")
	package org.semver;

As this is a package-level annotation, it needs to be declared in `package-info.java`.

Java also provides a method for specifying a specification version in `/META-INF/MANIFEST.MF`; this library uses that value as a fallback if no `@Version` annotation is present. This is also a good way to add semantic versioning to your packages if you don&rsquo;t want to introduce this library as a dependency.

For people who need to read or handle semantic versions, the `SemanticVersion` class handles the bulk of the logic. `SemanticVersion` represents an immutable semantic version. The class provides methods implementing everything laid out in the spec, including precedence calculation with `compareTo(SemanticVersion)`. There&rsquo;s also the static methods `parse(String)` and `get(Package)` for creating SemanticVersion objects from strings or packages. It also includes a capturing regular expression for validating and parsing SemVer strings.

As `SemanticVersion` is immutable, modifying versions is done through the `VersionBuilder`, which provides methods for basic modifications as well as SemVer&#x2010;compliant version increments.

##License

Copyright © 2012-2014 Colin Haber  
[n1nja@n1nja.com](mailto:n1nja@n1nja.com)  
Licensed under the LGPLv3  

The SemVer 2.0.0 spec (`semver.md`) is licensed CC BY 3.0.

/**
 * This file is part of JemVer.
 * 
 * JemVer is free software: you can redistribute it and/or modify it under the
 * terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later
 * version.
 * 
 * JemVer is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR
 * A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License along with
 * JemVer. If not, see <http://www.gnu.org/licenses/>.
 */
package com.n1nja.semver;
import java.io.Serializable;
import java.math.BigInteger;
import java.util.*;
import java.util.regex.*;
import com.n1nja.semver.internal.util.Classes;
/**
 * Represents a Semantic Version according to the <a
 * href="http://semver.org/spec/v2.0.0.html">SemVer 2.0.0</a> spec.
 * {@link SemanticVersion}s are immutable. This class also provides static
 * methods for {@linkplain #parse(CharSequence) parsing version strings} and
 * {@linkplain #get(Package) reading package versions}.
 * 
 * @since 0.2.0
 * @author Colin Haber
 */
public class SemanticVersion implements Comparable<SemanticVersion>, Serializable {
	/**
	 * A regular expression for validating build/prerelease identifiers. An
	 * identifier is a non&#x2010;empty string of ASCII alphanumerics or
	 * hyphen&#x2010;minus ({@code '-'}) characters. A purely numeric identifier
	 * must not contain leading zeros.
	 * 
	 * @see #NUMERIC_PATTERN
	 */
	static public final Pattern IDENTIFIER_PATTERN;
	/**
	 * A regular expression defining acceptable numeric strings for version
	 * numbers and additional identifiers. A numeric string must be
	 * non&#x2010;negative and may not have leading zeros. All numeric strings
	 * are represented in decimal form.
	 */
	static public final Pattern NUMERIC_PATTERN;
	/**
	 * A regular expression for validating Semantic SemanticVersion strings.
	 * Contains matching groups for each major part:
	 * <table>
	 * <thead>
	 * <tr>
	 * <th>#</th>
	 * <th>Part</th>
	 * </tr>
	 * </thead> <tbody>
	 * <tr>
	 * <td>1</td>
	 * <td>Major version</td>
	 * </tr>
	 * <tr>
	 * <td>2</td>
	 * <td>Minor version</td>
	 * </tr>
	 * <tr>
	 * <td>3</td>
	 * <td>Patch version</td>
	 * </tr>
	 * <tr>
	 * <td>4</td>
	 * <td>Pre&#x2010;release version</td>
	 * </tr>
	 * <tr>
	 * <td>5</td>
	 * <td>Build metadata</td>
	 * </tr>
	 * </tbody>
	 * </table>
	 * Pre&#x2010;release and build strings <i>do not</i> contain leading
	 * {@code '-'} or {@code '+'} characters.
	 */
	static public final Pattern SEMVER_PATTERN;
	/**
	 * The version of the SemVer spec this class implements. Currently 2.0.0.
	 * 
	 * @see <a
	 * href="http://semver.org/spec/v2.0.0.html">http://semver.org/spec/v2.0.0.html</a>
	 */
	static public final SemanticVersion SEMVER_VERSION;
	static final String BUILD_SEPARATOR = "+";
	static final String IDENTIFIER_SEPARATOR = ".";
	static final String PRERELEASE_SEPARATOR = "-";
	static final String VERSION_SEPARATOR = ".";
	static private final String IDENTIFIER_LIST_REGEX;
	static private final String IDENTIFIER_REGEX;
	static private final String NUMERIC_REGEX = "(?:[1-9][0-9]*)|0";
	static private final long serialVersionUID;
	static {
		IDENTIFIER_REGEX = String.format("%1$s|(?:[0-9]*[A-Za-z-][0-9A-Za-z]*)", NUMERIC_REGEX);
		IDENTIFIER_LIST_REGEX = String.format("(?:%1$s)(?:\\.(?:%1$s))*", IDENTIFIER_REGEX);
		NUMERIC_PATTERN = Pattern.compile(NUMERIC_REGEX);
		IDENTIFIER_PATTERN = Pattern.compile(IDENTIFIER_REGEX);
		SEMVER_PATTERN = Pattern.compile(String.format("(%1$s)\\.(%1$s)\\.(%1$s)(?:\\-(%2$s))?(?:\\+(%2$s))?", NUMERIC_REGEX, IDENTIFIER_LIST_REGEX));
		SEMVER_VERSION = SemanticVersion.parse("2.0.0");
		serialVersionUID = Classes.serial(SemanticVersion.class);
	}
	/**
	 * Gets the version of the given package. First checks for a {@link Version}
	 * annotation. If present, parses the value string. If not, does similarly
	 * with {@link java.lang.Package#getSpecificationVersion()
	 * Package#getSpecificationVersion()}. If neither are present or the latter
	 * is present and invalid, returns {@code null}.
	 * 
	 * @param p the {@link java.lang.Package Package} to check for a
	 * {@link Version @Version} annotation
	 * @return a {@link SemanticVersion} parsed from the package, or
	 * {@code null} if no valid version is present.
	 * @see #parse(CharSequence)
	 */
	static public final SemanticVersion get(final Package p) {
		if (p.isAnnotationPresent(Version.class)) {
			return parse(p.getAnnotation(Version.class).value());
		} else if (p.getSpecificationVersion() != null) {
			try {
				return parse(p.getSpecificationVersion());
			} catch (final VersionFormatException vfe) {
				return null;
			}
		} else {
			return null;
		}
	}
	/**
	 * Parses a version string into a {@link SemanticVersion}, according to the
	 * <a href="http://semver.org/spec/v2.0.0.html">Semantic Version 2.0.0</a>
	 * specification.
	 * 
	 * @param cs the version {@link java.lang.String String} to be parsed. May
	 * not be {@code null}.
	 * @return a {@link SemanticVersion} parsed from the annotation.
	 * @throws IllegalArgumentException if <code>cs</code> is null
	 * @throws VersionFormatException if <code>cs</code> is not a valid SemVer
	 * string
	 * @see #SEMVER_PATTERN
	 */
	static public final SemanticVersion parse(final CharSequence cs) {
		if (cs == null) throw new IllegalArgumentException();
		final Matcher m = SEMVER_PATTERN.matcher(cs);
		if (!m.matches()) throw new VersionFormatException();
		final String major = m.group(1);
		final String minor = m.group(2);
		final String patch = m.group(3);
		final String prere = m.groupCount() >= 5 ? m.group(4) : null;
		final String build = m.groupCount() >= 6 ? m.group(5) : null;
		return new SemanticVersion(Integer.valueOf(major), Integer.valueOf(minor), Integer.valueOf(patch), prere, build);
	}
	private final List<String> build;
	private transient String getBuildStringCache;
	private transient String getPrereleaseStringCache;
	private final int major;
	private final int minor;
	private final int patch;
	private final List<String> prere;
	private transient String toStringCache;
	/**
	 * Creates a {@link SemanticVersion} with no prerelease or build
	 * identifiers. Equivalent to calling
	 * {@linkplain #SemanticVersion(int, int, int, List, List)
	 * SemanticVersion(major, minor, patch, null, null)}.
	 * 
	 * @param major the major version number. May not be negative.
	 * @param minor the minor version number. May not be negative.
	 * @param patch the patch version number. May not be negative.
	 * 
	 * @see #SemanticVersion(int, int, int, List, List)
	 */
	public SemanticVersion(final int major, final int minor, final int patch) {
		this(major, minor, patch, (List<String>) null, (List<String>) null);
	}
	/**
	 * Creates a {@link SemanticVersion} with the given properties.
	 * 
	 * @param major the major version number. May not be negative.
	 * @param minor the minor version number. May not be negative.
	 * @param patch the patch version number. May not be negative.
	 * @param prere a {@linkplain java.util.List list} of prerelease
	 * identifiers, or {@code null} if there are none
	 * @param build a {@linkplain java.util.List list} of build identifiers, or
	 * {@code null} if there are none
	 */
	public SemanticVersion(final int major, final int minor, final int patch, final List<String> prere, final List<String> build) {
		if (major < 0) throw new IllegalArgumentException();
		this.major = major;
		if (minor < 0) throw new IllegalArgumentException();
		this.minor = minor;
		if (patch < 0) throw new IllegalArgumentException();
		this.patch = patch;
		if (prere != null) {
			for (final String id : prere) {
				if (!IDENTIFIER_PATTERN.matcher(id).matches()) throw new IllegalArgumentException();
			}
			this.prere = Collections.unmodifiableList(new ArrayList<String>(prere));
		} else {
			this.prere = Collections.emptyList();
		}
		if (build != null) {
			for (final String id : build) {
				if (!IDENTIFIER_PATTERN.matcher(id).matches()) throw new IllegalArgumentException();
			}
			this.build = Collections.unmodifiableList(new ArrayList<String>(build));
		} else {
			this.build = Collections.emptyList();
		}
	}
	/**
	 * Creates a {@link SemanticVersion} with the given properties. Prerelease
	 * and build identifiers are split on the {@code '.'} character.
	 * 
	 * @param major the major version number. May not be negative.
	 * @param minor the minor version number. May not be negative.
	 * @param patch the patch version number. May not be negative.
	 * @param prere a dot&#x2010;separated {@link java.lang.String String} of
	 * prerelease identifiers, or {@code null} if there are none
	 * @param build a dot&#x2010;separated {@link java.lang.String String} of
	 * build identifiers, or {@code null} if there are none
	 * 
	 * @see #SemanticVersion(int, int, int, List, List)
	 */
	public SemanticVersion(final int major, final int minor, final int patch, final String prere, final String build) {
		this(major, minor, patch, prere != null ? Arrays.asList(prere.split("\\.")) : null, build != null ? Arrays.asList(build.split("\\.")) : null);
	}
	public int compareTo(final SemanticVersion other) {
		if (this.major != other.major) return (int) Math.signum(this.major - other.major);
		if (this.minor != other.minor) return (int) Math.signum(this.minor - other.minor);
		if (this.patch != other.patch) return (int) Math.signum(this.patch - other.patch);
		if (this.prere.isEmpty() != other.prere.isEmpty()) {
			return (int) Math.signum(other.prere.size() - this.prere.size());
		} else if (!(this.prere.isEmpty() || other.prere.isEmpty())) {
			for (int i = 0; i < Math.min(this.prere.size(), other.prere.size()); i++) {
				final String tid = this.prere.get(i);
				final String oid = other.prere.get(i);
				final boolean tnum = NUMERIC_PATTERN.matcher(tid).matches();
				final boolean onum = NUMERIC_PATTERN.matcher(oid).matches();
				if (tnum && onum) {
					// If both numeric, convert and compare numerically.
					final BigInteger tint = new BigInteger(tid);
					final BigInteger oint = new BigInteger(oid);
					final int comp = tint.compareTo(oint);
					if (comp != 0) return comp;
				} else if (!(tnum || onum)) {
					// If neither numeric, compare lexicographically
					final int comp = tid.compareTo(oid);
					if (comp != 0) return comp;
				} else {
					// Numerics always have lower precedence than non-numerics
					return tnum ? -1 : 1;
				}
			}
			if (this.prere.size() != other.prere.size()) return this.prere.size() - other.prere.size();
		}
		return 0;
	}
	@Override
	public boolean equals(final Object other) {
		if (this == other) return true;
		if (other == null) return false;
		if (!(other instanceof SemanticVersion)) return false;
		return this.compareTo((SemanticVersion) other) == 0;
	}
	/**
	 * Returns the immutable list of build identifiers.
	 * 
	 * @return an {@linkplain java.util.Collections#unmodifiableList(List)
	 * unmodifiable list} of build identifier strings. Will never be
	 * {@code null}.
	 */
	public List<String> getBuild() {
		return this.build;
	}
	/**
	 * Returns the build metadata string.
	 * 
	 * @return a {@link java.lang.String String} of dot&#x2010;separated build
	 * identifiers. The returned string does not have the {@code '+'} prefix.
	 */
	public String getBuildString() {
		if (this.getBuildStringCache != null) return this.getBuildStringCache;
		if (!this.hasBuild()) return "";
		final StringBuilder sb = new StringBuilder(this.build.get(0));
		for (int i = 1; i < this.build.size(); i++) {
			sb.append(IDENTIFIER_SEPARATOR).append(this.build.get(i));
		}
		this.getBuildStringCache = sb.toString();
		return this.getBuildString();
	}
	/**
	 * Returns the major version number.
	 * 
	 * @return the major version number, a non&#x2010;negative {@code int}
	 */
	public int getMajor() {
		return this.major;
	}
	/**
	 * Returns the minor version number.
	 * 
	 * @return the minor version number, a non&#x2010;negative {@code int}
	 */
	public int getMinor() {
		return this.minor;
	}
	/**
	 * Returns the patch version number.
	 * 
	 * @return the patch version number, a non&#x2010;negative {@code int}
	 */
	public int getPatch() {
		return this.patch;
	}
	/**
	 * Returns the immutable list of prerelease identifiers.
	 * 
	 * @return an {@linkplain java.util.Collections#unmodifiableList(List)
	 * unmodifiable list} of prerelease identifier strings. Will never be
	 * {@code null}.
	 */
	public List<String> getPrerelease() {
		return this.prere;
	}
	/**
	 * Returns the prerelease version string.
	 * 
	 * @return a {@link java.lang.String String} of dot&#x2010;separated
	 * prerelease identifiers. The returned string does not have the {@code '-'}
	 * prefix.
	 */
	public String getPrereleaseString() {
		if (this.getPrereleaseStringCache != null) return this.getPrereleaseStringCache;
		if (!this.hasPrerelease()) return "";
		final StringBuilder sb = new StringBuilder(this.prere.get(0));
		for (int i = 1; i < this.prere.size(); i++) {
			sb.append(IDENTIFIER_SEPARATOR).append(this.prere.get(i));
		}
		this.getPrereleaseStringCache = sb.toString();
		return this.getPrereleaseString();
	}
	/**
	 * Checks whether this version has build metadata.
	 * 
	 * @return {@code true} if there is build metadata, or {@code false} if
	 * there is none. When this method returns false, build methods will return
	 * empty lists or strings.
	 */
	public boolean hasBuild() {
		return !this.build.isEmpty();
	}
	/**
	 * Returns a hash code value for this version. Guaranteed to be equal for
	 * any two versions where {@link #compareTo(SemanticVersion)} returns
	 * {@code 0}. The value will remain constant across separate application
	 * executions, allowing it to be used in {@linkplain java.io.Serializable
	 * serialization} calculations.
	 */
	@Override
	public int hashCode() {
		long hash = 0;
		hash += this.major * 100000000;
		hash += this.minor * 100000;
		hash += this.patch * 100;
		hash *= this.prere.size() + 1;
		hash *= this.build.size() + 1;
		return (int) (hash % Integer.MAX_VALUE);
	}
	/**
	 * Checks whether this version has a prerelease version.
	 * 
	 * @return {@code true} if there is a prerelease version, or {@code false}
	 * if there is none. When this method returns false, prerelease methods will
	 * return empty lists or strings.
	 */
	public boolean hasPrerelease() {
		return !this.prere.isEmpty();
	}
	/**
	 * Checks whether this version represents a stable API. Unstable APIs are
	 * subject to breaking changes at any time.
	 * 
	 * @return {@code true} if and only if this has a major version of {@code 0}
	 * or has a prerelease version.
	 */
	public boolean isStable() {
		return this.major == 0 || this.hasPrerelease();
	}
	/**
	 * Returns the semantic version string for this {@link SemanticVersion}.
	 * 
	 * @return the semantic version {@link java.lang.String String}. The string
	 * is guaranteed to conform to the <a
	 * href="http://semver.org/spec/v2.0.0.html">Semantic Version 2.0.0</a>
	 * spec.
	 */
	@Override
	public String toString() {
		if (this.toStringCache != null) return this.toStringCache;
		final StringBuilder sb = new StringBuilder();
		sb.append(this.major).append(VERSION_SEPARATOR).append(this.minor).append(VERSION_SEPARATOR).append(this.patch);
		if (this.hasPrerelease()) {
			sb.append(PRERELEASE_SEPARATOR).append(this.getPrereleaseString());
		}
		if (this.hasBuild()) {
			sb.append(BUILD_SEPARATOR).append(this.getBuildString());
		}
		this.toStringCache = sb.toString();
		return this.toString();
	}
}

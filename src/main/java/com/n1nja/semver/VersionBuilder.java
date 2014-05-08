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
import java.util.*;
/**
 * A builder for the {@link SemanticVersion} class. Includes basic methods for
 * setting versions and adding prerelease and build identifiers. Also includes
 * methods for incrementing version numbers in a SemVer&#x2010;compliant manner.
 * 
 * @since 0.2.1
 * @author Colin Haber
 */
public final class VersionBuilder {
	private final List<String> build;
	private int major;
	private int minor;
	private int patch;
	private final List<String> prere;
	/**
	 * Creates a {@link VersionBuilder} with zeroed versions and no prerelease
	 * or build identifiers.
	 */
	public VersionBuilder() {
		this(0, 0, 0, null, null);
	}
	/**
	 * Creates a {@link VersionBuilder} with the specified version data.
	 * 
	 * @param major the major version number
	 * @param minor the minor version number
	 * @param patch the patch version number
	 * @param prere the prerelease version
	 * @param build build metadata
	 */
	public VersionBuilder(final int major, final int minor, final int patch, final List<String> prere, final List<String> build) {
		this.major = major;
		this.minor = minor;
		this.patch = patch;
		this.prere = prere != null ? new LinkedList<String>(prere) : new LinkedList<String>();
		this.build = build != null ? new LinkedList<String>(build) : new LinkedList<String>();
	}
	/**
	 * Creates a {@link VersionBuilder} from the specified
	 * {@link SemanticVersion}.
	 * 
	 * @param v the {@link SemanticVersion} from which to create this builder.
	 */
	public VersionBuilder(final SemanticVersion v) {
		this(v.getMajor(), v.getMinor(), v.getPatch(), v.getPrerelease(), v.getBuild());
	}
	/**
	 * Creates a copy of the specified {@link VersionBuilder}.
	 * 
	 * @param vb the {@link VersionBuilder} to copy.
	 */
	public VersionBuilder(final VersionBuilder vb) {
		this(vb.major, vb.minor, vb.patch, vb.prere, vb.build);
	}
	/**
	 * Adds the given string to the list of build identifiers.
	 * 
	 * @param build the build {@link java.lang.String String} to be added. If
	 * the string will be split on any dot ({@code '.'}) character within, and
	 * all substrings will be added. If no dot is present, the full string will
	 * be added. The string is <i>not</i> checked to be a
	 * {@linkplain SemanticVersion#IDENTIFIER_PATTERN valid identifier}.
	 * @return this {@link VersionBuilder}
	 */
	public VersionBuilder addBuild(final String build) {
		this.build.addAll(Arrays.asList(build.split("\\.")));
		return this;
	}
	/**
	 * Adds the given strings to the list of build identifiers.
	 * 
	 * @param builds a {@link java.util.Collection Collection} of
	 * {@link java.lang.String String}s to be added to the list of build
	 * identifiers in the order they are returned by the collection&rsquo;s
	 * iterator. The strings are <i>not</i> checked to be
	 * {@linkplain SemanticVersion#IDENTIFIER_PATTERN valid identifiers}.
	 * @return this {@link VersionBuilder}
	 */
	public VersionBuilder addBuilds(final Collection<String> builds) {
		this.build.addAll(builds);
		return this;
	}
	/**
	 * Adds the given string to the list of prerelease identifiers.
	 * 
	 * @param prere the prerelease {@link java.lang.String String} to be added.
	 * If the string will be split on any dot ({@code '.'}) character within,
	 * and all substrings will be added. If no dot is present, the full string
	 * will be added. The string is <i>not</i> checked to be a
	 * {@linkplain SemanticVersion#IDENTIFIER_PATTERN valid identifier}.
	 * @return this {@link VersionBuilder}
	 */
	public VersionBuilder addPrerelease(final String prere) {
		this.prere.addAll(Arrays.asList(prere.split("\\.")));
		return this;
	}
	/**
	 * Adds the given strings to the list of prerelease identifiers.
	 * 
	 * @param preres a {@link java.util.Collection Collection} of
	 * {@link java.lang.String String}s to be added to the list of prerelease
	 * identifiers in the order they are returned by the collection&rsquo;s
	 * iterator. The strings are <i>not</i> checked to be
	 * {@linkplain SemanticVersion#IDENTIFIER_PATTERN valid identifiers}.
	 * @return this {@link VersionBuilder}
	 */
	public VersionBuilder addPrereleases(final Collection<String> preres) {
		this.prere.addAll(preres);
		return this;
	}
	/**
	 * Builds and returns a semantic version.
	 * 
	 * @return a {@link SemanticVersion} containing the current state of this
	 * builder.
	 */
	public SemanticVersion build() {
		return new SemanticVersion(this.major, this.minor, this.patch, this.prere, this.build);
	}
	/**
	 * Increments the major version, and sets the minor and patch versions to
	 * zero. This does <i>not</i> remove or change prerelease or build data if
	 * any are present.
	 * 
	 * @return this {@link VersionBuilder}
	 */
	public VersionBuilder bumpMajor() {
		this.major++;
		this.minor = 0;
		this.patch = 0;
		return this;
	}
	/**
	 * Increments the minor version, and sets the patch version to zero. This
	 * does <i>not</i> remove or change prerelease or build data if any are
	 * present.
	 * 
	 * @return this {@link VersionBuilder}
	 */
	public VersionBuilder bumpMinor() {
		this.minor++;
		this.patch = 0;
		return this;
	}
	/**
	 * Increments the patch version. This does <i>not</i> remove or change
	 * prerelease or build data if any are present.
	 * 
	 * @return this {@link VersionBuilder}
	 */
	public VersionBuilder bumpPatch() {
		this.patch++;
		return this;
	}
	/**
	 * Clears all build identifiers.
	 * 
	 * @return this {@link VersionBuilder}
	 */
	public VersionBuilder clearBuild() {
		this.build.clear();
		return this;
	}
	/**
	 * Clears all prerelease identifiers.
	 * 
	 * @return this {@link VersionBuilder}
	 */
	public VersionBuilder clearPrerelease() {
		this.prere.clear();
		return this;
	}
	/**
	 * Sets the major version.
	 * 
	 * @param major the major version to use. This argument is <i>not</i>
	 * checked for validity. Major versions should be non&#x2010;negative
	 * integers.
	 * @return this {@link VersionBuilder}
	 */
	public VersionBuilder major(final int major) {
		this.major = major;
		return this;
	}
	/**
	 * Sets the minor version.
	 * 
	 * @param minor the minor version to use. This argument is <i>not</i>
	 * checked for validity. Minor versions should be non&#x2010;negative
	 * integers.
	 * @return this {@link VersionBuilder}
	 */
	public VersionBuilder minor(final int minor) {
		this.minor = minor;
		return this;
	}
	/**
	 * Sets the patch version.
	 * 
	 * @param patch the patch version to use. This argument is <i>not</i>
	 * checked for validity. Patch versions should be non&#x2010;negative
	 * integers.
	 * @return this {@link VersionBuilder}
	 */
	public VersionBuilder patch(final int patch) {
		this.patch = patch;
		return this;
	}
}

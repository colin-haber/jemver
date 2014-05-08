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
package com.n1nja.semver.internal.util;
import com.n1nja.semver.SemanticVersion;
/**
 * Provides tools for working with {@linkplain java.lang.Class classes}. Not
 * part of the public API.
 * 
 * @since 0.2.0
 * @author Colin Haber
 */
public abstract class Classes {
	/**
	 * Calculates a serial version for the given class using the semantic
	 * version of its package and the canonical name of the class.
	 * 
	 * @param c the class for which to calculate a serial
	 * @return a {@code long} suitable for use as a {@code serialVersionUID}
	 */
	static public final long serial(final Class<? extends Object> c) {
		return SemanticVersion.get(c.getPackage()).hashCode() * c.getCanonicalName().hashCode();
	}
}

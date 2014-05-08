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
import com.n1nja.semver.internal.util.Classes;
/**
 * An exception thrown on semantic version parsing errors.
 * 
 * @since 0.2.0
 * @author Colin Haber
 * @see SemanticVersion#parse(CharSequence)
 */
public class VersionFormatException extends RuntimeException {
	private static final long serialVersionUID = Classes.serial(VersionFormatException.class);
}

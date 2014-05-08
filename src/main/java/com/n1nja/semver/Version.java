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
import java.lang.annotation.*;
/**
 * The {@link Version @Version} annotation provides a method for marking up
 * package API versions that is easy to both edit and access.
 * 
 * @since 0.2.0
 * @author Colin Haber
 */
@Target(ElementType.PACKAGE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Version {
	/**
	 * The semantic version string specified.
	 * 
	 * @return the semantic version {@link java.lang.String String}
	 */
	public String value();
}

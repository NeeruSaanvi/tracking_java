/*
 * Hibernate, Relational Persistence for Idiomatic Java
 *
 * License: GNU Lesser General Public License (LGPL), version 2.1 or later.
 * See the lgpl.txt file in the root directory or <http://www.gnu.org/licenses/lgpl-2.1.html>.
 */
package com.tracker.rest.hibernate;

import java.io.Serializable;

import org.hibernate.boot.model.naming.Identifier;
import org.hibernate.boot.model.naming.PhysicalNamingStrategy;
import org.hibernate.engine.jdbc.env.spi.JdbcEnvironment;

/**
 * Standard implementation of the PhysicalNamingStrategy contract.
 *
 * @author Steve Ebersole
 */
@SuppressWarnings("serial")
public class PhysicalNamingStrategyStandardImpl implements PhysicalNamingStrategy, Serializable {

	@SuppressWarnings("unused")
	private static final String PRURAL_APPEND = "s";
	/**
	 * Singleton access
	 */
	public static final PhysicalNamingStrategyStandardImpl INSTANCE = new PhysicalNamingStrategyStandardImpl();

	@Override
	public Identifier toPhysicalCatalogName(Identifier name, JdbcEnvironment context) {
		return name;
	}

	@Override
	public Identifier toPhysicalSchemaName(Identifier name, JdbcEnvironment context) {
		return name;
	}

	@Override
	public Identifier toPhysicalTableName(Identifier name, JdbcEnvironment context) {
		/*return Identifier
				.toIdentifier(CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, name.getText()) + PRURAL_APPEND);*/
		return Identifier
				.toIdentifier(name.getText().toLowerCase());
	}

	@Override
	public Identifier toPhysicalSequenceName(Identifier name, JdbcEnvironment context) {
		return name;
	}

	@Override
	public Identifier toPhysicalColumnName(Identifier name, JdbcEnvironment context) {
		/*return Identifier.toIdentifier(CaseFormat.LOWER_CAMEL.to(CaseFormat.UPPER_UNDERSCORE, name.getText()));*/
		return name;
	}
}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ru.natty.persist;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.Query;

/**
 *
 * @author necto
 */
public class QueryList
{
	static public class InitedQuery
	{
		Query q;
		public InitedQuery (Query q) {this.q = q;}

		public <T> List<T> getAllResults()
		{
			List rez = q.getResultList();
			List<T> ret = new ArrayList<T>();

			for (Object o : rez)
				ret.add ((T)o);
			return ret;
		}

		public <T> List<T> getResults (int start, int limit)
		{
			q.setFirstResult (start);
			q.setMaxResults (limit);
			return this.<T>getAllResults();
		}
	}

	static public InitedQuery forQuery (Query q)
	{
		return new InitedQuery (q);
	}
}

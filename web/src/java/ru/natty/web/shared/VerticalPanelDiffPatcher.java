package ru.natty.web.shared;

import com.google.gwt.user.client.ui.Widget;

import ru.natty.web.client.IVerticalPanel;
import ru.natty.web.client.IWidget;




public class VerticalPanelDiffPatcher extends ComplexPanelDiffPatcher
{

	@Override
	protected IWidget create(Integer id) {
		return new IVerticalPanel (id);
	}
//	@Override
//	protected ComplexPanel newPanel(int id) {
//		return new IVerticalPanel (id);
//	}
	/* Oracle SQL
	 * #18: insert into t values(42);
	 *		/<-- mistake
	 *		insert into t values (43);
	 *
	 *	#17: delete emp
	 *		  <-- not executed
	 *		 / <-- executed here
		Optimal = "and level <= 2" in connect by
		#103, ...:	circle; CONNECT BY IS SERCLE, NO CIRCLE
	 */
}

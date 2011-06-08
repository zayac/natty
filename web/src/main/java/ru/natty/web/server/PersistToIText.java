/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ru.natty.web.server;

import java.util.ArrayList;
import java.util.List;
import ru.natty.persist.IdNamed;
import ru.natty.web.server.wcontent.WTextCellList;
import ru.natty.web.shared.IText;

/**
 *
 * @author necto
 */
public class PersistToIText implements WTextCellList.Translator<IdNamed>
{
	@Override
	public List<IText> translate(List<? extends IdNamed> list)
	{
		List<IText> ret = new ArrayList<IText>();
		for (IdNamed item : list)
			ret.add(new IText(item.getId(), item.getName()));
		return ret;
	}
}

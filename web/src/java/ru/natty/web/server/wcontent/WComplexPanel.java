package ru.natty.web.server.wcontent;


import java.util.List;
import ru.natty.web.persist.PanelContents;
import ru.natty.web.server.DataBase;
import ru.natty.web.server.WContentCreator;
import ru.natty.web.shared.ComplexPanelDiffPatcher;
import ru.natty.web.shared.CompositePanelDP;
import ru.natty.web.shared.Parameters;

public class WComplexPanel extends WCompositePanel
{

	static public class Substance extends UnitContent
	{
		Integer pos;
		
		public Substance(Integer pos, WContent content) {
			super(content);
			this.pos = pos;
		}
		
		public void bringContentTo (CompositePanelDP cpdp, Integer id)
		{
			((ComplexPanelDiffPatcher)cpdp).addCreature(pos, id, content.getAllContent());
		}

		public UnitContent copy() {
			return new Substance(pos, content.copy());
		}

		@Override
		public String toString() {
			return "Substance [pos=" + pos + ", content=" + content + "]";
		}
		
	}
	Class<?extends ComplexPanelDiffPatcher> cpdp;
	
	public WComplexPanel(Class<? extends ComplexPanelDiffPatcher> cpdp)
	{
		this.cpdp = cpdp;
	}
	
	public void addItem (int id, Integer position, WContent item)
	{
		contents.put(id, new Substance(position, item));
	}

	@Override
	protected CompositePanelDP create() {
		try {
			return cpdp.newInstance();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	protected WCompositePanel makeMe()
	{
		return new WComplexPanel(cpdp);
	}
	
	@Override
	public String toString() {
		return "WComplexPanelContent [" + contents
				+ "]";
	}

	public static WComplexPanel make (Integer id, Parameters ps,
											 WComplexPanel billet,
											 DataBase db, WContentCreator creator)
	{
		List<PanelContents> pcs = db.queryPanelContentsById(id);
        for (PanelContents pc : pcs)
            billet.addItem (pc.getContentId(), pc.getOrdNumber(),
							creator.getContent (pc.getContentId(), ps));
		return billet;
	}

	public static WComplexPanel
			makeCustom (Integer id, Integer contentId,
						WContent view, Parameters ps,
						WComplexPanel billet,
						DataBase db, WContentCreator creator)
	{
        List<PanelContents> contents = db.queryPanelContentsById(id);
        for (PanelContents pc : contents)
        {
			if (pc.getContentId().equals(contentId))
				billet.addItem (pc.getContentId(), pc.getOrdNumber(), view);
			else
				billet.addItem (pc.getContentId(), pc.getOrdNumber(),
								creator.getContent (pc.getContentId(), ps));
        }
		return billet;
	}
}
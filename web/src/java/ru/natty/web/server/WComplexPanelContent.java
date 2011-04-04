package ru.natty.web.server;


import ru.natty.web.shared.ComplexPanelDiffPatcher;
import ru.natty.web.shared.CompositePanelDP;

public class WComplexPanelContent extends WCompositePanelContent//extends WContent
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
	
	public WComplexPanelContent(Class<? extends ComplexPanelDiffPatcher> cpdp)
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
	protected WCompositePanelContent makeMe()
	{
		return new WComplexPanelContent(cpdp);
	}
	
	@Override
	public String toString() {
		return "WComplexPanelContent [" + contents
				+ "]";
	}
}
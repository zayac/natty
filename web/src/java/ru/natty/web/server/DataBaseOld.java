package ru.natty.web.server;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;
import ru.natty.persist.Genre;
import ru.natty.web.persist.GuiProperties;
import ru.natty.web.persist.Label;

public class DataBaseOld
{
	static enum ContentType
	{
		LABEL, TAB_PANEL, VPANEL, HPANEL
	}
	
	static class ContentProperty
	{
		Integer dad;
		ContentType type;
		
		public ContentProperty(Integer dad, ContentType type) {
			super();
			this.dad = dad;
			this.type = type;
		}
	}
	
	static class Tab
	{
		private String label;
		private Integer host;
		private Integer order;
		private Integer content;
		
		public Tab(String label, Integer host, Integer order, Integer content) {
			this.label = label;
			this.host = host;
			this.order = order;
			this.content = content;
		}
		public String getLabel() {
			return label;
		}
		public void setLabel(String label) {
			this.label = label;
		}
		public Integer getHost() {
			return host;
		}
		public void setHost(Integer host) {
			this.host = host;
		}
		public Integer getOrder() {
			return order;
		}
		public void setOrder (Integer order) {
			this.order = order;
		}
		public Integer getContent() {
			return content;
		}
		public void setContent(Integer content) {
			this.content = content;
		}
	}
	
	
	static class OrderedItem
	{
		private Integer content;
		private Integer host;
		private Integer order;
		
		public OrderedItem(Integer content, Integer host, Integer order) {
			this.content = content;
			this.host = host;
			this.order = order;
		}
		public Integer getContent() {
			return content;
		}
		public void setContent(Integer content) {
			this.content = content;
		}
		public Integer getHost() {
			return host;
		}
		public void setHost(Integer host) {
			this.host = host;
		}
		public Integer getOrder() {
			return order;
		}
		public void setOrder(Integer order) {
			this.order = order;
		}
	}

	private Map<Integer, String> labels;
	private Set<Tab> tabs;
	private Set<OrderedItem> items;
	private Map<Integer, ContentProperty> properties;
	private Map<Integer, Integer> defViews;
	
	public DataBaseOld()
    {
		labels = new HashMap<Integer, String>();
		defViews = new HashMap<Integer, Integer>();
		tabs = new HashSet<DataBaseOld.Tab>();
		items = new HashSet<DataBaseOld.OrderedItem>();
		properties = new HashMap<Integer, ContentProperty>();
	}

    public WContent getContent (Integer id)
    {

        EntityManagerFactory emf = Persistence.createEntityManagerFactory("Natty.webPU");
        EntityManager em = emf.createEntityManager();

        em.getTransaction().begin();

        Query getProps = em.createNamedQuery("GuiProperties.findById");
        getProps.setParameter("id", id);
        GuiProperties gp = (GuiProperties)getProps.getSingleResult();
        return new WLabelContent(gp.getWidgetType().toString());

//        Query q = em.createNamedQuery ("Label.findById");
//        q.setParameter ("id", id);
//        Label ll = (Label)q.getSingleResult();
//
////        Query q = em.createNamedQuery("Genre.findAll");
////        List genres = q.getResultList();
////        String compilation = "";
////        for (Object o : genres)
////            compilation += ((Genre)o).getName();
//
////        Genre g = new Genre(s);
////        em.persist(g);
//
//        em.getTransaction().commit();
//        em.close();
//        emf.close();
//        return new WLabelContent(ll.getText());
    }
	
	public WContent createContent (Integer id)
	{
		return createContent(id, properties.get(id).type);
	}
	
	public WContent createContentBranch (Integer id)
	{
		WContent leaf = createContent(id);
		return createAggregatingBranch(id, leaf);
	}
	
	private void addChild (Integer content, Integer dad, Integer order)
	{
		ContentProperty dp = properties.get(dad);
		if (dp == null) throw new RuntimeException("Adding a child to unexisting host");
		switch (dp.type)
		{
		case TAB_PANEL:
			addTab(content, dad, "no label", order);
			break;
		case HPANEL:
		case VPANEL:
			addItem (content, dad, order);
			break;
		default:
			throw new RuntimeException ("Adding a child to a non aggregating widget");
		}
	}
	
	public void addLabel (Integer id, String label, Integer dad)//, Integer order)
	{
		labels.put(id, label);
		properties.put(id, new ContentProperty (dad, ContentType.LABEL));
//		if (null != dad)
//			addChild (id, dad, order);
	}
	
	public void addTabPanel (Integer id, Integer defaultTab, Integer dad)//, Integer order)
	{
		properties.put(id, new ContentProperty (dad, ContentType.TAB_PANEL));
		defViews.put(id, defaultTab);
//		if (null != dad)
//			addChild (id, dad, order);
	}
	
	public void addVerticalPanel (Integer id, Integer dad)//, Integer order)
	{
		properties.put(id, new ContentProperty (dad, ContentType.VPANEL));
//		if (null != dad)
//			addChild (id, dad, order);
	}
	
	public void addHorizontalPanel (Integer id, Integer dad)//, Integer order)
	{
		properties.put(id, new ContentProperty (dad, ContentType.HPANEL));
//		if (null != dad)
//			addChild (id, dad, order);
	}
	
	public void addTab (Integer content, Integer host, String label, Integer order)
	{
		tabs.add(new Tab(label, host, order, content));
	}
	public void addItem (Integer content, Integer host, Integer order)
	{
		items.add(new OrderedItem(content, host, order));
	}
	
	private WContent createContent (Integer id, ContentType type)
	{
		switch (type)
		{
		case LABEL:
			return createLabel (id);
		case TAB_PANEL:
			return createTabPanel (id);
		case VPANEL:
		case HPANEL:
			return createComplexPanel(id, type);
		default:
			return null;
		}
	}
	
	private boolean IsRoot (Integer id)
	{
		return 0 == id;
	}
	
	private WContent createAggregatingBranch (Integer widget, WContent data)
	{
		if (IsRoot (widget)) return data;
		Integer parent = properties.get(widget).dad;
		WContent aggr = createAggregatingContent(widget, parent, data);
		return createAggregatingBranch (parent, aggr);
	}
	
	private WContent createAggregatingContent (Integer chosen, Integer parent, WContent data)
	{
		ContentType type = properties.get(parent).type; 
		switch (type)
		{
		case TAB_PANEL:
			return createTabPanelCustom (parent, chosen, data);
		case HPANEL:
		case VPANEL:
			return createComplexPanelCustom (parent, type, chosen, data);
		default:
			throw new RuntimeException ("Database conflict: Wrong parent type :" + 
										type + " of " + parent + 
										" of " + chosen);
		}
	}

	private WLabelContent createLabel (Integer id)
	{
		return new WLabelContent(labels.get(id));
	}
	
	private WContent createVoid()
	{
		return new WVoid();//new WLabelContent ("void");
	}

	private WTabPanelContent createTabPanel (Integer id)
	{
		WTabPanelContent tpc = new WTabPanelContent();

		Integer def = defViews.get(id);
		tpc.setActiveTab(def);
	
		for (Tab t: tabs)
			if (t.host.equals(id))
				if (t.order.equals(def))
					tpc.InsertTab (t.content, t.label, t.order, createContent (t.content));
				else
					tpc.InsertTab (t.content, t.label, t.order, createVoid());
		
		return tpc;
	}
	
	private WTabPanelContent createTabPanelCustom (Integer id, Integer choiceId, WContent view)
	{
		WTabPanelContent tpc = new WTabPanelContent();
	
		for (Tab t: tabs)
			if (t.host.equals(id))
				if (t.content.equals(choiceId))
				{
					tpc.InsertTab (t.content, t.label, t.order, view);
					tpc.setActiveTab(t.order);
				}
				else
					tpc.InsertTab (t.content, t.label, t.order, createVoid());
		
		return tpc;
	}
	
	private WComplexPanelContent createComplexPanel (Integer id, ContentType type)
	{
		WComplexPanelContent cpc = null;
		switch (type)
		{
		case HPANEL:
			cpc = new WHorizontalPanelContent();
			break;
		case VPANEL:
			cpc = new WVerticalPanelContent();
			break;
		default:	//wrong type
			return null;
		}

		for (OrderedItem oi: items)
			if (oi.host.equals(id))
				cpc.addItem(oi.content, oi.order, createContent (oi.content));
		
		return cpc;
	}
	
	private WContent createComplexPanelCustom(Integer id, ContentType type,
			Integer chosen, WContent data)
	{
		WComplexPanelContent cpc = null;
		switch (type)
		{
		case HPANEL:
			cpc = new WHorizontalPanelContent();
			break;
		case VPANEL:
			cpc = new WVerticalPanelContent();
			break;
		default:	//wrong type
			return null;
		}

		for (OrderedItem oi: items)
			if (oi.host.equals(id))
				if (oi.content.equals(chosen))
					cpc.addItem(oi.content, oi.order, data);
				else
					cpc.addItem(oi.content, oi.order, createContent (oi.content));
		
		return cpc;
	}
}

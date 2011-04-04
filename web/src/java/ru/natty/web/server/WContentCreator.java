package ru.natty.web.server;

public class WContentCreator
{
	int query_num = 0;
	
	DataBaseOld db = new DataBaseOld();
	WComplexPanelContent def;
	WLabelContent cur_num;
	WTabPanelContent wt;
	WLabelContent zeroLabel;
	
	WContentCreator()
	{
		db.addLabel(53563, "I gonna to push it (from DB).", 34565);
		db.addLabel(5343, "zero label", 34565);
		db.addLabel(5323, "(from data base)", 34565);
		
		db.addTabPanel(34565, 1, 0);
		db.addTab(53563, 34565, "second tab", 1);
		db.addTab(5343, 34565, "first tab", 0);
		db.addTab(5323, 34565, "third tab", 2);
		
		db.addTabPanel(8839, 0, 34565);
		db.addTab(5343, 8839, "default tab", 0);
		db.addLabel(232312, "I currently have the below problem from a client machine when it tries to connect to the oracle database node. The odd thing is the I get the below error for 5 - 15 minutes and then all of a sudden it will connect fine??", 8839);
		db.addTab(232312, 8839, "default tab", 1);
		db.addTab(8839, 34565, "intab", 3);
		
		db.addVerticalPanel(0, 0);
		db.addLabel(2, "Oh this works!", 0);
		db.addItem (2, 0, 0);
		db.addLabel(6, "lap number 0", 0);
		db.addItem (6, 0, 1);
		db.addItem(34565, 0, 2);
		
		db.addHorizontalPanel(5, 0);
		db.addLabel(40, "I know what you want, ", 5);
		db.addLabel(50, "You know i got it", 5);
		db.addItem(40, 5, 0);
		db.addItem(50, 5, 1);
		db.addItem(5, 0, 3);
		
		db.addLabel(49, "last string", 0);
		db.addItem(49, 0, 4);
		
	/*	
		cur_num = new WLabelContent ("lap number 0");
//		WVerticalPanelContent wvpc = new WVerticalPanelContent();
		WComplexPanelContent wvpc = new WComplexPanelContent(VerticalPanelDiffPatcher.class);
		wvpc.addItem (2, 0, new WLabelContent("Oh this works!"));
		wvpc.addItem (6, 2, cur_num);
		zeroLabel = new WLabelContent("intab string");
//		wt = new WTabPanelContent();
//		wt.contents.put (53563, new WTabPanelContent.Tab ("third tab", 2, new WLabelContent("I gonna to push it.")));
//		wt.contents.put (5343, new WTabPanelContent.Tab ("first tab", 0, zeroLabel));
//		wt.contents.put (5323, new WTabPanelContent.Tab ("second tab", 1, new WLabelContent("hello, I'm gnome. I live here.")));
		wt = db.createTabPanel(34565);
		wvpc.addItem (34565, 3, wt);
//		WHorizontalPanelContent whpc = new WHorizontalPanelContent();
		WComplexPanelContent whpc = new WComplexPanelContent(HorizontalPanelDiffPatcher.class);
		whpc.addItem (40, 0, new WLabelContent("inner string"));
		whpc.addItem (50, 1, new WLabelContent("inner string #2"));
		wvpc.addItem (5, 4, whpc);
		wvpc.addItem (7, 5, new WLabelContent("last string"));
		def = wvpc;*/
	}
	public WContent createAll ()
	{
		++query_num;
//		cur_num.text = ("query number " + query_num);
//		WHorizontalPanelContent whpc = new WHorizontalPanelContent();
//		def.addItem (33*query_num, query_num + 5, whpc);
//		wt.contents.put(4312*(query_num + 1), new WTabPanelContent.Tab ("tab #" + (3 + query_num), 2 + query_num, new WLabelContent("intab string")));
//		zeroLabel.setText("changed inner text:" + query_num);
//		for (int i = 0; i < query_num; ++i)
//		{
//			whpc.addItem (7000*i+query_num, i, new WLabelContent ("; inner line " + query_num + ".-." + i));
//		}
//		return def.copy();
		db.addLabel(6, "lap number:" + query_num, 0);
		return new DataBase().createContent(0);//db.createContent(0);
	}
	public WContent createBranch (Integer id)
	{
		return new DataBase().createContentBranch(id);//db.getContent(0);//db.createContentBranch(id);//db.createContent (id);//new WLabelContent("created at query#" + ++query_num);
	}
}
